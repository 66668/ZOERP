package com.zhongou.view;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.NoticeLoadMoreListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApplicationModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.RefreshAndLoadListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 公告
 * Created by JackSong on 2016/10/25.
 */

public class NoticeAcitivity extends BaseActivity implements RefreshAndLoadListView.IReflashListener, RefreshAndLoadListView.ILoadMoreListener {
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
    @ViewInject(id = R.id.noticeList)
    RefreshAndLoadListView myListView;

    private NoticeLoadMoreListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;
    private ArrayList<MyApplicationModel> list = null;
    private String IMaxtime = null;
    private String IMinTime = null;

    //常量
    private static final int GET_MORE_DATA = -32;//上拉加载
    private static final int GET_NEW_DATA = -33;//
    private static final int GET_REFRESH_DATA = -34;//
    private static final int FRESH_NONE_NEWDATA = -35;//没有新数据
    private static final int LOAD_NONE_NEWDATA = -36;//没有新数据
    private static final int GET_NONE_NEWDATA = -37;//没有新数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_notice);
        tv_right.setText("");
        tv_title.setText(getResources().getString(R.string.app_notice));

        myListView.setIRefreshListener(this);//下拉刷新监听
        myListView.setILoadMoreListener(this);//上拉加载监听

        vAdapter = new NoticeLoadMoreListAdapter(this);// 上拉加载
        myListView.setAdapter(vAdapter);

        //		 点击一条记录后，跳转到登记时详细的信息
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = myListView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position
                MyApplicationModel myApplicationModel = (MyApplicationModel) vAdapter.getItem(newPosition);//
                String type = myApplicationModel.getApplicationType();//申请类型
                PageUtil.DisplayToast(type);
            }
        });
        getNewData();
    }

    public void getNewData() {
        Loading.run(NoticeAcitivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                String storeID = UserHelper.getCurrentUser().getStoreID();
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            NoticeAcitivity.this,
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
        Loading.noDialogRun(NoticeAcitivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            NoticeAcitivity.this,
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
                    sendMessage(FRESH_NONE_NEWDATA, e.getMessage());
                }
            }

        });
    }

    // 上拉加载
    @Override
    public void onLoadMore() {
        if (ifLoading) {
            return;
        }

        Loading.run(NoticeAcitivity.this, new Runnable() {

            @Override
            public void run() {

                ifLoading = true;//
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            NoticeAcitivity.this,
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
                    sendMessage(LOAD_NONE_NEWDATA, e.getMessage());
                }
            }
        });
    }

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

                myListView.loadAndFreshComplete();//刷新完成回调
                ifLoading = false;

                break;
            case GET_REFRESH_DATA://刷新
                list = (ArrayList<MyApplicationModel>) msg.obj;
                vAdapter.insertEntityList(list);
                //数据处理/只存最大值,做刷新新数据使用
                setIMaxTime(list);
                ifLoading = false;
                myListView.loadAndFreshComplete();
                break;

            case GET_MORE_DATA://加载
                list = (ArrayList<MyApplicationModel>) msg.obj;
                vAdapter.addEntityList(list);
                //数据处理，只存最小值
                setIMinTime(list);

                myListView.loadAndFreshComplete();
                ifLoading = false;
                break;

            case FRESH_NONE_NEWDATA://没有获取新数据
                sendToastMessage((String) msg.obj);

                myListView.loadAndFreshComplete();
                ifLoading = false;

                break;
            case LOAD_NONE_NEWDATA://没有获取新数据
                sendToastMessage((String) msg.obj);

                myListView.loadAndFreshComplete();
                ifLoading = false;

                break;
            case GET_NONE_NEWDATA://没有获取新数据
                sendToastMessage((String) msg.obj);

                ifLoading = false;

                break;

            default:
                break;
        }
        super.handleMessage(msg);
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
