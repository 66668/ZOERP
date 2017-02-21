package com.zhongou.view;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.VehicleReturnListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.base.BaseListAdapter;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.VehicleReturnModel;
import com.zhongou.view.vehiclereturn.VehicleReturnMaintenanceCompleteActivity;
import com.zhongou.view.vehiclereturn.VehicleReturnMaintenanceUncompleteActivity;
import com.zhongou.view.vehiclereturn.VehicleReturnUseCompleteActivity;
import com.zhongou.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 交车详细界面
 * <p>
 * 多次跳转完成交车后，需要再次跳转到该界面，未交车 显示 变成已交车
 * Created by sjy on 2017/2/14.
 */

public class VehicleReturnListActivity extends BaseActivity implements RefreshListView.IReflashListener {
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
    @ViewInject(id = R.id.listview_vehicleReturn)
    RefreshListView myListView;

    private VehicleReturnListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;
    private ArrayList<VehicleReturnModel> list = null;
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
        setContentView(R.layout.act_vehicle_return);
        tv_title.setText(getResources().getString(R.string.vehicleRe));
        tv_right.setText("");

        initMyView();
        initListener();

        getData();
    }

    private void initMyView() {

        myListView.setInterFace(VehicleReturnListActivity.this);//下拉刷新监听
        vAdapter = new VehicleReturnListAdapter(this, adapterCallBack);// 上拉加载
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
                VehicleReturnModel model = (VehicleReturnModel) vAdapter.getItem(newPosition);//

                //页面跳转
                transferTo(model);
            }
        });
    }

    private void getData() {
        Loading.run(VehicleReturnListActivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                String storeID = UserHelper.getCurrentUser().getStoreID();
                try {
                    List<VehicleReturnModel> visitorModelList = UserHelper.GetVehicleReturnResults(
                            VehicleReturnListActivity.this,
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
        Loading.noDialogRun(VehicleReturnListActivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {

                    List<VehicleReturnModel> visitorModelList = UserHelper.GetVehicleReturnResults(
                            VehicleReturnListActivity.this,
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

            Loading.run(VehicleReturnListActivity.this, new Runnable() {

                @Override
                public void run() {

                    ifLoading = true;//
                    try {
                        List<VehicleReturnModel> visitorModelList = UserHelper.GetVehicleReturnResults(
                                VehicleReturnListActivity.this,
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
                list = (ArrayList<VehicleReturnModel>) msg.obj;
                vAdapter.setEntityList(list);
                //数据处理，获取iLastUpdateTime参数方便后续上拉/下拉使用
                setIMinTime(list);
                setIMaxTime(list);
                Log.d("SJY", "MineApplicationActivity--GET_NEW_DATA--> myListView.reflashComplete");
                myListView.reflashComplete();
                ifLoading = false;
                break;
            case GET_REFRESH_DATA://刷新
                list = (ArrayList<VehicleReturnModel>) msg.obj;
                vAdapter.insertEntityList(list);
                //数据处理/只存最大值,做刷新新数据使用
                setIMaxTime(list);
                ifLoading = false;
                break;

            case GET_MORE_DATA://加载
                list = (ArrayList<VehicleReturnModel>) msg.obj;
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

    public void setIMaxTime(ArrayList<VehicleReturnModel> list) {
        IMaxtime = list.get(0).getCopyTime();
    }

    public void setIMinTime(ArrayList<VehicleReturnModel> list) {
        IMinTime = list.get(list.size() - 1).getCopyTime();
    }

    private void transferTo(VehicleReturnModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VehicleReturnModel", model);
        //用车
        if (model.getApplicationType().contains("用车申请") || model.getApplicationType().contains("用车")) {

            if (model.getIsBack().equals("1")) {
                startActivity(VehicleReturnUseCompleteActivity.class, bundle);//用车-已交车

            } else {
                Log.d("SJY", "用车-未交车");
                startActivity(VehicleReturnUseCompleteActivity.class, bundle);//用车-未交车
            }
        }
        //维保
        if (model.getApplicationType().contains("车辆维保") || model.getApplicationType().contains("维保")) {
            if (model.getIsBack().equals("1")) {
                Log.d("SJY", "维保-已交车");
                startActivity(VehicleReturnMaintenanceCompleteActivity.class, bundle);//维保-已交车

            } else {
                Log.d("SJY", "维保-未交车");
                startActivity(VehicleReturnMaintenanceUncompleteActivity.class, bundle);//维保-未交车
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
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
