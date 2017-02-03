package com.zhongou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongou.R;


public class LoadingDialog extends Dialog {
	private Context context;
	private int icon = 0;
	private Callback callback = null;// 本类中的接口
	private boolean cancelable = true;
	private AnimationDrawable animationDrawalbe;// 调用动画的基本类
	// 构造赋值01
	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialog);// 加载自定义资源位置
		this.context = context;
		init();// 动画效果显示登录状态
	}

	// 02
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		init();
	}
	//03
	public LoadingDialog(Context context, int theme, int icon) {
		super(context, theme);
		this.context = context;
		this.icon = icon;
		init();
	}
	//04
	public LoadingDialog(Context context, Callback callback, int theme, int icon) {
		super(context, theme);
		this.context = context;
		this.icon = icon;
		this.callback = callback;
		// 调用下方法,动画效果显示登录状态
		init();
	}
	
	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	//init
	void init(){
		LinearLayout contentView = new LinearLayout(context);
		contentView.setMinimumHeight(60);//最小高度
		contentView.setGravity(Gravity.CENTER);
		contentView.setOrientation(LinearLayout.HORIZONTAL);
		ImageView img = null;
		
		if(icon == -1){
			return;
		}else {
			//加载登录图片
			img = new ImageView(context);
			img.setImageResource(R.mipmap.loading);//图片
		}
		// 动画类，向控件中添加动画效果，rotate_repeat.xml是动画布局设置
		// 加载动画布局
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate_repeat);
		img.setAnimation(anim);
		//关联
		contentView.addView(img);
		setContentView(contentView);
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
