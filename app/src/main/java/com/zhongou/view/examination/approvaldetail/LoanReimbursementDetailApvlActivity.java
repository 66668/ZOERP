package com.zhongou.view.examination.approvaldetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.approvaldetailmodel.LoanReimbursementApvlModel;
import com.zhongou.utils.PageUtil;

/**
 * 借款报销
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
    @ViewInject(id = R.id.tv_fee)
    TextView tv_fee;

    //还款时间
    @ViewInject(id = R.id.tv_PlanbackTime)
    TextView tv_PlanbackTime;

    //户名
    @ViewInject(id = R.id.tv_AdminName)
    TextView tv_AdminName;

    //开户行
    @ViewInject(id = R.id.tv_BankAccount)
    TextView tv_BankAccount;

    //账户
    @ViewInject(id = R.id.tv_AccountNumber)
    TextView tv_AccountNumber;

    //备注
    @ViewInject(id = R.id.tv_reason)
    TextView tv_remark;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    //未审批bottom
    @ViewInject(id = R.id.laytout_decide)
    LinearLayout laytout_decide;

    //驳回
    @ViewInject(id = R.id.btn_refulse, click = "forRefulse")
    Button btn_refulse;

    //批准
    @ViewInject(id = R.id.btn_commit, click = "toForCommit")
    Button btn_commit;

    //转交
    @ViewInject(id = R.id.btn_transfer, click = "forTransfer")
    Button btn_transfer;

    //审批bottom
    @ViewInject(id = R.id.laytout_copy)
    LinearLayout laytout_copy;

    //抄送
    @ViewInject(id = R.id.btn_copytp, click = "forCopyto")
    Button btn_copytp;

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    //变量
    private MyApprovalModel myApprovalModel;
    private LoanReimbursementApvlModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_loanreimbursement_d2);
        tv_title.setText(getResources().getString(R.string.loanReimburse_d));
        tv_right.setText("");

        Intent intent = getIntent();
        myApprovalModel = (MyApprovalModel) intent.getSerializableExtra("MyApprovalModel");

        bottomType();
        //
        getDetailData();

    }

    private void setShow(LoanReimbursementApvlModel model) {
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getApplicationCreateTime());

        tv_loanreimType.setText(model.getType());
        tv_useType.setText(model.getUseage());
        tv_wayType.setText(model.getWay());
        tv_fee.setText(model.getFee());

        tv_PlanbackTime.setText(model.getPlanbackTime());
        tv_AdminName.setText(model.getAdminName());
        tv_BankAccount.setText(model.getBankAccount());
        tv_AccountNumber.setText(model.getAccountNumber());
        tv_remark.setText(model.getRemark());
        if (model.getApprovalInfoLists().size() > 0) {
            tv_Requester.setText(model.getApplicationCreateTime());
        } else {
            tv_Requester.setText("未审批");
        }
    }

    private void bottomType() {
        //
        if (myApprovalModel.getApprovalStatus().contains("1")) {

            laytout_decide.setVisibility(View.GONE);
            laytout_copy.setVisibility(View.VISIBLE);

        } else {
            laytout_decide.setVisibility(View.VISIBLE);
            laytout_copy.setVisibility(View.GONE);
        }
    }

    private void getDetailData() {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {

                try {
                    LoanReimbursementApvlModel model = UserHelper.approvalDetailPostVehicleloan(LoanReimbursementDetailApvlActivity.this,
                            myApprovalModel.getApplicationID(),
                            myApprovalModel.getApplicationType());
                    sendMessage(POST_SUCCESS, model);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                model = (LoanReimbursementApvlModel) msg.obj;
                setShow(model);
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    //驳回
    public void forRefulse(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonDisagreeActivity.class,bundle);
    }

    //同意
    public void toForCommit(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonAgreeActivity.class,bundle);
    }

    //转交
    public void forTransfer(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonTransfertoActivity.class,bundle);
    }

    // 抄送
    public void forCopyto(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonCopytoCoActivity.class,bundle);
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
