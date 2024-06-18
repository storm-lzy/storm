package com.storm.shard.pool;

import javafx.util.Pair;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 *
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        GenericObjectPool<Channel<String>> pool = new GenericObjectPool<>(new ChannelPoolObjectFactory());
        Channel<String> channel = pool.borrowObject();
        channel.put(new Pair<>("You", "Dog"));

        channel.put(new Pair<>("We", "Family"));

        System.err.println("【Object One】" + channel);

        Channel<String> channel1 = pool.borrowObject();

        channel.put(new Pair<>("A", "Dog"));

        channel.put(new Pair<>("A-B", "Family"));

        System.err.println("【Object Two】" + channel1);

        pool.returnObject(channel);

        Channel<String> channel3 = pool.borrowObject();

        System.err.println("【Object Three】" + channel3);

        pool.returnObject(channel3);
        pool.returnObject(channel1);

        Channel<String> channel4 = pool.borrowObject();

        System.err.println("【Object Four】" + channel4);
    }


}
