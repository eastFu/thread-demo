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
