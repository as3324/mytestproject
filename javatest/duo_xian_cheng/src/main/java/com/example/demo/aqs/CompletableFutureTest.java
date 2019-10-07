package com.example.demo.aqs;


import java.util.concurrent.*;

/**
 * @Author: yej
 * @Date: 2019/9/23 10:12
 * @Version 1.0
 */
public class CompletableFutureTest {

    /**
     * 测试completableFuture的用法
     * 结论：在一个线程中调用completableFuture的get方法会阻塞线程的执行，直到其他线程调用cancel、
     *      completeExceptionally、complete方法才使线程继续执行，其中cancel方法和completeExceptionally方法都会使get方法抛出异常
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        complete();
        cancel();
        completeExceptionally();
    }

    public static void complete(){
        final CompletableFuture<String> completableFuture = new CompletableFuture<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    completableFuture.complete("hello");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            System.out.println("the result that call cancel method :"+completableFuture.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void cancel()  {
        final CompletableFuture<String> completableFuture = new CompletableFuture<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    completableFuture.cancel(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            System.out.println("the result that call cancel method :"+ completableFuture.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void completeExceptionally()  {
        final CompletableFuture<String> completableFuture = new CompletableFuture<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    completableFuture.completeExceptionally(new ExceptionTest("这是一个自定义异常"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            System.out.println("the result that call completeExceptionally method :"+completableFuture.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class ExceptionTest extends Exception{
        public ExceptionTest(String msg){
            super(msg);
        }
    }

}
