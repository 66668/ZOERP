package com.zhongou.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsEmployeeModel;

/**
 * 通讯录员工详情
 * Created by sjy on 2017/1/14.
 */

public class ContactsEplDetailActivity extends BaseActivity {

    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //姓名
    @ViewInject(id = R.id.tv_name)
    TextView tv_name;

    //拨打手机
    @ViewInject(id = R.id.img_phone, click = "forPhone")
    ImageView img_phone;

    //发短信
    @ViewInject(id = R.id.img_message, click = "forMessage")
    ImageView img_message;

    //手机号
    @ViewInject(id = R.id.tv_phone)
    TextView tv_phone;

    //邮箱
    @ViewInject(id = R.id.tv_email)
    TextView tv_email;

    //部门
    @ViewInject(id = R.id.tv_dept)
    TextView tv_dept;

    //部门
    @ViewInject(id = R.id.tv_enterTime)
    TextView tv_enterTime;

    //变量
    private ContactsEmployeeModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contacts_employee_d);
        tv_title.setText("");
        tv_right.setText("");

        //获取跳转值
        Intent intent = getIntent();
        model = (ContactsEmployeeModel) intent.getSerializableExtra("ContactsEmployeModel");
        show();
    }

    private void show() {
        tv_name.setText(model.getsEmployeeName());
        tv_phone.setText(model.getsTelephone());
        tv_email.setText(model.getsEmail());
        tv_dept.setText(model.getsDepartmentName());
        tv_enterTime.setText(model.getsEntryDate());
    }

    /**
     * 打电话
     * 注意权限
     *
     * @param view
     */
    public void forPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getsTelephone()));
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//只进入界面，不打电话
        startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param view
     */

    public void forMessage(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + model.getsTelephone()));
        //        intent.putExtra("sms_body", smsBody);
        startActivity(intent);
    }


    /**
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }


}
