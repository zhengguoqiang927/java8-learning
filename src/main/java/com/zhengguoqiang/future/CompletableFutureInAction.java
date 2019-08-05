package com.zhengguoqiang.future;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhengguoqiang
 */
public class CompletableFutureInAction {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {
//        test1();
        test2();
//        test3();
    }

    private static void test1(){
        CompletableFuture<Double> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            double value = get();
            completableFuture.complete(value);
        }).start();

//        System.out.println(completableFuture.get());
        System.out.println("处理其他业务");
        completableFuture.whenComplete((v,t) -> {
            Optional.ofNullable(v).ifPresent(System.out::println);
            Optional.ofNullable(t).ifPresent(Throwable::printStackTrace);
        });
    }

    private static void test2() throws InterruptedException {
        //线程池里的线程默认是非守护线程
        ExecutorService executorService = Executors.newFixedThreadPool(2,r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(false);
            return thread;
        });


//        AtomicBoolean finished = new AtomicBoolean(false);
        //
        CompletableFuture.supplyAsync(CompletableFutureInAction::get,executorService)
        .whenComplete((v,t) -> {
            Optional.ofNullable(v).ifPresent(System.out::println);
            Optional.ofNullable(t).ifPresent(Throwable::printStackTrace);
//            finished.set(true);
        });

//        while (!finished.get()){
//            Thread.sleep(1);
//        }


//        executorService.shutdown();
    }

    private static void test3(){
        //线程池里的线程默认是非守护线程
        ExecutorService executorService = Executors.newFixedThreadPool(2,r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(false);
            return thread;
        });
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        Stream<CompletableFuture<Double>> completableFutureStream = list.stream().map(i -> CompletableFuture.supplyAsync(CompletableFutureInAction::get, executorService));
        Stream<CompletableFuture<Double>> futureStream = completableFutureStream.map(future -> future.thenApply(CompletableFutureInAction::multiple));
        List<Double> collect = futureStream.map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println(collect);
    }

    private static double multiple(double value){
        return value * 10d;
    }

    private static double get(){
        double result = 0;
        try {
            Thread.sleep(RANDOM.nextInt(10000));
            result = RANDOM.nextDouble();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


}
