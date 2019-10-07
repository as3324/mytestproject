package com.example.demo.aqs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CyclicBarrierTest {
	public static void main(String[] args) throws Exception {
		final int count=12;
		final Map<Integer,Integer> twelveMonthsDatas=new HashMap<Integer,Integer>();
		final CyclicBarrier barrier=new CyclicBarrier(count,new Runnable() {
			public void run() {
				System.out.println("twelveMonthsDatas="+twelveMonthsDatas.size());
			}
		});
		ExecutorService service=Executors.newFixedThreadPool(count);
		final CountDownLatch latch=new CountDownLatch(count);
		final TimeCounter counter=new TimeCounter();
		for(int i=0;i<count;i++){
			final Integer month=i+1;
			service.execute(new Runnable() {
				public void run() {
					counter.countStart();
					Integer data=0;
					try {
						data= getDataByMonth(month);
						twelveMonthsDatas.put(month, data);
						barrier.await(10, TimeUnit.SECONDS);
						Thread.sleep(getRandomNumber(1500));
						if(month==1){
							System.out.println("month="+month+" "+data);
						}else{
							Integer preMonthData=  twelveMonthsDatas.get(month-1);
							Integer deffData= data-preMonthData;
							System.out.println("month="+month+" "+ deffData);
						}
						counter.countEnd();
					} catch (InterruptedException e) {
						System.out.println(Thread.currentThread().getName()+": InterruptedException");
						printStack(e);
					} catch (BrokenBarrierException e) {
						System.out.println(Thread.currentThread().getName()+": BrokenBarrierException");
						printStack(e);
					} catch (TimeoutException e) {
						System.out.println(Thread.currentThread().getName()+": TimeoutException");
						printStack(e);
					}finally{
						latch.countDown();
					}
				}
				
				public void printStack(Exception e){
					StackTraceElement[] elements=e.getStackTrace();
					for(StackTraceElement el:elements){
						System.out.println("\tat "+el.getClassName()+" "+ el.getMethodName()+"("+el.getFileName()+")");
					}
					e.printStackTrace();
				}
			});
		}
		latch.await();
		try{
			System.out.println("执行计数时间是:"+counter.getDifferTime());
		}finally{
			service.shutdown();
		}
	}
	
	public static Integer getRandomNumber(Integer maxNuber){
		return Double.valueOf(Math.random()*maxNuber).intValue();
	}
	
	public static Integer getDataByMonth(Integer month) throws InterruptedException{
		if(month.equals(12)){
			//所有到达 CyclicBarrier 的屏障点point 时候，该屏障点point 结束，继续执行。如果不能全部到达（中断、失败或者超时等原因），
			//设置 BrokenBarrierException引起所有调用await方法的“Thread”全部报错，全部要么全不 (all-or-none) 的破坏模式。
			Thread.currentThread().interrupt();
		}
		Thread.sleep(getRandomNumber(1500));
		Integer data=Double.valueOf(getRandomNumber(100)).intValue();
		System.out.println(month+"="+data);
		return data;
	}
}
