package vn.sanobl.tests.functional;

import com.rabbitmq.client.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by cpu11118-local on 31/07/2017.
 */
@RunWith(Parameterized.class)
public class SendReceiverMessage  extends RabbitMQBorkerTestCase{

    private final String QUEUE_NAME = "queueDefault";

    @Parameterized.Parameter(0)
    public String message;
    @Parameterized.Parameter(1)
    public String result;

    @Parameterized.Parameters
    public static Collection<Object[]> initParam(){
        Object[][] data = new Object[][] { { "Tran Van B", "Tran Van B" }, { "Tran Van Canh", "Tran Van Canh" } };
        return Arrays.asList(data);
    }

    @Before
    public void setUp(){
        super.setUp();
    }

    @After
    public void tearDown(){
        super.tearDown();
    }

    @Test
    public void sendAndReceiverMessageWithExplicitlyQueue(){
        try {
            rbManager.setMessage(QUEUE_NAME, message);
            Assert.assertEquals("sendAndReceiverMessageWithExplicitlyQueue Error ==>>",result, rbManager.getMessage(QUEUE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void sendAndReceiverMessageWithExchangeTopic() throws Exception {
        String queueName = "abc21211";


        //receive message
        Channel reChannel = rbManager.get_rbChannelPool().borrowClient();
        reChannel.exchangeDeclare("demoExchange", "topic");
        queueName = reChannel.queueDeclare().getQueue();
        reChannel.queueBind(queueName, "demoExchange", "black");
        final BlockingQueue<String> queueBlocking = new ArrayBlockingQueue<String>(1);
        reChannel.basicConsume(queueName, new DefaultConsumer(reChannel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String reMessage = new String(body, "UTF-8");
                System.out.println(reMessage);
                queueBlocking.offer(reMessage);
            }
        });
        //send message
        Channel senChannel = rbManager.get_rbChannelPool().borrowClient();
        senChannel.exchangeDeclare("demoExchange", "topic");
        senChannel.basicPublish("demoExchange", "black", null, message.getBytes());
        rbManager.get_rbChannelPool().returnObject(senChannel);
        String reResult = queueBlocking.take();
        Assert.assertEquals("sendAndReceiverMessageWithExchangeTopic",result, reResult);
        rbManager.get_rbChannelPool().returnObject(reChannel);
    }


    @Test
    public void sendAndReceiverMessageWithExchangeDirect() throws Exception {
        //receive message
        Channel reChannel = rbManager.get_rbChannelPool().borrowClient();
        reChannel.exchangeDeclare("demoExchange2", "direct");
        String queueName = reChannel.queueDeclare().getQueue();
        reChannel.queueBind(queueName, "demoExchange2", "black");
        final BlockingQueue<String> queueBlocking = new ArrayBlockingQueue<String>(1);
        reChannel.basicConsume(queueName, new DefaultConsumer(reChannel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String reMessage = new String(body, "UTF-8");
                queueBlocking.offer(reMessage);
            }
        });
        //send message
        Channel senChannel = rbManager.get_rbChannelPool().borrowClient();
        senChannel.exchangeDeclare("demoExchange2", "direct");
        senChannel.basicPublish("demoExchange2", "black", null, message.getBytes());
        rbManager.get_rbChannelPool().returnObject(senChannel);
        String reResult = queueBlocking.take();
        Assert.assertEquals("sendAndReceiverMessageWithExchangeDirect",result, reResult);

        rbManager.get_rbChannelPool().returnObject(reChannel);
    }

}
