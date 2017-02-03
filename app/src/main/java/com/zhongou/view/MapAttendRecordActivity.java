package com.zhongou.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.adapter.MapAttendRecordListAdapter;
import com.zhongou.adapter.PerformanceManagerAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 考勤记录
 * Created by sjy on 2016/11/28.
 */

public class MapAttendRecordActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LayoutInflater layoutInflater;
    private List<String> tabTitleList = new ArrayList<>();
    private View view1;//
    private View view2;//
    private View view3;//
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private List<String> dataList;//数据集合
    private PerformanceManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_mapattend_record);
        tv_title.setText(getResources().getString(R.string.app_perfermanceManger));
        tv_right.setText("");

        initMyView();
        getData();
        show();
    }

    private void initMyView() {
        tabLayout = (TabLayout) findViewById(R.id.mapRecordTabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //页面详细布局
        layoutInflater = LayoutInflater.from(this);
        view1 = layoutInflater.inflate(R.layout.act_apps_mapattend_record_frg01, null);
        view2 = layoutInflater.inflate(R.layout.act_apps_mapattend_record_frg02, null);
        view3 = layoutInflater.inflate(R.layout.act_apps_mapattend_record_frg03, null);

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);

        //添加页卡标题
        tabTitleList.add(getResources().getString(R.string.mapAttendance_today));//
        tabTitleList.add(getResources().getString(R.string.mapAttendance_weekend));//
        tabTitleList.add(getResources().getString(R.string.mapAttendance_month));//

        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        //添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(tabTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitleList.get(2)));
    }

    private void getData() {

        //假数据，你替换一下你自己的
        dataList = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(100) + 31; i++) {
            dataList.add(String.valueOf(i));
        }

    }

    private void show() {

        adapter = new PerformanceManagerAdapter(mViewList, tabTitleList);
        viewPager.setAdapter(adapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        //本日
        ListView listview1 = (ListView) view1.findViewById(R.id.listview01);
        listview1.setAdapter(new MapAttendRecordListAdapter(this, dataList));

        //本周
        ListView listview2 = (ListView) view2.findViewById(R.id.listview02);
        listview2.setAdapter(new MapAttendRecordListAdapter(this, dataList));

        //本月
        ListView listview3 = (ListView) view3.findViewById(R.id.listview03);
        listview3.setAdapter(new MapAttendRecordListAdapter(this, dataList));
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