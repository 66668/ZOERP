package com.zhongou.model.applicationdetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2016/12/26.
 */

public class VehicleMaintainModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String EstimateFee;//
    public String StartMileage;//
    public String FinishMileage;//
    public String Number;//
    public String Purpose;//
    public String MaintenanceType;//
    public String PlanBorrowTime;//维保时间
    public String MaintenanceProject;//
    public String VehicleState;//车辆状态
    public String Destination;//维保地点


    public String ApprovalStatus;//状态
    public String StoreName;
    public String DepartmentName;
    public String EmployeeName;
    public String ApplicationCreateTime;
    public List<VehicleMaintainModel.ApprovalInfoLists> ApprovalInfoLists;

    public String Remark;//申请备注
    public String BackRemark;//交车备注

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getBackRemark() {
        return BackRemark;
    }

    public void setBackRemark(String backRemark) {
        BackRemark = backRemark;
    }

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

    public List<VehicleMaintainModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<VehicleMaintainModel.ApprovalInfoLists> ApprovalInfoLists) {
        this.ApprovalInfoLists = ApprovalInfoLists;
    }

    public String StoreID;

    public String getMaintenanceType() {
        return MaintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        MaintenanceType = maintenanceType;
    }

    public String getEstimateFee() {
        return EstimateFee;
    }

    public void setEstimateFee(String estimateFee) {
        EstimateFee = estimateFee;
    }

    public String getPlanBorrowTime() {
        return PlanBorrowTime;
    }

    public void setPlanBorrowTime(String planBorrowTime) {
        PlanBorrowTime = planBorrowTime;
    }

    public String getMaintenanceProject() {
        return MaintenanceProject;
    }

    public void setMaintenanceProject(String maintenanceProject) {
        MaintenanceProject = maintenanceProject;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getVehicleState() {
        return VehicleState;
    }

    public void setVehicleState(String vehicleState) {
        VehicleState = vehicleState;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getStartMileage() {
        return StartMileage;
    }

    public void setStartMileage(String startMileage) {
        StartMileage = startMileage;
    }

    public String getFinishMileage() {
        return FinishMileage;
    }

    public void setFinishMileage(String finishMileage) {
        FinishMileage = finishMileage;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
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
