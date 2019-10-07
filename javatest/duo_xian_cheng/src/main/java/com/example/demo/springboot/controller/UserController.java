package com.example.demo.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yej
 * @Date: 2019/9/26 12:24
 * @Version 1.0
 */
@RestController
@RequestMapping("sys/user")
public class UserController {

    @RequestMapping("/get")
    public Object queryUser(String id){
        return null;

    }

}
