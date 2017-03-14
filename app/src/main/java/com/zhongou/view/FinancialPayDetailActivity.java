package com.zhongou.view;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.applicationdetailmodel.FinancialAllModel;

/**
 * 应用-财务-借款详情
 * Created by sjy on 2017/2/25.
 */

public class FinancialPayDetailActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //付款方式
    @ViewInject(id = R.id.tv_feeType)
    TextView tv_feeType;

    //收款单位
    @ViewInject(id = R.id.tv_payOfficial)
    TextView tv_payOfficial;

    //账号
    @ViewInject(id = R.id.tv_Account)
    TextView tv_Account;

    //开户行
    @ViewInject(id = R.id.tv_bank)
    TextView tv_bank;
    //金额
    @ViewInject(id = R.id.tv_fee)
    TextView tv_fee;


    //备注
    @ViewInject(id = R.id.tv_remark, click = "RemarkExpended")
    TextView tv_remark;

    //申请人
    @ViewInject(id = R.id.tv_ApprovalPerson)
    TextView tv_ApprovalPerson;

    //部门
    @ViewInject(id = R.id.tv_approvaldept)
    TextView tv_approvaldept;

    //公司
    @ViewInject(id = R.id.tv_approvalCo)
    TextView tv_approvalCo;

    //申请时间
    @ViewInject(id = R.id.tv_approvalTime)
    TextView tv_approvalTime;


    //变量
    private FinancialAllModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_financial_pay_detail);

        initMyView();
        setShow();
    }

    private void initMyView() {
        tv_title.setText(getResources().getString(R.string.financial_pay));
        tv_right.setText("");

        Bundle bundle = this.getIntent().getExtras();
        model = (FinancialAllModel) bundle.getSerializable("FinancialAllModel");
    }

    private void setShow() {
        //
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getCreateTime());

        //
        tv_feeType.setText(model.getWay());
        tv_payOfficial.setText(model.getCollectionUnit());
        tv_Account.setText(model.getAccountNumber());
        tv_bank.setText(model.getBankAccount());
        tv_fee.setText(model.getFee());
        tv_remark.setText(model.getRemark());


    }

    /**
     * back
     *
     * @param view
     */

    public void forBack(View view) {
        this.finish();
    }
    private boolean isRemarkExpend = false;

    public void RemarkExpended(View view) {
        if (!isRemarkExpend) {
            tv_remark.setMinLines(0);
            tv_remark.setMaxLines(Integer.MAX_VALUE);
            isRemarkExpend = true;
        } else {
            tv_remark.setLines(3);
            isRemarkExpend = false;
        }

    }
}
