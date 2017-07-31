package vn.sanobl.tests.functional;


import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.sanobl.rabbitmq.RBConfiguration;
import vn.sanobl.rabbitmq.RBManager;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by cpu11118-local on 31/07/2017.
 */
public class RabbitMQBorkerTestCase {

    protected final String QUEUE_NAME = "queueDefault";

    protected RBManager rbManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQBorkerTestCase.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            LOGGER.info(
                    "Starting test: {}.{}",
                    description.getTestClass().getSimpleName(), description.getMethodName());
        }

        @Override
        protected void finished(Description description) {
            LOGGER.info("Test finished: {}.{}", description.getTestClass().getSimpleName(), description.getMethodName());
        }
    };

    @Before
    public void setUp(){
        RBConfiguration rbConfiguration = new RBConfiguration("localhost:5672;localhost:5673;localhost:5674", "myuser", "mypass", "/");
        try {
            rbManager = RBManager.getInstance(rbConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
    }

}
