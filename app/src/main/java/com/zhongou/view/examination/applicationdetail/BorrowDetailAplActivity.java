package com.zhongou.view.examination.applicationdetail;

import android.content.Context;
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
import com.zhongou.model.applicationdetailmodel.BorrowModel;
import com.zhongou.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zhongou.R.id.tv_contains;

/**
 * 借阅详情
 * Created by sjy on 2016/12/29.
 */

public class BorrowDetailAplActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //说明
    @ViewInject(id = R.id.tv_reason, click = "ReasonExpended")
    TextView tv_reason;

    //备注
    @ViewInject(id = R.id.tv_remark, click = "RemarkExpended")
    TextView tv_remark;

    //类型
    @ViewInject(id = R.id.tv_borrowType)
    TextView tv_borrowType;

    //借阅名称
    @ViewInject(id = R.id.tv_BorrowThings)
    TextView tv_BorrowThings;

    //开始时间
    @ViewInject(id = R.id.tv_startTime)
    TextView tv_startTime;

    //结束时间
    @ViewInject(id = R.id.tv_endTime)
    TextView tv_endTime;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    //审批状况
    @ViewInject(id = R.id.tv_state_result)
    TextView tv_state_result;
    @ViewInject(id = R.id.layout_state, click = "forState")
    LinearLayout layout_state;

    //获取子控件个数的父控件
    @ViewInject(id = R.id.layout_ll)
    LinearLayout layout_ll;

    private Intent intent = null;
    private BorrowModel borrowModel;
    private MyApplicationModel model;
    private List<BorrowModel.ApprovalInfoLists> modelList;

    //动态添加view
    private List<View> ls_childView;//用于保存动态添加进来的View
    private View childView;
    private LayoutInflater inflater;//ViewHolder对象用来保存实例化View的子控件
    private List<ViewHolder> listViewHolder = new ArrayList<>();
    //    private int mark = 5;//0显示在顶部

    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_borrow_d);

        initMyView();
        getDetailModel(model);
    }

    private void initMyView() {
        tv_title.setText(getResources().getString(R.string.borrows_d));
        tv_right.setText("");
        intent = getIntent();
        model = (MyApplicationModel) intent.getSerializableExtra("MyApplicationModel");
    }

    private void setShow(BorrowModel model) {
        tv_BorrowThings.setText(model.getBorrowThings());
        tv_startTime.setText(model.getStartTime());
        tv_endTime.setText(model.getFinishTime());
        tv_reason.setText(model.getReason());
        tv_remark.setText(model.getRemark());
        tv_borrowType.setText(model.getBorrowType());

        modelList = model.getApprovalInfoLists();
        // 审批人
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < modelList.size(); i++) {
            nameBuilder.append(modelList.get(i).getApprovalEmployeeName() + " ");
        }
        tv_Requester.setText(nameBuilder);

        //审批状态
        if (borrowModel.getApprovalStatus().contains("0")) {
            tv_state_result.setText("未审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.red));
        } else if (borrowModel.getApprovalStatus().contains("1")) {
            tv_state_result.setText("已审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.green));
        } else if (borrowModel.getApprovalStatus().contains("2")) {
            tv_state_result.setText("审批中...");
            tv_state_result.setTextColor(getResources().getColor(R.color.black));
        } else {
            tv_state_result.setText("你猜猜！");
        }

        if (borrowModel.getApprovalStatus().contains("1") || borrowModel.getApprovalStatus().contains("2")) {
            //插入意见
            for (int i = 0, mark = layout_ll.getChildCount(); i < modelList.size(); i++, mark++) {//mark是布局插入位置，放在mark位置的后边（从1开始计数）
                ViewHolder vh = AddView(this,mark);//添加布局
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


    }

    /**
     * 获取详情数据
     */
    public void getDetailModel(final MyApplicationModel model) {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {

                //泛型
                try {
                    BorrowModel model1 = new UserHelper<BorrowModel>(BorrowModel.class).applicationDetailPost(BorrowDetailAplActivity.this,
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
                borrowModel = (BorrowModel) msg.obj;
                setShow(borrowModel);
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


    private ViewHolder AddView(Context context, int marks) {
        ls_childView = new ArrayList<View>();
        inflater = LayoutInflater.from(context);
        childView = inflater.inflate(R.layout.item_examination_status, null);
        childView.setId(marks);
        layout_ll.addView(childView, marks);
        return getViewInstance(childView);

    }

    private ViewHolder getViewInstance(View childView) {
        ViewHolder vh = new ViewHolder();
        vh.id = childView.getId();
        vh.tv_name = (TextView) childView.findViewById(R.id.tv_name);
        vh.tv_yesOrNo = (TextView) childView.findViewById(R.id.tv_yesOrNo);
        vh.tv_time = (TextView) childView.findViewById(R.id.tv_time);
        vh.tv_contains = (TextView) childView.findViewById(tv_contains);
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

    private boolean isExpend = false;

    public void ReasonExpended(View view) {
        if (!isExpend) {
            tv_reason.setMinLines(0);
            tv_reason.setMaxLines(Integer.MAX_VALUE);
            isExpend = true;
        } else {
            tv_reason.setLines(3);
            isExpend = false;
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
