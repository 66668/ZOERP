package com.zhongou.view.examination;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.ZOApvlListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.base.BaseListAdapter;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.view.examination.approvaldetail.BorrowDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.ConferenceDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.ContractFileDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.DimissionDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.LeaveDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.NotificationAndNoticeDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.OfficeDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.OutGoingDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.PositionReplaceDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.ProcurementDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.ReceiveDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.RecruitmentDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.RetestDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.SalaryadjustDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.TakeDaysOffDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.VehicleDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.VehicleMaintainDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.WorkOverTimeDetailApvlActivity;
import com.zhongou.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的审批
 * Created by sjy on 2016/12/2.
 */

public class ZOApvlListActivity extends BaseActivity implements RefreshListView.IReflashListener {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    @ViewInject(id = R.id.myapprovalList)
    RefreshListView myListView;

    private ZOApvlListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;
    private ArrayList<MyApprovalModel> list = null;
    private String IMaxtime = null;
    private String IMinTime = null;

    //常量
    private static final int GET_MORE_DATA = -38;//上拉加载
    private static final int GET_NEW_DATA = -37;//上拉加载
    private static final int GET_REFRESH_DATA = -36;//上拉加载
    private static final int GET_NONE_NEWDATA = -35;//没有新数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_list);
        tv_right.setText("");
        tv_title.setText(getResources().getString(R.string.my_approval));

        myListView.setInterFace(this);//下拉刷新监听
        vAdapter = new ZOApvlListAdapter(ZOApvlListActivity.this, adapterCallBack);// 上拉加载
        myListView.setAdapter(vAdapter);
        initListener();

