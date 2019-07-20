package com.zhengguoqiang.collector;

import com.zhengguoqiang.stream.Dish;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhengguoqiang
 */
public class CollectorComplex {
    public static final List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH));

    public static void main(String[] args) {
        testAveragingDouble(menu);
        testAveragingInt(menu);
        testAveragingLong(menu);
        testCollectingAndThen(menu);
        testCounting(menu);
        testGroupingByFunction(menu);
        testGroupingByFunctionAndReduce(menu);
        testGroupingByFunctionAndSupplierAndReduce(menu);
        testSummarizingInt(menu);
    }

    private static void testAveragingDouble(List<Dish> menu) {
        System.out.println("testAveragingDouble");
        Optional.ofNullable(menu.stream().collect(Collectors.averagingDouble(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    private static void testAveragingInt(List<Dish> menu) {
        System.out.println("testAveragingInt");
        Optional.ofNullable(menu.stream().collect(Collectors.averagingInt(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    private static void testAveragingLong(List<Dish> menu) {
        System.out.println("testAveragingLong");
        Optional.ofNullable(menu.stream().collect(Collectors.averagingLong(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    private static void testCollectingAndThen(List<Dish> menu) {
        System.out.println("testCollectingAndThen");
        List<Dish> collect = menu.stream().filter(dish -> Dish.Type.MEAT.equals(dish.getType()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
//        collect.add(new Dish("",true,100,Dish.Type.OTHER));
        System.out.println(collect);
    }

    private static void testCounting(List<Dish> menu) {
        System.out.println("testCounting");
        Optional.ofNullable(menu.stream().collect(Collectors.counting()))
                .ifPresent(System.out::println);
    }

    private static void testGroupingByFunction(List<Dish> menu){
        System.out.println("testGroupingByFunction");
        Optional.ofNullable(menu.stream().collect(Collectors.groupingBy(Dish::getType)))
                .ifPresent(System.out::println);
    }

    private static void testGroupingByFunctionAndReduce(List<Dish> menu){
        System.out.println("testGroupingByFunctionAndReduce");
        Map<Dish.Type, Double> collect = menu.stream().collect(Collectors.groupingBy(Dish::getType, Collectors.averagingDouble(Dish::getCalories)));
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    private static void testGroupingByFunctionAndSupplierAndReduce(List<Dish> menu){
        System.out.println("testGroupingByFunctionAndSupplierAndReduce");
        TreeMap<Dish.Type, Long> collect = menu.stream().collect(Collectors.groupingBy(Dish::getType, TreeMap::new, Collectors.counting()));
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    private static void testSummarizingInt(List<Dish> menu){
        System.out.println("testSummarizingInt");
        IntSummaryStatistics collect = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }


}
