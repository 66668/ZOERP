package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.approvaldetailmodel.VehicleMaintainApvlModel;
import com.zhongou.utils.PageUtil;

/**
 * Created by sjy on 2016/12/2.
 */

public class VehicleMaintainDetailApvlActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;
    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;
    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;


    //申请人
    @ViewInject(id = R.id.tv_ApprovalPerson)
    TextView tv_ApprovalPerson;

    //部门
    @ViewInject(id = R.id.tv_approvaldept)
    TextView tv_approvaldept;

    //公司
    @ViewInject(id = R.id.tv_approvalCo)
    TextView tv_approvalCo;

    //申请时间
    @ViewInject(id = R.id.tv_approvalTime)
    TextView tv_approvalTime;


    //未审批bottom
    @ViewInject(id = R.id.laytout_decide)
    LinearLayout laytout_decide;

    //驳回
    @ViewInject(id = R.id.btn_refulse,click = "forRefulse")
    Button btn_refulse;

    //批准
    @ViewInject(id = R.id.btn_commit,click = "toForCommit")
    Button btn_commit;

    //转交
    @ViewInject(id = R.id.btn_transfer,click = "forTransfer")
    Button btn_transfer;

    //审批bottom
    @ViewInject(id = R.id.laytout_copy)
    LinearLayout laytout_copy;

    //抄送
    @ViewInject(id = R.id.btn_copytp,click = "forCopyto")
    Button btn_copytp;


    //维修类型
    @ViewInject(id = R.id.maintenanceType)
    TextView maintenanceType;

    //车辆状态
    @ViewInject(id = R.id.tv_stateType)
    TextView tv_stateType;

    //维修时间
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //车牌号
    @ViewInject(id = R.id.vehicleNumber)
    TextView vehicleNumber;

    //维修项目
    @ViewInject(id = R.id.tv_project)
    TextView tv_project;

    //行驶公里
    @ViewInject(id = R.id.tv_runMiles)
    TextView tv_runMiles;

    //维修地点
    @ViewInject(id = R.id.tv_address)
    TextView tv_address;

    //费用
    @ViewInject(id = R.id.tv_fee)
    TextView tv_fee;

    //说明
    @ViewInject(id = R.id.tv_reason)
    TextView tv_reason;


    //变量
    private MyApprovalModel myApprovalModel;
    private VehicleMaintainApvlModel model;
    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_vehiclemainennance_d2);
        tv_title.setText(getResources().getString(R.string.vehicleMainennance_d));
        tv_right.setText("");

        Bundle bundle = this.getIntent().getExtras();
        myApprovalModel = (MyApprovalModel) bundle.getSerializable("MyApprovalModel");
        Log.d("SJY", "详情MyApprovalModel");

        bottomType();
        //
        getDetailData();
    }

    private void setShow(VehicleMaintainApvlModel model) {
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getApplicationCreateTime());

        maintenanceType.setText(model.getMaintenanceType());
        tv_stateType.setText(model.getVehicleState());
        tv_startTime.setText(model.getPlanBorrowTime());
        tv_project.setText(model.getMaintenanceProject());
        tv_address.setText(model.getDestination());
        tv_reason.setText(model.getPurpose());

    }

    private void bottomType() {
        //
        if (myApprovalModel.getApprovalStatus().contains("1")) {

            laytout_decide.setVisibility(View.GONE);
            laytout_copy.setVisibility(View.VISIBLE);

        } else {
            laytout_decide.setVisibility(View.VISIBLE);
            laytout_copy.setVisibility(View.GONE);
        }
    }

    private void getDetailData() {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    VehicleMaintainApvlModel model =
                            UserHelper.approvalDetailPostVehicleMaintain(VehicleMaintainDetailApvlActivity.this,
                            myApprovalModel.getApplicationID(),
                            myApprovalModel.getApplicationType());
                    sendMessage(POST_SUCCESS, model);
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
                model = (VehicleMaintainApvlModel) msg.obj;
                setShow(model);
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    //驳回
    public void forRefulse(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonDisagreeActivity.class,bundle);
    }
    //同意
    public void toForCommit(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonAgreeActivity.class,bundle);
    }
    //转交
    public void forTransfer(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonTransfertoActivity.class,bundle);
    }
    // 抄送
    public void forCopyto(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonCopytoCoActivity.class,bundle);
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
