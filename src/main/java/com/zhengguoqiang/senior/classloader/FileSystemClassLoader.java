package com.zhengguoqiang.senior.classloader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类 FileSystemClassLoader 继承自类 java.lang.ClassLoader 。
 * 在 java.lang.ClassLoader 类介绍 中列出的 java.lang.ClassLoader 类的常用方法中，
 * 一般来说，自己开发的类加载器只需要覆写 findClass(String name) 方法即可。
 * java.lang.ClassLoader 类的方法 loadClass() 封装了前面提到的代理模式的实现。
 * 该方法会首先调用 findLoadedClass() 方法来检查该类是否已经被加载过；
 * 如果没有加载过的话，会调用父类加载器的 loadClass() 方法来尝试加载该类；
 * 如果父类加载器无法加载该类的话，就调用 findClass() 方法来查找该类。
 * 因此，为了保证类加载器都正确实现代理模式，在开发自己的类加载器时，最好不要覆写 loadClass() 方法，而是覆写 findClass() 方法。
 *
 * 类 FileSystemClassLoader 的 findClass() 方法首先根据类的全名在硬盘上查找类的字节代码文件（.class 文件），
 * 然后读取该文件内容，最后通过 defineClass() 方法来把这些字节代码转换成 java.lang.Class 类的实例。
 */
public class FileSystemClassLoader extends ClassLoader {
    private String rootDir;

    public FileSystemClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getClassData(name);
        if (classData == null){
            throw new ClassNotFoundException();
        }else {
            return defineClass(name,classData,0,classData.length);
        }
    }

    private byte[] getClassData(String className){
        String path = classNameToPath(className);
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            is = new FileInputStream(path);
            os = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead = 0;
            while ((bytesNumRead = is.read(buffer)) != -1){
                os.write(buffer,0,bytesNumRead);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String classNameToPath(String className){
        return rootDir + File.separatorChar +
                className.replace('.',File.separatorChar) + ".class";
    }

    public static void main(String[] args) {
        String rootDir = "/Users/zhengguoqiang/Projects/IdeaProjects/zhengguoqiang/java8-learning/target/classes";
        FileSystemClassLoader fileSystemClassLoader = new FileSystemClassLoader(rootDir);
        try {
            Class<?> aClass = fileSystemClassLoader.loadClass("com.zhengguoqiang.log.LogTest");
            Method method = aClass.getMethod("print", String.class);
            Object instance = aClass.newInstance();
            method.invoke(instance, "LogTest reflection call print method.");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
