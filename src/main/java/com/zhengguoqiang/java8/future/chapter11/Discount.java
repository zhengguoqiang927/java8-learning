package com.zhengguoqiang.java8.future.chapter11;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhengguoqiang
 */
public class Discount {

    @Getter
    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " +
                Discount.apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static double apply(double price, Code code) {
        Shop.delay();
        double discountPrice = price * (100 - code.getPercentage()) / 100;
        BigDecimal bigDecimal = new BigDecimal(discountPrice);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static List<String> findPrices(List<Shop> shops, String product) {
        return shops.parallelStream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
    }

    public static List<String> findPricesAsync(List<Shop> shops, String product) {
        ExecutorService executorService = Executors.newFixedThreadPool(10, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        //两个依赖任务的结合 thenCompose方法
        //两个无依赖关系任务的结合 thenCombine方法
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executorService))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() ->
                        Discount.applyDiscount(quote), executorService)))
                .collect(Collectors.toList());
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void findPricesStream(List<Shop> shops, String product)
            throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(10, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        Stream<CompletableFuture<String>> futureStream = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executorService))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() ->
                        Discount.applyDiscount(quote), executorService)));
        //thenAccept 定义如何处理ComletableFuture返回的结果；一旦CompletableFuture计算得到结果，thenAccept就返回一个CompletableFuture<Void>对象
        //CompletableFuture<Void>对象能做的事情有限，只能等待其运行结束
//        Stream<CompletableFuture<Void>> completableFutureStream = futureStream.map(f -> f.thenAccept(System.out::println));
//        CompletableFuture[] completableFutures = completableFutureStream.toArray(CompletableFuture[]::new);
        //等待所有任务完成
//        CompletableFuture.allOf(completableFutures).join();
        long start = System.nanoTime();
        Stream<CompletableFuture<Void>> completableFutureStream =
                futureStream.map(f -> f.thenAccept(s -> System.out.println(s + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " ms)")));
        CompletableFuture[] completableFutures = completableFutureStream.toArray(CompletableFuture[]::new);
        //等待所有任务完成
//        CompletableFuture.allOf(completableFutures).join();
        //等待任意任务返回即返回
        CompletableFuture.anyOf(completableFutures).join();
//        CompletableFuture.anyOf(completableFutures).get(1, TimeUnit.SECONDS);
//        CompletableFuture.anyOf(completableFutures).whenComplete(((o, throwable) -> System.out.println(o)));
        System.out.println("All shops have now responded in "
                + ((System.nanoTime() - start) / 1_000_000) + " msecs");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        List<Shop> shops = Arrays.asList(
                new Shop("BestPrice"), new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"), new Shop("BuyItAll"),
                new Shop("Taobao"), new Shop("Tianmao"),
                new Shop("JingDong"), new Shop("Dangdang"));

        long start = System.nanoTime();
//        System.out.println(findPrices(shops,"myPhone27S"));
//        System.out.println(findPricesAsync(shops,"myPhone27S"));
        findPricesStream(shops, "myPhone27S");
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }
}
