package vn.sanobl.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * Created by bangnk on 7/10/17.
 */
public class RBChannelFactory extends BasePoolableObjectFactory<Channel> {
    private Connection _connection = null;

    public RBChannelFactory(Connection connection) {
        this._connection = connection;
    }

    @Override
    public Channel makeObject() throws Exception {
        try {
            return _connection.createChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroyObject(Channel obj) throws Exception {
        super.destroyObject(obj);
        if (null != obj)
            obj.close();
    }

    @Override
    public boolean validateObject(Channel obj) {
        return super.validateObject(obj);
    }

    @Override
    public void activateObject(Channel obj) throws Exception {
        super.activateObject(obj);
    }

    @Override
    public void passivateObject(Channel obj) throws Exception {
        super.passivateObject(obj);
    }
}
