package com.zhengguoqiang.guava;

import com.github.onblog.commoon.entity.RateLimiterRule;
import com.github.onblog.commoon.entity.RateLimiterRuleBuilder;
import com.github.onblog.core.limiter.RateLimiter;
import com.github.onblog.core.limiter.RateLimiterFactory;

import java.util.concurrent.TimeUnit;

public class GuavaTest {

    public static void main(String[] args) {
        /*RateLimiter rateLimiter = RateLimiter.create(2,3,TimeUnit.SECONDS);

        while(true){
            System.out.println("get 1 tokens:" + rateLimiter.acquire(1) + " s");
            System.out.println("get 1 tokens:" + rateLimiter.acquire(1) + " s");
            System.out.println("get 1 tokens:" + rateLimiter.acquire(1) + " s");
            System.out.println("get 1 tokens:" + rateLimiter.acquire(1) + " s");
            System.out.println("end");
        }*/
        RateLimiterRule rateLimiterRule = new RateLimiterRuleBuilder()
                .setLimit(1)
                .setPeriod(2)
                .setUnit(TimeUnit.SECONDS)
                .build();

        RateLimiter limiter = RateLimiterFactory.of(rateLimiterRule);

        long time = System.currentTimeMillis();
        while (true) {
            if (limiter.tryAcquire()) {
                System.out.println("get 1 tokens:" + (System.currentTimeMillis() - time) / 1000 + " s");
            }
            time = System.currentTimeMillis();
        }
    }
}
