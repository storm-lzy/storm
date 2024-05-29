package demo;

import com.sun.xml.internal.ws.util.CompletedFuture;

import java.text.ParseException;
import java.util.concurrent.*;
import java.util.function.*;

/**
 *
 */
public class Demo2 {
    // 定义一个线程池对象
    private static ExecutorService service = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        CompletableFuture<Object> future = CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                System.out.println("456");
                return 1;
            }
        }, service);
        future.thenRun(new Runnable() {
            @Override
            public void run() {
                System.out.println("thenrun");
            }
        });
        System.out.println(future.get());
    }

}

class T implements Runnable{

    @Override
    public void run() {
        System.out.println("123123123");
    }
}

class C implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        Thread.sleep(2000);
        return 1;
    }
}
