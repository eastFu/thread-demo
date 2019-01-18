# Java并发编程：4种线程池和缓冲队列BlockingQueue

## 一. 线程池简介
### 1. 线程池的概念：

          线程池就是首先创建一些线程，它们的集合称为线程池。使用线程池可以很好地提高性能，线程池在系统启动时即创建大量空闲的线程，程序将一个任务传给线程池，线程池就会启动一条线程来执行这个任务，执行结束以后，该线程并不会死亡，而是再次返回线程池中成为空闲状态，等待执行下一个任务。

### 2. 线程池的工作机制

	2.1 在线程池的编程模式下，任务是提交给整个线程池，而不是直接提交给某个线程，线程池在拿到任务后，就在内部寻找是否有空闲的线程，如果有，则将任务交给某个空闲的线程。

	2.1 一个线程同时只能执行一个任务，但可以同时向一个线程池提交多个任务。

### 3. 使用线程池的原因：

     多线程运行时间，系统不断的启动和关闭新线程，成本非常高，会过渡消耗系统资源，以及过渡切换线程的危险，从而可能导致系统资源的崩溃。这时，线程池就是最好的选择了。

## 二. 四种常见的线程池详解
### 1. 线程池的返回值ExecutorService简介：

    ExecutorService是Java提供的用于管理线程池的类。该类的两个作用：控制线程数量和重用线程

### 2. 具体的4种常用的线程池实现如下：（返回值都是ExecutorService）

#### 2.1 Executors.newCacheThreadPool()：可缓存线程池，先查看池中有没有以前建立的线程，如果有，就直接使用。如果没有，就建一个新的线程加入池中，缓存型池子通常用于执行一些生存期很短的异步型任务

示例代码：
```
package pers.east.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors.newCacheThreadPool()：可缓存线程池，先查看池中有没有以前建立的线程，如果有，就直接使用。
 * 如果没有，就建一个新的线程加入池中，缓存型池子通常用于执行一些生存期很短的异步型任务
 */
public class ThreadDemo1 {

    public static void main(String[] args) {
        ExecutorService cachedThereadPool = Executors.newCachedThreadPool();
        for (int i=0; i<10;i++){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            cachedThereadPool.execute(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"正在被执行");
                }
            });
        }
    }
}


```



输出结果：

pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行
pool-1-thread-1正在被执行

线程池为无限大，当执行当前任务时上一个任务已经完成，会复用执行上一个任务的线程，而不用每次新建线程

#### 2.2  Executors.newFixedThreadPool(int n)：创建一个可重用固定个数的线程池，以共享的无界队列方式来运行这些线程。

         示例代码：
```
package pers.east.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors.newFixedThreadPool(int n)：创建一个可重用固定个数的线程池，以共享的无界队列方式来运行这些线程
 */
public class ThreadDemo2 {

    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i =0;i<10;i++){
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+"正在被执行");
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}


```


输出结果：

pool-1-thread-1正在被执行
pool-1-thread-2正在被执行
pool-1-thread-3正在被执行
pool-1-thread-1正在被执行
pool-1-thread-2正在被执行
pool-1-thread-3正在被执行
pool-1-thread-1正在被执行
pool-1-thread-2正在被执行
pool-1-thread-3正在被执行
pool-1-thread-1正在被执行

因为线程池大小为3，每个任务输出打印结果后sleep 2秒，所以每两秒打印3个结果。
定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()

#### 2.3  Executors.newScheduledThreadPool(int n)：创建一个定长线程池，支持定时及周期性任务执行

延迟执行示例代码：
```
package pers.east.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executors.newScheduledThreadPool(int n)：创建一个定长线程池，支持定时及周期性任务执行
 * 延迟执行示例代码:
 */
public class ThreadDemo3 {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(new Runnable() {
            public void run() {
                System.out.println("延迟1秒执行");
            }
        },1 , TimeUnit.SECONDS);
    }
}

```


输出结果：延迟1秒执行


定期执行示例代码：
```
package pers.east.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executors.newScheduledThreadPool(int n)：创建一个定长线程池，支持定时及周期性任务执行
 * 定期执行示例代码:
 *
 * 注意：
 * scheduleWithFixedDelay：
 * 从字面意义上可以理解为就是以固定延迟（时间）来执行线程任务，
 * 它实际上是不管线程任务的执行时间的，每次都要把任务执行完成后再延迟固定时间后再执行下一次
 *
 * scheduleAtFixedRate：
 * 是以固定频率来执行线程任务，
 * 固定频率的含义就是可能设定的固定时间不足以完成线程任务，但是它不管，达到设定的延迟时间了就要执行下一次了
 */
public class ThreadDemo4 {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("延迟1秒后每三秒执行一次");
            }
        },1,3, TimeUnit.SECONDS);
    }
}


```

输出结果：

延迟1秒后每3秒执行一次
延迟1秒后每3秒执行一次
.............

	注意：
  	scheduleWithFixedDelay从字面意义上可以理解为就是以固定延迟（时间）来执行线程任务，它实际上是不管线程任务的执行时间的，每次都要把任务执行完成后再延迟固定时间后再执行下一次
	scheduleAtFixedRate是以固定频率来执行线程任务，固定频率的含义就是可能设定的固定时间不足以完成线程任务，但是它不管，达到设定的延迟时间了就要执行下一次了

