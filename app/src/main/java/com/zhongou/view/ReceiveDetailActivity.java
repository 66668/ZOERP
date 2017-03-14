package com.zhongou.view;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ReceiveListModel;

import java.util.List;

/**
 * 应用-财务-采购详情
 * Created by sjy on 2017/2/25.
 */

public class ReceiveDetailActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //物品名称
    @ViewInject(id = R.id.tv_recevie_itemName)
    TextView tv_recevie_itemName;

    //规格
    @ViewInject(id = R.id.tv_recevie_spiceil)
    TextView tv_recevie_spiceil;

    //型号
    @ViewInject(id = R.id.tv_recevie_size)
    TextView tv_recevie_size;

    //数量
    @ViewInject(id = R.id.tv_recevie_number)
    TextView tv_recevie_number;

    //用途
    @ViewInject(id = R.id.tv_reason)
    TextView tv_recevie_useage;

    //备注
    @ViewInject(id = R.id.tv_remark, click = "RemarkExpended")
    TextView tv_remark;

    //申请时间
    @ViewInject(id = R.id.tv_recevie_aplTime)
    TextView tv_recevie_aplTime;

    //变量
    private ReceiveListModel model;
    private List<ReceiveListModel.ApprovalInfoLists> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_recevie_detail);


        initMyView();
        setShow();
    }

    private void initMyView() {
        tv_title.setText(getResources().getString(R.string.recevie));
        tv_right.setText("");
        Bundle bundle = this.getIntent().getExtras();
        model = (ReceiveListModel) bundle.getSerializable("ReceiveListModel");
    }

    private void setShow() {
        //
        tv_recevie_itemName.setText(model.getName());
        tv_recevie_spiceil.setText(model.getSpecification());
        tv_recevie_size.setText(model.getVersions());
        tv_recevie_number.setText(model.getAmount());
        tv_remark.setText(model.getRemark());
        tv_recevie_useage.setText(model.getRemark());
        tv_recevie_aplTime.setText(model.getCreateTime());//?

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
}
