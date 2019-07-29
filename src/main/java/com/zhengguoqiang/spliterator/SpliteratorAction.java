package com.zhengguoqiang.spliterator;

import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author zhengguoqiang
 */
public class SpliteratorAction {

    public static void main(String[] args) {
        String text = "* <p><a name=\"binding\">A Spliterator that does not report {@code IMMUTABLE} or\n" +
                " * {@code CONCURRENT} is expected to have a documented policy concerning:\n" +
                " * when the spliterator <em>binds</em> to the element source; and detection of\n" +
                " * structural interference of the element source detected after binding.</a>  A\n" +
                " * <em>late-binding</em> Spliterator binds to the source of elements at the\n" +
                " * point of first traversal, first split, or first query for estimated size,\n" +
                " * rather than at the time the Spliterator is created.  A Spliterator that is\n" +
                " * not <em>late-binding</em> binds to the source of elements at the point of\n" +
                " * construction or first invocation of any method.  Modifications made to the\n" +
                " * source prior to binding are reflected when the Spliterator is traversed.\n" +
                " * After binding a Spliterator should, on a best-effort basis, throw\n" +
                " * {@link ConcurrentModificationException} if structural interference is\n" +
                " * detected.  Spliterators that do this are called <em>fail-fast</em>.  The\n" +
                " * bulk traversal method ({@link #forEachRemaining forEachRemaining()}) of a\n" +
                " * Spliterator may optimize traversal and check for structural interference\n" +
                " * after all elements have been traversed, rather than checking per-element and\n" +
                " * failing immediately.";
        MySpliteratorText mySpliteratorText = new MySpliteratorText(text);
        Optional.of(mySpliteratorText.stream().count()).ifPresent(System.out::println);

//        mySpliteratorText.stream().forEach(System.out::println);
        mySpliteratorText.parallelStream().forEach(System.out::println);
    }

    static class MySpliteratorText {
        private final String[] data;

        public MySpliteratorText(String data) {
            Objects.requireNonNull(data, "The parameter can not be null");
            this.data = data.split("\n");
        }

        public Stream<String> stream() {
            return StreamSupport.stream(new MySpliterator(), false);
        }

        public Stream<String> parallelStream(){
            return StreamSupport.stream(new MySpliterator(),true);
        }

        private class MySpliterator implements Spliterator<String> {
            private int start, end;

            public MySpliterator() {
                this.start = 0;
                this.end = MySpliteratorText.this.data.length - 1;
            }

            public MySpliterator(int start, int end) {
                this.start = start;
                this.end = end;
            }

            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                if (start <= end) {
                    action.accept(MySpliteratorText.this.data[start++]);
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<String> trySplit() {
                int mid = (end - start) / 2;
                if (mid < 1) return null;

                int left = start;
                int right = start + mid;
                start = start + mid + 1;
                return new MySpliterator(left, right);
            }

            @Override
            public long estimateSize() {
                return end - start;
            }

            @Override
            public int characteristics() {
                return IMMUTABLE;
            }
        }
    }
}
