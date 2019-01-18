package pers.east.queue;

import java.util.concurrent.*;

public class BlockingQueueTest {
    public static void main(String[] args) throws InterruptedException{
        //声明一个容量为10的缓存队列
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);
        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);
        Producer producer3 = new Producer(queue);

        Consumer consumer = new Consumer(queue);
        //借助Excutors
        ExecutorService service = Executors.newCachedThreadPool();
        //启动线程
        service.execute(producer1);
        service.execute(producer2);
        service.execute(producer3);
        service.execute(consumer);

        //执行10s
        Thread.sleep(10*1000);
        producer1.stop();
        producer2.stop();
        producer3.stop();
        //退出Executor
        service.shutdown();

    }
}
