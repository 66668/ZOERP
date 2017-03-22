package com.zhongou.view.examination;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.ZOAplicationListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApplicationModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.view.examination.applicationdetail.BorrowDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.ConferenceDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.ContractFileDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.DimissionDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.FinancialFeeDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.FinancialLoanDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.FinancialPayDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.FinancialReimburseDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.LeaveDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.NotificationAndNoticeDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.OfficeDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.OutGoingDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.PositionReplaceDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.ProcurementDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.ReceiveDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.RecruitmentDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.RetestDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.SalaryadjustDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.TakeDaysOffDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.VehicleDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.VehicleMaintainDetailAplActivity;
import com.zhongou.view.examination.applicationdetail.WorkOverTimeDetailAplActivity;
import com.zhongou.widget.NiceSpinner;
import com.zhongou.widget.RefreshAndLoadListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.zhongou.R.id.tv_title;


/**
 * 我的申请 记录界面
 * Created by sjy on 2016/12/2.
 */

public class ZOAplicationListActivity extends BaseActivity implements RefreshAndLoadListView.IReflashListener, RefreshAndLoadListView.ILoadMoreListener {

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

    private ZOAplicationListAdapter vAdapter;//记录适配
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
    private ArrayList<MyApplicationModel> list = null;//获取数据 每次20条
    private ArrayList<MyApplicationModel> listAll = new ArrayList<>();//记录所有数据

    private ArrayList<MyApplicationModel> listDONEALL = new ArrayList<>();//记录已审批的总数据
    private ArrayList<MyApplicationModel> listUNDOALL = new ArrayList<>();//记录未审批的总数据
    private ArrayList<MyApplicationModel> listDOINGALL = new ArrayList<>();//记录审批中的总数据

