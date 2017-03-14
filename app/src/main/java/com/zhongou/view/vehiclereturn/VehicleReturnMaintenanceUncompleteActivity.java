package com.zhongou.view.vehiclereturn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.VehicleReturnModel;
import com.zhongou.model.copydetailmodel.VehicleMaintainCopyModel;
import com.zhongou.utils.PageUtil;

/**
 * 用车-未维保界面
 * Created by sjy on 2017/2/14.
 */

public class VehicleReturnMaintenanceUncompleteActivity extends BaseActivity {
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
    @ViewInject(id = R.id.tv_type)
    TextView tv_type;

    //维保地点
    @ViewInject(id = R.id.tv_address)
    TextView tv_address;

    //维保项目
    @ViewInject(id = R.id.tv_cotainer)
    TextView tv_cotainer;

    //维保时间
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //车牌号
    @ViewInject(id = R.id.tv_carNumber)
    TextView tv_carNumber;
    //费用
    @ViewInject(id = R.id.tv_EstimateFee)
    TextView tv_EstimateFee;

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
        setContentView(R.layout.act_vehicle_return_maintenanace);
        tv_title.setText(getResources().getString(R.string.vehicleRe));
        tv_right.setText("");

        //获取交车记录界面跳转值
        Bundle bundle = this.getIntent().getExtras();
        model = (VehicleReturnModel) bundle.getSerializable("VehicleReturnModel");

        getData(model);

        //多界面管理
        MyApplication.getInstance().addACT(this);

    }

    private void setShow(VehicleMaintainCopyModel model) {

        tv_copyPerson.setText(model.getEmployeeName());
        tv_copyTime.setText(model.getCopyTime());
        tv_type.setText(model.getMaintenanceType());

        tv_address.setText(model.getDestination());
        tv_cotainer.setText(model.getMaintenanceProject());
        tv_startTime.setText(model.getPlanBorrowTime());

        tv_carNumber.setText(model.getNumber());
        tv_EstimateFee.setText(model.getEstimateFee());
        tv_other.setText(model.getRemark());
    }

    private void getData(final VehicleReturnModel model) {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                //泛型
                try {
                    VehicleMaintainCopyModel vehicleCopylModel = new UserHelper<>(VehicleMaintainCopyModel.class)
                            .getVehicleReturnDetail(VehicleReturnMaintenanceUncompleteActivity.this
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
                VehicleMaintainCopyModel vehicleCopylModel = (VehicleMaintainCopyModel) msg.obj;
                setShow(vehicleCopylModel);
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
        }
    }

    public void forVehicleReturn(View view) {

        Intent intent = new Intent();
        intent.setClass(this, VehicleReturnMaintenanceToCompleteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("VehicleReturnModel", model);
        intent.putExtras(bundle);
        startActivity(intent);

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
