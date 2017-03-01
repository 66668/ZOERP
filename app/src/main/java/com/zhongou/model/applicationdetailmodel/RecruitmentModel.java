package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 招聘
 * Created by sjy on 2016/12/26.
 */

public class RecruitmentModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Position;//
    public String NumberOfPeople;//
    public String Responsibility;//
    public String ExpectedEntryDate;//


    public String ApprovalStatus;
    public String StoreName;
    public String Remark;
    public String Reason;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<RecruitmentModel.ApprovalInfoLists> ApprovalInfoLists;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

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

    public List<RecruitmentModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<RecruitmentModel.ApprovalInfoLists> ApprovalInfoLists) {
        this.ApprovalInfoLists = ApprovalInfoLists;
    }
    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getNumberOfPeople() {
        return NumberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        NumberOfPeople = numberOfPeople;
    }

    public String getResponsibility() {
        return Responsibility;
    }

    public void setResponsibility(String responsibility) {
        Responsibility = responsibility;
    }

    public String getExpectedEntryDate() {
        return ExpectedEntryDate;
    }

    public void setExpectedEntryDate(String expectedEntryDate) {
        ExpectedEntryDate = expectedEntryDate;
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