    private ArrayList<MyApplicationModel> listDONE;//每次获取的已审批的数据段
    private ArrayList<MyApplicationModel> listUNDO;//每次获取的未审批的数据段
    private ArrayList<MyApplicationModel> listDOING;//每次获取的审批中的数据段


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_list_common);

        initMyView();
        initListener();
        getData();
    }

    //初始化
    private void initMyView() {
        tv_right.setText("");
        myListView.setIRefreshListener(this);//下拉刷新监听
        myListView.setILoadMoreListener(this);//加载监听
        vAdapter = new ZOAplicationListAdapter(this);// 上拉加载
        myListView.setAdapter(vAdapter);

        //spinner绑定数据
        spinnerData = new LinkedList<>(Arrays.asList("我的申请", "已审批", "未审批", "审批中"));
        myLastSelectState = spinnerData.get(0);//默认为 我的申请
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


        //		 点击一条记录后，跳转到登记时详细的信息
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = myListView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position
                MyApplicationModel myApplicationModel = (MyApplicationModel) vAdapter.getItem(newPosition);//
                String type = myApplicationModel.getApplicationType();//申请类型
                myApplicationDetail(type, myApplicationModel);
            }
        });
    }

    //
    private void getData() {
        Loading.run(ZOAplicationListActivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            ZOAplicationListActivity.this,
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
        Loading.noDialogRun(ZOAplicationListActivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            ZOAplicationListActivity.this,
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
        Loading.noDialogRun(ZOAplicationListActivity.this, new Runnable() {

            @Override
            public void run() {

                ifLoading = true;//
                try {
                    List<MyApplicationModel> visitorModelList = UserHelper.GetMyApplicationSearchResults(
                            ZOAplicationListActivity.this,
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
                list = (ArrayList<MyApplicationModel>) msg.obj;//获取数据

                //重新获取数据，需要清空数据
                listAll.clear();
                listDOINGALL.clear();
                listDONEALL.clear();
                listUNDOALL.clear();

                SplitListState(list, GET_NEW_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_NEW_DATA);//根据spinner值和数据状态 确定显示数据

                setIMinTime(list);
                setIMaxTime(list);
                Log.d("SJY", "获取数据");
                ifLoading = false;
                break;


            case GET_REFRESH_DATA://刷新
                Log.d("SJY", "刷新数据");
                list = (ArrayList<MyApplicationModel>) msg.obj;
                SplitListState(list, GET_REFRESH_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_REFRESH_DATA);//根据spinner值和数据状态 确定显示数据

                setIMaxTime(list);
                ifLoading = false;
                break;


            case GET_MORE_DATA://加载
                Log.d("SJY", "加载数据");
                list = (ArrayList<MyApplicationModel>) msg.obj;
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

    public void setIMaxTime(ArrayList<MyApplicationModel> list) {
        IMaxtime = list.get(0).getCreateTime();
    }

    public void setIMinTime(ArrayList<MyApplicationModel> list) {
        IMinTime = list.get(list.size() - 1).getCreateTime();
    }


    /**
     * 筛选spinner状态下数据，并记录
     *
     * @param list  上拉下拉获取的数据记录 20条
     * @param STATE 具体上拉 下拉 获取 三个状态
     */
    private void SplitListState(List<MyApplicationModel> list, final int STATE) {
        if (list.size() <= 0) {
            return;
        }
        //每次来新数据，重新赋值spinner子状态
        listUNDO = new ArrayList<>();
        listDONE = new ArrayList<>();
        listDOING = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getApprovalStatus().contains("0")) {//未审批
                listUNDO.add(list.get(i));
            } else if (list.get(i).getApprovalStatus().contains("1")) {//已审批
                listDONE.add(list.get(i));
            } else {
                listDOING.add(list.get(i));
            }
        }
        switch (STATE) {
            case GET_NEW_DATA:
                Log.d("SJY", "GET_NEW_DATA筛选");
                //数据正常拼接
                //总数据拼接
                listAll.addAll(list);// 总记录数据
                listDOINGALL.addAll(listDOING);
                listUNDOALL.addAll(listUNDO);
                listDONEALL.addAll(listDONE);
                break;


            case GET_REFRESH_DATA:
                Log.d("SJY", "GET_REFRESH_DATA筛选");

                //数据插入 (已做拼接处理),使用：当切换spinner时，刷新了n个长度的数据可以直接显示
                //但是 spinner子状态下如何拼接数据？还有一种方式：每次刷新 清空子状态数据重新赋值？

                listAll.addAll(0, list);

                if (listDONE.size() > 0) {
                    listDONEALL.addAll(0, listDONE);
                }
                if (listDOING.size() > 0) {
                    listDOINGALL.addAll(0, listDOING);
                }
                if (listUNDO.size() > 0) {
                    listUNDOALL.addAll(0, listUNDO);
                }

                break;


            case GET_MORE_DATA:
                Log.d("SJY", "GET_MORE_DATA筛选");
                //总数据拼接
                listAll.addAll(list);// 总记录数据
                listDOINGALL.addAll(listDOING);
                listUNDOALL.addAll(listUNDO);
                listDONEALL.addAll(listDONE);
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
            case "我的申请":

                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listAll);//代替list，spinner切换时 listAll包含所有数据不会丢失

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(list);
                    myListView.loadAndFreshComplete();
                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(list);
                    myListView.loadAndFreshComplete();
                } else if (STATE == GET_NONE_NEWDATA) {

                }

                break;
            case "已审批":

                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listDONEALL);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listDONE);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listDONE);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {

                }

                break;
            case "未审批":

                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listUNDOALL);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listUNDO);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listUNDO);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {

                }
                break;

            case "审批中":


                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listDOINGALL);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listDOING);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listDOING);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {

                }
                break;
            default:
                PageUtil.DisplayToast("数组出错了！");
                break;

        }
    }

    //申请跳转详细
    private void myApplicationDetail(String type, MyApplicationModel model) {
        Intent intent = new Intent();
        intent.putExtra("MyApplicationModel", model);
        switch (type) {
            case "招聘申请"://01
                intent.setClass(this, RecruitmentDetailAplActivity.class);
                startActivity(intent);
                break;
            case "离职申请"://02
                intent.setClass(this, DimissionDetailAplActivity.class);
                startActivity(intent);
                break;
            case "请假申请"://03
                intent.setClass(this, LeaveDetailAplActivity.class);
                startActivity(intent);
                break;
            case "加班申请"://04
                intent.setClass(this, WorkOverTimeDetailAplActivity.class);
                startActivity(intent);
                break;
            case "调休申请"://05
                intent.setClass(this, TakeDaysOffDetailAplActivity.class);
                startActivity(intent);
                break;
            case "借阅申请"://06
                intent.setClass(this, BorrowDetailAplActivity.class);
                startActivity(intent);
                break;
            case "调薪申请"://07
                intent.setClass(this, SalaryadjustDetailAplActivity.class);
                startActivity(intent);
                break;
            case "用车申请"://08
                intent.setClass(this, VehicleDetailAplActivity.class);
                startActivity(intent);
                break;
            case "车辆维保"://09
                intent.setClass(this, VehicleMaintainDetailAplActivity.class);
                startActivity(intent);
                break;
            case "财务申请"://10

                /**
                 * 该接口不同于其他接口，数据需要再本activiy中获取后才做跳转
                 */
                if (model.getApplicationTitle().contains("借款")) {
                    intent.setClass(this, FinancialLoanDetailAplActivity.class);
                    startActivity(intent);
                } else if (model.getApplicationTitle().contains("付款")) {
                    intent.setClass(this, FinancialPayDetailAplActivity.class);
                    startActivity(intent);
                } else if (model.getApplicationTitle().contains("费用申请")) {
                    intent.setClass(this, FinancialFeeDetailAplActivity.class);
                    startActivity(intent);
                } else if (model.getApplicationTitle().contains("报销")) {
                    intent.setClass(this, FinancialReimburseDetailAplActivity.class);
                    startActivity(intent);
                } else {
                    PageUtil.DisplayToast("error!");
                }

                break;
            case "调动申请"://11
                intent.setClass(this, PositionReplaceDetailAplActivity.class);
                startActivity(intent);
                break;
            case "采购申请"://12
                intent.setClass(this, ProcurementDetailAplActivity.class);
                startActivity(intent);
                break;
            case "通知公告申请"://13
                intent.setClass(this, NotificationAndNoticeDetailAplActivity.class);
                startActivity(intent);
                break;
            case "办公室申请"://14
                intent.setClass(this, OfficeDetailAplActivity.class);
                startActivity(intent);
                break;
            case "领用申请"://15
                intent.setClass(this, ReceiveDetailAplActivity.class);
                startActivity(intent);
                break;
            case "合同文件申请"://16
                intent.setClass(this, ContractFileDetailAplActivity.class);
                startActivity(intent);
                break;
            case "外出申请"://17
                intent.setClass(this, OutGoingDetailAplActivity.class);
                startActivity(intent);
                break;
            case "复试申请"://18
                intent.setClass(this, RetestDetailAplActivity.class);
                startActivity(intent);
                break;
            case "会议申请"://19
                intent.setClass(this, ConferenceDetailAplActivity.class);
                startActivity(intent);
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
