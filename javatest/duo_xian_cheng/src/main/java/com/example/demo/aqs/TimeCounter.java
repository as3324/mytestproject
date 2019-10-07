package com.example.demo.aqs;

import com.yej.api.ITimeCounter;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class TimeCounter implements ITimeCounter {
	private volatile Date startTime;
	private volatile Date endTime;
	private ReentrantLock startLock=new ReentrantLock();
	private ReentrantLock endLock=new ReentrantLock(true);
	
	public void countStart(){
		startLock.lock();
		if(startTime==null){
			startTime=new Date();
		}
		startLock.unlock();
	}
	
	public void countEnd(){
		endLock.lock();
		endTime=new Date();
		endLock.unlock();
	}

	@Override
	public boolean isStart() {
		return this.startTime!=null;
	}

	@Override
	public boolean reStart() {
		this.startTime=new Date();
		this.endTime=null;
		return true;
	}

	public long getDifferTime() throws Exception{
		if(startTime==null||endTime==null){
			throw new Exception("状态异常"); 
		}
		return endTime.getTime()-startTime.getTime();
	}
}
