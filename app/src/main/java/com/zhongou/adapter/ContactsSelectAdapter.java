package com.zhongou.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.model.ContactsEmployeeModel;

import java.util.HashMap;
import java.util.List;

/**
 * 转交功能/审批人 通讯录 适配
 */
public class ContactsSelectAdapter extends BaseAdapter {

	private List<ContactsEmployeeModel> list = null;
	private Context mContext;
	private static HashMap<Integer, Boolean> isSelectedMap;//用来控制CheckBox的选中状况

	public ContactsSelectAdapter(Context mContext, List<ContactsEmployeeModel> list) {
		this.mContext = mContext;
		this.list = list;
		isSelectedMap = new HashMap<Integer, Boolean>();

		initSelectedData();
	}

	/**
	 * 初始化isSelected状态,设置为未选中状态
	 */
	private void initSelectedData() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelectedMap().put(i, false);
		}
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 *
	 * @param list
	 */
	public void updateListView(List<ContactsEmployeeModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup arg2) {
		ContactsSelectAdapter.MyViewHolder MyViewHolder = null;
		final ContactsEmployeeModel model = list.get(position);

		//判断是不是第一次进入
		if (convertView == null) {
			MyViewHolder = new ContactsSelectAdapter.MyViewHolder();
			//导入布局
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_select, null);
			MyViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_name);
			MyViewHolder.tvLetter = (TextView) convertView.findViewById(R.id.tv_letter);
			MyViewHolder.selectCheck = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(MyViewHolder);//设置view标签，以后就可以重复使用
		} else {
			//获取MyViewHolder
			MyViewHolder = (ContactsSelectAdapter.MyViewHolder) convertView.getTag();
		}


		// 设置list中TextView的显示
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			MyViewHolder.tvLetter.setVisibility(View.VISIBLE);
			MyViewHolder.tvLetter.setText(model.getFirstLetter());
		} else {
			MyViewHolder.tvLetter.setVisibility(View.GONE);
		}
		MyViewHolder.tvTitle.setText(model.getsEmployeeName());

		// 根据isSelected来设置checkbox的选中状况
		MyViewHolder.selectCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//保存状态
				isSelectedMap.put(position, isChecked);
				setIsSelectedMap(isSelectedMap);
				Log.d("SJY", "item=" + model.getsEmployeeName() + isChecked);
			}
		});

		return convertView;

	}

	public static class MyViewHolder {
		public TextView tvLetter;
		public TextView tvTitle;
		public CheckBox selectCheck;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getFirstLetter().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getFirstLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	//checkbox
	public static HashMap<Integer, Boolean> getIsSelectedMap() {
		return isSelectedMap;
	}

	public static void setIsSelectedMap(HashMap<Integer, Boolean> isSelected) {
		ContactsSelectAdapter.isSelectedMap = isSelected;
	}
}