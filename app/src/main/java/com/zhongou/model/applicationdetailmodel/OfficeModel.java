package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 办公室申请
 * Created by sjy on 2017/1/21.
 */

public class OfficeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Time;//
    public String Remark;//
    public String Useage;//
    public String ApplicationTitle;//
    public String NumParticipant;//


    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<OfficeModel.ApprovalInfoLists> ApprovalInfoLists;

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

    public List<OfficeModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<OfficeModel.ApprovalInfoLists> ApprovalInfoLists) {
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
    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUseage() {
        return Useage;
    }

    public void setUseage(String useage) {
        Useage = useage;
    }

    public String getNumParticipant() {
        return NumParticipant;
    }

    public void setNumParticipant(String numParticipant) {
        NumParticipant = numParticipant;
    }
}
