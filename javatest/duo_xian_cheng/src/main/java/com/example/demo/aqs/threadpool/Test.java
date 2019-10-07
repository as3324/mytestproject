package com.example.demo.aqs.threadpool;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: yej
 * @Date: 2019/9/25 22:16
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(new Test1());
    }

    static class Test1{
        private Object O=Datadic.test.getTest2();
    }

    enum Datadic{
        test(Test1.class,Test2.class);

        private Object o;
        private Object o1;
        private Datadic(Class o,Class o1){
            try {
                this.o=o.newInstance();
                this.o1=o1.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public Object getTest1(){
            return this.o;
        }

        public Object getTest2(){
            return this.o1;
        }
    }

    static class Test2{
        private Object O=Datadic.test.getTest1();
    }
}
