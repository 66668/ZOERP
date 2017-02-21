package com.zhongou.model.copydetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2017/1/21.
 */

public class RetestCopyModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Reexpeople;//
    public String Remark;//



    public String ApprovalStatus;
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;

    public List<RetestCopyModel.ApprovalInfoLists> ApprovalInfoLists;

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

    public List<RetestCopyModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<RetestCopyModel.ApprovalInfoLists> ApprovalInfoLists) {
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


    public String getRemark() {
        return Remark;
    }

    public String getReexpeople() {
        return Reexpeople;
    }

    public void setReexpeople(String reexpeople) {
        Reexpeople = reexpeople;
    }
}
