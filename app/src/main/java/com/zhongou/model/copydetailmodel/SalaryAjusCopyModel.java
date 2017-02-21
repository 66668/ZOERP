package com.zhongou.model.copydetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 调薪
 * Created by sjy on 2016/12/26.
 */

public class SalaryAjusCopyModel implements Serializable {
    private static final long serialVersionUID = 1L;


    public String TargetEmployee;//
    public String OriSalary;//
    public String SrcSalary;//
    public String Reason;//


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<SalaryAjusCopyModel.ApprovalInfoLists> ApprovalInfoLists;


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

    public List<SalaryAjusCopyModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<SalaryAjusCopyModel.ApprovalInfoLists> ApprovalInfoLists) {
        this.ApprovalInfoLists = ApprovalInfoLists;
    }

    public String getTargetEmployee() {
        return TargetEmployee;
    }

    public void setTargetEmployee(String targetEmployee) {
        TargetEmployee = targetEmployee;
    }

    public String getOriSalary() {
        return OriSalary;
    }

    public void setOriSalary(String oriSalary) {
        OriSalary = oriSalary;
    }

    public String getSrcSalary() {
        return SrcSalary;
    }

    public void setSrcSalary(String srcSalary) {
        SrcSalary = srcSalary;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
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
