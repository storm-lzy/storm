package com.storm.boot3.context;

import com.storm.boot3.enums.HandleEnum;
import com.storm.boot3.handle.AbstractHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 李治毅
 * @date 2024/7/19
 */
@Component
public class HandleContext implements ApplicationRunner {

    @Autowired
    private Map<String, AbstractHandle> abstractHandleMap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.err.println(abstractHandleMap);
    }

    public AbstractHandle getHandle(HandleEnum handleEnum){
        return abstractHandleMap.get(handleEnum.getValue().getSimpleName());
    }
}
