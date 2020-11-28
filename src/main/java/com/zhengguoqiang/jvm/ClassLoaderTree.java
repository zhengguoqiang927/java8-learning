package com.zhengguoqiang.jvm;

import java.net.URLClassLoader;

public class ClassLoaderTree {
    public static void main(String[] args) {
        ClassLoader classLoader = ClassLoaderTree.class.getClassLoader();
        while (classLoader != null){
            System.out.println(classLoader.toString());
            classLoader = classLoader.getParent();
        }

        String name = Thread.currentThread().getName();
        System.out.println("thread name:" + name);

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader.toString());
    }
}
