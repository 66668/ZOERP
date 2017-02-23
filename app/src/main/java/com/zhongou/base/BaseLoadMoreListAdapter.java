package com.zhongou.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 *该adapter设置了loadMore加载接口，和自定义widget 的RefreshListView onRefresh刷新接口， 完成了 上拉下拉效果
 *
 * 但本app设计了另一种MyRefreshListView,自动触发上拉下拉，BaseListAdapter只处理数据，见详情
 */
public abstract class BaseLoadMoreListAdapter extends BaseAdapter{

	public Context context;
	public LayoutInflater inflater;
	public AdapterCallBack callBack;
	public ArrayList entityList = new ArrayList();
	public boolean IsEnd=false;//翻页设置
	public static ArrayList<Boolean> isCheckedList = null;//用于标记checkBox值

	public BaseLoadMoreListAdapter(Context context, AdapterCallBack callBack){
		this.context = context;
		this.callBack = callBack;
		inflater = LayoutInflater.from(context);
	}
	
	public interface AdapterCallBack{
		void loadMore();
	}
	
	@Override
	public int getCount() {
		if(entityList != null){
			return entityList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return entityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	} 
	
	public ArrayList getEntityList() {
		return entityList;
	}

	//listView赋值
	public void setEntityList(ArrayList entityList) {
//		this.entityList = entityList;//
		this.entityList.clear();
		this.entityList.addAll(entityList);
		notifyDataSetChanged();
	}

	//listView拼接
	public void addEntityList(ArrayList entityList) { 
		this.entityList.addAll(entityList);
		notifyDataSetChanged();
	}

	//lisetView插入
	public void insertEntityList(ArrayList entityList){
		if(entityList != null){
			this.entityList.addAll(0,entityList);
		}
		notifyDataSetChanged();

	}

    public void addEntity(Object entity){
        this.entityList.add(entity);
        notifyDataSetChanged();
    }
	
	protected abstract View inflateConvertView();
	
	protected abstract void initViewData(int position, View convertView);

	//一条条详细数据处理
	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 
		if(convertView == null) {
			convertView = inflateConvertView();
		}
		initViewData(position, convertView);
		//加载数据
		if(position == getCount() - 1  && !IsEnd){//当数据最后一条，加载
			callBack.loadMore();
		} 
		return convertView;
	}

}
