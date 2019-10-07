package com.yej.api;

/**
 * @Author: yej
 * @Date: 2019/9/18 17:58
 * @Version 1.0
 */
public interface ITimeCounter {

    public void countStart();

    public void countEnd();

    public boolean isStart();

    public boolean reStart();

    public long getDifferTime() throws Exception;
}
