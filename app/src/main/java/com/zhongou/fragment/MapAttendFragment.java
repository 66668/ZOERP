package com.zhongou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sjy on 2016/11/30.
 */

public abstract class MapAttendFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public abstract int getDevider();

    public abstract void setDevider(int devider);

    public abstract void initView();


    public abstract void getRefreshData();
    public abstract void getLoadMoreData();


    public abstract void refresh();//刷新

    /**
     * 回到顶部
     */
    public abstract void changeToTop() ;

    public abstract boolean isRefreshing();

    public abstract void setRefreshing(boolean refreshing) ;

    public abstract void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) ;

    public abstract RecyclerView getScrollableView();
}
