package com.zhongou.view.examination;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.zhongou.utils.PageUtil;

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

    //行驶公里
    @ViewInject(id = R.id.et_TravelKilmetre)
    EditText et_TravelKilmetre;

    //维修地点
    @ViewInject(id = R.id.et_maintenancePlace)
    EditText et_maintenancePlace;

    //费用
    @ViewInject(id = R.id.et_EstimateFee)
    EditText et_EstimateFee;

    //说明
    @ViewInject(id = R.id.et_reason)
    EditText et_reason;

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    //变量
    private String approvalID = "";
    private String maintenanceType = "";
    private String maintenanceTime = "";
    private String vehicleNumber = "";
    private String maintenanceProject = "";
    private String travelKilmetre = "";
    private String maintenancePlace = "";
    private String estimateFee = "";
    private String reason = "";
    private String maintenanceState = "";
    private List<String> approvalIDList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_vehiclemainennance);
        tv_title.setText(getResources().getString(R.string.vehicleMainennance));

    }

    public void forCommit(View view) {
        approvalID = "0280c9c5-870c-46cf-aa95-cdededc7d86c,88dd7959-cb2f-40c6-947a-4d6801fc4765";
        vehicleNumber = et_vihicleNumber.getText().toString().trim();
        maintenanceProject = et_MaintenanceProject.getText().toString();
        estimateFee = et_EstimateFee.getText().toString();
        travelKilmetre = et_TravelKilmetre.getText().toString();
        maintenancePlace = et_maintenancePlace.getText().toString();
        reason = et_reason.getText().toString();

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

        if (TextUtils.isEmpty(travelKilmetre)) {
            PageUtil.DisplayToast("行驶公里不能为空");
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

        Loading.run(VehicleMaintainActivity.this, new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    js.put("MaintenanceType", maintenanceType);
                    js.put("EstimateFee", estimateFee);
                    js.put("MaintenanceTime", maintenanceTime);
                    js.put("MaintenanceProject", maintenanceProject);
                    js.put("Number", vehicleNumber);
                    js.put("TravelKilmetre", travelKilmetre);
                    js.put("VehicleState", maintenanceState);
                    js.put("MaintenancePlace", maintenancePlace);
                    js.put("Purpose", reason);
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
                PageUtil.DisplayToast("成功提交！");
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
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("维修时间");
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
