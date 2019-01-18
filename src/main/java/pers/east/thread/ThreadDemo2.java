package pers.east.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors.newFixedThreadPool(int n)：创建一个可重用固定个数的线程池，以共享的无界队列方式来运行这些线程
 * <p>
 * 注意：
 * 一些大型项目中，这种做法一般是禁止的。
 * WHY？？？
 * 因为用Executors创建的线程池存在性能隐患，我们看一下源码就知道，用Executors创建线程池时，使用的队列是new LinkedBlockingQueue<Runnable>()，
 * 这是一个无边界队列，如果不断的往里加任务时，最终会导致内存问题，也就是说在项目中由于使用了无边界队列，导致的内存占用的不可控性。
 * </p>
 */
public class ThreadDemo2 {
    public static void main(String[] args) {
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i =0;i<10;i++){
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+"正在被执行");
                        Thread.sleep(1000);
                        fixedThreadPool.shutdown();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
            System.out.println(11111);
        }
    }
}
