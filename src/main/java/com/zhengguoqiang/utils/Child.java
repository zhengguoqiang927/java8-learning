package com.zhengguoqiang.utils;

public class Child extends Parent{

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    void println(String s){
        System.out.println("Child:" + s);
    }

    public static void main(String[] args) {
        Child child = new Child();
        child.println("1");
        final int b;
        System.out.println("xxxxxx");
        b = 12;
        int[] arr = {1,2,3};
        for (final int a:arr){
            System.out.println(a);
        }
    }
}
