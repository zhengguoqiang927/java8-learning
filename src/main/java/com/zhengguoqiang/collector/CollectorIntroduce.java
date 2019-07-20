package com.zhengguoqiang.collector;

import com.zhengguoqiang.function.Apple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhengguoqiang
 */
public class CollectorIntroduce {

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
                new Apple("orange", 190),
                new Apple("green", 150),
                new Apple("yellow", 150),
                new Apple("red", 170),
                new Apple("orange", 190),
                new Apple("green", 150),
                new Apple("yellow", 150),
                new Apple("yellow", 170));
        Optional.ofNullable(groupByNormal(apples)).ifPresent(System.out::println);
        System.out.println("============================");
        Optional.ofNullable(groupByStream(apples)).ifPresent(System.out::println);
        System.out.println("============================");
        Optional.ofNullable(groupByCollector(apples)).ifPresent(System.out::println);
    }

    public static Map<String,List<Apple>> groupByNormal(List<Apple> apples){
        Map<String,List<Apple>> map = new HashMap<>();
        for (Apple apple:apples){
            //老的实现
            /*List<Apple> list = map.get(apple.getColor());
            if (list == null){
                list = new ArrayList<>();
                map.put(apple.getColor(),list);
            }
            list.add(apple);*/
            //java8之后普通实现
            map.computeIfAbsent(apple.getColor(),k -> new ArrayList<>()).add(apple);
        }
        return map;
    }

    public static Map<String,List<Apple>> groupByStream(List<Apple> apples){
        Map<String,List<Apple>> map = new HashMap<>();
        apples.forEach(apple -> {
            List<Apple> colorList = Optional.ofNullable(map.get(apple.getColor())).orElseGet(() -> {
                List<Apple> list = new ArrayList<>();
                map.put(apple.getColor(), list);
                return list;
            });
            colorList.add(apple);
        });
        return map;
    }

    public static Map<String,List<Apple>> groupByCollector(List<Apple> apples){
        return apples.stream().collect(Collectors.groupingBy(Apple::getColor));
    }
}
