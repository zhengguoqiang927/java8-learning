package com.zhengguoqiang.senior;

import jdk.internal.vm.annotation.Contended;

public class Counter {
    @Contended
    public volatile long count1 = 0;
    public volatile long count2 = 0;
}
