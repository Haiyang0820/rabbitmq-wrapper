package vn.sanobl.tests.functional;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by cpu11118-local on 31/07/2017.
 */
public class QueueingConsumer  extends RabbitMQBorkerTestCase{

    static final String QUEUE = "XXXXXXXX";
    static final int THREADS = 5;

    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void nThreadShutdown() throws Exception{
        Channel channel = rbManager.get_rbChannelPool().borrowClient();
        final com.rabbitmq.client.QueueingConsumer c = new com.rabbitmq.client.QueueingConsumer(channel);
        channel.queueDeclare(QUEUE, false, true, true, null);
        channel.basicConsume(QUEUE, c);
        final AtomicInteger count = new AtomicInteger(THREADS);
        final CountDownLatch latch = new CountDownLatch(THREADS);

        for(int i = 0; i < THREADS; i++){
            new Thread(){
                @Override public void run(){
                    try {
                        while(true){
                            c.nextDelivery();
                        }
                    } catch (ShutdownSignalException sig) {
                        count.decrementAndGet();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                }
            }.start();
        }

        rbManager.close();

        // Far longer than this could reasonably take
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(0, count.get());
    }

    @Test public void consumerCancellationInterruptsQueuingConsumerWait()
            throws Exception {
        String queue = "cancel_notification_queue_for_queueing_consumer";
        final BlockingQueue<Boolean> result = new ArrayBlockingQueue<Boolean>(1);
        Channel channel = rbManager.get_rbChannelPool().borrowClient();
        channel.queueDeclare(queue, false, true, false, null);
        final com.rabbitmq.client.QueueingConsumer consumer = new com.rabbitmq.client.QueueingConsumer(channel);
        Runnable receiver = new Runnable() {

            public void run() {
                try {
                    try {
                        consumer.nextDelivery();
                    } catch (ConsumerCancelledException e) {
                        result.put(true);
                        return;
                    } catch (ShutdownSignalException e) {
                    } catch (InterruptedException e) {
                    }
                    result.put(false);
                } catch (InterruptedException e) {
                    fail();
                }
            }
        };
        Thread t = new Thread(receiver);
        t.start();
        channel.basicConsume(queue, consumer);
        channel.queueDelete(queue);
        assertTrue(result.take());
        t.join();
    }
}
