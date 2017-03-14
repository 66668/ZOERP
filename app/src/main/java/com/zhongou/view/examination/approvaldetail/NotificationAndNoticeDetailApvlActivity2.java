package com.zhongou.view.examination.approvaldetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

/**
 * 审批 通知公告详情
 * Created by sjy on 2017/1/16.
 */

public class NotificationAndNoticeDetailApvlActivity2 extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;
    //
    @ViewInject(id = R.id.tv_content)
    TextView tv_content;

    //变量
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_notificationandnotice_content);
        tv_title.setText(getResources().getString(R.string.notificaitonAndNotice_content));
        tv_right.setText("");

        Bundle bundle = this.getIntent().getExtras();
        content = bundle.getString("Abstract");
        setShow(content);

    }

    private void setShow(String content) {
        tv_content.setText((TextUtils.isEmpty(content) || content == null) ? "参数错误" : content);
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
