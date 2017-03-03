package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.utils.PageUtil;

/**
 * 批准
 * Created by sjy on 2017/1/17.
 */

public class CommonAgreeActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;
    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //内容
    @ViewInject(id = R.id.et_container)
    EditText et_container;

    //同意
    @ViewInject(id = R.id.btn_commit, click = "forApprove")
    Button btn_commit;

    //终审
    @ViewInject(id = R.id.btn_commit_all, click = "forApproveAll")
    Button btn_commit_all;

    //变量
    private MyApprovalModel myApprovalModel;
    private String comment;

    //常量
    private static final int POST_SUCCESS = 11;
    private static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_myapproval_approve);
        tv_title.setText(getResources().getString(R.string.examination_commit));
        tv_right.setText("");
        //获取调转对象
        myApprovalModel = (MyApprovalModel) getIntent().getSerializableExtra("MyApprovalModel");
    }

    /**
     * 审批--同意
     *
     * @param view
     */
    public void forApprove(View view) {
        comment = et_container.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            PageUtil.DisplayToast("提交内容不能为空");
            return;
        }
        Loading.run(this, new Runnable() {

            @Override
            public void run() {
                try {
                    String result = UserHelper.agreeOrDisAgreeMyApproval(CommonAgreeActivity.this
                            , myApprovalModel.getApprovalID()
                            , comment
                            , "0"//终审0否
                            , myApprovalModel.getApplicationID()
                            , "1");//1同意

                    sendMessage(POST_SUCCESS, result);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });


    }

    /**
     * 审批--终审
     *
     * @param view
     */
    public void forApproveAll(View view) {
        comment = et_container.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            PageUtil.DisplayToast("提交内容不能为空");
            return;
        }
        Loading.run(this, new Runnable() {

            @Override
            public void run() {
                try {
                    String result = UserHelper.agreeOrDisAgreeMyApproval(CommonAgreeActivity.this
                            , myApprovalModel.getApprovalID()
                            , comment
                            , "1"//终审1
                            , myApprovalModel.getApplicationID()
                            , "1");//同意1

                    sendMessage(POST_SUCCESS, result);
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
                PageUtil.DisplayToast((String) msg.obj);
                this.finish();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
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
