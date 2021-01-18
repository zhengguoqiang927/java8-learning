package com.zhengguoqiang.senior.classloader;

import com.zhengguoqiang.utils.FileHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @author zhengguoqiang
 */

public class GlobalClassLoader {
    private static Method addURL;
    private static URLClassLoader system;

    static {
        try {
            addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        addURL.setAccessible(true);
        system = (URLClassLoader) getSystemClassLoader();
    }

    private static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    public static void addURL2SystemClassLoader(URL url) {
        try {
//            log.info("append jar to classpath:" + url.toString());
            addURL.invoke(system, url);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void addSystemClassPathFolder(String... dirs) throws IOException {
        List<String> uniqueLibPath = FileHelper.getUniqueLibPath(dirs);
        for (String path : uniqueLibPath) {
            URL url = new URL("file", "", path);
            addURL2SystemClassLoader(url);
        }
    }
}
