package com.yej.mpdemo.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yej.mpdemo.modules.pojo.User;

/**
 * @Author: yej
 * @Date: 2019/9/26 15:34
 * @Version 1.0
 */
public interface UserService extends IService<User> {

    public User getUser(Integer id);
}
