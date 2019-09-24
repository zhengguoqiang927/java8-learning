package com.zhengguoqiang.java8.future;

import java.util.concurrent.*;

/**
 * @author zhengguoqiang
 */
public class CompletableFutureTimeout {

    public static void main(String[] args) {
//        timeoutAsync();
        hypotheticalApp();
    }

    public static void timeoutSync(){
        CompletableFuture timeoutFuture = new CompletableFuture();
        try {
            timeoutFuture.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static int getValue(){
        System.out.println(Thread.currentThread().getName());
        System.out.println("I am Called");
        try {
            // Simulating a long network call of 1 second in the worst case
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 10;
    }

    public static void timeoutAsync(){
        CompletableFuture timeoutFuture = new CompletableFuture();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.schedule(() -> timeoutFuture.completeExceptionally(new TimeoutException()),1000,TimeUnit.MILLISECONDS);
        CompletableFuture<Object> finalFuture = CompletableFuture.anyOf(timeoutFuture, CompletableFuture.supplyAsync(CompletableFutureTimeout::getValue));
        finalFuture.join();
    }

    public static void hypotheticalApp() {
        ExecutorService executor = new ThreadPoolExecutor(10,10,0l,
                TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>());
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        CompletableFuture[] allFutures = new CompletableFuture[10];
//        for (int i=0;i<10;i++){
//            CompletableFuture dependencyFuture = CompletableFuture.supplyAsync(CompletableFutureTimeout::getValue,executor);
//            CompletableFuture timeoutFuture = new CompletableFuture();
//            service.schedule(() -> timeoutFuture.completeExceptionally(new TimeoutException()),100,TimeUnit.MILLISECONDS);
//            CompletableFuture result = CompletableFuture.anyOf(dependencyFuture,timeoutFuture);
//            allFutures[i] = result;
//        }
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(CompletableFutureTimeout::getValue, executor)
                    .acceptEither(timeoutAfter(500, TimeUnit.MILLISECONDS), System.out::println);
            CompletableFuture.anyOf(voidCompletableFuture).join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("do anythings other");
        //
//        CompletableFuture.allOf(allFutures).join();
//        List<Object> collect = Arrays.stream(allFutures).map(CompletableFuture::join).collect(Collectors.toList());
//        System.out.println("All futures completed.");
//        System.out.println(executor.toString());
    }

    public static ScheduledThreadPoolExecutor delay = new ScheduledThreadPoolExecutor(1);

    public static <T> CompletableFuture<T> timeoutAfter(long timeout,TimeUnit unit){
        CompletableFuture<T> result = new CompletableFuture<>();
        delay.schedule(()-> result.completeExceptionally(new TimeoutException("查询数据超时异常")),timeout,unit);
        return result;
    }
}
