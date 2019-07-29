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

