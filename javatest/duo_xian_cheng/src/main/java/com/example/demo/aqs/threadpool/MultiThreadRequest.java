package com.example.demo.aqs.threadpool;



import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yej
 * @Date: 2019/9/26 16:53
 * @Version 1.0
 */
public class MultiThreadRequest {

    public static final AtomicInteger integer=new AtomicInteger();

    public static void main(String[] args) {
        BlockingQueue<RequestResult> resultQueue= multiThreadRequest();
        listenRequestResult(resultQueue);
    }

    public static BlockingQueue<RequestResult> singleRequest(){
        BlockingQueue<RequestResult> resultQueue=new LinkedBlockingQueue<RequestResult>();
        CloseableHttpClient client= HttpClientBuilder.create().build();
        int id=1;
        HttpGet get=new HttpGet("http://localhost:8080/mpdemo/sys/user/get?id="+id);

        CloseableHttpResponse response=null;
        try {
            response= client.execute(get);
            HttpEntity entity= response.getEntity();
            if(response.getStatusLine().getStatusCode()==200){
                resultQueue.offer(new RequestResult(id,EntityUtils.toString(entity)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(client!=null){
                    client.close();
                }
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultQueue;
    }

    public static BlockingQueue<RequestResult> multiThreadRequest(){
        final BlockingQueue<Runnable> queue=new LinkedBlockingQueue<Runnable>(10000);
        final BlockingQueue<Runnable> misqueue=new LinkedBlockingQueue<Runnable>();
        final ThreadPoolExecutor executor=new ThreadPoolExecutor(500, 1000, 1, TimeUnit.MINUTES, queue, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                misqueue.offer(r);
            }
        });
        final BlockingQueue<RequestResult> resultQueue=new LinkedBlockingQueue<RequestResult>();
        final int maxNum=400000;
        final int threadNum=100000;

        for(int i=0;i<threadNum;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    int temp= integer.incrementAndGet();
                    CloseableHttpClient client= HttpClientBuilder.create().build();
                    int id=Double.valueOf(Math.random()*maxNum+1).intValue();
                    HttpGet get=new HttpGet("http://localhost:8080/mpdemo/sys/user/get?id="+id);
                    CloseableHttpResponse response=null;
                    try {
                        response= client.execute(get);
                        HttpEntity entity= response.getEntity();
                        resultQueue.offer(new RequestResult(id,EntityUtils.toString(entity)));
                    } catch (IOException e) {
                        System.out.println(temp);
                        e.printStackTrace();
                    }finally {
                        integer.decrementAndGet();
                        try {
                            if(client!=null){
                                client.close();
                            }
                            if(response!=null){
                                response.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return resultQueue;
    }

    public static void listenRequestResult(final BlockingQueue<RequestResult> queue){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                        RequestResult result= queue.take();
                        System.out.println(result.getId()+ ":"+result.getResult()+":"+integer.get()+":"+queue.size());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    static class RequestResult{
        private int id;
        private Object result;

        public RequestResult(int id,Object result){
            this.id=id;
            this.result=result;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }
}
