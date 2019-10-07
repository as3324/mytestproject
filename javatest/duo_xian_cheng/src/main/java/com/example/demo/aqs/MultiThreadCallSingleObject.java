package com.example.demo.aqs;

import com.sun.xml.internal.ws.util.CompletedFuture;

import java.util.concurrent.*;

/**
 * @Author: yej
 * @Date: 2019/9/20 22:04
 * @Version 1.0
 */
public class MultiThreadCallSingleObject {

    /**
     * 多线程调用单例对象方法的测试
     * 为了检验单例对象方法阻塞的时候其他线程调用会不会等待阻塞的方法调用完后再调用
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Runnable> queue=new ArrayBlockingQueue<Runnable>(8);
        RejectedExecutionHandler handler=new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("队列已经满了:" +executor.getQueue().size());
            }
        };
        final int n=100;

        final ThreadPoolExecutor executor=new ThreadPoolExecutor(1, 5, 10, TimeUnit.MINUTES, queue, handler);
   //     final SingleInatanceObject object=new SingleInatanceObject();
        final CountDownLatch latch=new CountDownLatch(n);
        for(int i=0;i<n;i++){
            Thread.sleep(1000);
            executor.submit(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+" start");
                        Thread.sleep(10000);
                        System.out.println(Thread.currentThread().getName()+" over");
                        latch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("call await ");
        latch.await();
        executor.shutdown();

    }

    static class SingleInatanceObject{

        public void call(Thread thread) throws InterruptedException {
            System.out.println("execute "+thread.getName());
            Thread.sleep(10000);
            System.out.println("execute "+thread.getName()+" over");
        }
    }
}
