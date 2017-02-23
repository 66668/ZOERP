package com.zhongou.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;
import com.zhongou.view.examination.BorrowActivity;
import com.zhongou.view.examination.DimissionActivity;
import com.zhongou.view.examination.FinancialMenuActivity;
import com.zhongou.view.examination.LeaveActivity;
import com.zhongou.view.examination.RecruitmentActivity;
import com.zhongou.view.examination.SalaryadjustActivity;
import com.zhongou.view.examination.TakeDaysOffActivity;
import com.zhongou.view.examination.VehicleActivity;
import com.zhongou.view.examination.VehicleMaintainActivity;
import com.zhongou.view.examination.WorkOverTimeActivity;
import com.zhongou.view.examination.ZOAplicationListActivity;
import com.zhongou.view.examination.ZOApprovelListActivity;
import com.zhongou.view.examination.ZOCopyListActivity;

/**
 * 审批
 * Created by JackSong on 2016/10/25.
 */

public class ExaminationAcitivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //我的申请
    @ViewInject(id = R.id.btn_MyApplication, click = "myApplication")
    LinearLayout btn_MyApplication;

    //我的审批
    @ViewInject(id = R.id.btn_MyApproval, click = "myApproval")
    LinearLayout btn_MyApproval;

    //我的抄送
    @ViewInject(id = R.id.btn_copyReturn, click = "myCopyReturn")
    LinearLayout btn_copyReturn;

    //1招聘
    @ViewInject(id = R.id.btn_jobs, click = "forJobs")
    LinearLayout btn_jobs;

    //2离职
    @ViewInject(id = R.id.btn_jobsLeave, click = "forJobsLeave")
    LinearLayout btn_jobsLeave;

    //3请假
    @ViewInject(id = R.id.btn_forLeave, click = "forLeave")
    LinearLayout btn_forLeave;

    //4加班
    @ViewInject(id = R.id.btn_overTime, click = "forWorkOverTime")
    LinearLayout btn_overTime;

    //5调休
    @ViewInject(id = R.id.btn_rest, click = "forWorkRest")
    LinearLayout btn_rest;

    //6借阅
    @ViewInject(id = R.id.btn_borrow, click = "forBorrow")
    LinearLayout btn_borrow;

    //7薪资调整
    @ViewInject(id = R.id.btn_Salaryadjust, click = "Salaryadjust")
    LinearLayout btn_Salaryadjust;

    //8用车
    @ViewInject(id = R.id.btn_carused, click = "forCarused")
    LinearLayout btn_carused;

    //9车辆维护
    @ViewInject(id = R.id.btn_vehicleMaintenance, click = "forVehicleMaintenance")
    LinearLayout btn_vehicleMaintenance;

    //10借款报销
    @ViewInject(id = R.id.btn_LoanReimbursement, click = "forLoanReimbursement")
    LinearLayout btn_LoanReimbursement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination);
        tv_title.setText(getResources().getString(R.string.examination));
        tv_right.setText("");

    }

    /**
     * 我的申请
     *
     * @param view
     */
    public void myApplication(View view) {
        startActivity(ZOAplicationListActivity.class);

    }

    /**
     * 我的审批
     *
     * @param view
     */
    public void myApproval(View view) {
        startActivity(ZOApprovelListActivity.class);
    }

    /**
     * 我的抄送
     *
     * @param view
     */
    public void myCopyReturn(View view) {
        startActivity(ZOCopyListActivity.class);
    }


    /**
     * 01招聘
     *
     * @param view
     */
    public void forJobs(View view) {
        startActivity(RecruitmentActivity.class);
    }

    /**
     * 02离职jobsLeave
     *
     * @param view
     */
    public void forJobsLeave(View view) {
        startActivity(DimissionActivity.class);
    }

    /**
     * 03请假Leave
     *
     * @param view
     */
    public void forLeave(View view) {
        startActivity(LeaveActivity.class);
    }

    /**
     * 04加班
     *
     * @param view
     */
    public void forWorkOverTime(View view) {
        startActivity(WorkOverTimeActivity.class);
    }

    /**
     * 05调休
     *
     * @param view
     */
    public void forWorkRest(View view) {
        startActivity(TakeDaysOffActivity.class);
    }

    /**
     * 06借阅
     *
     * @param view
     */
    public void forBorrow(View view) {
        startActivity(BorrowActivity.class);
    }

    /**
     * 07薪资调整
     *
     * @param view
     */
    public void Salaryadjust(View view) {
        startActivity(SalaryadjustActivity.class);
    }

    /**
     * 08用车
     *
     * @param view
     */
    public void forCarused(View view) {
        startActivity(VehicleActivity.class);
    }

    /**
     * 09车辆维护
     *
     * @param view
     */
    public void forVehicleMaintenance(View view) {
        startActivity(VehicleMaintainActivity.class);
    }

    /**
     * 10财务申请
     *
     * @param view
     */
    public void forLoanReimbursement(View view) {
        startActivity(FinancialMenuActivity.class);
    }



    /**
     * back
     */

    public void forBack(View view) {
        this.finish();
    }
}
