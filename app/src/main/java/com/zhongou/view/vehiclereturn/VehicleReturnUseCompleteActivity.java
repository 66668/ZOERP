package com.zhongou.view.vehiclereturn;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.VehicleReturnModel;
import com.zhongou.model.copydetailmodel.VehicleCopylModel;
import com.zhongou.utils.PageUtil;

/**
 * 用车-已交车详细
 * Created by sjy on 2017/2/14.
 */

public class VehicleReturnUseCompleteActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //抄送人
    @ViewInject(id = R.id.tv_copyPerson)
    TextView tv_copyPerson;

    //抄送时间
    @ViewInject(id = R.id.tv_copyTime)
    TextView tv_copyTime;

    //目的地
    @ViewInject(id = R.id.tv_Destination)
    TextView tv_Destination;

    //用途
    @ViewInject(id = R.id.tv_useage)
    TextView tv_CarPurpose;

    //计划用车时间
    @ViewInject(id = R.id.tv_PlanBorrowTime)
    TextView tv_PlanBorrowTime;

    //计划交车时间
    @ViewInject(id = R.id.tv_PlanReturnTime)
    TextView tv_PlanReturnTime;

    //实际用车时间
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //实际交车时间
    @ViewInject(id = R.id.tv_endTime)
    TextView tv_endTime;

    //驾驶人
    @ViewInject(id = R.id.tv_driver)
    TextView tv_driver;

    //乘车人
    @ViewInject(id = R.id.tv_passenger)
    TextView tv_passenger;

    //开始里程
    @ViewInject(id = R.id.tv_startMiles)
    TextView tv_startMiles;

    //交车里程
    @ViewInject(id = R.id.tv_endMiles)
    TextView tv_endMiles;

    //交车备注
    @ViewInject(id = R.id.tv_postother)
    TextView tv_postother;

    //申请备注
    @ViewInject(id = R.id.tv_other)
    TextView tv_other;

    //变量
    private VehicleReturnModel model;
    //常量
    private static final int POST_SUCCESS = 11;
    private static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vehicle_return_use_done);
        tv_title.setText(getResources().getString(R.string.vehicleRe_complete));
        tv_right.setText("");

        //获取交车记录界面跳转值
        Bundle bundle = this.getIntent().getExtras();
        model = (VehicleReturnModel) bundle.getSerializable("VehicleReturnModel");

        getData(model);
    }

    private void setShow(VehicleCopylModel model) {

        tv_copyPerson.setText(model.getEmployeeName());
        tv_copyTime.setText(model.getCopyTime());
        tv_Destination.setText(model.getDestination());
        tv_CarPurpose.setText(model.getPurpose());

        tv_PlanBorrowTime.setText(model.getPlanBorrowTime());
        tv_PlanReturnTime.setText(model.getPlanReturnTime());
        tv_startTime.setText(model.getActualBorrowTime());
        tv_endTime.setText(model.getActualReturnTime());

        tv_driver.setText(model.getDriver());
        tv_passenger.setText(model.getPassenger());
        tv_startMiles.setText(model.getStartMileage());
        tv_endMiles.setText(model.getFinishMileage());

        tv_postother.setText(model.getBackRemark());
        tv_other.setText(model.getRemark());

    }

    private void getData(final VehicleReturnModel model) {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                //泛型
                try {
                    VehicleCopylModel vehicleCopylModel = new UserHelper<>(VehicleCopylModel.class)
                            .getVehicleReturnDetail(VehicleReturnUseCompleteActivity.this
                                    , model.getApplicationID()
                                    , model.getApplicationType());
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
                VehicleCopylModel vehicleCopylModel = (VehicleCopylModel) msg.obj;
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
