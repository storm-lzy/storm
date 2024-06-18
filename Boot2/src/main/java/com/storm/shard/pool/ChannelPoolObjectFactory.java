package com.storm.shard.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 */
public class ChannelPoolObjectFactory implements PooledObjectFactory<Channel<String>> {

    @Override
    public void activateObject(PooledObject<Channel<String>> pooledObject) throws Exception {
        System.err.println("校验对象状态");
        boolean active = pooledObject.getObject().isActive();
        if(!active){
            throw new RuntimeException("对象不存在");
        }
    }

    @Override
    public void destroyObject(PooledObject<Channel<String>> pooledObject) throws Exception {
        System.err.println("销毁对象");
        pooledObject.getObject().close();
    }

    @Override
    public PooledObject<Channel<String>> makeObject() throws Exception {
        System.err.println("创建对象");
        Channel<String> channel = new Channel<>();
        channel.open(10);
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void passivateObject(PooledObject<Channel<String>> pooledObject) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<Channel<String>> pooledObject) {
        System.err.println("判断对象是否存活");
        return pooledObject.getObject().isActive();
    }
}
