package com.zhongou.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * 应用 viewpaper适配
 * Created by nick on 15/10/22.
 */

public class AppsViewPagerAdapter extends PagerAdapter implements IconPagerAdapter {
    private List<GridView> array;

    /**
     * 供外部调用（new）的方法
     *
     * @param context
     *            上下文

     */
    public AppsViewPagerAdapter(Context context, List<GridView> array) {
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        View view = array.get(arg1);
        ((ViewPager) arg0).addView(array.get(arg1));
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

}