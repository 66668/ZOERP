package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjy on 2016/12/2.
 */

public class LoanReimbursementDetailApvlActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //申请类型
    @ViewInject(id = R.id.tv_loanreimType)
    TextView tv_loanreimType;

    //用途类型
    @ViewInject(id = R.id.tv_useType)
    TextView tv_useType;

    //方式
    @ViewInject(id = R.id.tv_wayType)
    TextView tv_wayType;

    //金额
    @ViewInject(id = R.id.et_Fee)
    EditText et_Fee;

    //说明
    @ViewInject(id = R.id.et_Reason)
    EditText et_Reason;

    //申请人
    @ViewInject(id =R.id.tv_ApprovalPerson)
    EditText tv_ApprovalPerson;

    //部门
    @ViewInject(id =R.id.tv_approvaldept)
    EditText tv_approvaldept;

    //公司
    @ViewInject(id =R.id.tv_approvalCo)
    EditText tv_approvalCo;

    //申请时间
    @ViewInject(id =R.id.tv_approvalTime)
    EditText tv_approvalTime;

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    //变量
    private String approvalID = "";
    private String Type = "";
    private String Useage = "";
    private String way = "";
    private String fee = "";
    private String reason = "";
    private List<String> approvalIDList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_loanreimbursement_d2);
        tv_title.setText(getResources().getString(R.string.loanReimburse_d));
        tv_right.setText("");

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
