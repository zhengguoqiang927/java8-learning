package com.zhengguoqiang.collector;

import com.zhengguoqiang.stream.Dish;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author zhengguoqiang
 */
public class CollectorComplex2 {

    private static final List<Dish> menu = CollectorComplex.menu;

    public static void main(String[] args) {
        testGroupingByConcurrent();
        testGroupingByConcurrentCollector();
        testGroupingByConcurrentCollectorAndSupplier();
        testJoiningWithDelimiter();
        testJoiningWithDelimiterAndPrefixAndSuffix();
        testMapping();
        testMaxBy();
        testMinBy();
        testPartitioningBy();
        testPartitioningByWithCollector();
        testReducing();
        testReducingWithIdentity();
        testReducingWithIdentityAndMapper();
        testSummingDouble();
    }

    private static void testGroupingByConcurrent() {
        System.out.println("testGroupingByConcurrent");
        Optional.ofNullable(menu.stream().collect(Collectors.groupingByConcurrent(Dish::getType)))
                .ifPresent(d -> {
                    System.out.println(d.getClass());
                    System.out.println(d);
                });
    }

    private static void testGroupingByConcurrentCollector(){
        System.out.println("testGroupingByConcurrentCollector");
        Optional.ofNullable(menu.stream().collect(Collectors.groupingByConcurrent(Dish::getType,Collectors.counting())))
                .ifPresent(d -> {
                    System.out.println(d.getClass());
                    System.out.println(d);
                });
    }

    private static void testGroupingByConcurrentCollectorAndSupplier(){
        System.out.println("testGroupingByConcurrentCollectorAndSupplier");
        Optional.ofNullable(menu.stream().collect(
                Collectors.groupingByConcurrent(Dish::getType,
                        ConcurrentSkipListMap::new,Collectors.counting())))
                .ifPresent(d -> {
                    System.out.println(d.getClass());
                    System.out.println(d);
                });
    }

    private static void testJoiningWithDelimiter(){
        System.out.println("testJoiningWithDelimiter");
        Optional.ofNullable(menu.stream().map(Dish::getName).collect(Collectors.joining(",")))
                .ifPresent(System.out::println);
    }

    private static void testJoiningWithDelimiterAndPrefixAndSuffix(){
        System.out.println("testJoiningWithDelimiterAndPrefixAndSuffix");
        Optional.ofNullable(menu.stream().map(Dish::getName)
                .collect(Collectors.joining(",","Names:[","]")))
                .ifPresent(System.out::println);
    }

    private static void testMapping(){
        System.out.println("testMapping");
        Optional.ofNullable(menu.stream()
                .collect(Collectors.mapping(Dish::getName,Collectors.joining(","))))
                .ifPresent(System.out::println);
    }

    private static void testMaxBy(){
        System.out.println("testMaxBy");
//        menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)))
//                .ifPresent(System.out::println);
        menu.stream().max(Comparator.comparingInt(Dish::getCalories)).ifPresent(System.out::println);
    }

    private static void testMinBy(){
        System.out.println("testMinBy");
//        menu.stream().collect(Collectors.minBy(Comparator.comparingInt(Dish::getCalories)))
//                .ifPresent(System.out::println);
        menu.stream().min(Comparator.comparingInt(Dish::getCalories)).ifPresent(System.out::println);
    }

    private static void testPartitioningBy(){
        System.out.println("testPartitioningBy");
        Optional.ofNullable(menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian)))
                .ifPresent(System.out::println);
    }

    private static void testPartitioningByWithCollector(){
        System.out.println("testPartitioningByWithCollector");
        Optional.ofNullable(menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian,Collectors.counting())))
                .ifPresent(System.out::println);
    }

    private static void testReducing(){
        System.out.println("testReducing");
//        menu.stream().collect(Collectors.reducing(BinaryOperator.minBy(Comparator.comparingInt(Dish::getCalories))))
//                .ifPresent(System.out::println);
        menu.stream().reduce(BinaryOperator.minBy(Comparator.comparingInt(Dish::getCalories))).ifPresent(System.out::println);
    }

    private static void testReducingWithIdentity(){
        System.out.println("testReducingWithIdentity");
//        Integer collect = menu.stream().map(Dish::getCalories).collect(Collectors.reducing(0, (d1, d2) -> d1 + d2));
        Integer collect = menu.stream().map(Dish::getCalories).reduce(0, (d1, d2) -> d1 + d2);
        System.out.println(collect);
    }

    private static void testReducingWithIdentityAndMapper(){
        System.out.println("testReducingWithIdentityAndMapper");
//        Integer collect = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (d1, d2) -> d1 + d2));
        Integer collect = menu.stream().map(Dish::getCalories).reduce(0, (d1, d2) -> d1 + d2);
        System.out.println(collect);
    }

    private static void testSummingDouble(){
        System.out.println("testSummingDouble");
//        Integer collect = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        Integer collect = menu.stream().mapToInt(Dish::getCalories).sum();
        System.out.println(collect);
    }
}
