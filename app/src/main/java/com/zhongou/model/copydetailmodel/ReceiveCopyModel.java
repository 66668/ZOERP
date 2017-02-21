package com.zhongou.model.copydetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 领用
 * Created by sjy on 2017/1/21.
 */

public class ReceiveCopyModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Name;//领用物品名称
    public String Specification;//规格
    public String Versions;//型号
    public String Amount;//数量
    public String Remark;//
    public String Reason;//


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<ReceiveCopyModel.ApprovalInfoLists> ApprovalInfoLists;

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

    public List<ReceiveCopyModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<ReceiveCopyModel.ApprovalInfoLists> ApprovalInfoLists) {
        this.ApprovalInfoLists = ApprovalInfoLists;
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


    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpecification() {
        return Specification;
    }

    public void setSpecification(String specification) {
        Specification = specification;
    }

    public String getVersions() {
        return Versions;
    }

    public void setVersions(String versions) {
        Versions = versions;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getRemark() {
        return Remark;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
