package com.example.demo.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SemaPhoreTest {
	
	private AtomicInteger count=new AtomicInteger();
	private final int maxNumber=100;
	
	public static void main(String[] args) {
		SemaPhoreTest test=new SemaPhoreTest();
		test.test();
	}
	
	public void test(){
		ExecutorService service=Executors.newCachedThreadPool();
		final int threadTotal=1000;
		final Semaphore phore=new Semaphore(maxNumber);
		for(int i=0;i<threadTotal;i++){
			service.execute(new Runnable() {
				public void run() {
					try {
						phore.acquire();
						int temp= count.incrementAndGet();
						doTask(0);
						//count.decrementAndGet();
						//phore.release();
						if(temp==maxNumber){
							System.out.println("concurrent thread is full with value"+temp);
						}else if(temp>maxNumber){
							System.out.println("concurrent thread is over with value"+temp);
						}else{
							//System.err.println("concurrent thread is less");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public void doTask(int num) throws InterruptedException{
		Thread.sleep((int)((Math.random()*20)));
	}
}
