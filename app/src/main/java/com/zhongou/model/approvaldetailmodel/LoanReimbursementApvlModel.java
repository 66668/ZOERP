package com.zhongou.model.approvaldetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2016/12/26.
 */

public class LoanReimbursementApvlModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Type;
    public String Way;
    public String Fee;
    public String Useage;

    public String PlanbackTime;
    public String Remark;
    public String AdminName;
    public String BankAccount;
    public String AccountNumber;

    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<LoanReimbursementApvlModel.ApprovalInfoLists> ApprovalInfoLists;


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

    public List<LoanReimbursementApvlModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<LoanReimbursementApvlModel.ApprovalInfoLists> ApprovalInfoLists) {
        this.ApprovalInfoLists = ApprovalInfoLists;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getWay() {
        return Way;
    }

    public void setWay(String way) {
        Way = way;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }


    public String getUseage() {
        return Useage;
    }

    public void setUseage(String useage) {
        Useage = useage;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPlanbackTime() {
        return PlanbackTime;
    }

    public void setPlanbackTime(String planbackTime) {
        PlanbackTime = planbackTime;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
    }

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
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
