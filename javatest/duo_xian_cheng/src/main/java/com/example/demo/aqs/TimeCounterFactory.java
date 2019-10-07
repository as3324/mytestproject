package com.example.demo.aqs;

import com.example.demo.util.ComUtil;
import com.yej.api.ITimeCounter;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: yej
 * @Date: 2019/9/18 22:01
 * @Version 1.0
 */
public class TimeCounterFactory {

    public ITimeCounter getIndependentThreadTimeCounter(){
        return new IndependentThreadTimeCounter();
    }

    /**
     * 独立线程计时器：可使用于各个线程单独计时,
     *              多线程情况下使用同一个timeCounter实例不影响各个线程的计时结果
     */
    private class IndependentThreadTimeCounter implements ITimeCounter{

        private ThreadLocal<TimeStorage> localTimeStorage=new ThreadLocal<TimeStorage>();


        public boolean isStart(){
            TimeStorage storage=getTmeStorage();
            if(storage!=null){
                return true;
            }
            return false;
        }

        public boolean reStart(){
            try{
                if(isStart()){
                    TimeStorage timeStorage=getTmeStorage();
                    timeStorage.distroy();
                    timeStorage.setStartTime(new Date());
                }else{
                    countStart();
                }
                return true;
            }catch (Exception e){
                return false;
            }
        }

        public void countStart() {
            if(!isStart()){
                TimeStorage timeStorage= new TimeStorage();
                timeStorage.setStartTime(new Date());
                localTimeStorage.set(timeStorage);

            }else{
                throw new IllegalStateException("计时器已经开始计时");
            }
        }


        private TimeStorage getTmeStorage(){
            return localTimeStorage.get();
        }

        public void countEnd() {
            if(isStart()){
                TimeStorage timeStorage=getTmeStorage();
                timeStorage.setEndTime(new Date());
            }else{
                throw new IllegalStateException("计时器还未开始计时");
            }

        }

        private boolean isEnd(){
            if(isStart()){
                TimeStorage timeStorage= getTmeStorage();
                return timeStorage.getEndTime()!=null;
            }
            return false;
        }

        public long getDifferTime() throws Exception {
            if(isEnd()){
                TimeStorage timeStorage= getTmeStorage();
                return timeStorage.getEndTime().getTime()-
                        timeStorage.getStartTime().getTime();
            }
            return 0;
        }

        private class TimeStorage{
            private Date startTime;
            private Date endTime;

            public Date getStartTime() {
                return startTime;
            }

            public void setStartTime(Date startTime) {
                this.startTime = startTime;
            }

            public Date getEndTime() {
                return endTime;
            }

            public void setEndTime(Date endTime) {
                this.endTime = endTime;
            }

            public void distroy(){
                this.startTime=null;
                this.endTime=null;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TimeCounterFactory factory=new TimeCounterFactory();
        final ITimeCounter timeCounter= factory.getIndependentThreadTimeCounter();
        int n=1000;
        long[] array=new long[n];
        for(int i=0;i<n;i++){
            array[i]= ComUtil.getRandomNumber(5000);
        }
        ExecutorService executorService= Executors.newFixedThreadPool(n);
        final CountDownLatch latch=new CountDownLatch(n);
        class Task implements Runnable{
            private long taskTime;
            public Task(long taskTime){
                this.taskTime=taskTime;

            }

            public void run(){
                try {
                    timeCounter.countStart();
                    Thread.sleep(taskTime);
                    timeCounter.countEnd();
                    System.out.println(taskTime+":"+timeCounter.getDifferTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            }
        }
        timeCounter.countStart();
        for(int i=0;i<n;i++){
            executorService.execute(new Task(array[i]));
        }
        latch.await();
        timeCounter.countEnd();
        executorService.shutdown();
        System.out.println(timeCounter.getDifferTime());
        timeCounter.countStart();
    }



}
