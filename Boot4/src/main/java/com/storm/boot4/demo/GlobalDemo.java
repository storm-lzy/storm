package com.storm.boot4.demo;

import com.storm.boot4.aspect.GlobalLock;
import com.storm.boot4.aspect.LockTimeoutStrategy;
import com.storm.boot4.aspect.LockType;
import com.storm.boot4.aspect.RedisKeyEnum;
import org.springframework.stereotype.Service;

/**
 * @author 李治毅
 * @date 2024/9/2
 */
@Service
public class GlobalDemo {

    //                锁名称                  锁ID          锁类型:可重入、公平锁、读锁、写锁       加锁等待时间      获取锁使用时间        获取锁失败策略：退出、抛异常、持续轮询获取、自定义实现
    @GlobalLock(name = "ORDER_STATUS", key = "#orderNo", lockType = LockType.REENTRANT, waitTime = 10, leaseTime = 30, lockTimeoutStrategy = LockTimeoutStrategy.FAST_FAIL)
    public void lockOrder(String orderNo) {
    }

    // 默认 可重入锁、获取时间1小时、使用时间1小时、失败策略：退出
    @GlobalLock(name = "ORDER_STATUS", key = "#request.orderNo")
    public void lockOrder(GlobalRequest request) {
    }

    //                                            调用其它方法获取key
    @GlobalLock(name = "ORDER_STATUS", key = "@keyProvider.getKey(#request.orderNo)", lockType = LockType.REENTRANT, waitTime = 10, leaseTime = 30)
    public void lockMainOrder(GlobalRequest request) {
    }

    @GlobalLock(name = "ORDER_STATUS", key = "@keyProvider.getKey(#request.orderList.get(0).orderNo)")
    public void lockMainOrder1(GlobalRequest request) {

    }


    @GlobalLock(name = "ORDER_STATUS", key = "@globalDemo.request(#request)")
    public void lockMainOrder2(GlobalRequest request) {

    }

    // 默认 可重入锁、获取时间1小时、使用时间1小时、失败策略：退出
    @GlobalLock(value = RedisKeyEnum.class,name = "ORDER_STATUS")
    public void lockOrder1(GlobalRequest request) {
    }

    public void request(GlobalRequest request) {
        System.err.println(request);
    }

}
