package com.zhengguoqiang.stream;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author zhengguoqiang
 */
public class StreamOperation {
    public static void main(String[] args) {
//        operFlatMap();
//        operMatch();
//        operFind();
//        operReduce();
        operNumeric();
    }

    private static void operFlatMap() {
        String[] h = {"Hello", "World"};

        //{"H,e,l,l,o","W,o,r,l,d"}
        Stream<String[]> stream = Arrays.stream(h).map(s -> s.split(""));

        Stream<String> stream1 = stream.flatMap(Arrays::stream);
        stream1.distinct().forEach(System.out::println);
    }

    private static void operMatch() {
        Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        boolean b = stream.allMatch(integer -> integer > 10);
        System.out.println(b);

        Stream<Integer> stream1 = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        boolean b1 = stream1.anyMatch(integer -> integer > 6);
        System.out.println(b1);

        Stream<Integer> stream2 = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        boolean b2 = stream2.noneMatch(integer -> integer < 0);
        System.out.println(b2);
    }

    private static void operFind() {
        Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        Optional<Integer> any = stream.filter(integer -> integer % 2 == 0).findAny();
        System.out.println(any.orElse(-1));

        Stream<Integer> stream1 = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        Optional<Integer> first = stream1.filter(integer -> integer > 2).findFirst();
        System.out.println(first.filter(integer -> integer % 2 == 0).orElse(-1));
    }

    /**
     * 聚合操作
     */
    private static void operReduce() {
        Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        Integer reduce = stream.reduce(0, (integer, integer2) -> integer + integer2);
        System.out.println(reduce);

        Stream<Integer> stream1 = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        Integer reduce1 = stream1.reduce(0, Integer::sum);
        System.out.println(reduce1);

        Stream<Integer> stream2 = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        stream2.filter(i -> i % 2 == 0).reduce(BinaryOperator.maxBy(Comparator.comparingInt(Integer::intValue))).ifPresent(System.out::println);
    }

    private static void operNumeric() {
        Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        //元素为包装类的流转换为基本类型的流，节省内存空间
        IntStream intStream = stream.mapToInt(Integer::intValue);
        int sum = intStream.filter(i -> i > 3).sum();
        System.out.println(sum);

        int a = 9;
        IntStream.rangeClosed(1, 100)
                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
                .forEach(r -> System.out.println("a=" + r[0] + ",b=" + r[1] + ",c=" + r[2]));
    }
}
