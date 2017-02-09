package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
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
import com.zhongou.model.MyApprovalModel;
import com.zhongou.utils.PageUtil;

/**驳回
 * Created by sjy on 2017/1/17.
 */

public class CommonDisagreeActivity extends BaseActivity {
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
    TextView et_container;

    //提交
    @ViewInject(id = R.id.btn_commit,click = "forRefulse")
    Button btn_commit;

    //变量
    private MyApprovalModel myApprovalModel;
    private String comment;

    //常量
    private static final int POST_SUCCESS = 11;
    private static final int POST_FAILED = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_myapproval_refulse);
        tv_title.setText(getResources().getString(R.string.examination_refulse));
        tv_right.setText("");
        //获取调转对象
        myApprovalModel = (MyApprovalModel) getIntent().getSerializableExtra("MyApprovalModel");
    }

    /**
     *
     * @param view
     */
    public void forRefulse(View view) {
        comment = et_container.getText().toString();
        if (TextUtils.isEmpty(comment)){
            PageUtil.DisplayToast("提交内容不能为空");
            return;
        }
        Loading.run(this,new Runnable(){

            @Override
            public void run() {
                try {
                    String result = UserHelper.agreeOrDisAgreeMyApproval(CommonDisagreeActivity.this
                            ,myApprovalModel.getApprovalID()
                            ,comment
                            ,"0"//终审0否
                            ,myApprovalModel.getApplicationID()
                            ,"0");//0驳回

                    sendMessage(POST_SUCCESS,result);
                } catch (MyException e) {
                    sendMessage(POST_FAILED,e.getMessage());
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
                et_container.setText("");
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
