package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

/**批准
 * Created by sjy on 2017/1/17.
 */

public class CommonApproveActivity extends BaseActivity {
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
    @ViewInject(id = R.id.btn_commit,click = "forApprove")
    Button btn_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_myapproval_approve);
        tv_title.setText(getResources().getString(R.string.examination_commit));
        tv_right.setText("");
    }

    /**
     *
     * @param view
     */
    public void forApprove(View view) {

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
