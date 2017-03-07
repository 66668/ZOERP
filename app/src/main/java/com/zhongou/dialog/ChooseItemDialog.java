package com.zhongou.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.utils.Utils;


/**
 * 相机Util调用
 * 
 * @author JackSong
 *
 */

public class ChooseItemDialog extends Dialog implements View.OnClickListener{

	private Context context;
	private ChooseItemDialogCallBack callBack;
	private ImageView item1SelectedSign;
	private ImageView item2SelectedSign;
	private int whichItem;
	private boolean ifShowSign = true;
	
	public ChooseItemDialog(Context context, String item1Text, String item2Text, int whichItem, ChooseItemDialogCallBack callBack) {
		super(context, R.style.LoadingDialog);//自定义dialog样式
		this.context = context;
		this.callBack = callBack;
		this.whichItem = whichItem;
		init(item1Text,item2Text);
	}
	//CreateUserActivity--UpdateAvatar--ChooseItemDialog该方法：构造赋值
	public ChooseItemDialog(Context context, String item1Text, String item2Text, int whichItem, boolean ifShowSign, ChooseItemDialogCallBack callBack) {
		super(context, R.style.LoadingDialog);//自定义dialog样式
		this.context = context;
		this.callBack = callBack;
		this.whichItem = whichItem;
		this.ifShowSign = ifShowSign;
		//调用方法，设置弹窗内容及样式
		init(item1Text,item2Text);
	}
	//接口类,实现方法在UpdateAvatarUtil中使用：CreateUserActivity--CameraGalleryUtils--ChooseItemDialog该方法，处理弹窗选择
	public interface ChooseItemDialogCallBack{
		
		public void confirm(int whichItem);
		
		public void cancel();
		
	}

	//调用上边的方法，
	private void init(String item1Text,String item2Text) {
		//关联布局
		View dialogView = LayoutInflater.from(context).inflate(R.layout.act_item_choose, null);
		//实例化相关控件（1）
		RelativeLayout item1 = (RelativeLayout) dialogView.findViewById(R.id.item1);
		RelativeLayout item2 = (RelativeLayout) dialogView.findViewById(R.id.item2);
		//实例化相关控件（2）
		TextView item1TextView = (TextView) dialogView.findViewById(R.id.txt_item1);
		TextView item2TextView = (TextView) dialogView.findViewById(R.id.txt_item2);
		TextView cancelTextView = (TextView) dialogView.findViewById(R.id.cancel_btn);
		//实例化相关控件（3）
		item1SelectedSign = (ImageView) dialogView.findViewById(R.id.item1_selected_sign);//对号
		item2SelectedSign = (ImageView) dialogView.findViewById(R.id.item2_selected_sign);//对号
		
		item1TextView.setText(item1Text);//相机
		item2TextView.setText(item2Text);//从相册中选择
		//选择监听
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
		//调用方法，设置对号的显示和隐藏（没有效果）
		initItemSign();
		//弹窗中取消按钮的监听
		cancelTextView.setOnClickListener(this);
		//关联控件
		setContentView(dialogView);
		setCanceledOnTouchOutside(false);//弹窗范围以外的触摸无效
		getWindow().setGravity(Gravity.BOTTOM);//弹窗在底部出现
	}
	//init()方法中调用/弹窗响应中调用
	private void initItemSign(){
		
		if(ifShowSign){//拍照选择，传值是0
			if(whichItem == 0){
				//实际运行效果不大
				item1SelectedSign.setVisibility(View.VISIBLE);//imageView对号1 设置可见
				item2SelectedSign.setVisibility(View.INVISIBLE);//imageViwe对号2 设置不可见
			}else{
				item1SelectedSign.setVisibility(View.INVISIBLE);
				item2SelectedSign.setVisibility(View.VISIBLE);
			}
		}else{
			item1SelectedSign.setVisibility(View.INVISIBLE);
			item2SelectedSign.setVisibility(View.INVISIBLE);
		}
	}
	//弹窗显示位置设置
	@Override
	public void show() {
		super.show();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		int screenWidth = (int)(Utils.getScreenWidth((Activity)context));
		lp.width = screenWidth - screenWidth * 60 / 640;
		lp.gravity = Gravity.BOTTOM;
		this.getWindow().setAttributes(lp);
	}
	
	
	
	@Override
	//返回back键监听
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			callBack.cancel();
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	//设置监听的响应，设置数值
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item1://弹窗的监听：相机
			dismiss();//弹窗消失
			whichItem = 0;
			initItemSign();
			callBack.confirm(0);//拍照
			break;
		case R.id.item2://相册
			dismiss();
			whichItem = 1;
			initItemSign();
			callBack.confirm(1);//自定义相机
			break;
		case R.id.cancel_btn://取消
			dismiss();
			callBack.cancel();
			break;
		default:
			break;
		}
	}
	
}
