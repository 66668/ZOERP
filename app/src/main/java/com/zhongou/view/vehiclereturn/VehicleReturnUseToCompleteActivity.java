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
import com.zhongou.model.VehicleReturnPostUseModel;
import com.zhongou.utils.PageUtil;


/**
 * 用车-交车提交界面
 * Created by sjy on 2017/2/14.
 */

public class VehicleReturnUseToCompleteActivity extends BaseActivity {
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
    @ViewInject(id = R.id.tv_vehicleMainennanceTimeStart)
    TextView tv_vehicleMainennanceTimeStart;


    //实际交车时间
    @ViewInject(id = R.id.layout_endTime, click = "getEndTime")
    LinearLayout layout_endTime;
    @ViewInject(id = R.id.tv_vehicleRe_endTime)
    TextView tv_vehicleRe_endTime;


    //驾驶员
    @ViewInject(id = R.id.et_driver)
    EditText et_driver;


    //乘车人
    @ViewInject(id = R.id.et_passager)
    EditText et_passager;


    //车牌号
    @ViewInject(id = R.id.et_CarNumber)
    EditText et_CarNumber;


    //开始里程数
    @ViewInject(id = R.id.et_startMiles)
    EditText et_startMiles;


    //结束里程数
    @ViewInject(id = R.id.et_endMiles)
    EditText et_endMiles;


    //备注
    @ViewInject(id = R.id.et_other)
    EditText et_other;

    //变量
    private VehicleReturnModel vehicleReturnModel;
    private String ActualBorrowTime;//实际用车时间
    private String ActualReturnTime;//实际交车时间
    private String driver;
    private String passager;
    private String CarNumber;
    private String startMiles;
    private String endMiles;
    private String other;

    //常量
    private static final int POST_SUCCESS = 11;
    private static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vehicle_return_usesave);
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
        //
        driver = et_driver.getText().toString().trim();
        passager = et_passager.getText().toString().trim();
        CarNumber = et_CarNumber.getText().toString().trim();
        startMiles = et_startMiles.getText().toString().trim();
        endMiles = et_endMiles.getText().toString().trim();
        other = et_other.getText().toString().trim();

        //非空
        if (TextUtils.isEmpty(ActualBorrowTime) && TextUtils.isEmpty(ActualReturnTime)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_time_null));
            return;
        }
        if (TextUtils.isEmpty(driver)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_driver_null));
            return;
        }

        if (TextUtils.isEmpty(passager)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_Passenger_null));
            return;
        }

        if (TextUtils.isEmpty(CarNumber)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_Number_null));
            return;
        }

        if (TextUtils.isEmpty(startMiles) && TextUtils.isEmpty(startMiles)) {
            PageUtil.DisplayToast(this.getResources().getString(R.string.vehicleRe_Miles_null));
            return;
        }

        //对象存值
        final VehicleReturnPostUseModel model = new VehicleReturnPostUseModel();

        model.setActualBorrowTime(ActualBorrowTime);
        model.setActualReturnTime(ActualReturnTime);
        model.setDriver(driver);
        model.setPassenger(passager);

        model.setNumber(CarNumber);
        model.setStartMileage(startMiles);
        model.setFinishMileage(endMiles);
        model.setBackRemark(other);

        model.setApplicationType(vehicleReturnModel.getApplicationType());
        model.setApplicationType(vehicleReturnModel.getApplicationType());

        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    String str = UserHelper.postVehicleReturnUse(VehicleReturnUseToCompleteActivity.this, model);
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
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(VehicleReturnUseToCompleteActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        ActualBorrowTime = time;
                        tv_vehicleMainennanceTimeStart.setText(time);
                    }
                });
        //        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("实际用车时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    /**
     * 结束时间
     *
     * @param view
     */
    public void getEndTime(View view) {
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(VehicleReturnUseToCompleteActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        ActualReturnTime = time;
                        tv_vehicleRe_endTime.setText(time);
                    }
                });
        //        endDateChooseDialog.setTimePickerGone(true);
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
