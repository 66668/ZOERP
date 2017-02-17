package com.zhongou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.model.ContactsDeptModel;

import java.util.HashMap;
import java.util.List;

/**
 * 抄送 通讯录 适配
 */
public class ContactsCopyToDeptAdapter extends BaseAdapter implements SectionIndexer{

	private List<ContactsDeptModel> list = null;
	private Context mContext;
	private static HashMap<Integer,Boolean> isSelectedMap;//用来控制CheckBox的选中状况

	public ContactsCopyToDeptAdapter(Context mContext, List<ContactsDeptModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 初始化isSelected状态,设置为未选中状态
	 */
	private void initSelectedData(){
		for(int i = 0;i<list.size();i++) {
			getIsSelectedMap().put(i, false);
		}
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<ContactsDeptModel> list){
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
		ViewHolder viewHolder = null;
		final ContactsDeptModel mContent = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			//导入布局
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts, null);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.tv_letter);
			convertView.setTag(viewHolder);//设置view标签
		} else {
			//获取viewHolder
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 设置list中TextView的显示
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getsDeptName());

		return convertView;

	}


	class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
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
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

//	/**
//	 * 控件监听
//	 * @param viewHolder
//	 * @param position
//     */
//	private void initListener(ViewHolder viewHolder, final int position){
//		// 根据isSelected来设置checkbox的选中状况
//		viewHolder.selectCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				//保存状态
//				isSelectedMap.put(position,isChecked);
//				Log.d("SJY", "ContactsSelectAdatper--isChecked=" + isChecked + "--position=" + position);
//				setIsSelectedMap(isSelectedMap);
//			}
//		});
//	}

	//checkbox
	public static HashMap<Integer, Boolean> getIsSelectedMap() {
		return isSelectedMap;
	}

	public static void setIsSelectedMap(HashMap<Integer, Boolean> isSelected) {
		ContactsCopyToDeptAdapter.isSelectedMap = isSelected;
	}
}