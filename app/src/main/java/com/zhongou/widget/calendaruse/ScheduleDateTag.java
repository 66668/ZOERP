package com.zhongou.widget.calendaruse;

/**
 * 需要标记的日程日期
 * @author jack_peng
 *
 */
public class ScheduleDateTag {

	private int tagID;
	private int scheduleTypeID;
	private int remindID;
	private int month;
	private int year;
	private int day;
	private int scheduleID;


	public ScheduleDateTag() {

	}


	public ScheduleDateTag(int tagID, int year, int month, int day, int scheduleID) {

		this.tagID = tagID;
		this.month = month;
		this.year = year;
		this.day = day;
		this.scheduleID = scheduleID;
	}


	public int getTagID() {
		return tagID;
	}


	public void setTagID(int tagID) {
		this.tagID = tagID;
	}


	public int getScheduleTypeID() {
		return scheduleTypeID;
	}


	public void setScheduleTypeID(int scheduleTypeID) {
		this.scheduleTypeID = scheduleTypeID;
	}


	public int getRemindID() {
		return remindID;
	}


	public void setRemindID(int remindID) {
		this.remindID = remindID;
	}


	public int getMonth() {
		return month;
	}


	public void setMonth(int month) {
		this.month = month;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}


	public int getDay() {
		return day;
	}


	public void setDay(int day) {
		this.day = day;
	}


	public int getScheduleID() {
		return scheduleID;
	}


	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}

}
