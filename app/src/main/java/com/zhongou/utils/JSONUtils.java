package com.zhongou.utils;

import com.google.gson.Gson;
import com.google.gson.JsonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * 
 * @author JackSong
 *
 */
public class JSONUtils {
	// 采取单例模式
	private static Gson gson = new Gson();

	private JSONUtils() {
	}
	
	/**01 对象转json
	 * 
	 * @MethodName : toJson
	 * @Description : 将对象转为JSON串，此方法能够满足大部分需求
	 * @param src
	 *            :将要被转化的对象
	 * @return :转化后的JSON串
	 */
	public static String toJson(Object src) {
		if (src == null) {
			return gson.toJson(JsonNull.INSTANCE);
		}
		return gson.toJson(src);
	}

	/**02 对象
	 * 
	 * @MethodName : fromJson
	 * @Description : 用来将JSON串转为对象，但此方法不可用来转带泛型的List集合
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> Object fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, (Type) classOfT);
	}
	
	/** 03 获取对象
	 * 
	 * @MethodName : fromJson
	 * @Description : 用来将JSON串转为对象，此方法可用来转带泛型的集合，如：Type为 new
	 *              TypeToken<List<T>>(){}.getType()
	 *              ，其它类也可以用此方法调用，就是将List<T>替换为你想要转成的类
	 * @param json
	 * @param typeOfT
	 * @return
	 */
	public static Object fromJson(String json, Type typeOfT) {
		return gson.fromJson(json, typeOfT);
	}

	public static JSONObject getJsonObject(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				Object jsonObj = json.get(key);
				if (jsonObj instanceof JSONObject) {
					return (JSONObject) jsonObj;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	/**04 String
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getString(JSONObject json, String key) {
		return getString(json, key, "");
	}

	public static String getString(JSONObject json, String key, String defaultValue) {
		try {
			return json.has(key) ? json.getString(key) : defaultValue;
		} catch (JSONException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}
	/**05 int
	 * 
	 * @param json
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Integer getInt(JSONObject json, String key,
			Integer defaultValue) {
		try {
			return json.has(key) ? json.getInt(key) : defaultValue;
		} catch (JSONException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static Integer getInt(JSONObject json, String key) {
		return getInt(json, key, 0);
	}
	/**06 boolean
	 * 
	 * @param json
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Boolean getBoolean(JSONObject json, String key,
			Boolean defaultValue) {
		try {
			return json.has(key) ? json.getBoolean(key) : defaultValue;
		} catch (JSONException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static Boolean getBoolean(JSONObject json, String key) {
		return getBoolean(json, key, false);
	}
	/**07 double
	 * 
	 * @param json
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Double getDouble(JSONObject json, String key,
			Double defaultValue) {
		try {
			return json.has(key) ? json.getDouble(key) : defaultValue;
		} catch (JSONException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static Double getDouble(JSONObject json, String key) {
		return getDouble(json, key, 0d);
	}
	/**08 long
	 * 
	 * @param json
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Long getLong(JSONObject json, String key, Long defaultValue) {
		try {
			return json.has(key) ? json.getLong(key) : defaultValue;
		} catch (JSONException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static Long getLong(JSONObject json, String key) {
		return getLong(json, key, 0l);
	}
	/**09 数组
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static JSONArray getJSONArray(JSONObject json, String key){
		try {
			return json.getJSONArray(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}