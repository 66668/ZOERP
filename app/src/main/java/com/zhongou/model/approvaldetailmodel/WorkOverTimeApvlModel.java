package com.zhongou.model.approvaldetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 请假
 * Created by sjy on 2016/12/26.
 */

public class WorkOverTimeApvlModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String OverEmployee;
    public String StratOverTime;
    public String EndOverTime;
    public String OverCause;
    public String Remark;
    public String Reason;

    public String ApprovalStatus;
    public String EmployeeName;
    public String StoreName;
    public String DepartmentName;
    public String ApplicationCreateTime;
    public List<WorkOverTimeApvlModel.ApprovalInfoLists> ApprovalInfoLists;

    //未用
    public String ApplicationTitle;
    public String EmployeeID;
    public String CreateTime;

    public String StoreID;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public static class ApprovalInfoLists implements Serializable{
        public String Comment;
        public String ApprovalDate;
        public String YesOrNo;
        public String ApprovalEmployeeName;

        public String getComment() {
            return Comment;
        }

        public void setComment(String comment) {
            Comment = comment;
        }

        public String getApprovalDate() {
            return ApprovalDate;
        }

        public void setApprovalDate(String approvalDate) {
            ApprovalDate = approvalDate;
        }

        public String getYesOrNo() {
            return YesOrNo;
        }

        public void setYesOrNo(String yesOrNo) {
            YesOrNo = yesOrNo;
        }

        public String getApprovalEmployeeName() {
            return ApprovalEmployeeName;
        }

        public void setApprovalEmployeeName(String approvalEmployeeName) {
            ApprovalEmployeeName = approvalEmployeeName;
        }
    }

    public List<WorkOverTimeApvlModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<WorkOverTimeApvlModel.ApprovalInfoLists> approvalInfoLists) {
        this.ApprovalInfoLists = approvalInfoLists;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getApplicationCreateTime() {
        return ApplicationCreateTime;
    }

    public void setApplicationCreateTime(String applicationCreateTime) {
        ApplicationCreateTime = applicationCreateTime;
    }

    public String getApplicationTitle() {
        return ApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        ApplicationTitle = applicationTitle;
    }

    public String getOverEmployee() {
        return OverEmployee;
    }

    public void setOverEmployee(String overEmployee) {
        OverEmployee = overEmployee;
    }

    public String getStratOverTime() {
        return StratOverTime;
    }

    public void setStratOverTime(String stratOverTime) {
        StratOverTime = stratOverTime;
    }

    public String getEndOverTime() {
        return EndOverTime;
    }

    public void setEndOverTime(String endOverTime) {
        EndOverTime = endOverTime;
    }

    public String getOverCause() {
        return OverCause;
    }

    public void setOverCause(String overCause) {
        OverCause = overCause;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }
}
