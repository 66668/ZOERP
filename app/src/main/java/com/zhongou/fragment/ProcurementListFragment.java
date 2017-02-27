package com.zhongou.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.zhongou.R;
import com.zhongou.adapter.ProcurementListAdapter;
import com.zhongou.base.BaseFragment;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.model.ProcurementListModel;
import com.zhongou.view.ProcurementDetailActivity;
import com.zhongou.widget.RefreshAndLoadListView;

import java.util.ArrayList;
import java.util.List;


/**
 * BottomBar 应用
 */

public class ProcurementListFragment extends BaseFragment implements RefreshAndLoadListView.ILoadMoreListener, RefreshAndLoadListView.IReflashListener {
    private static final String TAG = "ProcurementFragment";
    private RefreshAndLoadListView listView;

    private ArrayList<ProcurementListModel> list = null;//获取数据 每次20条
    private ProcurementListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;
    private String IMaxtime = null;
    private String IMinTime = null;

    //常量
    private static final int GET_MORE_DATA = -38;//上拉加载
    private static final int GET_NEW_DATA = -37;//
    private static final int GET_REFRESH_DATA = -36;//
    private static final int GET_NONE_NEWDATA = -35;//没有新数据

    //单例模式
    public static ProcurementListFragment newInstance() {
        ProcurementListFragment appsFragment = new ProcurementListFragment();
        return appsFragment;
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act_apps_procure_list, container, false);
        initView(view);
        initListener();
        getData();
        return view;
    }

    //界面详细
    public void initView(View view) {
        listView = (RefreshAndLoadListView) view.findViewById(R.id.procurementList);

        vAdapter = new ProcurementListAdapter(getActivity());
        listView.setIRefreshListener(this);//刷新监听
        listView.setILoadMoreListener(this);
        listView.setAdapter(vAdapter);

    }

    //页面跳转详情监听
    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = listView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position

                //所需参数
                ProcurementListModel model = (ProcurementListModel) vAdapter.getItem(newPosition);//

                //跳转
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                bundle.putSerializable("ProcurementListModel", model);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), ProcurementDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    //进入获取数据
    private void getData() {
        final Message msg = handler.obtainMessage();

        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<ProcurementListModel> visitorModelList = UserHelper.GetAppProcurementList(
                            getActivity(),
                            "",//iMaxTime
                            "");

                    if (visitorModelList == null || visitorModelList.size() < pageSize) {
                        vAdapter.IsEnd = true;
                    }

                    handler.sendMessage(handler.obtainMessage(GET_NEW_DATA, visitorModelList));
                } catch (MyException e) {

                    handler.sendMessage(handler.obtainMessage(GET_NONE_NEWDATA, e.getMessage().toString()));
                }
            }
        });

    }

    //刷新
    @Override
    public void onRefresh() {
        Message msg = handler.obtainMessage();
        if (ifLoading) {
            return;
        }
        if (IMaxtime == "") {
            handler.sendMessage(handler.obtainMessage(GET_NONE_NEWDATA, "无最新数据"));
            return;
        }
        Loading.noDialogRun(getActivity(), new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {

                    List<ProcurementListModel> visitorModelList = UserHelper.GetAppProcurementList(
                            getActivity(),
                            IMaxtime,//iMaxTime
                            "");

                    Log.d("SJY", "loadMore--min=" + IMaxtime);
                    if (visitorModelList == null || visitorModelList.size() < pageSize) {
                        vAdapter.IsEnd = true;
                    }

                    handler.sendMessage(handler.obtainMessage(GET_REFRESH_DATA, visitorModelList));

                } catch (MyException e) {

                    handler.sendMessage(handler.obtainMessage(GET_NONE_NEWDATA, e.getMessage().toString()));
                }
            }

        });
    }

    //加载
    @Override
    public void onLoadMore() {
        Message msg = handler.obtainMessage();
        if (ifLoading) {
            return;
        }

        if (IMinTime == "") {
            handler.sendMessage(handler.obtainMessage(GET_NONE_NEWDATA, "无最新数据"));
            return;
        }
        Loading.run(getActivity(), new Runnable() {

            @Override
            public void run() {

                ifLoading = true;//
                try {
                    List<ProcurementListModel> visitorModelList = UserHelper.GetAppProcurementList(
                            getActivity(),
                            "",//iMaxTime
                            IMinTime);

                    Log.d("SJY", "loadMore--min=" + IMaxtime);

                    if (visitorModelList == null || visitorModelList.size() < pageSize) {
                        vAdapter.IsEnd = true;
                    }

                    handler.sendMessage(handler.obtainMessage(GET_MORE_DATA, visitorModelList));

                } catch (MyException e) {

                    handler.sendMessage(handler.obtainMessage(GET_NONE_NEWDATA, e.getMessage().toString()));
                }
            }
        });
    }

    /**
     * handler sendMessage的处理
     */
    @SuppressLint("HandlerLeak") // 确保类内部的handler不含有对外部类的隐式引用
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 调用下边的方法处理信息
            switch (msg.what) {

                case GET_NEW_DATA://进入页面加载最新
                    // 数据显示
                    list = (ArrayList<ProcurementListModel>) msg.obj;
                    vAdapter.setEntityList(list);
                    //数据处理，获取iLastUpdateTime参数方便后续上拉/下拉使用
                    setIMinTime(list);
                    setIMaxTime(list);
                    listView.loadAndFreshComplete();
                    ifLoading = false;
                    break;
                case GET_REFRESH_DATA://刷新
                    list = (ArrayList<ProcurementListModel>) msg.obj;
                    vAdapter.insertEntityList(list);
                    //数据处理/只存最大值,做刷新新数据使用
                    setIMaxTime(list);
                    ifLoading = false;
                    listView.loadAndFreshComplete();

                    break;

                case GET_MORE_DATA://加载
                    list = (ArrayList<ProcurementListModel>) msg.obj;
                    vAdapter.addEntityList(list);
                    //数据处理，只存最小值
                    setIMinTime(list);
                    ifLoading = false;
                    listView.loadAndFreshComplete();
                    break;

                case GET_NONE_NEWDATA://没有获取新数据
                    Toast.makeText(getActivity(),(String) msg.obj,Toast.LENGTH_SHORT).show();
                    listView.loadAndFreshComplete();
                    ifLoading = false;
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void setIMaxTime(ArrayList<ProcurementListModel> list) {
        //        IMaxtime = list.get(0).getPlanbackTime();
        IMaxtime = "";
    }

    public void setIMinTime(ArrayList<ProcurementListModel> list) {
        //        IMinTime = list.get(list.size() - 1).getPlanbackTime();
        IMinTime = "";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public boolean hasMultiFragment() {
        return false;
    }

    @Override
    protected String setFragmentName() {
        return null;
    }

    //重写setMenuVisibility方法，不然会出现叠层的现象
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }


}
