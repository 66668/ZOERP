package com.zhongou.common;

import android.os.UserManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问服务端的响应对象
 * @author JackSong
 *
 */
public class HttpResult {
	
	public Object returnObject;
	public int code = -2;
	public String Message = "";
	public JSONObject jsonObject;
	public JSONArray jsonArray;
	public String resultJsonString;
	public List<UserManager> result =  new ArrayList<UserManager>();

	public List<UserManager> getResult() {
		return result;
	}

	public static HttpResult createError(String msg) {
		HttpResult result = new HttpResult();
		result.code = 1;
		result.Message = msg;
		return result;
	}

	public static HttpResult createError(Exception ex) {
		return createError(ex.getMessage());
	}

	// 判断返回数据
	public boolean hasError() {
		Log.d("SJY","HttpResult--hasError="+code);
		return code == 0;
	}

	// 抛异常方法
	public MyException getError() {
		return new MyException(Message, code);
	}
}
