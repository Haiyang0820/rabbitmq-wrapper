package vn.sanobl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import vn.sanobl.tests.functional.QueueingConsumer;
import vn.sanobl.tests.functional.RabbitMQChanels;
import vn.sanobl.tests.functional.RabbitMQConnection;
import vn.sanobl.tests.functional.SendReceiverMessage;

/**
 * Unit test for simple App.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        RabbitMQConnection.class,
        SendReceiverMessage.class,
        RabbitMQChanels.class,
        QueueingConsumer.class
})
public class AppTest
{

}
