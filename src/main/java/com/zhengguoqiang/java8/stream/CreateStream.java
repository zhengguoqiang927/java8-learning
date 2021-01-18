package com.zhengguoqiang.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author zhengguoqiang
 */
public class CreateStream {
    public static void main(String[] args) {
//        createStreamFromCollection().forEach(System.out::println);
//        Stream<String> streamFromFiles = createStreamFromFiles();
//        System.out.println(streamFromFiles);
//        createStreamFromIterator().forEach(System.out::println);
//        createStreamFromGenerator().forEach(System.out::println);
        creatObjStreamFromGenerator().forEach(System.out::println);
    }

    /**
     * Generate the stream object from collection
     *
     * @return
     */
    private static Stream<String> createStreamFromCollection() {
        List<String> list = Arrays.asList("welcome", "to", "zhengguoqiang");
        return list.stream();
    }

    private static Stream<String> createStreamFromValues() {
        return Stream.of("welcome", "to", "zhengguoqiang");
    }

    private static Stream<String> createStreamFromArrays() {
        return Arrays.stream(new String[]{"welcome", "to", "zhengguoqiang"});
    }

    private static Stream<String> createStreamFromFiles() {
        Path path = Paths.get("/Users/zhengguoqiang/Projects/IdeaProjects/zhengguoqiang/java8-learning/src/main/java/com/zhengguoqiang/stream/CreateStream.java");
        //无法用返回的流，因为try-with-source会自动关闭流
        try (Stream<String> streamFromFile = Files.lines(path)) {
            streamFromFile.forEach(System.out::println);
            return streamFromFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Integer> createStreamFromIterator() {
        return Stream.iterate(0, n -> n + 2).limit(10);
    }

    private static Stream<Double> createStreamFromGenerator() {
        return Stream.generate(Math::random).limit(10);
    }

    private static Stream<Obj> creatObjStreamFromGenerator() {
        return Stream.generate(new ObjSupplier()).limit(10);
    }

    private static class ObjSupplier implements Supplier<Obj> {
        private int index = 0;
        private Random random = new Random(System.currentTimeMillis());

        @Override
        public Obj get() {
            index = random.nextInt(100);
            return new Obj(index, "Name -> " + index);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Obj {
        private int id;
        private String name;
    }
}
