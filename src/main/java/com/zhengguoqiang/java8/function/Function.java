package com.zhengguoqiang.java8.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

/**
 * FunctionalInterface Predicate/Consumer/Function/Supplier
 * Predicate boolean test(T t) BiPredicate/IntPredicate/LongPredicate/DoublePredicate
 * Consumer void accept(T t) BiConsumer/IntConsumer/LongConsumer/DoubleConsumer
 * Function R apply(T t) BiFunction/IntFunction/LongFunction/DoubleFunction
 * Supplier T get() BooleanSupplier/IntSupplier/LongSupplier/DoubleSupplier
 *
 * @author zhengguoqiang
 */
public class Function {

    private static List<Apple> filter(List<Apple> list, Predicate<String> p) {
        //boolean test(T t);
        List<Apple> result = new ArrayList<>();
        for (Apple apple : list) {
            if (p.test(apple.getColor()))
                result.add(apple);
        }
        return result;
    }

    private static List<Apple> filterByWeight(List<Apple> list, IntPredicate p) {
        //boolean test(int t)
        List<Apple> result = new ArrayList<>();
        for (Apple apple : list) {
            if (p.test(apple.getWeight()))
                result.add(apple);
        }
        return result;
    }

    private static List<Apple> filterByAll(List<Apple> list, BiPredicate<String, Integer> p) {
        //boolean test(T t,U u)
        List<Apple> result = new ArrayList<>();
        for (Apple apple : list) {
            if (p.test(apple.getColor(), apple.getWeight()))
                result.add(apple);
        }
        return result;
    }

    private static void consumer(List<Apple> list, Consumer<Apple> consumer){
        for (Apple apple:list){
            consumer.accept(apple);
        }
    }

    private static Apple functionWeightDouble(Apple apple, java.util.function.Function<Apple,Apple> function){
        return function.apply(apple);
    }

    private static Apple supplierApple(Supplier<Apple> supplier){
        return supplier.get();
    }


    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
                new Apple("orange", 190),
                new Apple("green", 150),
                new Apple("yellow", 150),
                new Apple("red", 170));

        List<Apple> greens = filter(apples, s -> s.equals("green"));
        System.out.println("filterByColor:" + greens);

        List<Apple> weight = filterByWeight(apples, value -> value > 130);
        System.out.println("filterByWeight" + weight);

        List<Apple> all = filterByAll(apples, (s, integer) -> s.equals("green") && integer > 130);
        System.out.println("filterByAll" + all);

        consumer(apples, System.out::println);

        Apple weightDouble = functionWeightDouble(apples.get(0), apple -> new Apple(apple.getColor(), apple.getWeight() * 2));
        System.out.println("weightDouble:" + weightDouble);

        Supplier<String> supplier = String::new;
        System.out.println(supplier.get().getClass());

        Apple apple = supplierApple(() -> new Apple("blue", 190));
        System.out.println("apple:" + apple);

        apples.sort(Comparator.comparingInt(Apple::getWeight).reversed());
        System.out.println(apples);
    }
}
