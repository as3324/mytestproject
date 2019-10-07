package com.example.demo.redis;

import redis.clients.jedis.Jedis;

public class JedisDemo {
	public static final String HOST="192.168.1.129";
	public static void main(String[] args) {
		Jedis jedis= new Jedis(HOST);
		jedis.auth("yej");
		System.out.println(jedis.get("name"));
		jedis.set("passwd", "123123");
		jedis.select(1);
		System.out.println(jedis.get("name"));
	}
}
