package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.ImageLoadingConfig;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.applicationdetailmodel.FinancialAllModel;
import com.zhongou.utils.PageUtil;

/**
 * 审批 付款申请
 * Created by sjy on 2016/12/2.
 */

public class FinancialReimburseDetailApvlActivity extends BaseActivity {
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

    //1
    @ViewInject(id = R.id.tv_feeOne)
    TextView tv_feeOne;
    //1
    @ViewInject(id = R.id.tv_useageOne)
    TextView tv_useageOne;
    //2
    @ViewInject(id = R.id.tv_feeTwo)
    TextView tv_feeTwo;
    //2
    @ViewInject(id = R.id.tv_useageTwo)
    TextView tv_useageTwo;
    //3
    @ViewInject(id = R.id.tv_feeThree)
    TextView tv_feeThree;
    //3
    @ViewInject(id = R.id.tv_useageThree)
    TextView tv_useageThree;
    //
    @ViewInject(id = R.id.tv_totle)
    TextView tv_totle;
    //备注
    @ViewInject(id = R.id.tv_remark, click = "RemarkExpended")
    TextView tv_remark;

    //图片1
    @ViewInject(id = R.id.img_01)
    ImageView img_01;

    //图片2
    @ViewInject(id = R.id.img_02)
    ImageView img_02;

    //图片3
    @ViewInject(id = R.id.img_03)
    ImageView img_03;

    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;


    //变量
    private MyApprovalModel myApprovalModel;
    private FinancialAllModel model;
    //imageLoader图片缓存
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_financial_reimburse_d2);
        tv_title.setText(getResources().getString(R.string.financial_reimburse_title_d));
        tv_right.setText("");

        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(this));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);

        Bundle bundle = this.getIntent().getExtras();
        myApprovalModel = (MyApprovalModel) bundle.getSerializable("MyApprovalModel");
        Log.d("SJY", "详情MyApprovalModel");
        bottomType();
        //
        getDetailData();

    }

    private void setShow(FinancialAllModel model) {
        Log.d("SJY", "图片size=" + model.getImageLists().size());

        if (model.getImageLists().size() == 1) {
            imgLoader.displayImage(model.getImageLists().get(0), img_01, imgOptions);
        }

        if (model.getImageLists().size() == 2) {
            imgLoader.displayImage(model.getImageLists().get(0), img_01, imgOptions);
            imgLoader.displayImage(model.getImageLists().get(1), img_02, imgOptions);
        }

        if (model.getImageLists().size() == 3) {
            imgLoader.displayImage(model.getImageLists().get(0), img_01, imgOptions);
            imgLoader.displayImage(model.getImageLists().get(1), img_02, imgOptions);
            imgLoader.displayImage(model.getImageLists().get(2), img_03, imgOptions);
        }
        //
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getCreateTime());

        //
        tv_feeOne.setText(model.getFeeone());
        tv_feeTwo.setText(model.getFeetwo());
        tv_feeThree.setText(model.getFeethree());

        tv_useageOne.setText(model.getUseageone());
        tv_useageTwo.setText(model.getUseagetwo());
        tv_useageThree.setText(model.getUseagethree());

        tv_totle.setText(model.getTotal());
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
                //泛型
                try {
                    FinancialAllModel model1 = new UserHelper<>(FinancialAllModel.class)
                            .approvalDetailPost(FinancialReimburseDetailApvlActivity.this,
                                    myApprovalModel.getApplicationID(),
                                    myApprovalModel.getApplicationType());
                    sendMessage(POST_SUCCESS, model1);
                } catch (MyException e) {
                    e.printStackTrace();
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
                model = (FinancialAllModel) msg.obj;
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgLoader.destroy();
    }
}
