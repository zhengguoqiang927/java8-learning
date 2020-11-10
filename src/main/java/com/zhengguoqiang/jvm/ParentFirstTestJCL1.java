package com.zhengguoqiang.jvm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;

public class ParentFirstTestJCL1 {
    public static void main(String[] args) throws Exception {

        URLClassLoader childClassLoader = new URLClassLoader(new URL[] {
                new URL("file:box1.jar"),
                new URL("file:lib/commons-logging.jar"),
                new URL("file:lib/log4j.jar")});

        Thread.currentThread().setContextClassLoader(childClassLoader);

        Log log = LogFactory.getLog(ParentFirstTestJCL1.class);
        log.info("xxxx");
        log.isDebugEnabled();
//        Class boxClass = childClassLoader.loadClass("box.BoxImplWithJCL");
//        Box box = (Box) boxClass.newInstance();
//        box.doOp();
        Logger logger = LoggerFactory.getLogger(ParentFirstTestJCL1.class);
        logger.info("this is a info:{}","xxx");
        logger.debug("xxxx:{}","xx");
    }
}
