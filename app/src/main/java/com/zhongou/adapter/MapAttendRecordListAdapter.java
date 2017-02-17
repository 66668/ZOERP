package com.zhongou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongou.R;

import java.util.List;

/**
 * 地图考勤记录 适配
 */
public class MapAttendRecordListAdapter extends BaseAdapter {
    Context mContext;
    List<String> list;
    public MapAttendRecordListAdapter(Context mContext, List<String> list){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_mapattendrecord, null, false);
            viewHolder.tv_local = (TextView) view.findViewById(R.id.tv_local);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tv_local.setText("标题："+list.get(i));
        viewHolder.tv_time.setText("结果显示："+list.get(i));
        return view;
    }

    class ViewHolder{
        TextView tv_local;
        TextView tv_time;
    }
}
