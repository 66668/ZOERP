package com.zhongou.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhongou.R;
import com.zhongou.utils.LogUtils;


/**
 *Fragment基类
 * 
 * @author dewyze
 * 
 */
public abstract class BaseFragment extends Fragment implements IBaseView{
	private Toast mToast;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.i(getFragmentName(), " onAttach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.i(getFragmentName(), " onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.i(getFragmentName(), " onCreateView()");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils.i(getFragmentName(), " onViewCreated()");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.i(getFragmentName(), " onActivityCreated()");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.i(getFragmentName(), " onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i(getFragmentName(), " onResume()");
		if(!hasMultiFragment()) {
			//  MobclickAgent.onPageStart(setFragmentName()); //统计页面，"MainScreen"为页面名称，可自定义
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtils.i(getFragmentName(), " onPause()");
		if(!hasMultiFragment()) {
			//  MobclickAgent.onPageEnd(setFragmentName());
		}
	}
	public abstract boolean hasMultiFragment();
	protected abstract String setFragmentName();

	@Override
	public void onStop() {
		super.onStop();
		LogUtils.i(getFragmentName(), " onStop()");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.i(getFragmentName(), " onDestroyView()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i(getFragmentName(), " onDestroy()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtils.i(getFragmentName(), " onDetach()");
	}

	/**
	 * fragment name
	 */
	public abstract String getFragmentName();

	@Override
	public void showProgress(String message) {
		//  mDialogLoad.setMessage(message);
		// mDialogLoad.showDialog();
	}
	@Override
	public void showProgress() {
		showProgress("");
	}

	@Override
	public void cancelProgress() {

		//mDialogLoad.cancelDialog();
	}
	@Override
	public void showTheToast(int resId) {
		showTheToast(getString(resId));
	}
	@Override
	public void showTheToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	@Override
	public Context getContext() {
		return getActivity();
	}
	@Override
	public void onServerFail(String msg) {

	}

	/**
	 * 根据传入的类(class)打开指定的activity
	 * @param pClass
	 */
	protected void startThActivity(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(getActivity(), pClass);
		startActivity(_Intent);
		getActivity().overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
	}

	protected void startThActivityByIntent(Intent pIntent){
		startActivity(pIntent);
		getActivity().overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
	}

}
