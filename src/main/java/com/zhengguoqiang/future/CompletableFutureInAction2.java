package com.zhengguoqiang.future;

import java.util.concurrent.CompletableFuture;

/**
 * @author zhengguoqiang
 */
public class CompletableFutureInAction2 {

    public static void main(String[] args) throws InterruptedException {
        //forkjoinpool默认使用守护线程
        /*CompletableFuture.supplyAsync(() -> 1)
                .thenApply(d -> Integer.sum(d,10))
                .whenComplete((v,t) -> System.out.println(v));*/

        //java8里很多方法都带有Async后缀的异步方法，异步方法使用场景，只有在涉及到远程服务调用或者I/O等耗时操作时才用
        //否则计算密集型任务直接采用同步方法效率更高，因为可以避免线程上下文切换开销
        /*CompletableFuture.supplyAsync(()->1)
                .handle((v,t) -> Integer.sum(v,10))
                .whenComplete((v,t) -> System.out.println(v))
                .thenRun(() -> System.out.println("AysncTask Done."));*/

        //thenAccept方法 当某个异步任务完成时即返回结果，不用在等全部异步任务完成在返回
        //基于异步回调机制
        /*CompletableFuture.supplyAsync(() -> 1)
                .thenApply(i -> Integer.sum(i,10))
                .thenAccept(System.out::println);*/

        //thenCompose，两个互相依赖的异步任务结合，第二个异步任务以第一个异步任务返回的结果作为参数
        /*CompletableFuture.supplyAsync(() -> 1)
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> i * 10))
                .thenAccept(System.out::println);*/

        //thenCombine 两个不相关的异步任务结合
        /*CompletableFuture.supplyAsync(() -> 1)
                .thenCombine(CompletableFuture.supplyAsync(() -> 2),
                        (t1,t2) -> t1 + t2)
                .thenAccept(System.out::println);*/

        /*CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running.");
            return 2;
        }), () -> System.out.println("Done."));*/

        CompletableFuture.supplyAsync(() -> {
            System.out.println("I am future1");
            return CompletableFutureInAction.get();
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            System.out.println("I am future2");
            return CompletableFutureInAction.get();
        }), d -> d * 10)
                .thenAccept(System.out::println);

        System.out.println("done.");
        Thread.currentThread().join();
    }
}
