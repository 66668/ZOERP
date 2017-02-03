package com.zhongou.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongou.R;
import com.zhongou.application.MyApplication;


/**
 *吐司 
 * @author JackSong
 *
 */
public class PageUtil {
	public static void DisplayToast(String loginactivityCompanyid) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), loginactivityCompanyid,Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static void DisplayToast(int loginactivityCompanyid) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), loginactivityCompanyid,Toast.LENGTH_SHORT);
		toast.show();
	}
	public static void toastInMiddle(String str){
		LayoutInflater inflater = LayoutInflater.from(MyApplication.getInstance());
		View view = inflater.inflate(R.layout.toast_in_middle, null);
		TextView chapterNameTV = (TextView) view.findViewById(R.id.toast);
		chapterNameTV.setText(str);

		Toast toast = new Toast(MyApplication.getInstance());
		toast.setGravity(Gravity.CENTER, 0, 50);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
}
