package com.zhengguoqiang.senior.classloader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MyClassLoaderParentFirst extends ClassLoader {

    private Map<String, String> classPathMap = new HashMap<>();

    private ClassLoader jdkClassLoader;

    public MyClassLoaderParentFirst(ClassLoader classLoader) {
        this.jdkClassLoader = classLoader;
        classPathMap.put("com.zhengguoqiang.senior.classloader.TestA", "/Users/zhengguoqiang/Projects/IdeaProjects/zhengguoqiang/java8-learning/target/classes/com/zhengguoqiang/senior/classloader/TestA.class");
        classPathMap.put("com.zhengguoqiang.senior.classloader.TestB", "/Users/zhengguoqiang/Projects/IdeaProjects/zhengguoqiang/java8-learning/target/classes/com/zhengguoqiang/senior/classloader/TestB.class");
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class result = null;
        try {
            //核心类库和扩展类库还有原类加载器进行加载
            result = jdkClassLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (result != null) {
            return result;
        }

        String classPath = classPathMap.get(name);
        File file = new File(classPath);
        if (!file.exists()) {
            throw new ClassNotFoundException();
        }

        byte[] classBytes = getClassData(file);
        if (classBytes.length == 0) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, classBytes, 0, classBytes.length);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = classPathMap.get(name);
        File file = new File(classPath);
        if (!file.exists()) {
            throw new ClassNotFoundException();
        }
        byte[] classBytes = getClassData(file);
        if (classBytes.length == 0) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, classBytes, 0, classBytes.length);
    }

    private byte[] getClassData(File file) {
        try (InputStream ins = new FileInputStream(file); ByteArrayOutputStream baos =
                new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesNumRead = 0;
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


}
