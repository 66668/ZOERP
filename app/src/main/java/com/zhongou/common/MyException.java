package com.zhongou.common;

import com.zhongou.application.MyApplication;

public class MyException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int code;

	int resId;

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public MyException(int resId) {
		super();
		setResId(resId);
	}

	public MyException(String msg) {
		super(msg);
	}

	public MyException(String msg,int code){
		super(msg);
		this.code = code;
	}
	
	@Override
	public String getMessage() {
		if (resId > 0) {
			try {
				return MyApplication.getInstance().getString(resId);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return super.getMessage();
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}