package com.zhongou.common;

import com.zhongou.model.ContactsEmployeeModel;

import java.util.Comparator;

/**
 * 通讯录使用
 *
 *
 */
public class PinyinComparator implements Comparator<ContactsEmployeeModel> {

	public int compare(ContactsEmployeeModel o1, ContactsEmployeeModel o2) {
		if (o1.getFirstLetter().equals("@") || o2.getFirstLetter().equals("#")) {
			return -1;
		} else if (o1.getFirstLetter().equals("#")
				|| o2.getFirstLetter().equals("@")) {
			return 1;
		} else {
			return o1.getFirstLetter().compareTo(o2.getFirstLetter());
		}
	}

}
