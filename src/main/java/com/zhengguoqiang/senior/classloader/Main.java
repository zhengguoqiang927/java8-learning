package com.zhengguoqiang.senior.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {
        MyClassLoaderParentFirst classloader = new MyClassLoaderParentFirst(Thread.currentThread().getContextClassLoader().getParent());
        Class<?> aClass = classloader.loadClass("com.zhengguoqiang.senior.classloader.TestA");
        Method main = aClass.getDeclaredMethod("main", String[].class);
        main.invoke(null,new Object[]{args});
    }
}
