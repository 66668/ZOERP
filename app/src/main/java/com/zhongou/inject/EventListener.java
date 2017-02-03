package com.zhongou.inject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import java.lang.reflect.Method;

public class EventListener implements OnClickListener, OnLongClickListener, OnItemClickListener,
OnItemSelectedListener,OnItemLongClickListener {
	private Object object;
	
	private String clickMethod;//01
	private String longClickMethod;//02
	private String itemClickMethod;//03
	private String itemLongClickMehtod;//04
	private String itemSelectMethod;//05
	private String nothingSelectedMethod;//06
	
	//构造赋值
	public EventListener (Object object){
		this.object = object;
	}
	//01
	public EventListener click(String method){
		this.clickMethod = method;
		return this;
	}
	//02
	public EventListener longClick(String method){
		this.longClickMethod = method;
		return this;
	}
	//03
	public EventListener itemClick(String method){
		this.itemClickMethod = method;
		return this;
	}
	//04
	public EventListener itemLongClick(String method){
		this.itemLongClickMehtod = method;
		return this;
	}
	//05
	public EventListener select(String method){
		this.itemSelectMethod = method;
		return this;
	}
	//06
	public EventListener noSelect(String method){
		this.nothingSelectedMethod = method;
		return this;
	}
	//01-12
	public void onClick(View v) {
		invokeClickMethod(object, clickMethod, v);
	}
	//02-07
	public boolean onLongClick(View v) {
		return invokeLongClickMethod(object,longClickMethod,v);
	}
	//03-11调用
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		invokeItemClickMethod(object,itemClickMethod,arg0,arg1,arg2,arg3);
	}
	//04-08
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		try {
			return invokeItemLongClickMethod(object,itemLongClickMehtod,arg0,arg1,arg2,arg3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	//05-09
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		
		invokeItemSelectMethod(object,itemSelectMethod,arg0,arg1,arg2,arg3);
	}
	//06-10
	public void onNothingSelected(AdapterView<?> arg0) {
		invokeNoSelectMethod(object,nothingSelectedMethod,arg0);
	}
	
	//01-12调用
	private static Object invokeClickMethod(Object object, String methodName, Object...params){
		if(object == null){
			return null;
		}
		Method method = null;
		try {
			method = object.getClass().getDeclaredMethod(methodName, View.class);
			if(method != null){
				return method.invoke(object, params);
			}else{
				throw new Exception("没有这个方法："+methodName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	//02-07调用
	private static boolean invokeLongClickMethod(Object object, String methodName, Object...params){
		if(object == null){
			return false;
		}
		Method method = null;
		try {
			method = object.getClass().getDeclaredMethod(methodName, View.class);
			if(method != null){
				Object obj = method.invoke(object, params);
				return obj==null?false:Boolean.valueOf(obj.toString());
			}else{
				throw new Exception("没有这个方法:"+methodName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	//03-11调用
	private static Object invokeItemClickMethod(Object object, String methodName, Object... params){
		if(object == null){
			return null;
		}
		Method method = null;
		try {
			method = object.getClass().getDeclaredMethod(methodName,
					AdapterView.class,
					View.class,
					int.class,
					long.class);
			if(method != null){
				return method.invoke(object, params);
			}else{
				throw new Exception("没有这个方法："+methodName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	//04-08调用
	private static boolean invokeItemLongClickMethod(Object object, String methodName, Object...parames) throws Exception{
		if(object == null){
			throw new Exception("invokeItemLongClickMethod()方法的object是空！");
		}
		Method method = null;
		
		try {
			method = object.getClass().getDeclaredMethod(methodName,
					AdapterView.class, 
					View.class, 
					int.class, 
					long.class);
			if(method != null){
				Object obj = method.invoke(object, parames);
				return Boolean.valueOf(obj==null?false:Boolean.valueOf(obj.toString()));
			}else{
				throw new Exception("没有这个方法："+methodName);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	//05-09调用
	private static Object invokeItemSelectMethod(Object object, String methodName, Object...params){
		if(object == null){
			return null;
		}
		Method method = null;
		try {
			method = object.getClass().getDeclaredMethod(methodName,
					AdapterView.class,
					View.class,
					int.class,
					long.class);
			if(method != null){
				return method.invoke(object, params);//object对象带参params,返回object对象
			}else{
				throw new Exception("没有这个方法："+methodName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//06-10调用
	private static Object invokeNoSelectMethod(Object object, String methodName, Object...params){
		if(object == null){
			return null;
		}
		Method method = null;
		try {
			method = object.getClass().getDeclaredMethod(methodName, AdapterView.class);
			if(method != null){
				return method.invoke(object, params);
			}else{
				throw new Exception("没有这个方法："+methodName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
