package com.zhongou.helper;

import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;

import com.zhongou.inject.EventListener;
import com.zhongou.inject.Select;
import com.zhongou.inject.ViewInject;
import com.zhongou.base.BaseActivity.Method;

import java.lang.reflect.Field;


/**
 * 反射机制，BaseActivity中调用
 * 
 * @author JackSong
 *
 */
public abstract class InjectHelper {
	Object mObject;

	public InjectHelper(Object obj) {
		this.mObject = obj;
	}

	// 抽象方法
	protected abstract View findViewById(int id);

	// BaseActivity--InjectHelper中调用
	/**
	 * 注解处理器
	 */
	public void initView() {
		Field[] fields = mObject.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				try {
					field.setAccessible(true);

					if (field.get(mObject) != null) 
						continue;
					
					// 创建自定义注解
					ViewInject viewInject = field.getAnnotation(ViewInject.class);
					if (viewInject != null) {
						int viewId = viewInject.id();
						field.set(mObject, findViewById(viewId));
						// 01
						setListener(field, viewInject.click(), Method.Click);
						// 02
						setListener(field, viewInject.longClick(), Method.LongClick);
						// 03
						setListener(field, viewInject.itemClick(), Method.ItemClick);
						// 04
						setListener(field, viewInject.itemLongClick(), Method.ItemLongClick);

						Select select = viewInject.select();
						if (!TextUtils.isEmpty(select.selected())) {
							// 05
							setViewSelectListener(field, select.selected(),select.noSelected());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 01 02 03 04
	private void setListener(Field field, String methodName, Method method) throws Exception {
		if (methodName == null || methodName.trim().length() == 0) {
			return;
		}

		Object obj = field.get(mObject);
		switch (method) {
		case Click:// 01
			if (obj instanceof View) {
				// 需要创建EventListener类
				((View) obj).setOnClickListener(new EventListener(mObject).click(methodName));
			}
			break;
		case LongClick:// 02
			if (obj instanceof View) {
				((View) obj).setOnLongClickListener(new EventListener(mObject).longClick(methodName));
			}
			break;
		case ItemClick:// 03
			if (obj instanceof AbsListView) {
				((AbsListView) obj).setOnItemClickListener(new EventListener(mObject).itemClick(methodName));
			}
			break;
		case ItemLongClick:// 04
			if (obj instanceof AbsListView) {
				((AbsListView) obj).setOnItemLongClickListener(new EventListener(mObject).itemLongClick(methodName));
			}
			break;
		default:
			break;
		}
	}

	// 05
	private void setViewSelectListener(Field field, String select, String noSelect) throws Exception {
		
		Object obj = field.get(mObject);
		if (obj instanceof View) {
			((AbsListView) obj).setOnItemSelectedListener(new EventListener(mObject).select(select).noSelect(noSelect));
		}
	}
}
