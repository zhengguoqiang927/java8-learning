package com.zhengguoqiang.jvm;

public class NoClassDefFoundErrorDemo {
    public static void main(String[] args) {
        try {
            SimpleCalculator calculator = new SimpleCalculator();
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e);
        }
        SimpleCalculator simpleCalculator = new SimpleCalculator();
    }
}
