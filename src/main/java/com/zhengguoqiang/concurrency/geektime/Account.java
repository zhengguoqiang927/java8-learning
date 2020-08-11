package com.zhengguoqiang.concurrency.geektime;

public class Account {
    private int balance;

    //方案一
    //缺点：需要保证传入的lock必须是同一个对象，如果不是同一个对象转账操作不在线程安全
    private Object lock;
    private Account(){}
    // 创建Account时传入同一个lock对象
    public Account(Object lock){
        this.lock = lock;
    }
    //转账
    void transfer1(Account target,int amt){
        //所有对象共享一把锁，所有对象的转账操作串行执行
        synchronized (lock){
            if (this.balance > amt){
                this.balance -= amt;
                target.balance += amt;
            }
        }
    }

    //方案二
    //解决方案一中锁的唯一性，采用Account.class来替代，Account.class是所有Account对象共享的
    //缺点：所有账户的转账操作依然是串行的
    void transfer2(Account target,int amt){
        synchronized (Account.class){
            if (this.balance > amt){
                this.balance -= amt;
                target.balance += amt;
            }
        }
    }

    //方案三
    //采用细粒度锁，只对涉及转账的两个账户上锁
    //缺点：容易造成死锁
    void transfer3(Account target,int amt){
        synchronized (this){
            synchronized (target){
                if (this.balance > amt){
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }

    //方案四
    void transfer4(Account target,int amt){

    }
}
