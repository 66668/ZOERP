package com.zhongou.view.examination.applicationdetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApplicationModel;
import com.zhongou.model.applicationdetailmodel.LoanReimbursementModel;
import com.zhongou.utils.PageUtil;

import java.util.List;

/**
 * 借款详情
 * Created by sjy on 2016/12/2.
 */

public class LoanReimbursementDetailActivity extends BaseActivity {
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
    @ViewInject(id = R.id.tv_type)
    TextView tv_type;

    //用途
    @ViewInject(id = R.id.tv_Useage)
    TextView tv_Useages;

    //方式
    @ViewInject(id = R.id.tv_way)
    TextView tv_way;

    //金额
    @ViewInject(id = R.id.tv_fee)
    TextView tv_fee;

    //说明
    @ViewInject(id = R.id.tv_reason)
    TextView tv_reason;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    //变量
    private Intent intent = null;
    private LoanReimbursementModel loanReimbursementModel;
    private MyApplicationModel model;
    private List<LoanReimbursementModel.ApprovalInfoLists> modelList;

    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_loanreimbursement_d);
        tv_title.setText(getResources().getString(R.string.loanReimburse_d));
        tv_right.setText("");

        intent = getIntent();
        model = (MyApplicationModel) intent.getSerializableExtra("MyApplicationModel");
        getDetailModel(model);
    }

    private void setShow(LoanReimbursementModel model) {
        tv_type.setText(model.getType());
        tv_Useages.setText(model.getUseage());
        tv_way.setText(model.getWay());
        tv_fee.setText(model.getFee());
        tv_reason.setText(model.getReason());

        modelList = model.getApprovalInfoLists();
        tv_Requester.setText(
                modelList.get(0).getApprovalDate() +
                        modelList.get(0).getApprovalEmployeeName() +
                        modelList.get(0).getComment() +
                        modelList.get(0).getYesOrNo()
        );

    }

    /**
     * 获取详情数据
     */
    public void getDetailModel(final MyApplicationModel model) {
        final String ApplicationID = model.getApplicationID();
        final String ApplicationType = model.getApplicationType();
        final String StoreID = model.getStoreID();
        final String EmployeeID = model.getEmployeeID();

        if (TextUtils.isEmpty(ApplicationID)) {
            PageUtil.DisplayToast("intent没有传递数值ApplicationID");
            return;

        }
        if (TextUtils.isEmpty(ApplicationType)) {
            PageUtil.DisplayToast("intent没有传递数值ApplicationType");
            return;
        }
        if (TextUtils.isEmpty(StoreID)) {
            PageUtil.DisplayToast("intent没有传递数值StoreID");
            return;
        }
        if (TextUtils.isEmpty(EmployeeID)) {
            PageUtil.DisplayToast("intent没有传递数值EmployeeID");
            return;
        }
        Log.d("SJY", ApplicationID + ApplicationType + StoreID + EmployeeID);
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    LoanReimbursementModel model1 =UserHelper.applicationDetailPostLoan(LoanReimbursementDetailActivity.this,
                            ApplicationID,
                            ApplicationType,
                            StoreID,
                            EmployeeID);
                    sendMessage(POST_SUCCESS, model1);
                } catch (MyException e) {
                    e.printStackTrace();
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS: // 1001
                loanReimbursementModel = (LoanReimbursementModel) msg.obj;
                setShow(loanReimbursementModel);
                break;
            case POST_FAILED: // 1001
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
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
