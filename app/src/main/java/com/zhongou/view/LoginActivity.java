package com.zhongou.view;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.igexin.sdk.PushManager;
import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.application.MyApplication;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.utils.ConfigUtil;
import com.zhongou.utils.PageUtil;


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
    private String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        //中断保存
        ConfigUtil configUtil = new ConfigUtil(this);
        et_storeId.setText(configUtil.getStoreId());
        et_workId.setText(configUtil.getWorkId());
        et_password.setText(configUtil.getPassword());

        //个推设置
        // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;


        // read phone state用于获取  设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(android.Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
            PushManager.getInstance().initialize(this.getApplicationContext());
        }
        //开始推送（需要clientID所以写在这里）
        PushManager.getInstance().turnOnPush(this);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext());
            } else {
                Log.d("SJY", "需要重新获取权限，否则一些功能无法正常使用");
                PushManager.getInstance().initialize(this.getApplicationContext());
            }
        } else {
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    // 登录
    public void forLogin(View view) {
        // //非空验证
        if (!checkInput()) {
            return;
        }

        storeId = et_storeId.getText().toString().trim();
        workId = et_workId.getText().toString().trim();
        password = et_password.getText().toString().trim();
//        clientId = MyApplication.getInstance().getClientID();//个推设备号
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
                    sendMessage(LOGIN_SUCESS);
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
                this.finish();// 页面注销
                // 文本跳转
                startActivity(MainActivity.class);
                break;
            case LOGIN_FAILED: // 1001
                PageUtil.DisplayToast((String)msg.obj);
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