#### 2.4  Executors.newSingleThreadExecutor()：创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。

示例代码：
```
package pers.east.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Executors.newSingleThreadExecutor()：创建一个单线程化的线程池，
 *  它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
 */
public class ThreadDemo5 {
    public static void main(String[] args) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i=0;i<10;i++){
            final int index =i ;
            singleThreadExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+"正在被执行，打印的变量值为："+index);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
```

输出结果：

pool-1-thread-1正在被执行,打印的值是:0
pool-1-thread-1正在被执行,打印的值是:1
pool-1-thread-1正在被执行,打印的值是:2
pool-1-thread-1正在被执行,打印的值是:3
pool-1-thread-1正在被执行,打印的值是:4
pool-1-thread-1正在被执行,打印的值是:5
pool-1-thread-1正在被执行,打印的值是:6
pool-1-thread-1正在被执行,打印的值是:7
pool-1-thread-1正在被执行,打印的值是:8
pool-1-thread-1正在被执行,打印的值是:9

## 三. 缓冲队列BlockingQueue和自定义线程池ThreadPoolExecutor
### 1. 缓冲队列BlockingQueue简介：

     BlockingQueue是双缓冲队列。BlockingQueue内部使用两条队列，允许两个线程同时向队列一个存储，一个取出操作。在保证并发安全的同时，提高了队列的存取效率。

### 2. 常用的几种BlockingQueue：

ArrayBlockingQueue（int i）:规定大小的BlockingQueue，其构造必须指定大小。其所含的对象是FIFO顺序排序的。

LinkedBlockingQueue（）或者（int i）:大小不固定的BlockingQueue，若其构造时指定大小，生成的BlockingQueue有大小限制，不指定大小，其大小有Integer.MAX_VALUE来决定。其所含的对象是FIFO顺序排序的。

PriorityBlockingQueue（）或者（int i）:类似于LinkedBlockingQueue，但是其所含对象的排序不是FIFO，而是依据对象的自然顺序或者构造函数的Comparator决定。

SynchronizedQueue（）:特殊的BlockingQueue，对其的操作必须是放和取交替完成。

### 3. 自定义线程池（ThreadPoolExecutor和BlockingQueue连用）：

     自定义线程池，可以用ThreadPoolExecutor类创建，它有多个构造方法来创建线程池。

    常见的构造函数：ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)

     示例代码：



输出结果：

pool-1-thread-1正在被执行
pool-1-thread-2正在被执行
pool-1-thread-3正在被执行



## 4.BlockingQueue成员详细介绍
#### 1. ArrayBlockingQueue
	基于数组的阻塞队列实现，在ArrayBlockingQueue内部，维护了一个定长数组，以便缓存队列中的数据对象，这是一个常用的阻塞队列，除了一个定长数组外，ArrayBlockingQueue内部还保存着两个整形变量，分别标识着队列的头部和尾部在数组中的位置。
　　ArrayBlockingQueue在生产者放入数据和消费者获取数据，都是共用同一个锁对象，由此也意味着两者无法真正并行运行，这点尤其不同于LinkedBlockingQueue；按照实现原理来分析，ArrayBlockingQueue完全可以采用分离锁，从而实现生产者和消费者操作的完全并行运行。Doug Lea之所以没这样去做，也许是因为ArrayBlockingQueue的数据写入和获取操作已经足够轻巧，以至于引入独立的锁机制，除了给代码带来额外的复杂性外，其在性能上完全占不到任何便宜。 ArrayBlockingQueue和LinkedBlockingQueue间还有一个明显的不同之处在于，前者在插入或删除元素时不会产生或销毁任何额外的对象实例，而后者则会生成一个额外的Node对象。这在长时间内需要高效并发地处理大批量数据的系统中，其对于GC的影响还是存在一定的区别。而在创建ArrayBlockingQueue时，我们还可以控制对象的内部锁是否采用公平锁，默认采用非公平锁。

### 2. LinkedBlockingQueue
      基于链表的阻塞队列，同ArrayListBlockingQueue类似，其内部也维持着一个数据缓冲队列（该队列由一个链表构成），当生产者往队列中放入一个数据时，队列会从生产者手中获取数据，并缓存在队列内部，而生产者立即返回；只有当队列缓冲区达到最大值缓存容量时（LinkedBlockingQueue可以通过构造函数指定该值），才会阻塞生产者队列，直到消费者从队列中消费掉一份数据，生产者线程会被唤醒，反之对于消费者这端的处理也基于同样的原理。而LinkedBlockingQueue之所以能够高效的处理并发数据，还因为其对于生产者端和消费者端分别采用了独立的锁来控制数据同步，这也意味着在高并发的情况下生产者和消费者可以并行地操作队列中的数据，以此来提高整个队列的并发性能。
