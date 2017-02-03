package com.zhongou.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
	private NetworkManager(){}
	//判断是否有网络（UserHelper在线登录）,返回boolean
	public static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		////得到网络连接信息
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			//没有网络返回false
			result = false;
		} else {
			//获取网络连接实例的数组
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED//对每一个info判断哪一个是正在连接的
							&&(info[i].getTypeName().equalsIgnoreCase("MOBILE")//满足是“移动”的情况
									|| info[i].getTypeName().equalsIgnoreCase("WIFI"))) {//满足是“wifi”的情况
						result = true;
						break;
					}
				}
			}
		}
		return result;
	} 
}
