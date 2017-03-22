package com.zhongou.view.examination;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
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
 * 用车申请
 * Created by sjy on 2016/12/2.
 */

public class VehicleActivity extends BaseActivity {

    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;


    //目的地
    @ViewInject(id = R.id.et_targetPlace)
    EditText et_targetPlace;

    //开始时间
    @ViewInject(id = R.id.layout_startTime, click = "startTime")
    LinearLayout layout_startTime;
    @ViewInject(id = R.id.tv_timeStart)
    TextView tv_timeStart;


    //结束时间
    @ViewInject(id = R.id.layout_endTime, click = "endTime")
    LinearLayout layout_endTime;
    @ViewInject(id = R.id.tv_timeEnd)
    TextView tv_timeEnd;

    //说明
    @ViewInject(id = R.id.et_purpose)
    EditText et_purpose;

    //申请备注
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
    private String purpose = "";
    public String startDate;
    public String endDates;
    public String remark;
    public String Destination;//目的地

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_vehicle);
        tv_title.setText(getResources().getString(R.string.carUsed));
    }

    public void forCommit(View view) {
        purpose = et_purpose.getText().toString();
        Destination = et_targetPlace.getText().toString();
        remark = et_remark.getText().toString();

        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDates)) {
            PageUtil.DisplayToast("时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(Destination)) {
            PageUtil.DisplayToast("目的地不能为空");
            return;
        }
        if (TextUtils.isEmpty(purpose)) {
            PageUtil.DisplayToast("用途不能为空");
            return;
        }
        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }

        Loading.run(VehicleActivity.this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    js.put("PlanBorrowTime", startDate);
                    js.put("PlanReturnTime", endDates);
                    js.put("Destination", Destination);
                    js.put("Purpose", purpose);
                    js.put("Remark", remark);//
                    js.put("ApprovalIDList", approvalID);//

                    UserHelper.vehiclePost(VehicleActivity.this, js);
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
        et_targetPlace.setText("");
        tv_timeStart.setText("");
        tv_timeEnd.setText("");
        et_purpose.setText("");
        et_remark.setText("");
        tv_Requester.setText("");
        approvalID = null;
        startDate = null;
        endDates = null;
    }
    /**
     * 开始时间
     *
     * @param view
     */
    public void startTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(VehicleActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        startDate = time;
                        tv_timeStart.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("出发时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 结束时间
     *
     * @param view
     */
    public void endTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(VehicleActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        endDates = time;
                        tv_timeEnd.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("返回时间");
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
            approvalID = getApprovalID(employeeId.toString());
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
