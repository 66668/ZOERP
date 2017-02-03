package com.zhongou.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 业绩管理适配器
 * Created by sjy on 2017/1/9.
 */

public class PerformanceManagerAdapter extends PagerAdapter {
    private List<View> mViewList;
    List<String> tabTitileList;

    public PerformanceManagerAdapter(List<View> mViewList,List<String> tabTitileList){
        this.mViewList = mViewList;
        this.tabTitileList = tabTitileList;
    }

    @Override
    public int getCount() {
        return mViewList.size();//页卡数
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//官方推荐写法;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));//添加页卡
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));//删除页卡
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitileList.get(position);//页卡标题
    }

}
