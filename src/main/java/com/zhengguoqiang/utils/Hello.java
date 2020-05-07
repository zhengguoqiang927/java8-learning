package com.zhengguoqiang.utils;

public class Hello {
    public static void main(String[] args) {
        Hello hello = new Hello();
        hello = null;

        //Requesting JVM to call Garbage Collector method
        System.gc();
        System.out.println("Main Completes");
    }

    /**
     * 在垃圾收集器删除/销毁可回收的对象之前调用该对象的finalize方法，以进行清理活动
     * 主要清理跟该对象有关联的资源比如数据库连接、网络连接，目的就是取消该对象上的资源分配
     * 该方法执行完之后垃圾收集器立即销毁该对象
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize method overriden");
    }
}
