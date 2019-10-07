package com.example.demo.springboot.service;

import com.example.demo.springboot.dao.UserDao;
import com.example.demo.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: yej
 * @Date: 2019/9/26 12:47
 * @Version 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserDao dao;

    public User queryById(Integer id){
          return dao.getById(id);
    }
}
