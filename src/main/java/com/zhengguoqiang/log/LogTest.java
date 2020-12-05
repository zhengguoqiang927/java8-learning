package com.zhengguoqiang.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogTest {

    public void print(String s){
        Log log = LogFactory.getLog(LogTest.class);
        log.info("xxxx");
    }

    public static void main(String[] args) {

//        Log log = LogFactory.getLog(LogTest.class);
//        log.info("xxxx");
//        Logger logger = LoggerFactory.getLogger(LogTest.class);
//        logger.info("xxxxx");
//        logger.info("error info.");
//        App app = new App();
//        app.logParent("child");
//        Logger logger = LoggerFactory.getLogger(LogTest.class);
//        logger.info("scf2");

//        Logger logger = LogManager.getLogger(LogTest.class);
//        logger.info("test async logger.");

        Log log = LogFactory.getLog(LogTest.class);
        log.info("xxxx");

    }
}
