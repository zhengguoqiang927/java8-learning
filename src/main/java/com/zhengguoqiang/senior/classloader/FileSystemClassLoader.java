package com.zhengguoqiang.senior.classloader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
