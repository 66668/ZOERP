package com.zhongou.model;

import java.io.Serializable;

/**
 * 应用-通知使用的model
 * Created by sjy on 2017/2/24.
 */

public class NotificationListModel implements Serializable {

    private static final long serialVersionUID = -1L;

    private String ApplicationID;//
    private String Abstract;//内容
    private String PublishTime;//发布时间
    private String ApplicationTitle;//标题
    private String EmployeeName ;//发布人
    private String PublishDeptName ;

    public String getPublishDeptName() {
        return PublishDeptName;
    }

    public void setPublishDeptName(String publishDeptName) {
        PublishDeptName = publishDeptName;
    }

    private String IsRead ;

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getApplicationTitle() {
        return ApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        ApplicationTitle = applicationTitle;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }
}
