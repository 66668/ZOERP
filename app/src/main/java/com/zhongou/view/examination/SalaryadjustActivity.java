package com.zhongou.view.examination;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
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
 * Created by sjy on 2016/12/2.
 */

public class SalaryadjustActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //调薪人
    @ViewInject(id = R.id.et_person)
    EditText et_person;

    //目前薪资
    @ViewInject(id = R.id.salary_now)
    EditText et_salary_now;

    //调后薪资
    @ViewInject(id = R.id.salary_after)
    EditText et_salary_after;

    //原因
    @ViewInject(id = R.id.et_reason)
    EditText et_reason;

    //备注
    @ViewInject(id = R.id.et_remark)
    EditText et_remark;

    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;
    //变量
    private String approvalID = "";
    private String TargetEmployee;
    private String reason;
    private String remark;
    private String OriSalary;//调前工资
    private String SrcSalary;//调后工资

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_salaryadjust);
        tv_title.setText(getResources().getString(R.string.salaryAdjust));
    }

    public void forCommit(View view) {
        TargetEmployee = et_person.getText().toString().trim();
        reason = et_reason.getText().toString();
        remark = et_remark.getText().toString();
        OriSalary = et_salary_now.getText().toString();
        SrcSalary = et_salary_after.getText().toString();

        if (TextUtils.isEmpty(TargetEmployee)) {
            PageUtil.DisplayToast("员工不能为空");
            return;
        }
        if (TextUtils.isEmpty(OriSalary) || TextUtils.isEmpty(SrcSalary)) {
            PageUtil.DisplayToast("薪资不能为空");
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("薪资说明不能为空");
            return;
        }
        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }
        Loading.run(SalaryadjustActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("TargetEmployee", TargetEmployee);
                    js.put("OriSalary", OriSalary);
                    js.put("SrcSalary", SrcSalary);
                    js.put("Reason", reason);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);//

                    UserHelper.changeSalary(SalaryadjustActivity.this, js);
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
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
                //                clear();
                startActivity(ZOAplicationListActivity.class);
                this.finish();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }
    private void clear(){
        et_person.setText("");
        et_salary_now.setText("");
        et_salary_after.setText("");
        et_reason.setText("");
        et_remark.setText("");
        tv_Requester.setText("");
        approvalID = null;
    }
    /**
     * 添加审批人
     *
     * @param view
     */
    public void forAddApprover(View view) {
        myStartForResult(ContactsSelectActivity.class, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0)//通过请求码(去SActivity)和回传码（回传数据到第一个页面）判断回传的页面
        {
            //判断返回值是否为空
            List<ContactsEmployeeModel> list = new ArrayList<>();
            if (data != null && (List<ContactsEmployeeModel>) data.getSerializableExtra("data") != null) {
                list = (List<ContactsEmployeeModel>) data.getSerializableExtra("data");
            } else {

            }
            StringBuilder name = new StringBuilder();
            StringBuilder employeeId = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                name.append(list.get(i).getsEmployeeName() + "  ");
                employeeId.append(list.get(i).getsEmployeeID() + ",");
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
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }

}
