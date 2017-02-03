package com.zhongou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;

import com.zhongou.R;


public class LoadingNoDialog extends Dialog {
	private Context context;
	private int icon = 0;
	private Callback callback = null;// 本类中的接口
	private boolean cancelable = true;
	private AnimationDrawable animationDrawalbe;// 调用动画的基本类
	// 构造赋值01
	public LoadingNoDialog(Context context) {
		super(context, R.style.LoadingDialog);// 加载自定义资源位置
		this.context = context;
	}

	// 02
	public LoadingNoDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}
	//03
	public LoadingNoDialog(Context context, int theme, int icon) {
		super(context, theme);
		this.context = context;
		this.icon = icon;

	}
	//04
	public LoadingNoDialog(Context context, Callback callback, int theme, int icon) {
		super(context, theme);
		this.context = context;
		this.icon = icon;
		this.callback = callback;

	}
	
	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	//back键修改
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && cancelable){
			if(callback != null){
				callback.update();//调用本类的接口方法
			}
			this.dismiss();
		}
		return true;
	}
	// 接口
	public interface Callback {
		// 接口方法
		public void update();
	}
}
