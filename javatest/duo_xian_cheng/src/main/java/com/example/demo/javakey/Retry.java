package com.example.demo.javakey;

/**
 * @Author: yej
 * @Date: 2019/9/21 12:10
 * @Version 1.0
 */
public class Retry {

    public static void main(String[] args) {
        int i=0;
        String msg="";
        retry:
            for(;;){
                System.out.println(msg+":"+i);
                msg="";
                try{
                    if(i%10==2){
                        msg="continue";
                        continue retry;
                    }
                    if(i%10==5){
                        msg="break";
                        break retry;
                    }
                }finally{
                    i++;
                }
            }
        System.out.println("run over");
    }
}
