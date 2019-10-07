package com.example.demo.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownLatchTest {
	public static AtomicInteger count=new AtomicInteger();
	public static void main(String[] args) throws InterruptedException {
		int threadTotal=1000;
		final CountDownLatch latch=new CountDownLatch(threadTotal);
		ExecutorService service=Executors.newCachedThreadPool();
		for(int i=0;i<threadTotal;i++){
			service.execute(new Runnable() {
				
				public void run() {
					try{
						add();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						latch.countDown();
					}
				}
			});
		}
		latch.await();
		System.out.println(count.get());
	}
	
	public static void add(){
		count.getAndIncrement();
	}
}
