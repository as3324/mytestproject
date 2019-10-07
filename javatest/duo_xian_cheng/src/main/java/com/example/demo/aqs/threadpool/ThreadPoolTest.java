package com.example.demo.aqs.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

import com.example.demo.util.ComUtil;

public class ThreadPoolTest {
	public static void main(String[] args) throws InterruptedException {
		final ReentrantLock lock=new ReentrantLock();
		BlockingQueue<Runnable> queue=new ArrayBlockingQueue<Runnable>(8);
		RejectedExecutionHandler handler=new RejectedExecutionHandler() {
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				System.out.println("队列已经满了:" +executor.getQueue().size());
			}
		};
		final ThreadPoolExecutor executor=new ThreadPoolExecutor(1, 5, 10, TimeUnit.MINUTES, queue, handler);
		final int maxNum=100;
		final CountDownLatch latch=new CountDownLatch(maxNum);
		//listenThreadPoolState(executor);
		for(int i=0;i<maxNum;i++){
			Thread.sleep(1000);
			executor.submit(new Runnable() {
				public void run() {
					try {
						System.out.println(Thread.currentThread().getName());
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

	/**
	 * 监听线程池状态
	 * @param executor
	 */
	public static void listenThreadPoolState(final ThreadPoolExecutor executor){
		new Thread(new Runnable() {
			public void run() {
				while(!executor.isTerminated()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					int activeCount= executor.getActiveCount();
					long completedTaskCount= executor.getCompletedTaskCount();
					int poolSize= executor.getPoolSize();
					long taskCount= executor.getTaskCount();
					int queueSize= executor.getQueue().size();
					StringBuilder sb=new StringBuilder();
					sb.append("activeCount:"+activeCount)
							.append(" completedTaskCount:"+completedTaskCount)
							.append(" poolSize:"+poolSize)
							.append(" taskCount:"+taskCount)
							.append(" queueSize:"+queueSize);
					System.out.println(sb.toString());
				}
			}
		}).start();
	}
}
