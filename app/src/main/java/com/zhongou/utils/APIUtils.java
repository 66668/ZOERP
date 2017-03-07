package com.zhongou.utils;

import android.util.Log;

import com.zhongou.common.HttpParameter;
import com.zhongou.common.HttpResult;
import com.zhongou.common.MyException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 访问服务端类 01 将json转成HttpResult对象 toHttpResult 02
 * 
 * @author JackSong
 *
 */
public class APIUtils {
	/**
	 * 01处理json格式 {"message": "","result": "","code":""}的result，
	 * 将result中的数值转换成对应的类型
	 * 
	 * @throws MyException
	 * @throws JSONException
	 * 
	 */
	public static HttpResult toHttpResult(JSONObject jsonObject) throws MyException {
		HttpResult httpResult = new HttpResult();

		try {
			httpResult.returnObject = jsonObject;
			httpResult.code = JSONUtils.getInt(jsonObject, "code");
			if (jsonObject.has("result")) {
				Object dataObject = jsonObject.get("result");// 包含数据

				if (dataObject instanceof JSONObject) {
					httpResult.jsonObject = (JSONObject) dataObject;
				} else if (dataObject instanceof JSONArray) {
					httpResult.jsonArray = (JSONArray) dataObject;
				} else {
					httpResult.resultJsonString = dataObject.toString();
				}
			}
			httpResult.Message = JSONUtils.getString(jsonObject, "message");
		} catch (Exception e) {
			throw new MyException(e.getMessage());
		}
		return httpResult;
	}

	/**
	 * 02-01
	 * 密码登录
	 * 修改密码
	 * 获取访客记录 0/1
	 * 添加wifi签到
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 * @throws MyException
	 */
	public static HttpResult postForObject(String url, HttpParameter parameters) throws MyException {
		// 调用下边方法,登录
		return postForObject(url, parameters, true);
	}

	/**
	 * 02-02
	 * post方法
	 * 
	 * @param url
	 * @param parameters
	 * @param withLogin
	 * @return
	 * @throws MyException
	 */
	public static HttpResult postForObject(String url, HttpParameter parameters, boolean withLogin) throws MyException //HttpResult
			 {
			try{
				// 登录服务器方法/httpURLConnection
				JSONObject jsonObject = HttpUtils.getInstance().postStringURL(url, parameters, withLogin); //
				if(jsonObject.toString() == null|| jsonObject.toString().equals("")){

				}
				Log.d("HTTP", "APIUtils--服务端返回="+jsonObject.toString());// 查看响应的信息

				// 调用本类方法，返回读取的信息，封装在HttpResult中返回给调用方法(登录/注册/验证码)
				return toHttpResult(jsonObject); // 将JSONObject对象-->HttpResult
			} catch (Exception e){
				throw new MyException(e.getMessage());
			}


	}

	/**
	 * 03 添加访客
	 * 
	 * @param url
	 * @param parameters
	 * @param fileName
	 * @return
	 * @throws MyException
	 */
	 
	public static HttpResult postForObject(String url, HttpParameter parameters, File fileName) throws MyException {
		try {
			JSONObject jsonObject = HttpUtils.getInstance().postStringURL(url, parameters, fileName); // httpURLConnection
			Log.d("HTTP", jsonObject.toString());// 查看响应的信息
			// 调用本类方法，返回读取的信息，封装在HttpResult中返回给调用方法(登录/注册/验证码)
			return toHttpResult(jsonObject); // 将JSONObject对象-->HttpResult
		} catch (MyException e) {
            throw new MyException(e.getMessage());
		}
	}

	/**
	 * 04
	 * 获取受访者
	 * 获取一条记录的详细信息
	 * 删除一条或多条记录
	 * 获取登录人信息
	 * httpGet
	 * 
	 * @param url
	 * @return
	 * @throws MyException
	 */

	public static HttpResult getForObject(String url) throws MyException {

		try {
			HttpUtils.getInstance();
			JSONObject jsonObject = HttpUtils.getInstance().getByURL(url);
			Log.d("HTTP", jsonObject.toString());
			return toHttpResult(jsonObject);
		} catch (MyException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			return HttpResult.createError(e);
		}
	}

	/**
	 * Get请求，获取人脸库 返回数据str（已使用，很好用2016.4.20） 获取人脸库
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	private static final int TIMEOUT_IN_MILLIONS = 3000;


}
