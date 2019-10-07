package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BeanFactory {
	private static BeanFactory instance;
	
	private Map<String,Object> factory=null;
	{
		factory=new HashMap<String,Object>();
	}
	
	public static BeanFactory getInstance(){
		if(instance==null){
			instance=new BeanFactory();
		}
		return instance;
	}
	
	public Object getBean(String beanName){
		return factory.get(beanName);
	}
	
	public static void main(String[] args) {
		BeanFactory factory= BeanFactory.getInstance();
		System.out.println(factory.getBean("demoBean"));
		List<Object> list=new ArrayList<Object>();
		list.add(1);
		list=Collections.unmodifiableList(list);
		//List<Object> list1=Collections.synchronizedList();
		System.out.println(list.get(0));
	}
}
