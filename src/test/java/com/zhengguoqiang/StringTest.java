package com.zhengguoqiang;

import org.junit.Test;

public class StringTest {
    private static final String a = "123";
    private static final String b = "123";

    @Test
    public void test(){
        System.out.println(a == b);
        System.out.println(a.equals(b));
        int[] array = new int[]{};

        String s = "Aman";
        System.out.println(s.hashCode());
    }
}
