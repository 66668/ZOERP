package com.zhongou.view.examination;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

/**
 * 财务申请
 * Created by sjy on 2016/12/2.
 */

public class FinancialAplActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    // 借款申请
    @ViewInject(id = R.id.btn_loan, click = "forLoan")
    Button btn_loan;

    //报销申请
    @ViewInject(id = R.id.btn_reimburse, click = "forReimburse")
    Button btn_reimburse;

    //付款申请
    @ViewInject(id = R.id.btn_pay, click = "forPay")
    Button btn_pay;

    //费用申请
    @ViewInject(id = R.id.btn_fee, click = "forFee")
    Button btn_fee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_financial);
        tv_title.setText(getResources().getString(R.string.financial_loan));
    }

    /**
     * 借款
     */
    public void forLoan(View view) {
        startActivity(FinancialLoanActivity.class);

    }

    /**
     * 报销
     */
    public void forReimburse(View view) {
        startActivity(FinancialReimburseActivity.class);
    }

    /**
     * 付款申请
     */
    public void forPay(View view) {
        startActivity(FinancialPayActivity.class);
    }

    /**
     * 费用申请
     */
    public void forFee(View view) {
        startActivity(FinancialFeeActivity.class);
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
