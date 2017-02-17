package com.zhongou.view;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.FinanceListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.base.BaseListAdapter;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.FinanceModel;
import com.zhongou.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用-财务(不同于审批-财务申请)
 * Created by JackSong on 2016/10/25.
 */

public class FinanceListAcitivity extends BaseActivity implements RefreshListView.IReflashListener {
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
    @ViewInject(id = R.id.listview_finance)
    RefreshListView myListView;

    private FinanceListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;
    private ArrayList<FinanceModel> list = null;
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
        setContentView(R.layout.act_apps_finance);
        tv_title.setText(getResources().getString(R.string.examination));
        tv_right.setText("");

        initMyView();
        initListener();
        getData();
    }

    private void initMyView() {

        myListView.setInterFace(FinanceListAcitivity.this);//下拉刷新监听
        vAdapter = new FinanceListAdapter(this, adapterCallBack);// 上拉加载
        myListView.setAdapter(vAdapter);

    }

    private void initListener() {

        //		 点击一条记录后，跳转到登记时详细的信息
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = myListView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position

                //所需参数
                FinanceModel model = (FinanceModel) vAdapter.getItem(newPosition);//

                //页面跳转
                transferTo(model);
            }
        });
    }

    private void getData() {
        Loading.run(FinanceListAcitivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                String storeID = UserHelper.getCurrentUser().getStoreID();
                try {
                    List<FinanceModel> visitorModelList = UserHelper.GetAppFinanceList(
                            FinanceListAcitivity.this,
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
        Loading.noDialogRun(FinanceListAcitivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {

                    List<FinanceModel> visitorModelList = UserHelper.GetAppFinanceList(
                            FinanceListAcitivity.this,
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

            Loading.run(FinanceListAcitivity.this, new Runnable() {

                @Override
                public void run() {

                    ifLoading = true;//
                    try {
                        List<FinanceModel> visitorModelList = UserHelper.GetAppFinanceList(
                                FinanceListAcitivity.this,
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
                list = (ArrayList<FinanceModel>) msg.obj;
                vAdapter.setEntityList(list);
                //数据处理，获取iLastUpdateTime参数方便后续上拉/下拉使用
                setIMinTime(list);
                setIMaxTime(list);
                Log.d("SJY", "MineApplicationActivity--GET_NEW_DATA--> myListView.reflashComplete");
                myListView.reflashComplete();
                ifLoading = false;
                break;
            case GET_REFRESH_DATA://刷新
                list = (ArrayList<FinanceModel>) msg.obj;
                vAdapter.insertEntityList(list);

                setIMaxTime(list); //数据处理/只存最大值,做刷新新数据使用
                ifLoading = false;
                break;

            case GET_MORE_DATA://加载
                list = (ArrayList<FinanceModel>) msg.obj;
                vAdapter.addEntityList(list);

                setIMinTime(list);//数据处理，只存最小值
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

    public void setIMaxTime(ArrayList<FinanceModel> list) {
        IMaxtime = list.get(0).getCopyTime();
    }

    public void setIMinTime(ArrayList<FinanceModel> list) {
        IMinTime = list.get(list.size() - 1).getCopyTime();
    }


    /**
     * 进入详情
     * <p>
     * 详情界面和我的申请的详情一样
     *
     * @param model
     */
    private void transferTo(FinanceModel model) {

        //        Intent intent = new Intent();
        //        Bundle bundle = new Bundle();
        //        bundle.putSerializable("VehicleReturnModel", model);
        //        intent.putExtras(bundle);
        //        //用车
        //        if (model.getApplicationType().contains("用车申请") || model.getApplicationType().contains("用车")) {
        //
        //            if (model.getIsBack().equals("1")) {
        //                Log.d("SJY", "用车-已交车");
        //                intent.setClass(this, VehicleReturnUseCompleteActivity.class);
        //                startActivity(intent);//用车-已交车
        //            } else {
        //                Log.d("SJY", "用车-未交车");
        //                intent.setClass(this, VehicleReturnUseUncompleteActivity.class);
        //                startActivity(intent);//用车-未交车
        //            }
        //        }
        //        //维保
        //        if (model.getApplicationType().contains("车辆维保") || model.getApplicationType().contains("维保")) {
        //            if (model.getIsBack().equals("1")) {
        //                Log.d("SJY", "维保-已交车");
        //                intent.setClass(this, VehicleReturnMaintenanceCompleteActivity.class);
        //                startActivity(intent);//维保-已交车
        //            } else {
        //                Log.d("SJY", "维保-未交车");
        //                intent.setClass(this, VehicleReturnMaintenanceUncompleteActivity.class);
        //                startActivity(intent);//维保-未交车
        //            }
        //        }
    }


    /**
     * back
     */

    public void forBack(View view) {
        this.finish();
    }
}
