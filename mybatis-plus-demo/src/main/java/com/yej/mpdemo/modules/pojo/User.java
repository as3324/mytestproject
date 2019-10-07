package com.yej.mpdemo.modules.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: yej
 * @Date: 2019/9/26 15:33
 * @Version 1.0
 */
@TableName("t_user")
public class User {
    @TableId("id")
    private Integer id;

    @TableField("name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
