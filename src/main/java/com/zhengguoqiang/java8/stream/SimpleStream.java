package com.zhengguoqiang.java8.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhengguoqiang
 */
public class SimpleStream {

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));

//        List<String> dishNamesByCollections = getDishNamesByCollections(menu);
//        System.out.println(dishNamesByCollections);

        List<String> dishNamesByStream = getDishNamesByStream(menu);
        System.out.println(dishNamesByStream);
    }

    private static List<String> getDishNamesByStream(List<Dish> menu) {
        return menu.parallelStream()
                .filter(dish -> {
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return dish.getCalories() < 400;
                })
                .sorted(Comparator.comparingInt(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    private static List<String> getDishNamesByCollections(List<Dish> menu) {
        List<Dish> lowCalories = new ArrayList<>();

        for (Dish dish : menu) {
            if (dish.getCalories() < 400) {
                lowCalories.add(dish);
            }
        }

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lowCalories.sort(Comparator.comparingInt(Dish::getCalories));
        List<String> names = new ArrayList<>();
        for (Dish dish : lowCalories) {
            names.add(dish.getName());
        }

        return names;
    }
}
