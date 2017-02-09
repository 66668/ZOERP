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
 * 调休
 * Created by sjy on 2016/12/2.
 */

public class TakeDaysOffActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //调休开始时间
    @ViewInject(id = R.id.layout_startTime, click = "startTime")
    LinearLayout layout_startTime;
    @ViewInject(id = R.id.tv_timeStart)
    TextView tv_timeStart;


    //调休结束时间
    @ViewInject(id = R.id.layout_end, click = "endTime")
    LinearLayout layout_end;
    @ViewInject(id = R.id.tv_timeEnd)
    TextView tv_timeEnd;

    //原工作开始时间
    @ViewInject(id = R.id.layout_startTimebefore, click = "startTimeBefore")
    LinearLayout layout_startTimebefore;
    @ViewInject(id = R.id.tv_timeStartbefore)
    TextView tv_timeStartbefore;


    //原工作调休结束时间
    @ViewInject(id = R.id.layout_endTimebefore, click = "endTimeBefore")
    LinearLayout layout_endTimebefore;
    @ViewInject(id = R.id.tv_timeEndbefore)
    TextView tv_timeEndBefore;

    //原因
    @ViewInject(id = R.id.et_reason)
    EditText et_reason;

    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;


    private String StartOffDate;
    private String EndOffDate;
    private String StartTakeDate;
    private String EndTakeDate;
    private String reason;
    private String remark = "";
    private String approvalID = "";
    private List<String> approvalIDList = new ArrayList<String>();

    //常量
    public static final int POST_SUCCESS = 19;
    public static final int POST_FAILED = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_takedaysoff);
        tv_title.setText(getResources().getString(R.string.workRest));

    }

    /**
     * 提交
     */
    public void forCommit(View view) {
        reason  = et_reason.getText().toString();
        if (TextUtils.isEmpty(StartOffDate) || TextUtils.isEmpty(EndOffDate)
                || TextUtils.isEmpty(StartTakeDate) ||TextUtils.isEmpty(EndTakeDate)) {
            PageUtil.DisplayToast("时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("调休原因不能为空");
            return;
        }
        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }
        Loading.run(TakeDaysOffActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("StartTakeDate", StartTakeDate);
                    js.put("EndTakeDate", EndTakeDate);
                    js.put("StartOffDate", StartOffDate);
                    js.put("EndOffDate", EndOffDate);
                    js.put("Reason", reason);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);

                    UserHelper.takeDaysOffPost(TakeDaysOffActivity.this, js);
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());

                }catch (JSONException e) {
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
                PageUtil.DisplayToast("成功提交！");
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }
    /**
     * 调休开始时间
     *
     * @param view
     */
    public void startTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        StartOffDate = time;
                        tv_timeStart.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("开始时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 调休结束时间
     *
     * @param view
     */
    public void endTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        EndOffDate = time;
                        tv_timeEnd.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("结束时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 原工作开始时间
     *
     * @param view
     */
    public void startTimeBefore(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        StartTakeDate = time;
                        tv_timeStartbefore.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("开始时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 原工作结束时间
     *
     * @param view
     */
    public void endTimeBefore(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(TakeDaysOffActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        EndTakeDate = time;
                        tv_timeEndBefore.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("结束时间");
        endDateChooseDialog.showDateChooseDialog();
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
            data.getStringExtra("data");
            List<ContactsEmployeeModel> list = (List<ContactsEmployeeModel>) data.getSerializableExtra("data");
            Log.d("SJY", "返回数据=" + list.size());
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
