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
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.applicationdetailmodel.FinancialAllModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.NiceSpinner;
import com.zhongou.widget.RefreshAndLoadListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.zhongou.R.id.tv_title;

/**
 * 应用-财务列表界面(不同于审批-财务申请)
 * spinner+listView上拉下拉
 * Created by JackSong on 2016/10/25.
 */

public class FinancialListAcitivity extends BaseActivity implements RefreshAndLoadListView.IReflashListener, RefreshAndLoadListView.ILoadMoreListener {
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
    @ViewInject(id = R.id.listview_finance)
    RefreshAndLoadListView myListView;

    private FinanceListAdapter vAdapter;//记录适配
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

    private ArrayList<FinancialAllModel> list = null;//获取数据 每次20条
    private ArrayList<FinancialAllModel> listAll = new ArrayList<>();//记录所有数据

    private ArrayList<FinancialAllModel> listLoanAll = new ArrayList<>();//记录借款的总数据
    private ArrayList<FinancialAllModel> listRemburseAll = new ArrayList<>();//记录报销的总数据
    private ArrayList<FinancialAllModel> listFeeAll = new ArrayList<>();//记录费用的总数据
    private ArrayList<FinancialAllModel> listPayAll = new ArrayList<>();//记录付款的总数据

    private ArrayList<FinancialAllModel> listLoan;//每次获取的借款的数据段
    private ArrayList<FinancialAllModel> listRemburse;//每次获取的报销的数据段
    private ArrayList<FinancialAllModel> listFee;//每次获取的费用的数据段
    private ArrayList<FinancialAllModel> listPay;//记录付款的数据段

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_finance);
        tv_right.setText("");

        initMyView();
        initListener();
        getData();
    }

    private void initMyView() {

        vAdapter = new FinanceListAdapter(this);
        myListView.setIRefreshListener(FinancialListAcitivity.this);//下拉刷新监听
        myListView.setILoadMoreListener(this);
        myListView.setAdapter(vAdapter);

        //spinner数据
        spinnerData = new LinkedList<>(Arrays.asList("所有", "借款", "报销", "费用申请", "付款"));
        myLastSelectState = spinnerData.get(0);//默认为 我的申请
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

        //		 点击一条记录后，跳转到详细的信息
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = myListView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position

                //所需参数
                FinancialAllModel model = (FinancialAllModel) vAdapter.getItem(newPosition);//
                String type = model.getTypes();

                //页面跳转
                transferTo(type, model);
            }
        });
    }

    private void getData() {
        Loading.run(FinancialListAcitivity.this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;//
                try {
                    List<FinancialAllModel> visitorModelList = UserHelper.GetAppFinanceList(
                            FinancialListAcitivity.this,
                            "",//iMaxTime
                            "");

                    if (visitorModelList == null || visitorModelList.size() < pageSize) {
                        vAdapter.IsEnd = true;
                    }

                    sendMessage(GET_NEW_DATA, visitorModelList);
                } catch (MyException e) {
                    Log.d("SJY", "取值异常=" + e.getMessage());
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
        Loading.noDialogRun(FinancialListAcitivity.this, new Runnable() {

            @Override
            public void run() {
                ifLoading = true;//
                try {

                    List<FinancialAllModel> visitorModelList = UserHelper.GetAppFinanceList(
                            FinancialListAcitivity.this,
                            IMaxtime,//iMaxTime
                            "");

                    Log.d("SJY", "loadMore--min=" + IMaxtime);
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

        if (ifLoading) {
            return;
        }

        if (IMinTime == "") {
            sendMessage(GET_NONE_NEWDATA, "无最新数据");
            return;
        }
        Loading.run(FinancialListAcitivity.this, new Runnable() {

            @Override
            public void run() {

                ifLoading = true;//
                try {
                    List<FinancialAllModel> visitorModelList = UserHelper.GetAppFinanceList(
                            FinancialListAcitivity.this,
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
            case GET_NEW_DATA://进入页面加载最新
                // 数据显示
                list = (ArrayList<FinancialAllModel>) msg.obj;

                //刷新数据前要清空数据
                listAll.clear();
                listFeeAll.clear();
                listLoanAll.clear();
                listPayAll.clear();
                listRemburseAll.clear();

                SplitListState(list, GET_NEW_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_NEW_DATA);//根据spinner值和数据状态 确定显示数据

                setIMinTime(list);
                setIMaxTime(list);
                ifLoading = false;
                break;
            case GET_REFRESH_DATA://刷新
                list = (ArrayList<FinancialAllModel>) msg.obj;
                SplitListState(list, GET_REFRESH_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_REFRESH_DATA);//根据spinner值和数据状态 确定显示数据

                setIMaxTime(list); //数据处理/只存最大值,做刷新新数据使用
                ifLoading = false;
                break;

            case GET_MORE_DATA://加载
                list = (ArrayList<FinancialAllModel>) msg.obj;

                SplitListState(list, GET_MORE_DATA);//筛选数据状态
                showSelectData(myLastSelectState, GET_MORE_DATA);//根据spinner值和数据状态 确定显示数据

                setIMinTime(list);//数据处理，只存最小值
                ifLoading = false;
                break;

            case GET_NONE_NEWDATA://没有获取新数据

                sendToastMessage((String) msg.obj);
                ifLoading = false;
                myListView.loadAndFreshComplete();//停止footerView动作
                break;

            default:
                break;
        }
        super.handleMessage(msg);
    }


    public void setIMaxTime(ArrayList<FinancialAllModel> list) {
        //        IMaxtime = list.get(0).getPlanbackTime();
        IMaxtime = "";
    }

    public void setIMinTime(ArrayList<FinancialAllModel> list) {
        //        IMinTime = list.get(list.size() - 1).getPlanbackTime();
        IMinTime = "";
    }

    /**
     * 筛选spinner状态下数据，并记录
     *
     * @param list  上拉下拉获取的数据记录 20条
     * @param STATE 具体上拉 下拉 获取 三个状态
     */
    private void SplitListState(List<FinancialAllModel> list, final int STATE) {
        if (list.size() <= 0) {
            return;
        }
        //每次来新数据，重新赋值spinner子状态
        listLoan = new ArrayList<>();
        listRemburse = new ArrayList<>();
        listFee = new ArrayList<>();
        listPay = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTypes().contains("借款")) {//未审批
                listLoan.add(list.get(i));
            } else if (list.get(i).getTypes().contains("报销")) {//已审批
                listRemburse.add(list.get(i));
            } else if (list.get(i).getTypes().contains("费用申请")) {
                listFee.add(list.get(i));
            } else if (list.get(i).getTypes().contains("付款")) {
                listPay.add(list.get(i));
            } else {
                PageUtil.DisplayToast("参数出问题！");
            }
        }
        switch (STATE) {
            case GET_NEW_DATA:
                Log.d("SJY", "GET_NEW_DATA筛选");

                //总数据拼接
                listAll.addAll(list);// 总记录数据
                listLoanAll.addAll(listLoan);
                listRemburseAll.addAll(listRemburse);
                listFeeAll.addAll(listFee);
                listPayAll.addAll(listPay);
                break;

            case GET_REFRESH_DATA:
                Log.d("SJY", "GET_REFRESH_DATA筛选");

                //数据插入 (已做拼接处理),使用：当切换spinner时，刷新了n个长度的数据可以直接显示
                //但是 spinner子状态下如何拼接数据？还有一种方式：每次刷新 清空子状态数据重新赋值？

                listAll.addAll(0, list);

                if (listLoan.size() > 0) {
                    listLoanAll.addAll(0, listLoan);
                }
                if (listRemburse.size() > 0) {
                    listRemburseAll.addAll(0, listRemburse);
                }
                if (listFee.size() > 0) {
                    listFeeAll.addAll(0, listFee);
                }
                if (listPay.size() > 0) {
                    listPayAll.addAll(0, listPay);
                }

                break;


            case GET_MORE_DATA:
                Log.d("SJY", "GET_MORE_DATA筛选");

                listAll.addAll(list);// 总记录数据
                listLoanAll.addAll(listLoan);
                listRemburseAll.addAll(listRemburse);
                listFeeAll.addAll(listFee);
                listPayAll.addAll(listPay);
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
            case "所有":

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
            case "借款":

                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listLoanAll);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listLoan);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listLoan);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {

                }

                break;
            case "报销":

                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listRemburseAll);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listRemburse);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listRemburse);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {

                }
                break;

            case "费用申请":


                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listFeeAll);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listFee);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listFee);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_NONE_NEWDATA) {

                }
                break;

            case "付款":


                if (STATE == GET_NEW_DATA) {
                    vAdapter.setEntityList(listPayAll);

                } else if (STATE == GET_REFRESH_DATA) {
                    vAdapter.insertEntityList(listPay);
                    myListView.loadAndFreshComplete();

                } else if (STATE == GET_MORE_DATA) {
                    vAdapter.addEntityList(listPay);
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
     * 进入详情
     * <p>
     * 详情界面和我的申请的详情一样
     *
     * @param model
     */
    private void transferTo(String type, FinancialAllModel model) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("FinancialAllModel", model);
        switch (type) {
            case "借款":
                startActivity(FinancialLoanDetailActivity.class, bundle);
                break;

            case "报销":
                startActivity(FinancialRemburseDetailActivity.class, bundle);
                break;

            case "费用申请":
                startActivity(FinancialFeeDetailActivity.class, bundle);
                break;

            case "付款":
                startActivity(FinancialPayDetailActivity.class, bundle);
                break;
        }
    }


    /**
     * back
     */

    public void forBack(View view) {
        this.finish();
    }
}
