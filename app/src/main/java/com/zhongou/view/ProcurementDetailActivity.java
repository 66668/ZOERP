package com.zhongou.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ProcurementListModel;

import java.util.ArrayList;
import java.util.List;

import static com.zhongou.R.id.tv_contains;

/**
 * 应用-财务-采购详情
 * Created by sjy on 2017/2/25.
 */

public class ProcurementDetailActivity extends BaseActivity {
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
    @ViewInject(id = R.id.tv_procurement_thingsName)
    TextView tv_procurement_thingsName;

    //类型
    @ViewInject(id = R.id.tv_procurement_thingsType)
    TextView tv_procurement_thingsType;

    //规格
    @ViewInject(id = R.id.tv_procurement_ItemSpecifics)
    TextView tv_procurement_ItemSpecifics;

    //型号
    @ViewInject(id = R.id.tv_procurement_ItemSize)
    TextView tv_procurement_ItemVersion;

    //数量
    @ViewInject(id = R.id.tv_procurement_ItemNumber)
    TextView tv_procurement_ItemNumber;

    //金额
    @ViewInject(id = R.id.tv_procurement_ItemFees)
    TextView tv_ritv_procurement_ItemFees;

    //理由
    @ViewInject(id = R.id.tv_procurement_buyFor)
    TextView tv_procurement_buyFor;

    //购买人
    @ViewInject(id = R.id.tv_procurement_buyer)
    TextView tv_procurement_buyer;

    //申请时间
    @ViewInject(id = R.id.tv_procurement_aplTime)
    TextView tv_procurement_aplTime;

    //计划购买时间
    @ViewInject(id = R.id.tv_procurement_PlanBuyTime)
    TextView tv_procurement_PlanBuyTime;


    //备注
    @ViewInject(id = R.id.tv_remark)
    TextView tv_remark;


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


    //变量
    private ProcurementListModel model;
    private List<ProcurementListModel.ApprovalInfoLists> modelList;

    //动态添加view 变量
    private List<View> ls_childView;//用于保存动态添加进来的View
    private View childView;
    private LayoutInflater inflater;//ViewHolder对象用来保存实例化View的子控件
    private List<ViewHolder> listViewHolder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_procurement_d);

        initMyView();
        setShow();
    }

    private void initMyView() {
        tv_title.setText(getResources().getString(R.string.procurement));
        tv_right.setText("");

        Bundle bundle = this.getIntent().getExtras();
        model = (ProcurementListModel) bundle.getSerializable("ProcurementListModel");
    }

    private void setShow() {
        tv_procurement_thingsName.setText(model.getItemName());
        tv_procurement_thingsType.setText(model.getItemType());
        tv_procurement_ItemSpecifics.setText(model.getSpecification());
        tv_procurement_ItemVersion.setText(model.getVersions());
        tv_procurement_ItemNumber.setText(model.getAmount());
        tv_ritv_procurement_ItemFees.setText(model.getEstimateFee());
        tv_procurement_buyer.setText(model.getBuyer());
        tv_procurement_buyFor.setText(model.getReason());
        tv_procurement_aplTime.setText(model.getCreateTime());
        tv_procurement_PlanBuyTime.setText(model.getPlanTime());
        tv_remark.setText(model.getRemark());

//        // 审批人
//        modelList = model.getApprovalInfoLists();
//        StringBuilder nameBuilder = new StringBuilder();
//        for (int i = 0; i < modelList.size(); i++) {
//            nameBuilder.append(modelList.get(i).getApprovalEmployeeName() + " ");
//        }
//        tv_Requester.setText(nameBuilder);
//
//        //审批状态
//        if (model.getApprovalStatus().contains("0")) {
//            tv_state_result.setText("未审批");
//            tv_state_result.setTextColor(getResources().getColor(R.color.red));
//        } else if (model.getApprovalStatus().contains("1")) {
//            tv_state_result.setText("已审批");
//            tv_state_result.setTextColor(getResources().getColor(R.color.green));
//        } else if (model.getApprovalStatus().contains("2")) {
//            tv_state_result.setText("审批中...");
//            tv_state_result.setTextColor(getResources().getColor(R.color.black));
//        } else {
//            tv_state_result.setText("你猜猜！");
//        }
//
//        //插入审批意见
//        if (model.getApprovalStatus().contains("1") || model.getApprovalStatus().contains("2")) {
//            //插入意见
//            for (int i = 0, mark = layout_ll.getChildCount(); i < modelList.size(); i++, mark++) {//mark是布局插入位置，放在mark位置的后边（从1开始计数）
//                ViewHolder vh = AddView(mark);//添加布局
//                vh.tv_name.setText(modelList.get(i).getApprovalEmployeeName());
//                vh.tv_time.setText(modelList.get(i).getApprovalDate());
//                vh.tv_contains.setText(modelList.get(i).getComment());
//                if (modelList.get(i).getYesOrNo().contains("1")) {
//                    vh.tv_yesOrNo.setText("已审批");
//                } else {
//                    vh.tv_yesOrNo.setText("未审批");
//                    vh.tv_yesOrNo.setTextColor(getResources().getColor(R.color.red));
//                }
//            }
//        }

    }

    /**
     * 动态插入view
     */
    public class ViewHolder {
        private int id = -1;
        private TextView tv_name;
        private TextView tv_yesOrNo;
        private TextView tv_time;
        private TextView tv_contains;
    }

    //初始化参数
    private ViewHolder AddView(int marks) {
        ls_childView = new ArrayList<View>();
        inflater = LayoutInflater.from(getApplicationContext());
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
}
