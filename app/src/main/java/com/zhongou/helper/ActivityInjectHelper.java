package com.zhongou.helper;

import android.app.Activity;
import android.view.View;

public class ActivityInjectHelper extends InjectHelper {
	//添加get方法
	public Activity getActivity(){
		return (Activity)mObject;
	}
	//添加构造方法
	public ActivityInjectHelper(Activity activity) {
		super(activity);
	}
	//重写父类方法
	@Override
	protected View findViewById(int id) {
		return getActivity().findViewById(id);
	}
}
