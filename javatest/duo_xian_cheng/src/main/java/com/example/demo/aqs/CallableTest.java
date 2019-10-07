package com.example.demo.aqs;

import java.util.concurrent.*;

public class CallableTest<T>{
	public static void main(String[] args) throws Exception {
		TimeCounter counter=new TimeCounter();
		methodCallTimerCount(counter);
		System.out.println(counter.getDifferTime());
	}

	public static void methodCallTimerCount(TimeCounter counter) throws InterruptedException, ExecutionException {
		counter.countStart();
		execute();
		counter.countEnd();
	}

	private static void execute() throws InterruptedException, ExecutionException {
		CallableTest<String> test=new CallableTest<String>();
		ExecutorService service=Executors.newCachedThreadPool();
		Future<String> task= service.submit(test.getCallable());
		System.out.println("do main");
		Thread.sleep(3000);
		System.out.println(task.get());
		service.shutdown();
	}


	public T call() throws Exception {
		Thread.sleep(4000);
		System.out.println("do callable");
		return (T)"Done";
	}

	public Callable<T> getCallable(){
		return new MyCallable();
	}


	class MyCallable implements Callable<T>{
		public T call() throws Exception {
			return CallableTest.this.call();
		}
	}

}
