package com.zhongou.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.ScheduleListAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.db.sqlite.SQLiteScheduledb;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.ScheduleModel;
import com.zhongou.utils.PageUtil;
import com.zhongou.widget.RefreshListView;

import java.util.ArrayList;

/**
 * 日程表显示
 *
 *
 * Created by sjy on 2017/2/28.
 */

public class ScheduleListActivity extends BaseActivity {

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

    //变量
    private ScheduleListAdapter vAdapter;//记录适配

//    private ScheduleDAO dao = null;
    private SQLiteScheduledb dao = null;

    private ScheduleModel scheduleModel = null;
    private ArrayList<ScheduleModel> schList;
    private String scheduleInfo = "";
    private int scheduleID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_list);

        initMyView();
        getData();
        initListener();
    }

    private void initMyView() {
        tv_right.setText("");
        tv_title.setText("所有日程");
        schList = new ArrayList<ScheduleModel>();

        //        dao = new ScheduleDAO(this);
        dao = new SQLiteScheduledb(this, UserHelper.getCurrentUser().getEmployeeID() + ".db");
    }

    //获取所有sql数据
    private void getData() {
        schList = dao.getAllSchedule();
        if (schList == null || schList.size() <= 0) {
            PageUtil.DisplayToast("没有日程记录！");
            this.finish();

        } else {
            vAdapter = new ScheduleListAdapter(this, schList);
            myListView.setAdapter(vAdapter);
        }
    }

    //进入详情，
    private void initListener() {
        // 点击一条记录后，跳转到登记时详细的信息
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headerViewsCount = myListView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position

                //
                ScheduleModel model = (ScheduleModel) vAdapter.getItem(newPosition);
                String[] scheduleIDs = new String[]{String.valueOf(model.getScheduleID())};

                Intent intent = new Intent();
                intent.setClass(ScheduleListActivity.this, ScheduleSingleDetailActivity.class);
                intent.putExtra("scheduleID", scheduleIDs);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * @param v
     */

    public void forBack(View v) {
        this.finish();
    }
}