        getNewData();
    }

    private void initListener() {
        // 点击一条记录后，跳转到登记时详细的信息
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = myListView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position

                MyApprovalModel myApprovalModel = (MyApprovalModel) vAdapter.getItem(newPosition);//
                String type = myApprovalModel.getApplicationType();//申请类型
                myApprovalTransfer(type, myApprovalModel);
            }
        });
    }

    public void getNewData() {
        Log.d("SJY", "MyApprovalActivity--getNewData");
        Loading.run(ZOApvlListActivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                String storeID = UserHelper.getCurrentUser().getStoreID();
                try {
                    List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                            ZOApvlListActivity.this,
                            "",//iMaxTime
                            "");

                    if (visitorModelList == null) {
                        vAdapter.IsEnd = true;
                    } else if (visitorModelList.size() < pageSize) {
                        vAdapter.IsEnd = true;
                    }


                    sendMessage(GET_NEW_DATA, visitorModelList);
                } catch (MyException e) {
                    sendMessage(GET_NONE_NEWDATA, e.getMessage());
                }
            }
        });
    }

    //RefreshListView.IReflashListener接口 下拉刷新
    @Override
    public void onRefresh() {
        Loading.run(ZOApvlListActivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                            ZOApvlListActivity.this,
                            IMaxtime,//iMaxTime
                            "");

                    Log.d("SJY", "loadMore--min=" + IMaxtime);
                    if (visitorModelList == null) {
                        vAdapter.IsEnd = true;
                    } else if (visitorModelList.size() < pageSize) {
                        vAdapter.IsEnd = true;
                    }


                    sendMessage(GET_REFRESH_DATA, visitorModelList);

                } catch (MyException e) {
                    sendMessage(GET_NONE_NEWDATA, e.getMessage());
                }
            }

        });
    }

    // 上拉加载
    BaseListAdapter.AdapterCallBack adapterCallBack = new BaseListAdapter.AdapterCallBack() {
        @Override
        public void loadMore() {

            if (ifLoading) {
                return;
            }

            Loading.run(ZOApvlListActivity.this, new Runnable() {

                @Override
                public void run() {

                    ifLoading = true;//
                    try {
                        List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                                ZOApvlListActivity.this,
                                "",//iMaxTime
                                IMinTime);

                        Log.d("SJY", "loadMore--min=" + IMaxtime);
                        if (visitorModelList == null) {
                            vAdapter.IsEnd = true;
                        } else if (visitorModelList.size() < pageSize) {
                            vAdapter.IsEnd = true;
                        }
                        sendMessage(GET_MORE_DATA, visitorModelList);

                    } catch (MyException e) {
                        sendMessage(GET_NONE_NEWDATA, e.getMessage());
                    }
                }
            });

        }
    };

    @Override
    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case GET_NEW_DATA://进入页面加载最新
                // 数据显示
                list = (ArrayList<MyApprovalModel>) msg.obj;
                vAdapter.setEntityList(list);
                //数据处理，获取iLastUpdateTime参数方便后续上拉/下拉使用
                setIMinTime(list);
                setIMaxTime(list);
                Log.d("SJY", "MineApplicationActivity--GET_NEW_DATA--> myListView.reflashComplete");
                myListView.reflashComplete();
                ifLoading = false;
                break;
            case GET_REFRESH_DATA://刷新
                list = (ArrayList<MyApprovalModel>) msg.obj;
                vAdapter.insertEntityList(list);
                //数据处理/只存最大值,做刷新新数据使用
                setIMaxTime(list);
                ifLoading = false;
                break;

            case GET_MORE_DATA://加载
                list = (ArrayList<MyApprovalModel>) msg.obj;
                vAdapter.addEntityList(list);
                //数据处理，只存最小值
                setIMinTime(list);
                ifLoading = false;
                break;

            case GET_NONE_NEWDATA://没有获取新数据
                //                vAdapter.insertEntityList(null);
                sendToastMessage((String) msg.obj);
                Log.d("SJY", "MineApplicationActivity--GET_NONE_NEWDATA--> myListView.reflashComplete");
                myListView.reflashComplete();
                ifLoading = false;
                break;

            default:
                break;
        }
        super.handleMessage(msg);
    }

    public void setIMaxTime(ArrayList<MyApprovalModel> list) {
        IMaxtime = list.get(0).getCreateTime();
    }

    public void setIMinTime(ArrayList<MyApprovalModel> list) {
        IMinTime = list.get(list.size() - 1).getCreateTime();
    }

    /**
     * 申请跳转详细
     */

    private void myApprovalTransfer(String type, MyApprovalModel myApprovalModel) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel",myApprovalModel);

        switch (type) {
            case "招聘申请"://01

                startActivity(RecruitmentDetailApvlActivity.class,bundle);
                break;
            case "离职申请"://02
                startActivity(DimissionDetailApvlActivity.class,bundle);
                break;
            case "请假申请"://03

                startActivity(LeaveDetailApvlActivity.class,bundle);
                break;
            case "加班申请"://04
                startActivity(WorkOverTimeDetailApvlActivity.class,bundle);
                break;
            case "调休申请"://05
                startActivity(TakeDaysOffDetailApvlActivity.class,bundle);
                break;
            case "借阅申请"://06
                startActivity(BorrowDetailApvlActivity.class,bundle);
                break;
            case "调薪申请"://07
                startActivity(SalaryadjustDetailApvlActivity.class,bundle);
                break;
            case "用车申请"://08
                startActivity(VehicleDetailApvlActivity.class,bundle);
                break;
            case "车辆维保"://09
                startActivity(VehicleMaintainDetailApvlActivity.class,bundle);
                break;
            case "借款报销申请"://10
                break;

            case "调动申请"://11
                startActivity(PositionReplaceDetailApvlActivity.class,bundle);
                break;
            case "采购申请"://12
                startActivity(ProcurementDetailApvlActivity.class,bundle);
                break;
            case "通知公告申请"://13
                startActivity(NotificationAndNoticeDetailApvlActivity.class,bundle);
                break;
            case "办公室申请"://14
                startActivity(OfficeDetailApvlActivity.class,bundle);
                break;
            case "领用申请"://15
                startActivity(ReceiveDetailApvlActivity.class,bundle);
                break;
            case "合同文件申请"://16
                startActivity(ContractFileDetailApvlActivity.class,bundle);
                break;
            case "外出申请"://17
                startActivity(OutGoingDetailApvlActivity.class,bundle);
                break;
            case "复试申请"://18
                startActivity(RetestDetailApvlActivity.class,bundle);
                break;
            case "会议申请"://19
                startActivity(ConferenceDetailApvlActivity.class,bundle);
                break;
        }
    }

    /**
     * @param v
     */

    public void forBack(View v) {
        this.finish();
    }
}
