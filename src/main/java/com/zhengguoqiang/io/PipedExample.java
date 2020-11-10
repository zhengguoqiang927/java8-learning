package com.zhengguoqiang.io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipedExample {
    public static void main(String[] args) throws IOException {
        final PipedOutputStream outputStream = new PipedOutputStream();
//        final PipedInputStream inputStream = new PipedInputStream(outputStream);
        final PipedInputStream inputStream = new PipedInputStream();
        inputStream.connect(outputStream);

        //写入数据线程
        new Thread(() -> {
            try {
                outputStream.write("Hello World,Piped!".getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                int data = inputStream.read();
                while (data != -1){
                    System.out.print((char)data);
                    data = inputStream.read();
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
