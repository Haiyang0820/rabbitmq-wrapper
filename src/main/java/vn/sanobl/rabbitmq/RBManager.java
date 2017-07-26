package vn.sanobl.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.parseInt;

/**
 * Created by bangnk on 7/11/17.
 */
public class RBManager {
    private static final Map<String, RBManager> _instances = new ConcurrentHashMap<>();

    private RBChannelPool _rbChannelPool = null;
    private final AtomicInteger _connectionCount = new AtomicInteger(0);
    private Connection _connection = null;

    public static RBManager getInstance(RBConfiguration rbConfiguration) throws IOException, TimeoutException {
        String key = rbConfiguration.getListhost();
        if (!_instances.containsKey(key)) {
            _instances.put(key, new RBManager(rbConfiguration));
        }
        return _instances.get(key);
    }

    private RBManager(RBConfiguration rbConfiguration) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        String[] urls = rbConfiguration.getListhost().split(";");
        List<Address> addresses = new LinkedList<Address>();
        for (String url : urls) {
            String[] urlInf = url.split(":");
            String hostname = urlInf[0];
            int port = parseInt(urlInf[1]);
            addresses.add(new Address(hostname, port));
        }
        factory.setConnectionTimeout(20);
        factory.setUsername(rbConfiguration.getUsername());
        factory.setPassword(rbConfiguration.getPassword());
        factory.setVirtualHost(rbConfiguration.getVirtualhost());
        factory.setAutomaticRecoveryEnabled(true);
        _connection = factory.newConnection(addresses.toArray(new Address[addresses.size()]));
        _connectionCount.incrementAndGet();
        this._rbChannelPool = new RBChannelPool(_connection);

    }

    public void setMessage(String queueName, String message) throws Exception {
        setMessage("", "", queueName, message, "");
    }

    public void setMessage(String exchangeName, String routingKey, String message) throws Exception {
        setMessage(exchangeName, routingKey, "", message, "");
    }

    public void setMessage(String exchangeName, String routingKey, String queueName, String message) throws Exception {
        setMessage(exchangeName, routingKey, queueName, message, "");
    }

    public void setMessage(String exchangeName, String routingKey, String queueName, String message, String exchangeType) throws Exception {
        Channel channel = this._rbChannelPool.borrowClient();
        if (!exchangeName.isEmpty()) {
            switch (exchangeType) {
                case "direct": {
                    processExchangeDirect(channel, exchangeName, routingKey, queueName, message);
                    break;
                }
                case "topic": {
                    processExchangeTopic(channel, exchangeName, routingKey, queueName, message);
                    break;
                }
                default:
                    processExchangeDirect(channel, exchangeName, routingKey, queueName, message);
                    break;
            }
        } else {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, new AMQP.BasicProperties.Builder()
                    .contentType("text/plain").deliveryMode(1).build(), message.getBytes());
        }
        this._rbChannelPool.returnObject(channel);

    }

    private void processExchangeDirect(Channel channel, String exchangeName, String routingKey, String queueName, String message) throws IOException {
        channel.exchangeDeclare(exchangeName, "direct", true);
        if (!queueName.isEmpty()) {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);
        }
        channel.basicPublish(exchangeName, routingKey, new AMQP.BasicProperties.Builder()
                .contentType("text/plain").deliveryMode(1).build(), message.getBytes());
    }

    private void processExchangeTopic(Channel channel, String exchangeName, String routingKey, String queueName, String message) throws IOException {
        channel.exchangeDeclare(exchangeName, "topic", true);
        if (!queueName.isEmpty()) {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);
        }
        channel.basicPublish(exchangeName, routingKey, new AMQP.BasicProperties.Builder()
                .contentType("text/plain").deliveryMode(1).build(), message.getBytes());
    }

    public String getMessage(String queueName) throws Exception {
        return getMessage("", "", queueName, "");
    }

    public String getMessage(String exchangeName, String routingKey) throws Exception {
        return getMessage(exchangeName, routingKey, "", "");
    }

    public String getMessage(String exchangeName, String routingKey, String queueName) throws Exception {
        return getMessage(exchangeName, routingKey, queueName, "");
    }

    public String getMessage(String exchangeName, String routingKey, String queueName, String exchangeType) throws Exception {
        String result = "";
        Channel channel = this._rbChannelPool.borrowClient();
        if (!exchangeName.isEmpty()) {
            if (exchangeType.isEmpty())
                exchangeType = "direct";
            channel.exchangeDeclare(exchangeName, exchangeType, true);
            if (!queueName.isEmpty()) {
                channel.queueDeclare(queueName, true, false, false, null);
            } else
                queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, routingKey);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String tmp = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + tmp + "'");
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } else {
            GetResponse response = channel.basicGet(queueName, false);
            if (response == null) {
                // No message retrieved.
                System.out.println("response: null");
            } else {
                byte[] body = response.getBody();
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                channel.basicAck(deliveryTag, true);
                result = new String(body, "UTF-8");
            }
        }
        this._rbChannelPool.returnObject(channel);
        return result;
    }

    public void close() {
        if (null != _rbChannelPool)
            _rbChannelPool.close();
        if (null != _connection) {
            try {
                _connection.close();
                _connectionCount.decrementAndGet();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int countConnection() {
        return _connectionCount.get();
    }

    public int countChannel() {
        return _rbChannelPool.countChannel();
    }
}
