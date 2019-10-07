package com.yej.mpdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: yej
 * @Date: 2019/9/26 15:01
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.yej.mpdemo.modules.dao"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
