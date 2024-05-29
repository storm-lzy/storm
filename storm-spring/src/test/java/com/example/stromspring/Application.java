package com.example.stromspring;

import com.example.stormspring.StormSpringApplication;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 *
 */
@SpringBootTest(classes = StormSpringApplication.class)
@Slf4j
class Application {


    @Resource
    Cache<String, String> cache;


    @Test
    void testCache() throws Exception {
        cache.put("abc", "Hello World！");
//        System.out.println(cache.get("abc",s -> "啦啦啦啦"));
        System.out.println(cache.getIfPresent("abc"));

        Thread.sleep(3500);

        System.out.println(cache.getIfPresent("abc"));

        ArrayList<String> list = new ArrayList<>();
        list.add("abc");
        Map<String, String> all = cache.getAll(list, new Function<Iterable<? extends String>, Map<String, String>>() {
            @Override
            public Map<String, String> apply(Iterable<? extends String> strings) {
                log.warn("执行apply：" + strings);
                HashMap<String, String> map = new HashMap<>();
                for (String string : strings) {
                    map.put(string, "啊啊啊啊啊");
                }
                return map;
            }
        });
        System.out.println(all);
        System.out.println(cache.getIfPresent("abc"));
    }


    @Test
    public void testCache2() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(2)
                .removalListener(((key, value, cause) -> System.out.println("键："+key+" 值："+value+" 清除原因："+cause)))
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .build();
        cache.put("name","张三");
        cache.put("sex","男");
        cache.put("age","18");
        TimeUnit.SECONDS.sleep(2);
        cache.put("name2","张三");
        cache.put("age2","18");
        cache.invalidate("age2");
        TimeUnit.SECONDS.sleep(10);
    }
}
