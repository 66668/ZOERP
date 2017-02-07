package com.zhongou.view.examination.copydetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApplicationModel;
import com.zhongou.model.applicationdetailmodel.LeaveModel;
import com.zhongou.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 请假详细
 * Created by sjy on 2016/12/2.
 */

public class LeaveDetailCopyActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;
    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //请假原因
    @ViewInject(id = R.id.tv_reason)
    TextView tv_reason;

    //开始时间
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //结束时间
    @ViewInject(id = R.id.tv_endTime)
    TextView tv_endTime;

    //标题
    @ViewInject(id = R.id.tv_ApplicationTitle)
    TextView tv_ApplicationTitle;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    //审批状况
    @ViewInject(id = R.id.tv_state_result)
    TextView tv_state_result;
    @ViewInject(id = R.id.layout_state, click = "forState")
    LinearLayout layout_state;
    //变量
    private Intent intent = null;
    private LeaveModel leaveModel;
    private MyApplicationModel model;
    private List<LeaveModel.ApprovalInfoLists> modelList;
    //动态添加view
    private List<View> ls_childView;//用于保存动态添加进来的View
    private View childView;
    private LayoutInflater inflater;//ViewHolder对象用来保存实例化View的子控件
    private List<LeaveDetailCopyActivity.ViewHolder> listViewHolder = new ArrayList<LeaveDetailCopyActivity.ViewHolder>();
    private LinearLayout ll_main;
    //    private int mark = 5;//0显示在顶部
    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_forleave_d);
        tv_title.setText(getResources().getString(R.string.leave_d));
        tv_right.setText("");

        intent = getIntent();
        model = (MyApplicationModel) intent.getSerializableExtra("MyApplicationModel");
        getDetailModel(model);
    }

    private void setShow(LeaveModel model) {
        tv_ApplicationTitle.setText("model无参数");
        tv_startTime.setText(model.getStartDate());
        tv_endTime.setText(model.getEndDate());
        tv_reason.setText(model.getContent());

        modelList = model.getApprovalInfoLists();
        // 审批人
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < modelList.size(); i++) {
            nameBuilder.append(modelList.get(i).getApprovalEmployeeName() + " ");
        }
        tv_Requester.setText(nameBuilder);

        //审批状态
        //审批状态
        if (leaveModel.getApprovalStatus().contains("0")) {
            tv_state_result.setText("未审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.red));
        } else if (leaveModel.getApprovalStatus().contains("1")) {
            tv_state_result.setText("已审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.green));
        } else if (leaveModel.getApprovalStatus().contains("2")) {
            tv_state_result.setText("审批中...");
            tv_state_result.setTextColor(getResources().getColor(R.color.black));
        } else {
            tv_state_result.setText("你猜猜！");
        }

        for (int i = 0, mark = 6; i < modelList.size(); i++, mark++) {//mark是布局插入位置，放在mark位置的后边（从1开始计数）
            ViewHolder vh = AddView(mark);//添加布局
            vh.tv_name.setText(modelList.get(i).getApprovalEmployeeName());
            vh.tv_time.setText(modelList.get(i).getApprovalDate());
            vh.tv_contains.setText(modelList.get(i).getComment());
            if (modelList.get(i).getYesOrNo().contains("1")) {
                vh.tv_yesOrNo.setText("已审批");
            } else {
                vh.tv_yesOrNo.setText("未审批");
                vh.tv_yesOrNo.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    /**
     * 获取详情数据
     */
    public void getDetailModel(final MyApplicationModel model) {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    LeaveModel model1 = (LeaveModel) UserHelper.applicationDetailPostLeave(LeaveDetailCopyActivity.this,
                            model.getApplicationID(),
                            model.getApplicationType());
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
            case POST_SUCCESS: // 1001
                leaveModel = (LeaveModel) msg.obj;
                setShow(leaveModel);
                break;
            case POST_FAILED: // 1001
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    public class ViewHolder {
        private int id = -1;
        private TextView tv_name;
        private TextView tv_yesOrNo;
        private TextView tv_time;
        private TextView tv_contains;
    }

    //初始化参数
    private LeaveDetailCopyActivity.ViewHolder AddView(int marks) {
        ll_main = (LinearLayout) findViewById(R.id.layout_ll);
        ls_childView = new ArrayList<View>();
        inflater = LayoutInflater.from(getApplicationContext());
        childView = inflater.inflate(R.layout.item_examination_status, null);
        childView.setId(marks);
        ll_main.addView(childView, marks);
        return getViewInstance(childView);

    }

    private LeaveDetailCopyActivity.ViewHolder getViewInstance(View childView) {
        LeaveDetailCopyActivity.ViewHolder vh = new LeaveDetailCopyActivity.ViewHolder();
        vh.id = childView.getId();
        vh.tv_name = (TextView) childView.findViewById(R.id.tv_name);
        vh.tv_yesOrNo = (TextView) childView.findViewById(R.id.tv_yesOrNo);
        vh.tv_time = (TextView) childView.findViewById(R.id.tv_time);
        vh.tv_contains = (TextView) childView.findViewById(R.id.tv_contains);
        listViewHolder.add(vh);
        ls_childView.add(childView);
        return vh;
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
