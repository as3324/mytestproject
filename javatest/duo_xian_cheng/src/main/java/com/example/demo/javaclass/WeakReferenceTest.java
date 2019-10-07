package com.example.demo.javaclass;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yej
 * @Date: 2019/9/21 16:53
 * @Version 1.0
 */
public class WeakReferenceTest {

    static class OOMObject{
        private int[] array=new int[10];
    }

    /**
     *测试软引用和虚引用什么时候被系统回收
     * 结论：1.软引用->在系统内存不足的情况下才会被回收，若是系统资源充足的情况下即使系统资源充足也不会回收
     *      2.弱引用->系统不管是否内存足够，只要系统发起垃圾回收机制就会回收资源
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final WeakReference<OOMObject> ref=new WeakReference<OOMObject>(new OOMObject());
        final SoftReference<OOMObject> sofref=new SoftReference<OOMObject>(new OOMObject());
        listenStatus(ref,sofref);
        List<OOMObject> list=new ArrayList<OOMObject>();
        retry:for(;;){
            try{
                if(!list.add(new OOMObject())){
                    System.out.println("add falling");
                }
            }catch(OutOfMemoryError e){
                System.out.println(e.getMessage());
                System.gc();
                continue retry;

            }
        }

    }

    public static void listenStatus(final WeakReference ref,final SoftReference sofref){
        final Runtime run=Runtime.getRuntime();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        Thread.sleep(1000);
                        System.out.println(ref.get()+":"+sofref.get()+":"+run.maxMemory()
                                +":"+run.totalMemory());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
