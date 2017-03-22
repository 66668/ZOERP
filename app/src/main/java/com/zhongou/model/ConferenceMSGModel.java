package com.zhongou.model;

import java.io.Serializable;

/**
 * 消息界面使用的会议modelS
 */

public class ConferenceMSGModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String EmployeeName; //发布人
    private String PublishDeptName;//发布部门
    private String PublishTime;//发布时间
    private String ConferenceName;//会议名称
    private String ApplicationTitle;//标题
    private String Abstract;//内容
    private String Title; //主题
    private String StartTime;//开始时间
    private String Remark;//备注;
    private String IsRead;
    private String ApplicationID;

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getPublishDeptName() {
        return PublishDeptName;
    }

    public void setPublishDeptName(String publishDeptName) {
        PublishDeptName = publishDeptName;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }

    public String getConferenceName() {
        return ConferenceName;
    }

    public void setConferenceName(String conferenceName) {
        ConferenceName = conferenceName;
    }

    public String getApplicationTitle() {
        return ApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        ApplicationTitle = applicationTitle;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
}
