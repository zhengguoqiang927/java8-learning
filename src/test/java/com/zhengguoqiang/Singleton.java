package com.zhengguoqiang;

public class Singleton {
    private Singleton(){}

    //饿汉式
//    private static volatile Singleton singleton = null;




    public static Singleton getInstance(){
//        if (singleton == null){
//            synchronized (Singleton.class){
//                if (singleton == null){
//                    singleton = new Singleton();
//                }
//            }
//        }
//        return singleton;
        return SingletonHolder.singleton;
    }


    private static class SingletonHolder{
        private static final Singleton singleton = new Singleton();
    }
}
