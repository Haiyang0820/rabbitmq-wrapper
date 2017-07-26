package vn.sanobl.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bangnk on 7/11/17.
 */
public class RBChannelPool {
    private GenericObjectPool _pool = null;
    private final AtomicInteger _channelCount = new AtomicInteger(0);
    public RBChannelPool(Connection _connection) throws IOException, TimeoutException {
        RBChannelFactory cf = new RBChannelFactory(_connection);
        this._pool = createPool(cf);
    }

    private GenericObjectPool createPool(RBChannelFactory cf) {
        System.out.println("start createPool Channel");
        GenericObjectPool p = new GenericObjectPool<>(cf);
        p.setMaxActive(20);
        p.setMaxIdle(20);
        p.setTestOnBorrow(true);
        p.setTestOnReturn(true);
//        p.setMaxWait(30000);
        p.setMinEvictableIdleTimeMillis(50000L);
        p.setTimeBetweenEvictionRunsMillis(55000L);
        return p;
    }

    public Channel borrowClient() throws Exception {
        _channelCount.incrementAndGet();
        return (Channel) this._pool.borrowObject();
    }

    public void returnObject(Channel client) throws Exception {
        _channelCount.decrementAndGet();
        this._pool.returnObject(client);
    }

    public void close() {
        System.out.println("close pool channel");
        try {
            if (null != _pool)
                _pool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int countChannel()
    {
        return _channelCount.get();
    }
}
