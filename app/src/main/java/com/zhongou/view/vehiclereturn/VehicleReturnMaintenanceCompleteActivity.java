package com.zhongou.view.vehiclereturn;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.VehicleReturnModel;
import com.zhongou.model.copydetailmodel.VehicleMaintainCopyModel;
import com.zhongou.utils.PageUtil;

/**
 * 维保-已交车详细
 * Created by sjy on 2017/2/14.
 */

public class VehicleReturnMaintenanceCompleteActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //交车
    @ViewInject(id = R.id.btn_returnVehicle, click = "forVehicleReturn")
    Button btn_returnVehicle;


    //抄送人
    @ViewInject(id = R.id.tv_copyPerson)
    TextView tv_copyPerson;

    //抄送时间
    @ViewInject(id = R.id.tv_copyTime)
    TextView tv_copyTime;

    //维保类型
    @ViewInject(id = R.id.tv_vehicleMainennanceType)
    TextView tv_vehicleMainennanceType;

    //维保地点
    @ViewInject(id = R.id.tv_vehicleMainennanceAddress)
    TextView tv_vehicleMainennanceAddress;

    //维保项目
    @ViewInject(id = R.id.tv_vehicleMainennanceContainer)
    TextView tv_vehicleMainennanceContainer;

    //维保时间
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //车牌号
    @ViewInject(id = R.id.tv_carNumber)
    TextView tv_carNumber;

    //驾驶人
    @ViewInject(id = R.id.tv_driver)
    TextView tv_driver;

    //预计费用
    @ViewInject(id = R.id.tv_EstimateFee)
    TextView tv_EstimateFee;

    //实际用车时间
    @ViewInject(id = R.id.tv_ActStartTime)
    TextView tv_ActStartTime;

    //实际交车时间
    @ViewInject(id = R.id.tv_actEndTime)
    TextView tv_actEndTime;

    //乘车人
    @ViewInject(id = R.id.tv_passenger)
    TextView tv_passenger;

    //开始里程
    @ViewInject(id = R.id.tv_startMiles)
    TextView tv_startMiles;

    //交车里程
    @ViewInject(id = R.id.tv_endMiles)
    TextView tv_endMiless;

    //交车备注
    @ViewInject(id = R.id.tv_backRemark)
    TextView tv_backRemark;

    //    //申请备注
    //    @ViewInject(id = R.id.tv_other)
    //    TextView tv_other;


    //变量
    private VehicleReturnModel model;
    //常量
    private static final int POST_SUCCESS = 11;
    private static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vehicle_return_maintenanace_done);
        tv_title.setText(getResources().getString(R.string.vehicleRe_complete));
        tv_right.setText("");

        //获取交车记录界面跳转值
        Bundle bundle = this.getIntent().getExtras();
        model = (VehicleReturnModel) bundle.getSerializable("VehicleReturnModel");

        getData(model);
    }

    private void setShow(VehicleMaintainCopyModel model) {

        tv_copyPerson.setText(model.getEmployeeName());
        tv_copyTime.setText(model.getCopyTime());
        tv_vehicleMainennanceType.setText(model.getMaintenanceType());
        tv_vehicleMainennanceAddress.setText(model.getDestination());
        tv_vehicleMainennanceContainer.setText(model.getMaintenanceProject());

        tv_startTime.setText(model.getPlanBorrowTime());
        tv_carNumber.setText(model.getNumber());
        tv_driver.setText(model.getDriver());
        tv_EstimateFee.setText(model.getEstimateFee());
        tv_ActStartTime.setText(model.getActualBorrowTime());

        tv_actEndTime.setText(model.getActualReturnTime());
        tv_passenger.setText(model.getPassenger());
        tv_startMiles.setText(model.getStartMileage());
        tv_endMiless.setText(model.getFinishMileage());
        tv_backRemark.setText(model.getBackRemark());


    }

    private void getData(final VehicleReturnModel model) {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    VehicleMaintainCopyModel vehicleCopylModel = UserHelper.getVehicleReturnMaintenanceDetail(VehicleReturnMaintenanceCompleteActivity.this, model.getApplicationID(), model.getApplicationType());
                    sendMessage(POST_SUCCESS, vehicleCopylModel);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });

    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                VehicleMaintainCopyModel vehicleCopylModel = (VehicleMaintainCopyModel) msg.obj;
                setShow(vehicleCopylModel);
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
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
