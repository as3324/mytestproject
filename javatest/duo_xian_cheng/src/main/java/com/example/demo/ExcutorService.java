package com.example.demo;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcutorService {
	
	public void test() throws InterruptedException{
		for(int i=0;i<10;i++){
			Thread.sleep(1000);
			System.out.println(i);
		}
	}
	
	public static void main(String[] args) {
		final ExcutorService service=new ExcutorService();
		Thread t=new Thread(new Runnable() {
			
			public void run() {
				try {
					service.test();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		final ExcutorService service1=new ExcutorService();
		ExecutorService executor=Executors.newCachedThreadPool();
		executor.execute(new Runnable() {
			public void run() {
				try {
					service1.test();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
