package com.zhongou.view;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.MapAttendListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MapAttendModel;
import com.zhongou.utils.DateUtils;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.NiceSpinner;
import com.zhongou.widget.RefreshAndLoadListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.zhongou.R.id.tv_title;

/**
 * 考勤记录
 * Created by sjy on 2016/11/28.
 */

public class MapAttendRecordListActivity extends BaseActivity implements RefreshAndLoadListView.IReflashListener, RefreshAndLoadListView.ILoadMoreListener {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = tv_title)
    NiceSpinner niceSpinner;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    //list
    @ViewInject(id = R.id.myapprovalList)
    RefreshAndLoadListView myListView;


    private MapAttendListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;

    private String IMaxtime = null;
    private String IMinTime = null;
    //常量
    private static final int GET_MORE_DATA = -38;//上拉加载
    private static final int GET_NEW_DATA = -37;//
    private static final int GET_REFRESH_DATA = -36;//
    private static final int GET_NONE_NEWDATA = -35;//没有新数据

    //spinner
    private List<String> spinnerData;
    private String myLastSelectState;//记录spinner上次选中的值
    private ArrayList<MapAttendModel> list = null;//获取数据 每次20条
    private ArrayList<MapAttendModel> listAll = new ArrayList<>();//记录所有数据

    private ArrayList<MapAttendModel> listTodayALL = new ArrayList<>();//记录已审批的总数据
    private ArrayList<MapAttendModel> listWeekedALL = new ArrayList<>();//记录未审批的总数据
    private ArrayList<MapAttendModel> listMonthALL = new ArrayList<>();//记录审批中的总数据

    private ArrayList<MapAttendModel> listToday;//每次获取的已审批的数据段
    private ArrayList<MapAttendModel> listWeeked;//每次获取的未审批的数据段
    private ArrayList<MapAttendModel> listMonth;//每次获取的审批中的数据段

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_list_common);
        tv_right.setText("");

        initMyView();
        initListener();
        getData();
    }

    private void initMyView() {
        tv_right.setText("");
        myListView.setIRefreshListener(this);//下拉刷新监听
        myListView.setILoadMoreListener(this);//加载监听
        vAdapter = new MapAttendListAdapter(this);// 上拉加载
        myListView.setAdapter(vAdapter);

        //spinner数据
        spinnerData = new LinkedList<>(Arrays.asList("所有记录", "本日记录", "本周记录", "本月记录"));
        myLastSelectState = spinnerData.get(0);//默认为 所有记录
        niceSpinner.attachDataSource(spinnerData);//绑定数据
    }

    //监听
    private void initListener() {

        //spinner监听，筛选数据
        niceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SJY", "spinner监听--" + spinnerData.get(position));

                //清空adapter的list长度 isHanging状态改变

                //如果选择状态没变，就不做处理
                if (!spinnerData.get(position).equals(myLastSelectState)) {
                    showSelectData(spinnerData.get(position).trim(), GET_NEW_DATA);//参数2必填GET_NEW_DATA
                } else {
                    return;
                }

            }
        });
    }


    //
    private void getData() {
        Loading.run(MapAttendRecordListActivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MapAttendModel> visitorModelList = UserHelper.getMapAttendRecord(
                            MapAttendRecordListActivity.this,
                            "",//iMaxTime
                            "");

                    if (visitorModelList == null || visitorModelList.size() < pageSize) {
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
        if (IMaxtime == "") {
            sendMessage(GET_NONE_NEWDATA, "无最新数据");
            return;
        }
        Loading.noDialogRun(MapAttendRecordListActivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MapAttendModel> visitorModelList = UserHelper.getMapAttendRecord(
                            MapAttendRecordListActivity.this,
                            IMaxtime,//iMaxTime
                            "");

                    Log.d("SJY", "onRefresh--IMaxtime=" + IMaxtime);
                    if (visitorModelList == null || visitorModelList.size() < pageSize) {
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
    @Override
    public void onLoadMore() {
        if (IMinTime == "") {
            sendMessage(GET_NONE_NEWDATA, "无最新数据");
            return;
        }
        Loading.noDialogRun(MapAttendRecordListActivity.this, new Runnable() {

            @Override
            public void run() {

                ifLoading = true;//
                try {
                    List<MapAttendModel> visitorModelList = UserHelper.getMapAttendRecord(
                            MapAttendRecordListActivity.this,
                            "",//iMaxTime
                            IMinTime);

                    Log.d("SJY", "loadMore--min=" + IMaxtime);
                    if (visitorModelList == null || visitorModelList.size() < pageSize) {
                        vAdapter.IsEnd = true;
                    }

                    sendMessage(GET_MORE_DATA, visitorModelList);

                } catch (MyException e) {
                    sendMessage(GET_NONE_NEWDATA, e.getMessage());
                }
            }
        });

    }

    @Override
    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case GET_NEW_DATA:
                list = (ArrayList<MapAttendModel>) msg.obj;//获取数据

                //刷数据前清空数据
                listAll.clear();
                listTodayALL.clear();
                listWeekedALL.clear();
                listMonthALL.clear();

                Log.d("SJY", "第一次获取数据长度=" + list.size());
                SplitListState(list, GET_NEW_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_NEW_DATA);//根据spinner值和数据状态 确定显示数据

                setIMinTime(list);
                setIMaxTime(list);
                Log.d("SJY", "获取数据");
                ifLoading = false;
                break;


            case GET_REFRESH_DATA://刷新
                Log.d("SJY", "刷新数据");
                list = (ArrayList<MapAttendModel>) msg.obj;
                SplitListState(list, GET_REFRESH_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_REFRESH_DATA);//根据spinner值和数据状态 确定显示数据

                setIMaxTime(list);
                ifLoading = false;
                break;


            case GET_MORE_DATA://加载
                Log.d("SJY", "加载数据");
                list = (ArrayList<MapAttendModel>) msg.obj;
                SplitListState(list, GET_MORE_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_MORE_DATA);//根据spinner值和数据状态 确定显示数据

                setIMinTime(list);
                ifLoading = false;
                break;


            case GET_NONE_NEWDATA://没有获取新数据
                Log.d("SJY", "无最新数据");
                sendToastMessage((String) msg.obj);
                ifLoading = false;

                myListView.loadAndFreshComplete();//停止footerView动作
                break;

            default:
                break;
        }
        super.handleMessage(msg);
    }

    public void setIMaxTime(ArrayList<MapAttendModel> list) {
        IMaxtime = list.get(0).getAttendCapTime();
    }

    public void setIMinTime(ArrayList<MapAttendModel> list) {
        IMinTime = list.get(list.size() - 1).getAttendCapTime();
    }

    /**
     * 筛选spinner状态下数据，并记录
     *
     * @param list  上拉下拉获取的数据记录 20条
     * @param STATE 具体上拉 下拉 获取 三个状态
     */
    private void SplitListState(List<MapAttendModel> list, final int STATE) {
        if (list.size() <= 0) {
            return;
        }

        //每次来新数据，重新赋值spinner子状态
        listWeeked = new ArrayList<>();
        listToday = new ArrayList<>();
        listMonth = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (DateUtils.isThisMonth(list.get(i).getAttendCapTime())) {//本月
                listMonth.add(list.get(i));
            }

            if (DateUtils.isToday(list.get(i).getAttendCapTime())) {//本日
                listToday.add(list.get(i));
            }

            if (DateUtils.isThisWeek(list.get(i).getAttendCapTime())) {//本周
                listWeeked.add(list.get(i));
            }
        }

        switch (STATE) {
            case GET_NEW_DATA:
                Log.d("SJY", "GET_NEW_DATA筛选");
                //数据正常拼接

                //总数据拼接
                listAll.addAll(list);// 总记录数据
                listMonthALL.addAll(listMonth);
                listWeekedALL.addAll(listWeeked);
                listTodayALL.addAll(listToday);
                break;


            case GET_REFRESH_DATA:
                Log.d("SJY", "GET_REFRESH_DATA筛选");

                //数据插入
                listAll.addAll(0, list);

                //有数据就插入
                if (listToday.size() > 0) {
                    listTodayALL.addAll(0, listToday);
                }
                if (listMonth.size() > 0) {
                    listMonthALL.addAll(0, listMonth);
                }
                if (listWeeked.size() > 0) {
                    listWeekedALL.addAll(0, listWeeked);
                }

                break;


            case GET_MORE_DATA:
                Log.d("SJY", "GET_MORE_DATA筛选");
                //数据正常拼接
                listAll.addAll(list);// 总记录数据
                listMonthALL.addAll(listMonth);
                listWeekedALL.addAll(listWeeked);
                listTodayALL.addAll(listToday);
                break;

            default:
                break;
        }

    }

    /**
     * 数据展示
     *
     * @param spinnerState spinner状态 很重要
     * @param STATE        上拉下拉状态
     */

    private void showSelectData(String spinnerState, final int STATE) {

        myLastSelectState = spinnerState;//记录spinner修改状态
        switch (spinnerState) {
            case "所有记录":

                if (STATE == GET_NEW_DATA) {
                    //                    vAdapter.setEntityList(listAll);//代替list，spinner切换时 listAll包含所有数据不会丢失
                    vAdapter.setEntityList(list);//代替list，spinner切换时 listAll包含所有数据不会丢失

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(list);
                    myListView.loadAndFreshComplete();
                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(list);
                    myListView.loadAndFreshComplete();
                } else if (STATE == GET_NONE_NEWDATA) {
                    myListView.loadAndFreshComplete();
                }

                break;
            case "本日记录":

                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listTodayALL);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listToday);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listToday);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {
                    myListView.loadAndFreshComplete();
                }

                break;
            case "本周记录":

                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listWeekedALL);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listWeeked);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listWeeked);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {
                    myListView.loadAndFreshComplete();
                }
                break;

            case "本月记录":


                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listMonthALL);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listMonth);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listMonth);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {
                    myListView.loadAndFreshComplete();
                }
                break;
            default:
                PageUtil.DisplayToast("数组出错了！");
                break;

        }
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