package com.zhongou.common;

import java.util.HashMap;

/**
 * UserHelper用户登录，将用户名和密码封装到map中 UserHelper用户注册，将用户名和密码封装到map中
 * UserHelper完善个人信息，将用户信息封装到map中
 * @author JackSong
 */

public class HttpParameter extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;

	public static HttpParameter create() {
		HttpParameter p = new HttpParameter();
		return p;
	}
	//map集合的put方法
	public HttpParameter add(String key, String value){
		this.put(key, value);
		return this;
	}
	
	//方法重载
	public HttpParameter add(String key, int value){
		this.put(key, ""+value);
		return this;
	}
}
