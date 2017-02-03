package com.zhongou.model.approvaldetailmodel;

import java.io.Serializable;
import java.util.List;

/**审批详情
 *
 * 招聘
 * Created by sjy on 2016/12/26.
 */

public class RecruitmentApvlModel implements Serializable {
    private static final long serialVersionUID = 1L;


    public String ApprovalStatus;
    public String EmployeeName;
    public String StoreName;
    public String DepartmentName;
    public String ApplicationCreateTime;

    public String Position;
    public String NumberOfPeople;
    public String ExpectedEntryDate;
    public String Responsibility;
    public List<RecruitmentApvlModel.ApprovalInfoLists> ApprovalInfoLists;

    public static class ApprovalInfoLists {
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

    public List<RecruitmentApvlModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<RecruitmentApvlModel.ApprovalInfoLists> ApprovalInfoLists) {
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
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

    public String getExpectedEntryDate() {
        return ExpectedEntryDate;
    }

    public void setExpectedEntryDate(String expectedEntryDate) {
        ExpectedEntryDate = expectedEntryDate;
    }
}
