package com.zhongou.view;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.utils.PageUtil;

/**
 * 修改密码
 */
public class ChangePassWordActivity extends BaseActivity {

	//back
	@ViewInject(id = R.id.layout_back, click = "forBack")
	RelativeLayout layout_back;

	//
	@ViewInject(id = R.id.tv_title)
	TextView tv_title;

	//提交
	@ViewInject(id = R.id.tv_right)
	TextView tv_right;

	// 修改按钮
	@ViewInject(id = R.id.btn_commit, click = "commit")
	Button btn_commit;

	// 旧密码
	@ViewInject(id = R.id.et_oldpsd)
	EditText et_oldpsd;

	// 新密码
	@ViewInject(id = R.id.et_newpsd)
	EditText et_newpsd;

	// 新密码2
	@ViewInject(id = R.id.et_newpsd_again)
	EditText et_newpsd_again;
	
	// 变量
	public String oldUserPassword;
	public String newUserPassword;
	public String psdStr_newAgain;

	//变量
	private static final int CHANGE_SUCCESS = 22;
	private static final int CHANGE_FAILED = 23;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_psn_changepsd);
		tv_title.setText("修改密码");
		tv_right.setText("");
		//获取 storeUserID
	}

	public void commit(View view) {
		//获取界面信息
		oldUserPassword = et_oldpsd.getText().toString().trim();
		newUserPassword = et_newpsd.getText().toString().trim();
		psdStr_newAgain = et_newpsd_again.getText().toString().trim();
		// 非空验证
		if (!checkInput()) {
			return;
		}
		// 线程处理修改密码
		Loading.run(this, new Runnable() {
			@Override
			public void run() {
				
				try {
					String msg = UserHelper.changePassword(ChangePassWordActivity.this,
							oldUserPassword,
							newUserPassword);
					sendMessage(CHANGE_SUCCESS,msg);
				} catch (MyException e) {
					e.printStackTrace();
					// 修改判断
					if (TextUtils.isEmpty(e.getMessage())) {
						sendMessage(CHANGE_FAILED,R.string.person_psd_oldpsd_error);
					} else {
						sendMessage(CHANGE_FAILED,e.getMessage());
					}
				}
			}
		});
	}
	@Override
	protected void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
			case CHANGE_SUCCESS:
				PageUtil.DisplayToast((String)msg.obj);
				clear();
				break;
			case CHANGE_FAILED:
				PageUtil.DisplayToast((String) msg.obj);
				break;
		}
	}
	private void clear(){
		et_oldpsd.setText("");
		et_newpsd.setText("");
		et_newpsd_again.setText("");

		oldUserPassword = null;
		newUserPassword = null;
		psdStr_newAgain = null;
	}
	// 非空验证
	public boolean checkInput() {
		// 非空
		if (TextUtils.isEmpty(oldUserPassword)) {
			PageUtil.DisplayToast(R.string.person_psd_oldpsd_in);
			return false;
		}

		if (TextUtils.isEmpty(et_newpsd.getText().toString().trim())) {
			PageUtil.DisplayToast(R.string.person_psd_newpsd_in);
			return false;
		}

		if (!(newUserPassword.equals(psdStr_newAgain))) {
			PageUtil.DisplayToast(R.string.person_psd_newpsd_errorin);
			return false;
		}
		// 密码长度问题
		if (!(newUserPassword.length() >= 6&& newUserPassword.length() <= 20)) {
			PageUtil.DisplayToast(R.string.person_psd_error_lenght);
			return false;
		}
		//内容要求
		for(int i = 0; i<newUserPassword.length()-1; i++){
			char c = newUserPassword.charAt(i);
			if(!(c >='0' && c <= '9' || c>= 'a'&& c <= 'z' || c >= 'A' && c <='Z' || c == '_')){
				PageUtil.DisplayToast(R.string.person_psd_requestForPsd_error);
				return false;
			}
		}
		//判断是不是原密码
		return true;
	}

	// back
	public void forBack(View view) {
		this.finish();
	}
}
