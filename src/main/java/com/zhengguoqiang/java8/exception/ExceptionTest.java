package com.zhengguoqiang.java8.exception;

public class ExceptionTest {

    public static int f(int n){
        int r = -1;
        try{
            r = n*n;
        }finally {
            if (n == 2) return 0;
        }
        return r;
    }

    public static void main(String[] args) {
        System.out.println(f(2));
    }
}
