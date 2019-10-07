package com.yej.mpdemo.modules.controller;

import com.yej.mpdemo.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yej
 * @Date: 2019/9/26 15:33
 * @Version 1.0
 */
@RestController
@RequestMapping("sys/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("get")
    public Object getUser(Integer id){
        return userService.getUser(id);
    }
}
