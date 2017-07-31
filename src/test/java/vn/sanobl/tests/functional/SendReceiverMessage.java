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
import java.util.Scanner;
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
        Object[][] data = new Object[][] { { "Tran Van B", "Tran Van B" } };
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
    public void sendAndReceiverMessageWithExchangeTopic(){
        try {
            rbManager.setMessage("topicExchange1", "root", "", message, "topic");

            //handler receiver
            Channel channel = rbManager.get_rbChannelPool().borrowClient();
            channel.exchangeDeclare("topicExchange1", "topic", true);
            String nameQueue = channel.queueDeclare().getQueue();
//            channel.queueDeclare("abc", true, false, false, null);
            channel.queueBind(nameQueue, "topicExchange1", "root");
//            GetResponse response = channel.basicGet("abc", true);
            final ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String result = new String(body, "UTF-8");
                    blockingQueue.offer(result);

                }
            };
//            channel.queueBind("abc", "topicExchange1", "root");
            channel.basicConsume(nameQueue, true, consumer);
            String str = blockingQueue.take();
            Assert.assertEquals("sendAndReceiverMessageWithExchangeTopic Error ==>>", result, str);
//            if(response == null){
//                Assert.assertNotNull("Not receive message",response);
//            }else {
//                String str = new String(response.getBody(), "UTF-8");
//                System.out.println(str);
//                Assert.assertEquals("sendAndReceiverMessageWithExchangeTopic Error ==>>", result, str);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendAndReceiverMessageWithExchangeDirect(){
        try {
            rbManager.setMessage("directExchange1", "root", "", message, "direct");
            //handler receiver
            Channel channel = rbManager.get_rbChannelPool().borrowClient();
            channel.exchangeDeclare("directExchange1", "direct", true);
            String nameQueue = channel.queueDeclare().getQueue();
            final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String result = new String(body, "UTF-8");
                    blockingQueue.offer(result);

                }
            };
            channel.queueBind(nameQueue, "directExchange1", "root");
            channel.basicConsume(nameQueue, true, consumer);
            String str = blockingQueue.take();
            Assert.assertEquals("sendAndReceiverMessageWithExchangeDirect Error ==>>",result, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
