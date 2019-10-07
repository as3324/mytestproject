package com.example.demo.util;

public class ComUtil {
	public static Integer getRandomNumber(Integer maxNuber){
		return Double.valueOf(Math.random()*maxNuber).intValue();
	}

	public static void main(String[] args) {
		System.out.println(getRandomNumber(100));
	}
}