作为开发者，我们需要注意的是，如果构造一个LinkedBlockingQueue对象，而没有指定其容量大小，LinkedBlockingQueue会默认一个类似无限大小的容量（Integer.MAX_VALUE），这样的话，如果生产者的速度一旦大于消费者的速度，也许还没有等到队列满阻塞产生，系统内存就有可能已被消耗殆尽了。

ArrayBlockingQueue和LinkedBlockingQueue是两个最普通也是最常用的阻塞队列，一般情况下，在处理多线程间的生产者消费者问题，使用这两个类足以。

下面的代码演示了如何使用BlockingQueue：
```
/**
 * 生产者线程
 *
 *
 */
public class Producer implements Runnable {

    private volatile boolean      isRunning               = true;
    private BlockingQueue queue;
    private static AtomicInteger  count                   = new AtomicInteger();
    private static final int      DEFAULT_RANGE_FOR_SLEEP = 1000;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        String data = null;
        Random r = new Random();

        System.out.println("启动生产者线程！");
        try {
            while (isRunning) {
                System.out.println("正在生产数据...");
                Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));

                data = "data:" + count.incrementAndGet();
                System.out.println("将数据：" + data + "放入队列...");
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println("放入数据失败：" + data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("退出生产者线程！");
        }
    }

    public void stop() {
        isRunning = false;
    }
 }
```

```
/**
 * 消费者线程
 *
 *
 */
public class Consumer implements Runnable {

    private BlockingQueue<String> queue;
    private static final int      DEFAULT_RANGE_FOR_SLEEP = 1000;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void run() {
        System.out.println("启动消费者线程！");
        Random r = new Random();
        boolean isRunning = true;
        try {
            while (isRunning) {
                System.out.println("正从队列获取数据...");
                String data = queue.poll(2, TimeUnit.SECONDS);
                if (null != data) {
                    System.out.println("拿到数据：" + data);
                    System.out.println("正在消费数据：" + data);
                    Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                } else {
                    // 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
                    isRunning = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("退出消费者线程！");
        }
    }


}

```
```
/**
 * 测试类
 */
public class BlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        // 声明一个容量为10的缓存队列
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);
        Producer producer3 = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        // 借助Executors
        ExecutorService service = Executors.newCachedThreadPool();
        // 启动线程
        service.execute(producer1);
        service.execute(producer2);
        service.execute(producer3);
        service.execute(consumer);

        // 执行10s
        Thread.sleep(10 * 1000);
        producer1.stop();
        producer2.stop();
        producer3.stop();

        Thread.sleep(2000);
        // 退出Executor
        service.shutdown();
    }
}
```

### 3. DelayQueue
DelayQueue中的元素只有当其指定的延迟时间到了，才能够从队列中获取到该元素。DelayQueue是一个没有大小限制的队列，因此往队列中插入数据的操作（生产者）永远不会被阻塞，而只有获取数据的操作（消费者）才会被阻塞。
使用场景：
DelayQueue使用场景较少，但都相当巧妙，常见的例子比如使用一个DelayQueue来管理一个超时未响应的连接队列。

### 4. PriorityBlockingQueue
基于优先级的阻塞队列（优先级的判断通过构造函数传入的Compator对象来决定），但需要注意的是PriorityBlockingQueue并不会阻塞数据生产者，而只会在没有可消费的数据时，阻塞数据的消费者。因此使用的时候要特别注意，生产者生产数据的速度绝对不能快于消费者消费数据的速度，否则时间一长，会最终耗尽所有的可用堆内存空间。在实现PriorityBlockingQueue时，内部控制线程同步的锁采用的是公平锁。

### 5. SynchronousQueue

一种无缓冲的等待队列，类似于无中介的直接交易，有点像原始社会中的生产者和消费者，生产者拿着产品去集市销售给产品的最终消费者，而消费者必须亲自去集市找到所要商品的直接生产者，如果一方没有找到合适的目标，那么对不起，大家都在集市等待。相对于有缓冲的BlockingQueue来说，少了一个中间经销商的环节（缓冲区），如果有经销商，生产者直接把产品批发给经销商，而无需在意经销商最终会将这些产品卖给那些消费者，由于经销商可以库存一部分商品，因此相对于直接交易模式，总体来说采用中间经销商的模式会吞吐量高一些（可以批量买卖）；但另一方面，又因为经销商的引入，使得产品从生产者到消费者中间增加了额外的交易环节，单个产品的及时响应性能可能会降低。
　　声明一个SynchronousQueue有两种不同的方式，它们之间有着不太一样的行为。公平模式和非公平模式的区别:
　　如果采用公平模式：SynchronousQueue会采用公平锁，并配合一个FIFO队列来阻塞多余的生产者和消费者，从而体系整体的公平策略；
　　但如果是非公平模式（SynchronousQueue默认）：SynchronousQueue采用非公平锁，同时配合一个LIFO队列来管理多余的生产者和消费者，而后一种模式，如果生产者和消费者的处理速度有差距，则很容易出现饥渴的情况，即可能有某些生产者或者是消费者的数据永远都得不到处理。
小结
　　BlockingQueue不光实现了一个完整队列所具有的基本功能，同时在多线程环境下，他还自动管理了多线间的自动等待于唤醒功能，从而使得程序员可以忽略这些细节，关注更高级的功能