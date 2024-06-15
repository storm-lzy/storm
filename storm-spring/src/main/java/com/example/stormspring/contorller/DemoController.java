package com.example.stormspring.contorller;

import com.example.stormspring.annotation.DoubleCache;
import com.example.stormspring.entity.DemoEntity;
import com.example.stormspring.enums.CacheType;
import com.example.stormspring.rebbit.UserMessage;
import com.example.stormspring.service.impl.DemoServiceImpl;
import com.storm.mq.MqClient;
import com.storm.mq.concurrent.CallbackFuture;
import com.storm.mq.concurrent.FutureCallback;
import com.storm.mq.message.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 *
 */
@RestController
@RequestMapping("demo")
@Slf4j
public class DemoController {


    @Resource
    private DemoServiceImpl demoService;

    @Resource
    private MqClient defaultMqClient;

    @GetMapping("insert")
    public void insert() {
        DemoEntity demo = new DemoEntity();;
        demo.setAge("24");
        demo.setName("李治毅");
        demo.setEmail("15690788116@163.com");
        demoService.insert(demo);
    }


    @GetMapping("getUser/{userId}")
    @DoubleCache(cacheName = "user",key = "#userId",TimeOut = 60,type = CacheType.FULL)
    public DemoEntity getUserById(@PathVariable Long userId){
        return demoService.getUserById(userId);
    }


    @PostMapping("update")
    @DoubleCache(cacheName = "user",key = "#demo.id",type = CacheType.DELETE)
    public void update(DemoEntity demo){
        demoService.updateById(demo);
    }


    @GetMapping("sendUser")
    public void sendUser() throws ExecutionException, InterruptedException {
        UserMessage message = new UserMessage();
        message.setAge(12);
        message.setName("张三");
        message.setMan(true);
        CallbackFuture<MessageContext> future = defaultMqClient.publish("storm-user", message);
        future.addCallback(new FutureCallback<MessageContext>() {
            @Override
            public void complete(MessageContext result, Throwable ex, int status) {
                log.info("消息发送完成 topic:{} ","storm-user");
            }

            @Override
            public void success(MessageContext result) {
                log.info("消息发送成功 topic:{} ","storm-user");
            }

            @Override
            public void failed(Throwable ex) {
                log.info("消息发送失败 topic:{} ","storm-user");
            }

            @Override
            public void cancelled() {
                log.info("消息发送取消 topic:{} ","storm-user");
            }
        });
        MessageContext messageContext = future.get();

    }

}
