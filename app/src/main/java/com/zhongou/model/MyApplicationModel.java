package com.zhongou.model;

import java.io.Serializable;


/**
 * Created by sjy on 2016/12/5.
 * 我的申请model
 */

public class MyApplicationModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String Temp;//
    private String ApprovalInfomodel;
    private String ApprovalID;
    private String ApplicationID;
    private String ApplicationTitle;//标题
    private String ApplicationType;
    private String YesOrNo;

    private String EmployeeID;
    private String EmployeeName;
    private String DepartmentID;
    private String DepartmentName;

    private String ApprovalDepartmentID;
    private String ApprovalDepartmentName;
    private String ApprovalEmployeeID;
    private String ApprovalEmployeeName;

    private String ApprovalDate;
    private String ApprovalStatus;
    private String Comment;
    private String ActiveFlg;
    private String CreateTimeForApp;
    private String CreateTime;
    private String StoreID;

    public String getYesOrNo() {
        return YesOrNo;
    }

    public void setYesOrNo(String yesOrNo) {
        YesOrNo = yesOrNo;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getApprovalDepartmentID() {
        return ApprovalDepartmentID;
    }

    public void setApprovalDepartmentID(String approvalDepartmentID) {
        ApprovalDepartmentID = approvalDepartmentID;
    }

    public String getApprovalDepartmentName() {
        return ApprovalDepartmentName;
    }

    public void setApprovalDepartmentName(String approvalDepartmentName) {
        ApprovalDepartmentName = approvalDepartmentName;
    }

    public String getApprovalEmployeeID() {
        return ApprovalEmployeeID;
    }

    public void setApprovalEmployeeID(String approvalEmployeeID) {
        ApprovalEmployeeID = approvalEmployeeID;
    }

    public String getApprovalEmployeeName() {
        return ApprovalEmployeeName;
    }

    public void setApprovalEmployeeName(String approvalEmployeeName) {
        ApprovalEmployeeName = approvalEmployeeName;
    }

    public String getApprovalDate() {
        return ApprovalDate;
    }

    public void setApprovalDate(String approvalDate) {
        ApprovalDate = approvalDate;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getActiveFlg() {
        return ActiveFlg;
    }

    public void setActiveFlg(String activeFlg) {
        ActiveFlg = activeFlg;
    }

    public String getCreateTimeForApp() {
        return CreateTimeForApp;
    }

    public void setCreateTimeForApp(String createTimeForApp) {
        CreateTimeForApp = createTimeForApp;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }


    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public String getApprovalID() {
        return ApprovalID;
    }

    public void setApprovalID(String approvalID) {
        ApprovalID = approvalID;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getApplicationTitle() {
        return ApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        ApplicationTitle = applicationTitle;
    }

    public String getApplicationType() {
        return ApplicationType;
    }

    public void setApplicationType(String applicationType) {
        ApplicationType = applicationType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getApprovalInfomodel() {
        return ApprovalInfomodel;
    }

    public void setApprovalInfomodel(String approvalInfomodel) {
        ApprovalInfomodel = approvalInfomodel;
    }
}
