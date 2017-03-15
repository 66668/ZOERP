package com.zhongou.view.examination;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * 离职
 * Created by sjy on 2016/12/2.
 */

public class DimissionActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //提交
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //开始时间
    @ViewInject(id = R.id.layout_timestart, click = "startTime")
    LinearLayout layout_timestart;
    @ViewInject(id = R.id.tv_jobsLeaveTimeIn)
    TextView tv_jobsLeaveTimeIn;

    //结束时间
    @ViewInject(id = R.id.layout_timeend, click = "endTime")
    LinearLayout layout_timeend;
    @ViewInject(id = R.id.tv_jobsLeavetimeOut)
    TextView tv_jobsLeavetimeOut;

    //原因
    @ViewInject(id = R.id.et_reason)
    EditText et_reason;

    //备注
    @ViewInject(id = R.id.et_remark)
    EditText et_remark;

    //离职类型
    @ViewInject(id = R.id.layout_dismissionType, click = "dismissionType")
    LinearLayout layout_dismissionType;
    @ViewInject(id = R.id.tv_dismissiontype)
    TextView tv_dismissiontype;


    //添加审批人
    @ViewInject(id = R.id.AddApprover, click = "forAddApprover")
    RelativeLayout AddApprover;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    private String EntryDate;//入职时间
    private String DimissionDate;//离职时间
    private String approvalID = "";
    private String remark = "";
    private String reason = "";
    private String dimissionID = "";//离职类型
    private List<String> approvalIDList = new ArrayList<String>();

    //常量
    public static final int POST_SUCCESS = 15;
    public static final int POST_FAILED = 16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_dismission);
        tv_title.setText(getResources().getString(R.string.jobsForLeave));

    }

    /**
     * 提交
     *
     * @param v
     */
    public void forCommit(View v) {
        reason = et_reason.getText().toString();
        remark = et_remark.getText().toString();
        if (TextUtils.isEmpty(dimissionID)) {
            PageUtil.DisplayToast("离职类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(DimissionDate)) {
            PageUtil.DisplayToast("离职时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            PageUtil.DisplayToast("离职说明不能为空");
            return;
        }

        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }
        Loading.run(DimissionActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("DimissionID", dimissionID);//离职类型
                    js.put("Content", reason);//原因
                    js.put("EntryDate", EntryDate);
                    js.put("DimissionDate", DimissionDate);
                    js.put("Remark", remark);
                    js.put("Reason", reason);
                    js.put("ApprovalIDList", approvalID);

                    UserHelper.dimissionPost(DimissionActivity.this, js);

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
                clear();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }

    private void clear() {
        tv_jobsLeaveTimeIn.setText("");
        tv_jobsLeavetimeOut.setText("");
        et_reason.setText("");
        et_remark.setText("");
        tv_dismissiontype.setText("");
        tv_Requester.setText("");
        EntryDate = null;
        DimissionDate = null;
        approvalID = null;
        dimissionID = null;
    }

    /**
     * 离职类型
     */
    public void dismissionType(View view) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.jobsleavetype));
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        final String[] data = getResources().getStringArray(R.array.spDimissionID);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dimissionID = data[which];
                tv_dismissiontype.setText(dimissionID.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }

    /**
     * 入职时间
     *
     * @param v
     */

    public void startTime(View v) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(DimissionActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        EntryDate = time;
                        tv_jobsLeaveTimeIn.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("入职时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 离职时间
     *
     * @param v
     */
    public void endTime(View v) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(DimissionActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        DimissionDate = time;
                        tv_jobsLeavetimeOut.setText(time);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("离职时间");
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
     *
     */
    public void forBack(View view) {
        this.finish();
    }
}
