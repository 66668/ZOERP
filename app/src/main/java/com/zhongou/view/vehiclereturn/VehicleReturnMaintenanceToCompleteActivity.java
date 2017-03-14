package com.zhongou.view.vehiclereturn;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.DateChooseWheelViewDialog;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.VehicleReturnModel;
import com.zhongou.model.VehicleReturnPostMaintenanceModel;
import com.zhongou.utils.PageUtil;

/**
 * 维保-交车提交界面
 * Created by sjy on 2017/2/14.
 */

public class VehicleReturnMaintenanceToCompleteActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "forVehicleReturn")
    TextView tv_right;


    //实际用车时间
    @ViewInject(id = R.id.layout_timeStart, click = "getStartTime")
    LinearLayout layout_timeStart;
    @ViewInject(id = R.id.tv_TimeStart)
    TextView tv_TimeStart;

    //实际交车时间
    @ViewInject(id = R.id.layout_endTime, click = "getEndTime")
    LinearLayout layout_endTime;
    @ViewInject(id = R.id.tv_endTime)
    TextView tv_endTime;

    //驾驶员
    @ViewInject(id = R.id.tv_driver)
    EditText tv_driver;

    //乘车人
    @ViewInject(id = R.id.tv_passenger)
    EditText tv_passenger;

    //开始里程
    @ViewInject(id = R.id.tv_startMiles)
    EditText tv_startMiles;

    //交车里程
    @ViewInject(id = R.id.tv_endMiles)
    EditText tv_endMiles;

    //备注
    @ViewInject(id = R.id.tv_remark)
    EditText tv_remark;


    //变量
    private VehicleReturnModel vehicleReturnModel;
    private String ActualBorrowTime;//实际用车时间
    private String ActualReturnTime;//实际交车时间
    private String Passenger;
    private String Driver;
    private String StartMileage;
    private String FinishMileage;
    private String Remark;

    //常量
    private static final int POST_SUCCESS = 11;
    private static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vehicle_return_maintenanace_save);
        tv_title.setText(getResources().getString(R.string.vehicleRe));
        tv_right.setText(getResources().getString(R.string.vehicleRe_save));
        //获取像显式跳转值
        Bundle bundle = this.getIntent().getExtras();
        vehicleReturnModel = (VehicleReturnModel) bundle.getSerializable("VehicleReturnModel");

        //多界面管理
        MyApplication.getInstance().addACT(this);

    }

    /**
     * 保存接口
     *
     * @param view
     */
    public void forVehicleReturn(View view) {
        Passenger = tv_passenger.getText().toString().trim();
        Driver = tv_driver.getText().toString().trim();
        StartMileage = tv_startMiles.getText().toString().trim();
        FinishMileage = tv_endMiles.getText().toString().trim();
        Remark = tv_remark.getText().toString().trim();

        //非空
        if (TextUtils.isEmpty(ActualBorrowTime) && TextUtils.isEmpty(ActualReturnTime)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_time_null));
            return;
        }
        if (TextUtils.isEmpty(Driver)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_driver_null));
            return;
        }
        if (TextUtils.isEmpty(Passenger)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_Passenger_null));
            return;
        }
        if (TextUtils.isEmpty(StartMileage) && TextUtils.isEmpty(FinishMileage)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_Miles_null));
            return;
        }

        //对象存值
        final VehicleReturnPostMaintenanceModel model = new VehicleReturnPostMaintenanceModel();

        model.setApplicationType(vehicleReturnModel.getApplicationType());
        model.setApplicationID(vehicleReturnModel.getApplicationID());
        model.setEmployeeID(UserHelper.getCurrentUser().getEmployeeID());

        model.setActualBorrowTime(ActualBorrowTime);
        model.setActualReturnTime(ActualReturnTime);
        model.setDriver(Driver);

        model.setStartMileage(StartMileage);
        model.setFinishMileage(FinishMileage);
        model.setPassenger(Passenger);
        model.setBackRemark(Remark);


        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    String str = UserHelper.postVehicleReturnMaintenance(VehicleReturnMaintenanceToCompleteActivity.this, model);
                    sendMessage(POST_SUCCESS, str);
                } catch (MyException e) {
                    sendMessage(POST_SUCCESS, e.getMessage());
                }
            }
        });


    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                PageUtil.DisplayToast((String) msg.obj);
                //消除多界面
                MyApplication.getInstance().closeACT();

                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }

    /**
     * 开始时间
     *
     * @param view
     */
    public void getStartTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(VehicleReturnMaintenanceToCompleteActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        ActualBorrowTime = time;
                        tv_TimeStart.setText(time);
                    }
                });
        //        endDateChooseDialog.setTimePickerGone(true);//分钟
        endDateChooseDialog.setDateDialogTitle("实际维保时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 结束时间
     *
     * @param view
     */
    public void getEndTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(VehicleReturnMaintenanceToCompleteActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        ActualReturnTime = time;
                        tv_endTime.setText(time);
                    }
                });
        //        endDateChooseDialog.setTimePickerGone(true);//分钟
        endDateChooseDialog.setDateDialogTitle("实际交车时间");
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
