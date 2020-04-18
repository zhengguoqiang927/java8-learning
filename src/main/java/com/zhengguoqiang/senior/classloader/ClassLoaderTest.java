package com.zhengguoqiang.senior.classloader;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.SecureClassLoader;

/**
 * @author zhengguoqiang
 */
@Slf4j
public class ClassLoaderTest {

    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        String path = "/Users/zhengguoqiang/Server/scf/service/deploy/commercialPostServer";
//        List<String> uniqueLibPath = FileHelper.getUniqueLibPath(path);
//        log.info(JSON.toJSONString(uniqueLibPath));
        GlobalClassLoader.addSystemClassPathFolder(path);
        DynamicClassLoader classLoader = new DynamicClassLoader();
        try {
            Class<?> aClass = classLoader.loadClass("com.bj58.fang.commercial.post.Init");
            Object o = aClass.newInstance();
            log.info("Hash:{}",o.hashCode());
            System.out.println(aClass.getSimpleName());
            Method[] methods = aClass.getDeclaredMethods();
            System.out.println(JSON.toJSONString(methods));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class DynamicClassLoader extends SecureClassLoader{
    public DynamicClassLoader(){
    }
}
