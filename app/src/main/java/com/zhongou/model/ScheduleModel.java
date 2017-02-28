package com.zhongou.model;

import java.io.Serializable;

/**
 * 日程model类
 *
 * @author jack_peng
 */
public class ScheduleModel implements Serializable{
    private static final long serialVersionUID = -1L;

    private int scheduleID;
    private int scheduleTypeID;
    private int remindID;
    private String scheduleContent;
    private String scheduleDate;

    public ScheduleModel() {

    }

    public ScheduleModel(int scheduleID, int scheduleTypeID, int remindID, String scheduleContent, String scheduleDate) {
        this.scheduleID = scheduleID;
        this.scheduleTypeID = scheduleTypeID;
        this.remindID = remindID;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
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

    public String getScheduleContent() {
        return scheduleContent;
    }

    public void setScheduleContent(String scheduleContent) {
        this.scheduleContent = scheduleContent;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

}
