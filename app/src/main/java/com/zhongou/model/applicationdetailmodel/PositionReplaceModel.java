package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2017/1/21.
 */

public class PositionReplaceModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String ApplicationTitle;//标题
    public String TransferEmployeeName;//调动人员
    public String TransferEmployeeID;//调动人员
    public String TransferDate;//调动日期
    public String TransferReason;//调动原由

    public String OriDepartmentName;//原部门
    public String OriDepartmentID;//原部门
    public String OriPostName;//原岗位
    public String OriPostID;//原岗位

    public String NewDepartmentName;//新部门
    public String NewDepartmentID;//新部门
    public String NewPostName;//新岗位
    public String NewPostID;//新岗位

    public String HandlerEmployeeName;//决定人
    public String Remark;//
    public String Reason;//
    public String CreateTime;//


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<PositionReplaceModel.ApprovalInfoLists> ApprovalInfoLists;


    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getTransferEmployeeName() {
        return TransferEmployeeName;
    }

    public void setTransferEmployeeName(String transferEmployeeName) {
        TransferEmployeeName = transferEmployeeName;
    }

    public String getTransferEmployeeID() {
        return TransferEmployeeID;
    }

    public void setTransferEmployeeID(String transferEmployeeID) {
        TransferEmployeeID = transferEmployeeID;
    }

    public String getOriDepartmentID() {
        return OriDepartmentID;
    }

    public void setOriDepartmentID(String oriDepartmentID) {
        OriDepartmentID = oriDepartmentID;
    }

    public String getOriPostID() {
        return OriPostID;
    }

    public void setOriPostID(String oriPostID) {
        OriPostID = oriPostID;
    }

    public String getNewDepartmentID() {
        return NewDepartmentID;
    }

    public void setNewDepartmentID(String newDepartmentID) {
        NewDepartmentID = newDepartmentID;
    }

    public String getNewPostID() {
        return NewPostID;
    }

    public void setNewPostID(String newPostID) {
        NewPostID = newPostID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
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

    public List<PositionReplaceModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<PositionReplaceModel.ApprovalInfoLists> ApprovalInfoLists) {
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
