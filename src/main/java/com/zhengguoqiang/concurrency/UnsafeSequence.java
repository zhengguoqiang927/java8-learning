package com.zhengguoqiang.concurrency;

public class UnsafeSequence {
    private int value;

    public int getNext() {
        return value++;
    }

    public static void main(String[] args) throws InterruptedException {
        UnsafeSequence sequence = new UnsafeSequence();
        for (int i = 0; i < 20000; i++) {
            new Thread(() -> sequence.getNext()).start();
        }
        System.out.println(sequence.getNext());
    }
}
