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
import com.zhongou.model.MyCopyModel;
import com.zhongou.model.copydetailmodel.OfficeCopyModel;
import com.zhongou.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zhongou.R.id.tv_contains;

/**
 * 申请 办公室详情
 * Created by sjy on 2017/1/16.
 */

public class OfficeDetailCopyActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;


    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;


    //审批状况
    @ViewInject(id = R.id.tv_state_result)
    TextView tv_state_result;
    @ViewInject(id = R.id.layout_state, click = "forState")
    LinearLayout layout_state;

    //标题
    @ViewInject(id = R.id.tv_office_title)
    TextView tv_office_title;

    //时间
    @ViewInject(id = R.id.tv_office_time)
    TextView tv_office_time;

    //人数
    @ViewInject(id = R.id.tv_office_number)
    TextView tv_office_number;

    //用途
    @ViewInject(id = R.id.tv_office_useage)
    TextView tv_office_useage;

    //备注
    @ViewInject(id = R.id.tv_remark)
    TextView tv_remark;


    //获取子控件个数的父控件
    @ViewInject(id = R.id.layout_ll)
    LinearLayout layout_ll;

    //抄送人
    @ViewInject(id = R.id.tv_copyer)
    TextView tv_copyer;

    //抄送时间
    @ViewInject(id = R.id.tv_copyTime)
    TextView tv_copyTime;
    //变量
    private Intent intent = null;
    private OfficeCopyModel officeModel;
    private MyCopyModel model;
    private List<OfficeCopyModel.ApprovalInfoLists> modelList;
    //动态添加view
    private List<View> ls_childView;//用于保存动态添加进来的View
    private View childView;
    private LayoutInflater inflater;//ViewHolder对象用来保存实例化View的子控件
    private List<ViewHolder> listViewHolder = new ArrayList<>();
    private LinearLayout ll_main;
    //    private int mark = 5;//0显示在顶部
    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_office_d3);
        tv_title.setText(getResources().getString(R.string.office));
        tv_right.setText("");

        Bundle bundle = this.getIntent().getExtras();
        model = (MyCopyModel) bundle.getSerializable("MyCopyModel");
        getDetailModel(model);
    }
    private void setShow(OfficeCopyModel model) {
        tv_copyer.setText(model.getEmployeeName());
        tv_copyTime.setText(model.getApplicationCreateTime());
        //
        tv_office_title.setText(model.getApplicationTitle());
        tv_office_time.setText(model.getTime());
        tv_office_number.setText(model.getNumParticipant());
        tv_office_useage.setText(model.getUseage());
        tv_remark.setText(model.getRemark());


        // 审批人
        modelList = model.getApprovalInfoLists();
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < modelList.size(); i++) {
            nameBuilder.append(modelList.get(i).getApprovalEmployeeName() + " ");
        }
        tv_Requester.setText(nameBuilder);

        //审批状态
        if (officeModel.getApprovalStatus().contains("0")) {
            tv_state_result.setText("未审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.red));
        } else if (officeModel.getApprovalStatus().contains("1")) {
            tv_state_result.setText("已审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.green));
        } else if (officeModel.getApprovalStatus().contains("2")) {
            tv_state_result.setText("审批中...");
            tv_state_result.setTextColor(getResources().getColor(R.color.black));
        } else {
            tv_state_result.setText("你猜猜！");
        }

        for (int i = 0, mark = 5; i < modelList.size(); i++, mark++) {//mark是布局插入位置，放在mark位置的后边（从1开始计数）
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
    public void getDetailModel(final MyCopyModel model) {

        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    OfficeCopyModel model1 = UserHelper.copyDetailPostOffice(OfficeDetailCopyActivity.this,
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
                officeModel = (OfficeCopyModel) msg.obj;
                setShow(officeModel);
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
    private ViewHolder AddView(int marks) {
        ll_main = (LinearLayout) findViewById(R.id.layout_ll);
        ls_childView = new ArrayList<View>();
        inflater = LayoutInflater.from(getApplicationContext());
        childView = inflater.inflate(R.layout.item_examination_status, null);
        childView.setId(marks);
        ll_main.addView(childView, marks);
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
