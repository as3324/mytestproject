package com.example.demo.sync;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StaticClassSyncTest {
	private static StaticClassSyncTest instance=null;
	public static void main(String[] args) throws InterruptedException {
		final int count=2;
		ExecutorService executor=Executors.newFixedThreadPool(count);
		final CountDownLatch countDown=new CountDownLatch(count);
		final ThreadLocal<Date> d0=new ThreadLocal<Date>();
		for(int i=0;i<count;i++){
			executor.execute(new Runnable() {
				public void run() {
					try {
						synchronized (d0) {
							if(d0.get()==null){
								d0.set(new Date());
							}
							getInstance();
							Thread.sleep(4000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					countDown.countDown();
				}
			});
		}
		System.out.println(SingLeton.leton2.name);
		System.out.println("完毕!");
		Date d1=new Date();
		countDown.await(2,TimeUnit.SECONDS);
		Date d2=new Date();
		System.out.println("等待了"+ ((d2.getTime()-d1.getTime())/1000)+"秒钟");
		executor.shutdown();
	}
	
	private StaticClassSyncTest(){
		
	}
	
	public synchronized static StaticClassSyncTest getInstance() throws InterruptedException{
		System.out.println("aa "+ Thread.currentThread().getName());
		if(instance==null){
			instance=new StaticClassSyncTest();
		}
		return instance;
	}
	
	private enum SingLeton{
		leton1,leton2("bb",2);
		
		private String name;
		private int age;
		
		private SingLeton(){
			this.name="aa";
			this.age=1;
		}
		
		private SingLeton(String name,int age){
			this.name=name;
			this.age=age;
		}
	}
}
