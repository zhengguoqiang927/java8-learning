# java8-learning

## Fork/Join

### Overview

fork/join框架基于ExecutorService接口实现，充分利用现代服务器多核的性质，该框架可以将任务递归地分解成更小的任务（pieces），然后利用多核特性执行分解后的任务来提高应用性能。

fork/join框架将任务分配给线程池里的工作线程，fork/join框架采用任务窃取算法，完成任务的线程会去其他繁忙的线程的任务队列里窃取任务拿来执行。

### ForkJoinPool

ForkJoinPool是fork/join框架的核心，实现ExecutorService，能够管理工作线程并提供工具用来查询线程池状态和性能等相关信息。

工作线程一次只能执行一个任务，但是ForkJoinPool不会为每一个子任务都创建一个单独的线程。线程池的每一个线程都有一个用来存储任务的双端队列（double-ended queue or deque）。

平衡工作线程负载最重要的就是依赖任务窃取算法（work-stealing algorithm）

### Work Stealing Algorithm

简单的说就是空闲的线程从繁忙的线程的双端队列尾部偷取任务并执行

### ForkJoinPool 实例化

Java8里最方便的获取forkjoinpool实例的方式是使用它的静态方法commonPool()，生成的线程池是默认的线程池。

根据oracle官方文档，使用预定义的commonpool能够减少资源消耗，因为它不鼓励为每个任务单独创建线程。

创建方式：

```java
//第一种
ForkJoinPool commonPool = ForkJoinPool.commonPool();

//第二种
public class PoolUtil {
    public static ForkJoinPool forkJoinPool = new ForkJoinPool(2);//2 表示 并发级别，意味着线程池使用两个处理器核
}
//引用
ForkJoinPool forkJoinPool = PoolUtil.forkJoinPool;
```

### ForkJoinTask<V>

ForkJoinTask是ForkJoinPool线程池能够处理任务的父类，常用的两个子类是：RecursiveAction（无返回值）和RecursiveTask<V>（有返回值），它们都是抽象类，定义了一个抽象方法compute用于定义任务逻辑。

### 总结

fork/join框架能够加速大量任务的处理速度，但是需要遵循以下几点：

1. 使用尽可能少的线程池，正常情况下一个应用或系统只有一个线程池
2. 使用默认的commonpool
3. 使用合理的阀值来将ForkJoinTask分割成相应的子任务
4. 避免任务是阻塞的

### 底层原理

ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque），里面存放的对象是任务（ForkJoinTask）。
每个工作线程在运行中产生新的任务（通常是因为调用了 fork()）时，会放入工作队列的队尾，并且工作线程在处理自己的工作队列时，使用的是 LIFO 方式，也就是说每次从队尾取出任务来执行。
每个工作线程在处理自己的工作队列同时，会尝试窃取一个任务（或是来自于刚刚提交到 pool的任务，或是来自于其他工作线程的工作队列），窃取的任务位于其他线程的工作队列的队首，也就是说工作线程在窃取其他工作线程的任务时，使用的是 FIFO 方式。



## 并发编程

### 线程基础

#### 线程介绍

##### main线程

java应用程序的main函数是一个线程，是被jvm启动的时候调用的，线程的名字为main，并且该线程是一个非守护线程。

##### 创建线程

线程创建的方式有两种：

1. 创建Thread类的子类并重写run方法
2. 实现Runnable接口并实现run方法，然后创建一个实例，并作为Thread类的参数来构造一个线程

**创建线程注意点**

1. 如果创建Thread对象时没有Runnable实现或者重写run方法，则线程启动后不执行任何业务逻辑。如果传入Runnable实现或者重写run方法，则执行run方法内的业务逻辑

2. 如果创建线程时没有传入ThreadGroup对象，Thread会默认获取父线程的ThreadGroup作为该线程的ThreadGroup，此时父线程和子线程在同一个ThreadGroup中

3. 构造Thread的时候传入的stackSize表示该线程占用的栈大小，该值越大线程递归的越深，该值越小同时存在的线程数量越多。如果没有指定大小，默认为0，就与Thread(ThreadGroup,Runnable,String)构造函数一样。该参数只在部分平台有效。**-Xss 1024k 设置线程栈的大小**
4. 创建守护线程的方法setDaemon方法一定要在start方法前调用，并且当jvm中所有的线程都为守护线程时则jvm退出



Thread类的四种常用构造方法：

new Thread();

new Thread(Runnable run);

new Thread(String name);

new Thread(Runnable run,String name);

##### 启动线程

通过调用Thread类的start方法来启动线程，jvm会调用该线程的run方法。是通过本地方法start0（C++代码）调起我们自己重写的run方法。

##### 线程生命周期

![线程生命周期](https://i.loli.net/2019/08/22/DHATtjI5KuzpwC1.png)

Thread类中的枚举值State定义了线程的状态，任意时刻线程只会处于其中一个状态：

1. **NEW**：新创建的线程，还没有调start方法去执行
2. **RUNNABLE**：或者在运行，或者等待资源去运行（实际上就是等待CPU调度）
3. **BLOCKED**：等待监视器锁去进入或者重新进入synchronize代码块或方法
4. **WAITING**：等待其他线程去执行一个特殊的动作，没有超时
5. **TIMED_WAITING**：等待其他线程去执行一个特殊的动作，有超时时间限制
6. **TERMINATED**：线程完成

> Runnable 可执行状态，在多线程环境下，通过jvm的线程调度器（Thread-Scheduler）分配时间片来执行，时间片执行完后，cpu会让出控制权给其他可执行的线程

##### wait和sleep的区别

wait()是一个用于线程同步的实例方法，是java.lang.Object类中的方法，只能在synchronized同步代码块内调用。

Thread.sleep()方法能被在任何地方调用，该方法终止当前线程，但是不释放任何锁。

**wait方法使用注意事项：**

1. 当前线程必须拥有对象监视器（synchronized的作用）
2. 调用wait方法后，当前线程处于监视器所属对象的等待队列中，直到以下发生以下四种情况时，当前线程重新进入Runnable状态：
   1. 其他线程调用了监视器所属对象的notify方法，并且当前线程恰巧被选为唤醒的线程
   2. 其他线程调用了监视器所属对象的notifyAll方法
   3. 其他线程中断了当前线程
   4. 设置了超时时间（ wait(long millis) ），并且超时时间已到，如果没有设置超时时间那就只能通过上面三种方式唤醒
3. 当当前线程再次获取到这个对象的控制权（monitor）后，所有在这个对象上的同步声明（synchronization claims）都会恢复到wait方法调用那一刻的状态（线程上下文切换时产生的中间变量）,然后从wait方法返回并继续执行
4. 当前线程可能会产生**虚假唤醒**，解决虚假唤醒的办法就是不断的循环判断条件是否达到了线程应该被唤醒的条件，不过产生了虚假唤醒，但是条件不满足则继续等待（就是又调用了一遍object.wait方法）

```java
synchronized (obj) {
    while (&lt;condition does not hold&gt;)
        obj.wait(timeout);
    ... // Perform action appropriate to condition
}
```

##### wait和notify

