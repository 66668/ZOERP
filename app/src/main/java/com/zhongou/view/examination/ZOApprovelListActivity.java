package com.zhongou.view.examination;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.ZOApprovelListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.view.examination.approvaldetail.BorrowDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.ConferenceDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.ContractFileDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.DimissionDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.FinancialFeeDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.FinancialLoanDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.FinancialPayDetailApvlActivity;
import com.zhongou.view.examination.approvaldetail.FinancialReimburseDetailApvlActivity;
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
import com.zhongou.widget.NiceSpinner;
import com.zhongou.widget.RefreshAndLoadListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.zhongou.R.id.tv_title;


/**
 * 我的审批
 * Created by sjy on 2016/12/2.
 */

public class ZOApprovelListActivity extends BaseActivity implements RefreshAndLoadListView.IReflashListener, RefreshAndLoadListView.ILoadMoreListener {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = tv_title)
    NiceSpinner niceSpinner;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    @ViewInject(id = R.id.myapprovalList)
    RefreshAndLoadListView myListView;

    private ZOApprovelListAdapter vAdapter;//记录适配
    private boolean ifLoading = false;//标记
    private int pageSize = 20;
    private String IMaxtime = null;
    private String IMinTime = null;

    //常量
    private static final int GET_MORE_DATA = -38;//上拉加载
    private static final int GET_NEW_DATA = -37;//上拉加载
    private static final int GET_REFRESH_DATA = -36;//上拉加载
    private static final int GET_NONE_NEWDATA = -35;//没有新数据


    //spinner
    private List<String> spinnerData;
    private String myLastSelectState;//记录spinner上次选中的值
    private ArrayList<MyApprovalModel> list = null;//获取数据 每次20条
    private ArrayList<MyApprovalModel> listAll = new ArrayList<>();//记录所有数据

    private ArrayList<MyApprovalModel> listDONEALL = new ArrayList<>();//记录已审批的总数据
    private ArrayList<MyApprovalModel> listUNDOALL = new ArrayList<>();//记录未审批的总数据
    private ArrayList<MyApprovalModel> listDOINGALL = new ArrayList<>();//记录审批中的总数据

    private ArrayList<MyApprovalModel> listDONE;//每次获取的已审批的数据段
    private ArrayList<MyApprovalModel> listUNDO;//每次获取的未审批的数据段
    private ArrayList<MyApprovalModel> listDOING;//每次获取的审批中的数据段

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
        vAdapter = new ZOApprovelListAdapter(this);// 上拉加载
        myListView.setAdapter(vAdapter);

        //spinner数据
        spinnerData = new LinkedList<>(Arrays.asList("我的审批", "已审批", "未审批", "审批中"));
        myLastSelectState = spinnerData.get(0);//默认为 我的审批
        niceSpinner.attachDataSource(spinnerData);//绑定数据
    }

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

    public void getData() {
        Loading.run(ZOApprovelListActivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                String storeID = UserHelper.getCurrentUser().getStoreID();
                try {
                    List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                            ZOApprovelListActivity.this,
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
        if (IMaxtime == "" || IMaxtime == null) {
            Log.d("SJY", "IMaxtime == kong");
            sendMessage(GET_NONE_NEWDATA, "参数为空");
            return;
        }
        Loading.noDialogRun(ZOApprovelListActivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                            ZOApprovelListActivity.this,
                            IMaxtime,//iMaxTime
                            "");

                    Log.d("SJY", "loadMore--max=" + IMaxtime);
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
        if (IMinTime == "" || IMinTime == null) {
            Log.d("SJY", "IMinTime == kong");
            sendMessage(GET_NONE_NEWDATA, "参数为空");
            return;
        }
        Loading.noDialogRun(ZOApprovelListActivity.this, new Runnable() {

            @Override
            public void run() {

                ifLoading = true;//
                try {
                    List<MyApprovalModel> visitorModelList = UserHelper.getApprovalSearchResults(
                            ZOApprovelListActivity.this,
                            "",//iMaxTime
                            IMinTime);

                    Log.d("SJY", "loadMore--min=" + IMinTime);
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
            case GET_NEW_DATA://进入页面加载最新
                // 数据显示
                list = (ArrayList<MyApprovalModel>) msg.obj;
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
                list = (ArrayList<MyApprovalModel>) msg.obj;
                SplitListState(list, GET_REFRESH_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_REFRESH_DATA);//根据spinner值和数据状态 确定显示数据

                setIMaxTime(list);
                ifLoading = false;
                break;

            case GET_MORE_DATA://加载
                list = (ArrayList<MyApprovalModel>) msg.obj;
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

    public void setIMaxTime(ArrayList<MyApprovalModel> list) {
        IMaxtime = list.get(0).getCreateTime();
    }

    public void setIMinTime(ArrayList<MyApprovalModel> list) {
        IMinTime = list.get(list.size() - 1).getCreateTime();
    }

    /**
     * 筛选spinner状态下数据，并记录
     *
     * @param list  上拉下拉获取的数据记录 20条
     * @param STATE 具体上拉 下拉 获取 三个状态
     */
    private void SplitListState(List<MyApprovalModel> list, final int STATE) {
        if (list.size() <= 0) {
            return;
        }
        //每次来新数据，重新赋值spinner子状态
        listUNDO = new ArrayList<>();
        listDONE = new ArrayList<>();
        listDOING = new ArrayList<>();
        //数据正常拼接
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
            case "我的审批":

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

    /**
     * 申请跳转详细
     */

    private void myApprovalTransfer(String type, MyApprovalModel myApprovalModel) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);

        switch (type) {
            case "招聘申请"://01

                startActivity(RecruitmentDetailApvlActivity.class, bundle);
                break;
            case "离职申请"://02
                startActivity(DimissionDetailApvlActivity.class, bundle);
                break;
            case "请假申请"://03

                startActivity(LeaveDetailApvlActivity.class, bundle);
                break;
            case "加班申请"://04
                startActivity(WorkOverTimeDetailApvlActivity.class, bundle);
                break;
            case "调休申请"://05
                startActivity(TakeDaysOffDetailApvlActivity.class, bundle);
                break;
            case "借阅申请"://06
                startActivity(BorrowDetailApvlActivity.class, bundle);
                break;
            case "调薪申请"://07
                startActivity(SalaryadjustDetailApvlActivity.class, bundle);
                break;
            case "用车申请"://08
                startActivity(VehicleDetailApvlActivity.class, bundle);
                break;
            case "车辆维保"://09
                startActivity(VehicleMaintainDetailApvlActivity.class, bundle);
                break;
            case "财务申请"://10
                //根据type决定跳转到具体界面
                if (myApprovalModel.getApplicationTitle().contains("借款")) {

                    startActivity(FinancialLoanDetailApvlActivity.class, bundle);

                } else if (myApprovalModel.getApplicationTitle().contains("付款")) {

                    startActivity(FinancialPayDetailApvlActivity.class, bundle);

                } else if (myApprovalModel.getApplicationTitle().contains("费用申请")) {

                    startActivity(FinancialFeeDetailApvlActivity.class, bundle);

                } else if (myApprovalModel.getApplicationTitle().contains("报销")) {

                    startActivity(FinancialReimburseDetailApvlActivity.class, bundle);

                } else {
                    PageUtil.DisplayToast("error!");
                }
                break;

            case "调动申请"://11
                startActivity(PositionReplaceDetailApvlActivity.class, bundle);
                break;
            case "采购申请"://12
                startActivity(ProcurementDetailApvlActivity.class, bundle);
                break;
            case "通知公告申请"://13
                startActivity(NotificationAndNoticeDetailApvlActivity.class, bundle);
                break;
            case "办公室申请"://14
                startActivity(OfficeDetailApvlActivity.class, bundle);
                break;
            case "领用申请"://15
                startActivity(ReceiveDetailApvlActivity.class, bundle);
                break;
            case "合同文件申请"://16
                startActivity(ContractFileDetailApvlActivity.class, bundle);
                break;
            case "外出申请"://17
                startActivity(OutGoingDetailApvlActivity.class, bundle);
                break;
            case "复试申请"://18
                startActivity(RetestDetailApvlActivity.class, bundle);
                break;
            case "会议申请"://19
                startActivity(ConferenceDetailApvlActivity.class, bundle);
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
