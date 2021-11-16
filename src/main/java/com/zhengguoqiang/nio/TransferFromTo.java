package com.zhengguoqiang.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class TransferFromTo {

    public static void main(String[] args) {
        try {
            RandomAccessFile fromFile = new RandomAccessFile("/Users/zhengguoqiang/Documents/xml/fromFile.txt", "rw");
            FileChannel fromFileChannel = fromFile.getChannel();

            RandomAccessFile toFile = new RandomAccessFile("/Users/zhengguoqiang/Documents/xml/toFile.txt", "rw");
            FileChannel toFileChannel = toFile.getChannel();

            long position = 0;
            long count = fromFileChannel.size();

            toFileChannel.transferFrom(fromFileChannel, position, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
