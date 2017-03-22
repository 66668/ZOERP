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
 * Created by sjy on 2016/12/2.
 */

public class VehicleMaintainActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forCommit")
    TextView forCommit;

    //维修类型
    @ViewInject(id = R.id.layout_mainennanceType, click = "mainennanceType")
    LinearLayout layout_mainennanceType;
    @ViewInject(id = R.id.tv_MainennanceType)
    TextView tv_MainennanceType;

    //车辆状态
    @ViewInject(id = R.id.layout_state, click = "stateType")
    LinearLayout layout_state;
    @ViewInject(id = R.id.tv_MainennanceState)
    TextView tv_MainennanceState;

    //维修时间
    @ViewInject(id = R.id.layout_timeStart, click = "forStartTime")
    LinearLayout layout_timeStart;
    @ViewInject(id = R.id.tv_vehicleMainennanceTimeStart)
    TextView tv_vehicleMainennanceTimeStart;

    //车牌号
    @ViewInject(id = R.id.et_vihicleNumber)
    EditText et_vihicleNumber;

    //维修项目
    @ViewInject(id = R.id.et_MaintenanceProject)
    EditText et_MaintenanceProject;

    //维修地点
    @ViewInject(id = R.id.et_maintenancePlace)
    EditText et_maintenancePlace;

    //费用
    @ViewInject(id = R.id.et_EstimateFee)
    EditText et_EstimateFee;

    //申请备注
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
    private String approvalID = "";
    private String maintenanceType = "";
    private String maintenanceTime = "";
    private String vehicleNumber = "";
    private String maintenanceProject = "";
    private String maintenancePlace = "";
    private String estimateFee = "";
    private String remark = "";
    private String maintenanceState = "";

    private void clear(){
        tv_MainennanceType.setText("");
        tv_MainennanceState.setText("");
        tv_vehicleMainennanceTimeStart.setText("");
        et_vihicleNumber.setText("");
        et_MaintenanceProject.setText("");
        et_maintenancePlace.setText("");
        et_EstimateFee.setText("");
        et_remark.setText("");
        tv_Requester.setText("");
        approvalID = null;
        maintenanceType = null;
        maintenanceTime = null;
        maintenanceState = null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_vehiclemainennance);
        tv_title.setText(getResources().getString(R.string.vehicleMainennance));

    }

    public void forCommit(View view) {
        vehicleNumber = et_vihicleNumber.getText().toString().trim();
        maintenanceProject = et_MaintenanceProject.getText().toString();
        estimateFee = et_EstimateFee.getText().toString();
        maintenancePlace = et_maintenancePlace.getText().toString();
        remark = et_remark.getText().toString();

        if (TextUtils.isEmpty(maintenanceType)) {
            PageUtil.DisplayToast("维修类型不能为空");
            return;
        }

        if (TextUtils.isEmpty(maintenanceState)) {
            PageUtil.DisplayToast("维修状态不能为空");
            return;
        }

        if (TextUtils.isEmpty(maintenanceTime)) {
            PageUtil.DisplayToast("时间不能为空");
            return;
        }

        if (TextUtils.isEmpty(vehicleNumber)) {
            PageUtil.DisplayToast("车牌号不能为空");
            return;
        }

        if (TextUtils.isEmpty(maintenanceProject)) {
            PageUtil.DisplayToast("维保项目不能为空");
            return;
        }


        if (TextUtils.isEmpty(maintenancePlace)) {
            PageUtil.DisplayToast("维修地点不能为空");
            return;
        }

        if (TextUtils.isEmpty(estimateFee)) {
            PageUtil.DisplayToast("维修费用不能为空");
            return;
        }
        if (TextUtils.isEmpty(approvalID)) {
            PageUtil.DisplayToast("审批人不能为空");
            return;
        }
        Loading.run(VehicleMaintainActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("MaintenanceType", maintenanceType);
                    js.put("EstimateFee", estimateFee);
                    js.put("PlanBorrowTime", maintenanceTime);
                    js.put("MaintenanceProject", maintenanceProject);
                    js.put("Number", vehicleNumber);
                    js.put("VehicleState", maintenanceState);
                    js.put("Destination", maintenancePlace);
                    js.put("Remark", remark);
                    js.put("ApprovalIDList", approvalID);//

                    UserHelper.maintenancePost(VehicleMaintainActivity.this, js);

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

    /**
     * 维修类型
     */
    public void mainennanceType(View view) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.vehicleMainennanceType));
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        final String[] data = getResources().getStringArray(R.array.spMaintenanceType);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                maintenanceType = data[which];
                tv_MainennanceType.setText(maintenanceType.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }

    /**
     * 维修状态
     */
    public void stateType(View view) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setTitle(getResources().getString(R.string.vehicleMainennanceStatus));
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        final String[] data = getResources().getStringArray(R.array.spMaintenanceState);
        buidler.setSingleChoiceItems(data, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                maintenanceState = data[which];
                tv_MainennanceState.setText(maintenanceState.trim());
                dialog.dismiss();
            }
        });
        buidler.show();
    }

    /**
     * 维修时间
     *
     * @param view
     */
    public void forStartTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(VehicleMaintainActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        maintenanceTime = time;
                        tv_vehicleMainennanceTimeStart.setText(time);
                    }
                });
//        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("维保时间");
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
     * back
     *
     * @param view
     */

    public void forBack(View view) {
        this.finish();
    }
}
