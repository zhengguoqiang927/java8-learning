package com.zhengguoqiang.java8.function;

/**
 * @author zhengguoqiang
 */
@FunctionalInterface
public interface ThreeFunction<T, U, K, R> {
    R apply(T t, U u, K k);
}
