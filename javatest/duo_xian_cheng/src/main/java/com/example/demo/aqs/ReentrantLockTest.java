package com.example.demo.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

	/**
	 * 知识补充:
	 * 独占锁和共享锁区别
	 *   1.独享锁(互斥锁)：同时只能有一个线程获得锁。比如，ReentrantLock 是互斥锁，ReadWriteLock 中的写锁是互斥锁。
	 *   2.共享锁：可以有多个线程同时获得锁。比如，Semaphore、CountDownLatch 是共享锁，ReadWriteLock 中的读锁是共享锁。
	 *
	 * 实验目的：测试ReentrantLock是否是独占锁
	 *
	 * 实验过程：1.创建a、b两个线程
	 *         2.让线程b先执行并且给代码加锁，加锁后等待足够长的一段时间让线程a后入锁
	 *
	 * 实验结果情况与分析：
	 * 		   情况a:如果当线程b获取锁后等待的过程中线程a调用trylock方法结果为false
	 * 		   结果a:为独占锁
	 *
	 * 		   情况b：如果当线程b获取锁后等待的过程中线程调用trylocck方法结果为true
	 * 		   结果b: 为共享锁
	 *
	 * 实验最终结果:情况a 结果a
	 * @param args
	 */
	public static void main(String[] args) {
		//unlockInOtherThread();
		trylockTest();
	}
	
	public static void trylockTest(){
		final CountDownLatch latch=new CountDownLatch(2);
		final ReentrantLock lock=new ReentrantLock();
		Thread a= new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1500);
					System.out.println("prepare trylock thread a");
					if(lock.tryLock()){
						try{
							System.out.println("enter trylock thread a");
						}finally{
							if(lock.isLocked()){
								System.out.println("unlock thread a");
								lock.unlock();
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{

					latch.countDown();
				}
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("thread a run over");
			}
		});
		Thread b=new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					lock.lock();
					System.out.println("enter lock thread b");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					System.out.println("unlock thread b");
					if(lock.isLocked()){
						lock.unlock();
					}
					latch.countDown();
				}
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("thread b run over");
				
			}
		});
		a.start();
		b.start();
		try {
			latch.await();
			System.out.println("running over");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void unlockInOtherThread(){
		final ReentrantLock lock=new ReentrantLock();
		Thread a=new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					lock.lock();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Thread b=new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1500);
					System.out.println("aa");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					lock.unlock(); //测试ReentrantLock能否在其他线程中调用unlock方法,结果：线程a中先进行lock()，而后线程b中使用unlock()抛出异常
				}
				System.out.println("bb");
			}
		});
		b.start();
		a.start();
	}
}
