package com.zhongou.dialog;

import android.content.Context;

import org.json.JSONObject;

public class Loading {
	//有弹窗的登录样式
	public static LoadingDialog run(Context context, final Runnable runnalbe){
		return run(context, true, runnalbe);
	}
	//登陆的耗时操作
	public static LoadingDialog run(Context context, boolean cancelable, final Runnable runnable){
		//弹窗显示登录状态
		final LoadingDialog loadingDialog = new LoadingDialog(context);
		loadingDialog.setCanceledOnTouchOutside(false);//弹窗之外触摸无效
		loadingDialog.setCancelable(cancelable);//true:可以按返回键back取消
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					runnable.run();
				} finally{
					//运行完弹窗消失
					loadingDialog.dismiss();
				}
			}
		}).start();
		return loadingDialog;
	}
	//无弹窗的登录样式
	public static LoadingNoDialog noDialogRun(Context context, final Runnable runnalbe){
		return noDialogRun(context, true, runnalbe);
	}

	//登陆的耗时操作
	public static LoadingNoDialog noDialogRun(Context context, boolean cancelable, final Runnable runnable){
		//弹窗显示登录状态
		final LoadingNoDialog loadingNoDialog = new LoadingNoDialog(context);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					runnable.run();
				} finally{

				}
			}
		}).start();

		return loadingNoDialog;
	}

	/*
	 * 添加接口
	 */
	public interface HttpResult {
		void onSuccess(JSONObject result);
		void onError(Exception e);
	}


}
