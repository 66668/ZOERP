package com.zhongou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongou.R;

import java.util.List;

/**
 * 业绩管理适配
 */
public class PerformanceManagerListAdapter extends BaseAdapter {
    Context mContext;
    List<String> list;
    public PerformanceManagerListAdapter(Context mContext, List<String> list){
        this.mContext = mContext;
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_performacemanager, null, false);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tv_result = (TextView) view.findViewById(R.id.tv_result);
            viewHolder.imageView = (ImageView)view.findViewById(R.id.img_icon);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tv_title.setText("标题："+list.get(i));
        viewHolder.tv_result.setText("结果显示："+list.get(i));
        return view;
    }

    class ViewHolder{
        TextView tv_title;
        TextView tv_result;
        ImageView imageView;
    }
}
