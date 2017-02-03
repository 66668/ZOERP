package com.zhongou.view;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.db.entity.UserEntity;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;

/**
 * 个人信息
 * Created by sjy on 2017/1/3.
 */

public class PersonalInfoActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //标题
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //姓名
    @ViewInject(id = R.id.tv_name)
    TextView tv_name;

    //电话
    @ViewInject(id = R.id.tv_phone, click = "forPhoneNumber")
    TextView tv_phone;

    //公司
    @ViewInject(id = R.id.tv_company)
    TextView tv_company;

    //工号
    @ViewInject(id = R.id.tv_number)
    TextView tv_number;

    //部门
    @ViewInject(id = R.id.tv_department)
    TextView tv_department;

    //职位
    @ViewInject(id = R.id.tv_work)
    TextView tv_work;

    //入职时间
    @ViewInject(id = R.id.tv_timeIn)
    TextView tv_timeIn;

    //变量
    UserEntity entity ;

    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_psn_info);
        tv_title.setText(getResources().getString(R.string.person_info));
        tv_right.setText("");

        entity = UserHelper.getCurrentUser();
        setShow();
    }
    private void setShow() {
        tv_name.setText(entity.getName());
        tv_phone.setText(entity.getTelephone());
        tv_company.setText(entity.getStoreName());
        tv_number.setText(entity.getJobNumber());
        tv_department.setText(entity.getDepartmentName());
        tv_work.setText(entity.getPostName());
        tv_timeIn.setText(entity.getEntryDate());
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
