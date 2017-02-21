package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 采购申请详情
 * Created by sjy on 2017/1/21.
 */

public class ProcurementModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String ItemName;//物品名称
    public String ItemType;//物品类型
    public String Specification;//规格
    public String Versions;//型号
    public String Remark;//备注
    public String CreateTime;//申请时间
    public String Reason;//置购原因
    public String EstimateFee;//预计金额
    public String Amount;//数量
    public String PlanTime;//计划时间
    public String Buyer;//购买人


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    //
    public String ApplicationID;//申请ID
    public String EmployeeID;//EmployeeID
    public String LastComment;//最新批示
    public String LastUpdateTime;//最后修改时间
    public String OperatorName;//操作人
    public String StoreID;//StoreID

    public List<ProcurementModel.ApprovalInfoLists> ApprovalInfoLists;

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

    public List<ProcurementModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<ProcurementModel.ApprovalInfoLists> ApprovalInfoLists) {
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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getEstimateFee() {
        return EstimateFee;
    }

    public void setEstimateFee(String estimateFee) {
        EstimateFee = estimateFee;
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

    public String getPlanTime() {
        return PlanTime;
    }

    public void setPlanTime(String planTime) {
        PlanTime = planTime;
    }

    public String getBuyer() {
        return Buyer;
    }

    public void setBuyer(String buyer) {
        Buyer = buyer;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getLastComment() {
        return LastComment;
    }

    public void setLastComment(String lastComment) {
        LastComment = lastComment;
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

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }
}
