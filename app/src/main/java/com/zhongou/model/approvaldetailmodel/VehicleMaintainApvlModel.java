package com.zhongou.model.approvaldetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2016/12/26.
 */

public class VehicleMaintainApvlModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Purpose= "";
    public String MaintenanceType= "";
    public String PlanBorrowTime= "";
    public String MaintenanceProject= "";
    public String VehicleState= "";
    public String Destination= "";

    public String ApprovalStatus;
    public String EmployeeName;
    public String StoreName;
    public String DepartmentName;
    public String ApplicationCreateTime;

    public List<VehicleMaintainApvlModel.ApprovalInfoLists> ApprovalInfoLists ;


    public String EmployeeID;
    public String EstimateFee;
    public String Number;
    public String Driver;
    public String StartMileage;
    public String FinishMileage;
    public String ActualBorrowTime;
    public String ActualReturnTime;
    public String Passenger;
    public String CreateTime;

    public static class ApprovalInfoLists{
        public String Comment = "";
        public String ApprovalDate = "";
        public String YesOrNo= "";
        public String ApprovalEmployeeName= "";

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

    public List<VehicleMaintainApvlModel.ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<VehicleMaintainApvlModel.ApprovalInfoLists> approvalInfoLists) {
        this.ApprovalInfoLists = approvalInfoLists;
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

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
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

    public String getApplicationCreateTime() {
        return ApplicationCreateTime;
    }

    public void setApplicationCreateTime(String applicationCreateTime) {
        ApplicationCreateTime = applicationCreateTime;
    }

    public String getPlanBorrowTime() {
        return PlanBorrowTime;
    }

    public void setPlanBorrowTime(String planBorrowTime) {
        PlanBorrowTime = planBorrowTime;
    }


    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getMaintenanceType() {
        return MaintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        MaintenanceType = maintenanceType;
    }

    public String getMaintenanceProject() {
        return MaintenanceProject;
    }

    public void setMaintenanceProject(String maintenanceProject) {
        MaintenanceProject = maintenanceProject;
    }

    public String getVehicleState() {
        return VehicleState;
    }

    public void setVehicleState(String vehicleState) {
        VehicleState = vehicleState;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getEstimateFee() {
        return EstimateFee;
    }

    public void setEstimateFee(String estimateFee) {
        EstimateFee = estimateFee;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
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

    public String getActualBorrowTime() {
        return ActualBorrowTime;
    }

    public void setActualBorrowTime(String actualBorrowTime) {
        ActualBorrowTime = actualBorrowTime;
    }

    public String getActualReturnTime() {
        return ActualReturnTime;
    }

    public void setActualReturnTime(String actualReturnTime) {
        ActualReturnTime = actualReturnTime;
    }

    public String getPassenger() {
        return Passenger;
    }

    public void setPassenger(String passenger) {
        Passenger = passenger;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
