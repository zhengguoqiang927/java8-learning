package com.zhengguoqiang.concurrency.multithread;

/**
 * @author zhengguoqiang
 */
public class Bank {

    private static void testVersion1(){
        Thread t1 = new TicketWindow("一号柜台");
        t1.start();

        Thread t2 = new TicketWindow("二号柜台");
        t2.start();


        Thread t3 = new TicketWindow("三号柜台");
        t3.start();
    }

    private static void testVersion2(){
        final TicketWindowRunnable ticketWindowRunnable =
                new TicketWindowRunnable();
        Thread t1 = new Thread(ticketWindowRunnable,"一号柜台");
        t1.start();

        Thread t2 = new Thread(ticketWindowRunnable,"二号柜台");
        t2.start();

        Thread t3 = new Thread(ticketWindowRunnable,"三号柜台");
        t3.start();
    }

    public static void main(String[] args) {
//        testVersion1();
        testVersion2();
    }
}
