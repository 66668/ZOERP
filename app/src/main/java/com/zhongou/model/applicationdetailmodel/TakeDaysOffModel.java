package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2016/12/26.
 */

public class TakeDaysOffModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Reason;//
    public String Remark;//
    public String StartTakeDate;//
    public String EndTakeDate;//
    public String StartOffDate;//
    public String EndOffDate;//


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;
    public List<TakeDaysOffModel.ApprovalInfoLists> ApprovalInfoLists;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public static class ApprovalInfoLists{
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

    public List<TakeDaysOffModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<TakeDaysOffModel.ApprovalInfoLists> ApprovalInfoLists) {
        this.ApprovalInfoLists = ApprovalInfoLists;
    }

    public String getStartTakeDate() {
        return StartTakeDate;
    }

    public void setStartTakeDate(String startTakeDate) {
        StartTakeDate = startTakeDate;
    }

    public String getEndTakeDate() {
        return EndTakeDate;
    }

    public void setEndTakeDate(String endTakeDate) {
        EndTakeDate = endTakeDate;
    }

    public String getStartOffDate() {
        return StartOffDate;
    }

    public void setStartOffDate(String startOffDate) {
        StartOffDate = startOffDate;
    }

    public String getEndOffDate() {
        return EndOffDate;
    }

    public void setEndOffDate(String endOffDate) {
        EndOffDate = endOffDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
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

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getApplicationCreateTime() {
        return ApplicationCreateTime;
    }

    public void setApplicationCreateTime(String applicationCreateTime) {
        ApplicationCreateTime = applicationCreateTime;
    }
}
