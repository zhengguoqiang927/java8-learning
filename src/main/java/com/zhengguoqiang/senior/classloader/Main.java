package com.zhengguoqiang.senior.classloader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws Exception {
        MyClassLoaderParentFirst classloader = new MyClassLoaderParentFirst(Thread.currentThread().getContextClassLoader().getParent());
        Class<?> aClass = classloader.loadClass("com.zhengguoqiang.senior.classloader.TestA");
        Method main = aClass.getDeclaredMethod("main", String[].class);
        main.invoke(null,new Object[]{args});


        //获取当前类相对路径的资源，return：file:/Users/zhengguoqiang/Projects/IdeaProjects/zhengguoqiang/java8-learning/target/classes/com/zhengguoqiang/senior/classloader/
        System.out.println(Main.class.getResource(""));
        //获取classpath根路径下的资源，return：file:/Users/zhengguoqiang/Projects/IdeaProjects/zhengguoqiang/java8-learning/target/classes/
        System.out.println(Main.class.getResource("/"));
        //获取classpath根路径下的资源，return：file:/Users/zhengguoqiang/Projects/IdeaProjects/zhengguoqiang/java8-learning/target/classes/
        System.out.println(Main.class.getClassLoader().getResource(""));
        //返回null，ClassLoader.getResource()方法路径不能以/开头
        System.out.println(Main.class.getClassLoader().getResource("/"));
        //获取classpath根路径下的log4j2.xml文件
        URL resource = Main.class.getClassLoader().getResource("log4j2.xml");
        System.out.println(resource);
        if (resource == null) {
            throw new ClassNotFoundException();
        }
        File file = new File(resource.toURI());
        if (!file.exists()){
            throw new FileNotFoundException();
        }

        try(BufferedInputStream baos = new BufferedInputStream(new FileInputStream(file))){
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[4096];
            int bytesNum = 0;
            while ((bytesNum = baos.read(buffer)) != -1){
                sb.append(new String(buffer,0,bytesNum));
            }
            System.out.println(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
