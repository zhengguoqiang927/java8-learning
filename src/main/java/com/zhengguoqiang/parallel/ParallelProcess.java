package com.zhengguoqiang.parallel;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author zhengguoqiang
 */
public class ParallelProcess {

    private static long iterateStream(long limit) {
        return Stream.iterate(1L, i -> i + 1L)
                .limit(limit).reduce(0L, Long::sum);
    }

    private static long parallelStream(long limit) {
        return Stream.iterate(1L, i -> i + 1L)
                .mapToLong(Long::longValue)
                .parallel()
                .limit(limit).reduce(0L, Long::sum);
    }

    private static long parallelStream2(long limit) {
        return LongStream.rangeClosed(0,limit).parallel().sum();
    }

    private static long normalAdd(long limit) {
        long sum = 0L;
        for (long i = 1L; i <= limit; i++) {
            sum += i;
        }
        return sum;
    }

    private static long measureSumPerformance(Function<Long, Long> adder, long limit) {
        long fasttime = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long starttime = System.currentTimeMillis();
            long sum = adder.apply(limit);
            long duration = System.currentTimeMillis() - starttime;
            System.out.println("The result of sum is " + sum);
            if (duration < fasttime) fasttime = duration;
        }
        return fasttime;
    }

    public static void main(String[] args) {
        System.out.println("The best process time(normalAdd) is " +
                measureSumPerformance(ParallelProcess::normalAdd, 100_000_000) + " ms");
//        System.out.println("The best process time(iterateStream) is " +
//                measureSumPerformance(ParallelProcess::iterateStream, 50_000_000) + " ms");
//        System.out.println("The best process time(parallelStream) is " +
//                measureSumPerformance(ParallelProcess::parallelStream, 50_000_000) + " ms");
        System.out.println("The best process time(parallelStream2) is " +
                measureSumPerformance(ParallelProcess::parallelStream2, 100_000_000) + " ms");
    }
}
