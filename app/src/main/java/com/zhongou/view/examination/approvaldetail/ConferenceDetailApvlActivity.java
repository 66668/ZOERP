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
import com.zhongou.model.approvaldetailmodel.ConferenceApvlModel;
import com.zhongou.utils.PageUtil;

/**
 * 申请 会议详情
 * Created by sjy on 2017/1/16.
 */

public class ConferenceDetailApvlActivity extends BaseActivity {
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
    @ViewInject(id = R.id.btn_refulse, click = "forRefulse")
    Button btn_refulse;

    //批准
    @ViewInject(id = R.id.btn_commit, click = "toForCommit")
    Button btn_commit;

    //转交
    @ViewInject(id = R.id.btn_transfer, click = "forTransfer")
    Button btn_transfer;

    //审批bottom
    @ViewInject(id = R.id.laytout_copy)
    LinearLayout laytout_copy;

    //抄送
    @ViewInject(id = R.id.btn_copytp, click = "forCopyto")
    Button btn_copytp;

    //会议标题
    @ViewInject(id = R.id.tv_conference_name)
    TextView tv_conference_name;

    //会议主题
    @ViewInject(id = R.id.tv_conference_title)
    TextView tv_conference_title;


    //准备
    @ViewInject(id = R.id.tv_conference_Device, click = "DeviceExpended")
    TextView tv_conference_Device;

    //简介
    @ViewInject(id = R.id.tv_conference_Abstract, click = "AbstractExpended")
    TextView tv_conference_Abstract;

    //备注
    @ViewInject(id = R.id.tv_remark, click = "RemarkExpended")
    TextView tv_remark;


    //开始
    @ViewInject(id = R.id.tv_conference_start)
    TextView tv_conference_start;

    //结束
    @ViewInject(id = R.id.tv_conference_end)
    TextView tv_conference_end;


    //
    private MyApprovalModel myApprovalModel;
    private ConferenceApvlModel model;

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_conference_d2);
        tv_title.setText(getResources().getString(R.string.conference));
        tv_right.setText("");

        Bundle bundle = this.getIntent().getExtras();
        myApprovalModel = (MyApprovalModel) bundle.getSerializable("MyApprovalModel");
        Log.d("SJY", "详情MyApprovalModel");

        bottomType();
        //
        getDetailData();
    }

    private void setShow(ConferenceApvlModel model) {
        //
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getApplicationCreateTime());

        //
        tv_conference_name.setText(model.getConferenceName());
        tv_conference_title.setText(model.getTitle());
        tv_conference_Device.setText(model.getDeviceName());
        tv_conference_Abstract.setText(model.getAbstract());
        tv_conference_start.setText(model.getStartTime());
        tv_conference_end.setText(model.getFinishTime());
        tv_remark.setText(model.getRemark());

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
                    ConferenceApvlModel model = UserHelper.approvalDetailPostConference(ConferenceDetailApvlActivity.this,
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
                model = (ConferenceApvlModel) msg.obj;
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
    public void forRefulse(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonDisagreeActivity.class, bundle);
    }

    //同意
    public void toForCommit(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonAgreeActivity.class, bundle);
    }

    //转交
    public void forTransfer(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonTransfertoActivity.class, bundle);
    }

    // 抄送
    public void forCopyto(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonCopytoCoActivity.class, bundle);
    }

    /**
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }
    private boolean isDeviceExpend = false;

    public void DeviceExpended(View view) {
        if (!isDeviceExpend) {
            tv_conference_Device.setMinLines(0);
            tv_conference_Device.setMaxLines(Integer.MAX_VALUE);
            isDeviceExpend = true;
        } else {
            tv_conference_Device.setLines(3);
            isDeviceExpend = false;
        }

    }

    private boolean isAbstractExpended = false;

    public void AbstractExpended(View view) {
        if (!isAbstractExpended) {
            tv_conference_Abstract.setMinLines(0);
            tv_conference_Abstract.setMaxLines(Integer.MAX_VALUE);
            isAbstractExpended = true;
        } else {
            tv_conference_Abstract.setLines(3);
            isAbstractExpended = false;
        }

    }

    private boolean isRemarkExpend = false;

    public void RemarkExpended(View view) {
        if (!isRemarkExpend) {
            tv_remark.setMinLines(0);
            tv_remark.setMaxLines(Integer.MAX_VALUE);
            isRemarkExpend = true;
        } else {
            tv_remark.setLines(3);
            isRemarkExpend = false;
        }

    }
}
