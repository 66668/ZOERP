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
import com.zhongou.adapter.ProcueRecordListAdapter;
import com.zhongou.adapter.ProcuerecordAdapter;
import com.zhongou.base.BaseActivity;
import com.zhongou.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 采购领用
 * Created by sjy on 2016/12/13.
 */

public class ProcueRecordActivity extends BaseActivity {
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
    private View view1;//绩效成绩
    private View view2;//业绩评定
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    private List<String> dataList;//数据集合
    private  ProcuerecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_procuerecord);
        tv_title.setText(getResources().getString(R.string.app_procueRecord));
        tv_right.setText("");

        initMyView();
        getData();
        show();
    }

    private void initMyView() {
        tabLayout = (TabLayout) findViewById(R.id.procuerecordTabs);
        viewPager = (ViewPager) findViewById(R.id.procuerecordViewpager);

        //页面详细布局
        layoutInflater = LayoutInflater.from(this);
        view1 = layoutInflater.inflate(R.layout.act_apps_procuerecord_frg01, null);
        view2 = layoutInflater.inflate(R.layout.act_apps_procuerecord_frg02, null);

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);

        //添加页卡标题
        tabTitleList.add(getResources().getString(R.string.mg_procuerecord01));//采购记录
        tabTitleList.add(getResources().getString(R.string.mg_procuerecord02));//领用记录

        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        //添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(tabTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitleList.get(1)));
    }

    private void getData() {

        //假数据，你替换一下你自己的
        dataList = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(100) + 31; i++) {
            dataList.add(String.valueOf(i));
        }

    }

    private void show() {

        adapter = new ProcuerecordAdapter(mViewList, tabTitleList);
        viewPager.setAdapter(adapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);


        ListView listview = (ListView) view1.findViewById(R.id.listview01);
        listview.setAdapter(new ProcueRecordListAdapter(this, dataList));
        ListView listview1 = (ListView) view2.findViewById(R.id.listview02);
        listview1.setAdapter(new ProcueRecordListAdapter(this, dataList));
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
