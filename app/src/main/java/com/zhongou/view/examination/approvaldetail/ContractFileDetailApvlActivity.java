package com.zhongou.view.examination.approvaldetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.zhongou.model.approvaldetailmodel.ContractFileApvlModel;
import com.zhongou.utils.PageUtil;

/**
 * 申请 合同文件详情
 * Created by sjy on 2017/1/16.
 */

public class ContractFileDetailApvlActivity extends BaseActivity {
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


    //借阅名称
    @ViewInject(id = R.id.tv_borrowThings)
    TextView tv_borrowThings;

    //借阅类型
    @ViewInject(id = R.id.tv_borrowType)
    TextView tv_borrowType;

    //使用时间
    @ViewInject(id = R.id.tv_StartTime)
    TextView tv_StartTime;

    //归还时间
    @ViewInject(id = R.id.tv_endTime)
    TextView tv_endTime;

    //借阅说明
    @ViewInject(id = R.id.tv_reason)
    TextView tv_reason;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;


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

    private MyApprovalModel myApprovalModel;
    private ContractFileApvlModel model;
    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_contractfile_d2);
        tv_title.setText(getResources().getString(R.string.contractfile));
        tv_right.setText("");

        Intent intent = getIntent();
        myApprovalModel = (MyApprovalModel) intent.getSerializableExtra("MyApprovalModel");

        bottomType();
        //
        getDetailData();
    }
    private void setShow(ContractFileApvlModel model) {
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getApplicationCreateTime());

        //        tv_borrowThings.setText(model.getBorrowThings());
        //        tv_borrowType.setText(model.getBorrowType());
        //        tv_StartTime.setText(model.getStartTime());
        //        tv_endTime.setText(model.getFinishTime());
        //        tv_reason.setText(model.getReason());
        //        if (model.getApprovalInfoLists().size() > 0) {
        //            tv_Requester.setText(model.getApplicationCreateTime());
        //        } else {
        //            tv_Requester.setText("未审批");
        //        }
    }
    private void bottomType() {
        //
        if(myApprovalModel.getApprovalStatus().contains("1")){

            laytout_decide.setVisibility(View.GONE);
            laytout_copy.setVisibility(View.VISIBLE);

        }else {
            laytout_decide.setVisibility(View.VISIBLE);
            laytout_copy.setVisibility(View.GONE);
        }
    }

    private void getDetailData() {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {

                try {
                    ContractFileApvlModel model = UserHelper.approvalDetailPostContractFile(ContractFileDetailApvlActivity.this,
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
                model = (ContractFileApvlModel) msg.obj;
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
        startActivity(CommonRefulseActivity.class);
    }
    //批转
    public void toForCommit(View view){
        startActivity(CommonApproveActivity.class);
    }
    //转交
    public void forTransfer(View view){
        startActivity(CommonTransfertoActivity.class);
    }
    // 抄送
    public void forCopyto(View view){
        startActivity(CommonCopytoActivity.class);
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