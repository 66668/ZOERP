package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 应用-审批 我的申请 财务详情model
 * Created by sjy on 2016/12/26.
 */

public class FinancialAllModel implements Serializable {
    private static final long serialVersionUID = 1L;


    public String EmployeeID;
    public String ApplicationID;
    public String ApplicationTitle;
    public String Types;
    public String Way;
    public String Fee;
    public String Useage;
    public String Useageone;
    public String Feeone;

    public String Useagetwo;
    public String Feetwo;

    public String Useagethree;
    public String Feethree;

    public String Total;

    public String PlanbackTime;//还款时间
    public String CollectionUnit;//收款单位
    public String BankAccount;
    public String AccountNumber;
    public String Remark;
    public String Reason;//借款事由

    public String LastComment;//LastComment
    public String ActiveFlg;//ActiveFlg
    public String LastUpdateTime;//LastUpdateTime
    public String OperatorName;//OperatorName
    public String CreateTime;//CreateTime
    public String StoreID;//StoreID


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;
    public List<String> ImageLists;//图片路径集合;
    public List<FinancialAllModel.ApprovalInfoLists> ApprovalInfoLists;

    public List<String> getImageLists() {
        return ImageLists;
    }

    public void setImageLists(List<String> imageLists) {
        ImageLists = imageLists;
    }

    public String getApplicationTitle() {
        return ApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        ApplicationTitle = applicationTitle;
    }

    public String getLastComment() {
        return LastComment;
    }

    public void setLastComment(String lastComment) {
        LastComment = lastComment;
    }

    public String getActiveFlg() {
        return ActiveFlg;
    }

    public void setActiveFlg(String activeFlg) {
        ActiveFlg = activeFlg;
    }

    public String getLastUpdateTime() {
        return LastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        LastUpdateTime = lastUpdateTime;
    }

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
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

    public static class ApprovalInfoLists implements Serializable {
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

    public List<FinancialAllModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<FinancialAllModel.ApprovalInfoLists> ApprovalInfoLists) {
        this.ApprovalInfoLists = ApprovalInfoLists;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getTypes() {
        return Types;
    }

    public void setTypes(String Types) {
        Types = Types;
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

    public String getUseageone() {
        return Useageone;
    }

    public void setUseageone(String useageone) {
        Useageone = useageone;
    }

    public String getFeeone() {
        return Feeone;
    }

    public void setFeeone(String feeone) {
        Feeone = feeone;
    }

    public String getUseagetwo() {
        return Useagetwo;
    }

    public void setUseagetwo(String useagetwo) {
        Useagetwo = useagetwo;
    }

    public String getFeetwo() {
        return Feetwo;
    }

    public void setFeetwo(String feetwo) {
        Feetwo = feetwo;
    }

    public String getUseagethree() {
        return Useagethree;
    }

    public void setUseagethree(String useagethree) {
        Useagethree = useagethree;
    }

    public String getFeethree() {
        return Feethree;
    }

    public void setFeethree(String feethree) {
        Feethree = feethree;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getCollectionUnit() {
        return CollectionUnit;
    }

    public void setCollectionUnit(String collectionUnit) {
        CollectionUnit = collectionUnit;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
