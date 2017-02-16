package com.zhongou.view.examination;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongou.R;
import com.zhongou.adapter.MyApplicationListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.base.BaseListAdapter.AdapterCallBack;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApplicationModel;
import com.zhongou.view.examination.applicationdetail.BorrowDetailActivity;
import com.zhongou.view.examination.applicationdetail.ConferenceDetailActivity;
import com.zhongou.view.examination.applicationdetail.ContractFileDetailActivity;
import com.zhongou.view.examination.applicationdetail.DimissionDetailActivity;
import com.zhongou.view.examination.applicationdetail.LeaveDetailActivity;
import com.zhongou.view.examination.applicationdetail.NotificationAndNoticeDetailActivity;
import com.zhongou.view.examination.applicationdetail.OfficeDetailActivity;
import com.zhongou.view.examination.applicationdetail.OutGoingDetailActivity;
import com.zhongou.view.examination.applicationdetail.PositionReplaceDetailActivity;
import com.zhongou.view.examination.applicationdetail.ProcurementDetailActivity;
import com.zhongou.view.examination.applicationdetail.ReceiveDetailActivity;
import com.zhongou.view.examination.applicationdetail.RecruitmentDetailActivity;
import com.zhongou.view.examination.applicationdetail.RetestDetailActivity;
import com.zhongou.view.examination.applicationdetail.SalaryadjustDetailActivity;
import com.zhongou.view.examination.applicationdetail.TakeDaysOffDetailActivity;
import com.zhongou.view.examination.applicationdetail.VehicleDetailActivity;
import com.zhongou.view.examination.applicationdetail.VehicleMaintainDetailActivity;
import com.zhongou.view.examination.applicationdetail.WorkOverTimeDetailActivity;
import com.zhongou.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjy on 2016/12/2.
 */

public class ZOAplListActivity extends BaseActivity implements RefreshListView.IReflashListener {

    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //list
    @ViewInject(id = R.id.myapprovalList)
    RefreshListView myListView;

    private MyApplicationListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;
    private ArrayList<MyApplicationModel> list = null;
    private String IMaxtime = null;
    private String IMinTime = null;

    //常量
    private static final int GET_MORE_DATA = -38;//上拉加载
    private static final int GET_NEW_DATA = -37;//
    private static final int GET_REFRESH_DATA = -36;//
    private static final int GET_NONE_NEWDATA = -35;//没有新数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_list);
        tv_right.setText("");
        tv_title.setText(getResources().getString(R.string.my_application));

        myListView.setInterFace(this);//下拉刷新监听
        vAdapter = new MyApplicationListAdapter(this, adapterCallBack);// 上拉加载
        myListView.setAdapter(vAdapter);

        //		 点击一条记录后，跳转到登记时详细的信息
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ZOAplListActivity.this, "position=" + position, Toast.LENGTH_SHORT).show();

                int headerViewsCount = myListView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position
                MyApplicationModel myApplicationModel = (MyApplicationModel) vAdapter.getItem(newPosition);//
                String type = myApplicationModel.getApplicationType();//申请类型
                myApplicationDetail(type, myApplicationModel);
            }
        });
        getNewData();
    }

    public void getNewData() {
        Log.d("SJY", "MineApplicationActivity--getNewData");
        Loading.run(ZOAplListActivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                String storeID = UserHelper.getCurrentUser().getStoreID();
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            ZOAplListActivity.this,
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
        Loading.noDialogRun(ZOAplListActivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            ZOAplListActivity.this,
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
    AdapterCallBack adapterCallBack = new AdapterCallBack() {
        @Override
        public void loadMore() {

            if (ifLoading) {
                return;
            }

            Loading.run(ZOAplListActivity.this, new Runnable() {

                @Override
                public void run() {

                    ifLoading = true;//
                    try {
                        List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                                ZOAplListActivity.this,
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
                list = (ArrayList<MyApplicationModel>) msg.obj;
                vAdapter.setEntityList(list);
                //数据处理，获取iLastUpdateTime参数方便后续上拉/下拉使用
                setIMinTime(list);
                setIMaxTime(list);
                Log.d("SJY", "MineApplicationActivity--GET_NEW_DATA--> myListView.reflashComplete");
                myListView.reflashComplete();
                ifLoading = false;
                break;
            case GET_REFRESH_DATA://刷新
                list = (ArrayList<MyApplicationModel>) msg.obj;
                vAdapter.insertEntityList(list);
                //数据处理/只存最大值,做刷新新数据使用
                setIMaxTime(list);
                ifLoading = false;
                break;

            case GET_MORE_DATA://加载
                list = (ArrayList<MyApplicationModel>) msg.obj;
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

    //申请跳转详细
    private void myApplicationDetail(String type, MyApplicationModel model) {
        Intent intent = new Intent();
        intent.putExtra("MyApplicationModel", model);
        switch (type) {
            case "招聘申请"://01
                intent.setClass(this, RecruitmentDetailActivity.class);
                startActivity(intent);
                break;
            case "离职申请"://02
                intent.setClass(this, DimissionDetailActivity.class);
                startActivity(intent);
                break;
            case "请假申请"://03
                intent.setClass(this, LeaveDetailActivity.class);
                startActivity(intent);
                break;
            case "加班申请"://04
                intent.setClass(this, WorkOverTimeDetailActivity.class);
                startActivity(intent);
                break;
            case "调休申请"://05
                intent.setClass(this, TakeDaysOffDetailActivity.class);
                startActivity(intent);
                break;
            case "借阅申请"://06
                intent.setClass(this, BorrowDetailActivity.class);
                startActivity(intent);
                break;
            case "调薪申请"://07
                intent.setClass(this, SalaryadjustDetailActivity.class);
                startActivity(intent);
                break;
            case "用车申请"://08
                intent.setClass(this, VehicleDetailActivity.class);
                startActivity(intent);
                break;
            case "车辆维保"://09
                intent.setClass(this, VehicleMaintainDetailActivity.class);
                startActivity(intent);
                break;
            case "借款报销申请"://10
//                intent.setClass(this, .class);
//                startActivity(intent);
                break;


            case "调动申请"://11
                intent.setClass(this, PositionReplaceDetailActivity.class);
                startActivity(intent);
                break;
            case "采购申请"://12
                intent.setClass(this, ProcurementDetailActivity.class);
                startActivity(intent);
                break;
            case "通知公告申请"://13
                intent.setClass(this, NotificationAndNoticeDetailActivity.class);
                startActivity(intent);
                break;
            case "办公室申请"://14
                intent.setClass(this, OfficeDetailActivity.class);
                startActivity(intent);
                break;
            case "领用申请"://15
                intent.setClass(this, ReceiveDetailActivity.class);
                startActivity(intent);
                break;
            case "合同文件申请"://16
                intent.setClass(this, ContractFileDetailActivity.class);
                startActivity(intent);
                break;
            case "外出申请"://17
                intent.setClass(this, OutGoingDetailActivity.class);
                startActivity(intent);
                break;
            case "复试申请"://18
                intent.setClass(this, RetestDetailActivity.class);
                startActivity(intent);
                break;
            case "会议申请"://19
                intent.setClass(this, ConferenceDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setIMaxTime(ArrayList<MyApplicationModel> list) {
        IMaxtime = list.get(0).getCreateTime();
    }

    public void setIMinTime(ArrayList<MyApplicationModel> list) {
        IMinTime = list.get(list.size() - 1).getCreateTime();
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
