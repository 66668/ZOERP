package com.zhongou.receiver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

//广播关闭程序（）
public class ExitAppReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(context != null){
			if(context instanceof Activity){
				((Activity) context).finish();
				//到这里，多个activity可以关闭掉程序了 但是进程仍然存在，因此加上了下边一句话，可以杀死进程  
//				android.os.Process.killProcess(android.os.Process.myPid());  
			}else if(context instanceof FragmentActivity){
				((FragmentActivity) context).finish();
//				android.os.Process.killProcess(android.os.Process.myPid());  
			}else if(context instanceof Service){
				((Service) context).stopSelf();
//				android.os.Process.killProcess(android.os.Process.myPid());  
			}
		}
	}
}
