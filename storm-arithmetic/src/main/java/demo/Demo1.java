package demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Demo1 {
    static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行！");
            }
        });

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行~");
            }
        });
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行~");
            }
        },5, TimeUnit.SECONDS);

        System.out.println(executorService.isShutdown());
        executorService.shutdownNow();
    }
}
