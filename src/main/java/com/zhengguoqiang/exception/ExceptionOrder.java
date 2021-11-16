package com.zhengguoqiang.exception;

import java.io.*;

public class ExceptionOrder {
    public static void main(String[] args) {

        try (InputStream inputStream = new FileInputStream(new File("/Users/zhengguoqiang/Desktop/xx.txt"))) {
            int read = inputStream.read();
            System.out.println((char) read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
