package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2017/1/21.
 */

public class PositionReplaceModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String ApplicationTitle;//
    public String TransferDate;//
    public String TransferReason;//
    public String OriDepartmentName;//
    public String OriPostName;//
    public String NewDepartmentName;//
    public String NewPostName;//
    public String HandlerEmployeeName;//
    public String Remark;//


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<RecruitmentModel.ApprovalInfoLists> ApprovalInfoLists;

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

    public List<RecruitmentModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<RecruitmentModel.ApprovalInfoLists> ApprovalInfoLists) {
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

    public String getApplicationTitle() {
        return ApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        ApplicationTitle = applicationTitle;
    }

    public String getTransferDate() {
        return TransferDate;
    }

    public void setTransferDate(String transferDate) {
        TransferDate = transferDate;
    }

    public String getTransferReason() {
        return TransferReason;
    }

    public void setTransferReason(String transferReason) {
        TransferReason = transferReason;
    }

    public String getOriDepartmentName() {
        return OriDepartmentName;
    }

    public void setOriDepartmentName(String oriDepartmentName) {
        OriDepartmentName = oriDepartmentName;
    }

    public String getOriPostName() {
        return OriPostName;
    }

    public void setOriPostName(String oriPostName) {
        OriPostName = oriPostName;
    }

    public String getNewDepartmentName() {
        return NewDepartmentName;
    }

    public void setNewDepartmentName(String newDepartmentName) {
        NewDepartmentName = newDepartmentName;
    }

    public String getNewPostName() {
        return NewPostName;
    }

    public void setNewPostName(String newPostName) {
        NewPostName = newPostName;
    }

    public String getHandlerEmployeeName() {
        return HandlerEmployeeName;
    }

    public void setHandlerEmployeeName(String handlerEmployeeName) {
        HandlerEmployeeName = handlerEmployeeName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
