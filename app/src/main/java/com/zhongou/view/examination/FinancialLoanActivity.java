package com.zhongou.view.examination;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.DateChooseWheelViewDialog;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.view.ContactsSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 借款申请
 * Created by sjy on 2016/12/2.
 */

public class FinancialLoanActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //还款时间
    @ViewInject(id = R.id.layout_loanReturnTime, click = "returnLoanTime")
    LinearLayout layout_loanReturnTime;
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //金额
    @ViewInject(id = R.id.et_Fee)
    EditText et_Fee;

    //原因
    @ViewInject(id = R.id.et_Reason)
    EditText et_Reason;

    //备注
    @ViewInject(id = R.id.et_remark)
    EditText et_remark;

    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    //变量
    private String returnLoanTime = "";//还款时间
    private String approvalID = "";
    private String reason = "";
    private String remark = "";
    private String fee = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_financial_loan);
        tv_title.setText(getResources().getString(R.string.financial_loan));

    }

    /**
     * 数据提交
     *
     * @param view
     */
    public void forCommit(View view) {
        fee = et_Fee.getText().toString().trim();
        reason = et_Reason.getText().toString();
        remark = et_remark.getText().toString();
        if (TextUtils.isEmpty(returnLoanTime)) {
            PageUtil.DisplayToast("时间不能为空");
            return;
        }

        if (TextUtils.isEmpty(fee)) {
            PageUtil.DisplayToast("金额不能为空");
            return;
        }

        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("借款事由不能为空");
            return;
        }

        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为s空");
            return;
        }

        Loading.run(FinancialLoanActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();

                    //参数
                    js.put("PlanbackTime", returnLoanTime);
                    js.put("Type", getResources().getString(R.string.financial_loan_apl));//借款
                    js.put("Fee", fee);
                    js.put("Reason", reason);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);

                    String resultMessage = UserHelper.LRApplicationPost(FinancialLoanActivity.this, js);

                    Log.d("SJY", "failed=" + resultMessage);
                    sendMessage(POST_SUCCESS, resultMessage);
                } catch (MyException e) {
                    Log.d("SJY", "failed=" + e.getMessage());
                    sendMessage(POST_FAILED, e.getMessage());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                PageUtil.DisplayToast(getResources().getString(R.string.approval_success));
                //清空
                clear();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }

    private void clear() {
        et_Fee.setText("");
        et_Reason.setText("");
        returnLoanTime = null;
        approvalID = null;
        tv_startTime.setText("");
        tv_Requester.setText("");

    }

    /**
     * 添加审批人
     *
     * @param view
     */
    public void forAddApprover(View view) {
        myStartForResult(ContactsSelectActivity.class, 0);
    }

    //添加审批人 回调处理 待修改 0220
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {//通过请求码(去SActivity)和回传码（回传数据到第一个页面）判断回传的页面

            //判断返回值是否为空
            List<ContactsEmployeeModel> list = new ArrayList<>();
            if (data != null && (List<ContactsEmployeeModel>) data.getSerializableExtra("data") != null) {
                list = (List<ContactsEmployeeModel>) data.getSerializableExtra("data");
            } else {

            }

            StringBuilder name = new StringBuilder();
            StringBuilder employeeId = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                name.append(list.get(i).getsEmployeeName() + "  ");//展示名字
                employeeId.append(list.get(i).getsEmployeeID() + ",");//后台处理id
            }
            //            approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
            approvalID = getApprovalID(employeeId.toString());
            Log.d("SJY", "approvalID=" + approvalID);
            tv_Requester.setText(name);
        }

    }

    /*
     *处理字符串，去除末尾逗号
     */
    private String getApprovalID(String str) {
        if (str.length() > 1) {
            return str.substring(0, str.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * 还款时间
     *
     * @param view
     */
    public void returnLoanTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(FinancialLoanActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        returnLoanTime = time;
                        tv_startTime.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);//分钟
        endDateChooseDialog.setDateDialogTitle("还款时间");
        endDateChooseDialog.showDateChooseDialog();
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
