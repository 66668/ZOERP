package com.zhongou.view;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.utils.ConfigUtil;
import com.zhongou.utils.PageUtil;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by sjy on 2016/11/23.
 */

public class LoginActivity extends BaseActivity {

    //公司账号
    @ViewInject(id = R.id.tv_AdminUserName)
    EditText et_storeId;

    //用户账号
    @ViewInject(id = R.id.tv_userName)
    EditText et_workId;

    //密码
    @ViewInject(id = R.id.tv_password)
    EditText et_password;

    //登录
    @ViewInject(id = R.id.btn_login, click = "forLogin")
    Button btn_login;

    //常量
    private final int LOGIN_SUCESS = 2001; // 登陆成功
    private final int LOGIN_FAILED = 2002; // 失败
    private static final int REQUEST_PERMISSION = 0;// SDK服务是否启动

    //变量
    boolean isLogin = false;// 是否登录
    private String storeId, workId, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        //判断自动登录
        if (MyApplication.getInstance().isLogin()) {
            startActivity(MainActivity.class);
            this.finish();
        }
        //中断保存
        ConfigUtil configUtil = new ConfigUtil(this);
        et_storeId.setText(configUtil.getStoreId());
        et_workId.setText(configUtil.getWorkId());
        et_password.setText(configUtil.getPassword());

    }

    // 登录
    public void forLogin(View view) {
        // //非空验证
        if (!checkInput()) {
            return;
        }

        String rid = JPushInterface.getRegistrationID(getApplicationContext());//获取推送id
        Log.d("SJY", "login--RegistrationID=" + rid);

        storeId = et_storeId.getText().toString().trim();
        workId = et_workId.getText().toString().trim();
        password = et_password.getText().toString().trim();
        Loading.run(this, new Runnable() {

            @Override
            public void run() {
                try {
                    UserHelper.loginByPs(LoginActivity.this,
                            storeId, // 公司编号
                            workId, // 工号
                            password// 密码
                    );
                    // 访问服务端成功，消息处理
                    sendMessage(LOGIN_SUCESS,workId);
                    //设置自动登录
                    MyApplication.getInstance().setIsLogin(true);
                } catch (MyException e) {
                    sendMessage(LOGIN_FAILED, e.getMessage());
                }
            }
        });

    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case LOGIN_SUCESS: // 1001
                // 文本跳转
                startActivity(MainActivity.class);
                this.finish();// 页面注销
                break;
            case LOGIN_FAILED: // 1001
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    /*
     * 非空验证
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(et_storeId.getText().toString().trim())) {
            PageUtil.DisplayToast(R.string.LoginActivity_companyNum);
            return false;
        }
        if (TextUtils.isEmpty(et_workId.getText().toString().trim())) {
            PageUtil.DisplayToast(R.string.LoginActivity_employeeNum);// 工号
            return false;
        }
        if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
            PageUtil.DisplayToast(R.string.LoginActivity_psd);
        }
        return true;
    }




}
