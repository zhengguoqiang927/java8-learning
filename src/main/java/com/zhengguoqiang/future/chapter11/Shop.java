package com.zhengguoqiang.future.chapter11;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author zhengguoqiang
 */
@Data
@AllArgsConstructor
public class Shop {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private String name;

    public String getPrice(String product) {
//        return calculatePrice(product);
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[RANDOM.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s",name,price,code);
    }

    private double calculatePrice(String product) {
        delay();
        return RANDOM.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public static void delay() {
        int delay = 500 + RANDOM.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                //开启线程异步计算价格
                double price = calculatePrice(product);
                //设置计算结果
                future.complete(price);
            } catch (Exception e) {
                //抛出该线程内部异常给调用线程
                future.completeExceptionally(e);
//                e.printStackTrace();
            }
        }).start();
        return future;
    }

    public static List<String> findPrices(String product) {
        List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("LetsSaveBig"), new Shop("MyFavoriteShop")
                , new Shop("BuyItAll"), new Shop("BuyItAll"),
                new Shop("Taobao"), new Shop("Tianmao"),
                new Shop("JingDong"), new Shop("Dangdang"),
                new Shop("Yamaxun"));
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(),
                        shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    public static List<String> findPricesAsync(String product) {
        List<Shop> shops = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"), new Shop("BuyItAll"),
                new Shop("Taobao"), new Shop("Tianmao"),
                new Shop("JingDong"), new Shop("Dangdang"),
                new Shop("Yamaxun"));
        ExecutorService executorService = Executors.newFixedThreadPool(9, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop ->
                        CompletableFuture.supplyAsync(
                                () -> shop.getName() + " price is " + shop.getPrice(product),
                                executorService)
                )
                .collect(Collectors.toList());
        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {
//        Future<Double> future = getPriceAsync("Bestprice");
//        System.out.println(future.get());
        long start = System.nanoTime();
        System.out.println(findPricesAsync("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

        System.out.println("The number of processor is " + Runtime.getRuntime().availableProcessors());

        //java8 Stream的延迟特性（引起顺序执行），进行了性能优化，短路方法
        long count = IntStream.rangeClosed(1, 10)
                .map(i -> {
                    System.out.println("filter-" + (i + 1));
                    return i + 1;
                })
                .map(i -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("map-" + i * 2);
                    return i * 2;
                }).limit(3).count();
    }
}