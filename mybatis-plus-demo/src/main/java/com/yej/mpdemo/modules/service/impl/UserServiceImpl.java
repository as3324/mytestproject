package com.yej.mpdemo.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yej.mpdemo.modules.dao.UserDao;
import com.yej.mpdemo.modules.pojo.User;
import com.yej.mpdemo.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: yej
 * @Date: 2019/9/26 15:37
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService {


    @Override
    public User getUser(Integer id) {
        return this.getById(id);
    }
}
