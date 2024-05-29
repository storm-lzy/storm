package com.example.stormspring.contorller;

import com.example.stormspring.annotation.DoubleCache;
import com.example.stormspring.entity.DemoEntity;
import com.example.stormspring.enums.CacheType;
import com.example.stormspring.service.impl.DemoServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *
 */
@RestController
@RequestMapping("demo")
public class DemoController {


    @Resource
    private DemoServiceImpl demoService;

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

}
