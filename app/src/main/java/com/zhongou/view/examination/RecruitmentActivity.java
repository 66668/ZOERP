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
import com.zhongou.utils.Utils;
import com.zhongou.view.ContactsSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 招聘
 * Created by sjy on 2016/12/2.
 */

public class RecruitmentActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //职位名称
    @ViewInject(id = R.id.et_positionRequest)
    EditText et_positionRequest;

    //个数
    @ViewInject(id = R.id.et_requestNumber)
    EditText et_numberOfPeople;
    //时间
    @ViewInject(id = R.id.layout_time, click = "chooseTime")
    LinearLayout layout_time;
    @ViewInject(id = R.id.tv_timeIn)
    TextView sp_timeIn;

    //Responsibility
    @ViewInject(id = R.id.et_responsibility)
    EditText et_responsibility;

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
    private String position;//职位名称
    private String numberOfPeople;
    private String responsibility;
    private String ExpectedEntryDate;//时间
    private String remark = "";//备注
    private String approvalID = "";

    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_recruitment);
        tv_title.setText(getResources().getString(R.string.forjobs));

    }

    /**
     * 选择入职时间
     *
     * @param view
     */
    public void chooseTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(RecruitmentActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        ExpectedEntryDate = time;
                        sp_timeIn.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("入职时间");
        endDateChooseDialog.showDateChooseDialog();
    }


    /**
     * 提交
     */
    public void forCommit(View view) {
        Log.d("SJY", "Utils.getCurrentTime()=" + Utils.getCurrentTime());
        position = et_positionRequest.getText().toString().trim();
        numberOfPeople = et_numberOfPeople.getText().toString().trim();
        responsibility = et_responsibility.getText().toString().trim();
        remark = et_remark.getText().toString();
        if (TextUtils.isEmpty(position)) {
            PageUtil.DisplayToast("招聘职位不能为空");
            return;
        }
        if (TextUtils.isEmpty(numberOfPeople)) {
            PageUtil.DisplayToast("招聘人数不能为空");
            return;
        }
        if (TextUtils.isEmpty(responsibility)) {
            PageUtil.DisplayToast("职位职责不能为空");
            return;
        }
        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }
        Loading.run(RecruitmentActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("Position", position);//职位名称
                    js.put("NumberOfPeople", numberOfPeople);//人数
                    js.put("Responsibility", responsibility);//职责
                    js.put("ExpectedEntryDate", ExpectedEntryDate);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);//审批人

                    UserHelper.recruitmentPost(RecruitmentActivity.this, js);
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
    private void clear() {
        et_positionRequest.setText("");
        et_numberOfPeople.setText("");
        sp_timeIn.setText("");
        et_responsibility.setText("");
        tv_Requester.setText("");
        et_remark.setText("");
        ExpectedEntryDate = null;
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
     */
    public void forBack(View v) {
        this.finish();
    }
}
