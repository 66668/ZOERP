package com.zhongou.view.examination.applicationdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.zhongou.model.MyApplicationModel;
import com.zhongou.model.applicationdetailmodel.FinancialAllModel;
import com.zhongou.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zhongou.R.id.tv_contains;

/**
 * 报销详情
 * Created by sjy on 2016/12/2.
 */

public class FinancialReimburseDetailAplActivity extends BaseActivity {
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

    //获取子控件个数的父控件
    @ViewInject(id = R.id.layout_ll)
    LinearLayout layout_ll;


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

    //变量
    private Intent intent = null;
    private FinancialAllModel financialAllModel;
    private MyApplicationModel model;
    private List<FinancialAllModel.ApprovalInfoLists> modelList;

    //imageLoader图片缓存
    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

    //动态添加view 变量
    private List<View> ls_childView;//用于保存动态添加进来的View
    private View childView;
    private LayoutInflater inflater;//ViewHolder对象用来保存实例化View的子控件
    private List<ViewHolder> listViewHolder = new ArrayList<>();

    //常量
    public static final int POST_SUCCESS = 11;
    public static final int POST_FAILED = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_financial_reimburse_d);
        tv_title.setText(getResources().getString(R.string.financial_reimburse));
        tv_right.setText("");

        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(this));
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.ic_launcher);

        Bundle bundle = this.getIntent().getExtras();
        model = (MyApplicationModel) bundle.getSerializable("MyApplicationModel");
        Log.d("SJY", "model==null?" + (model == null));
        getDetailModel(model);
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

        tv_feeOne.setText(model.getFeeone());
        tv_feeTwo.setText(model.getFeetwo());
        tv_feeThree.setText(model.getFeethree());

        tv_useageOne.setText(model.getUseageone());
        tv_useageTwo.setText(model.getUseagetwo());
        tv_useageThree.setText(model.getFeethree());

        tv_totle.setText(model.getTotal());
        tv_remark.setText(model.getRemark());//?

        // 审批人
        modelList = model.getApprovalInfoLists();
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < modelList.size(); i++) {
            nameBuilder.append(modelList.get(i).getApprovalEmployeeName() + " ");
        }
        tv_Requester.setText(nameBuilder);

        //审批状态
        if (financialAllModel.getApprovalStatus().contains("0")) {
            tv_state_result.setText("未审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.red));
        } else if (financialAllModel.getApprovalStatus().contains("1")) {
            tv_state_result.setText("已审批");
            tv_state_result.setTextColor(getResources().getColor(R.color.green));
        } else if (financialAllModel.getApprovalStatus().contains("2")) {
            tv_state_result.setText("审批中...");
            tv_state_result.setTextColor(getResources().getColor(R.color.black));
        } else {
            tv_state_result.setText("ApprovalStatus传值为空！");
        }

        if (financialAllModel.getApprovalStatus().contains("1") || financialAllModel.getApprovalStatus().contains("2")) {
            Log.d("SJY", "进入已审批意见 1 2的情况--modelList.size()=" + modelList.size());
            //插入意见
            for (int i = 0, mark = layout_ll.getChildCount(); i < modelList.size(); i++, mark++) {//mark是布局插入位置，放在mark位置的后边（从1开始计数）
                Log.d("SJY", "进入添加界面---mark=" + layout_ll.getChildCount());
                ViewHolder vh = AddView(this, mark);//添加布局
                vh.tv_name.setText(modelList.get(i).getApprovalEmployeeName());
                vh.tv_time.setText(modelList.get(i).getApprovalDate());
                vh.tv_contains.setText(modelList.get(i).getComment());
                if (modelList.get(i).getYesOrNo().contains("0")) {
                    vh.tv_yesOrNo.setText("不同意");
                    vh.tv_yesOrNo.setTextColor(getResources().getColor(R.color.red));
                } else {
                    vh.tv_yesOrNo.setText("同意");
                    vh.tv_yesOrNo.setTextColor(getResources().getColor(R.color.green));
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
                    FinancialAllModel model1 = new UserHelper<>(FinancialAllModel.class)
                            .applicationDetailPost(FinancialReimburseDetailAplActivity.this,
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
                financialAllModel = (FinancialAllModel) msg.obj;
                setShow(financialAllModel);
                break;
            case POST_FAILED: // 1001
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
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
