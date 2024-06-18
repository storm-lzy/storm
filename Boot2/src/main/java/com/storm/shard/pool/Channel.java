package com.storm.shard.pool;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Channel<T> {

    private Map<String,T> channelMap;

    public boolean close(){
        if(null != channelMap){
            channelMap.clear();
            channelMap = null;
        }
        return true;
    }


    public boolean open(int size){
        channelMap = new HashMap<>(size);
        return true;
    }

    public T put(Pair<String,T> pair){
        return channelMap.put(pair.getKey(),pair.getValue());
    }


    public boolean isActive(){
        return null != channelMap;
    }

}
