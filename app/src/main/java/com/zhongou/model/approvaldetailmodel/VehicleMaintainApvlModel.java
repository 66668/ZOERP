package com.zhongou.model.approvaldetailmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjy on 2016/12/26.
 */

public class VehicleMaintainApvlModel implements Serializable {
    private static final long serialVersionUID = 1L;
    public String MaintenanceType = "";//维保类型
    public String Purpose = "";//用途
    public String PlanBorrowTime = "";//维保时间
    public String MaintenanceProject = "";//维保项目
    public String VehicleState = "";//车辆状态
    public String Destination = "";

    public String CopyTime;//抄送时间
    public String ApprovalStatus;
    public String EmployeeName;//抄送人
    public String StoreName;
    public String DepartmentName;
    public String ApplicationCreateTime;

    public List<ApprovalInfoLists> ApprovalInfoLists;


    public String EmployeeID;//申请人ID
    public String EstimateFee;//费用
    public String Number;//车牌号
    public String Driver;//驾驶人
    public String StartMileage;//开始里程
    public String FinishMileage;//结束里程
    public String ActualBorrowTime;//实际维保时间
    public String ActualReturnTime;
    public String Passenger;//乘车人
    public String CreateTime;//申请时间

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

    public static class ApprovalInfoLists implements Serializable {
        public String Comment = "";
        public String ApprovalDate = "";
        public String YesOrNo = "";
        public String ApprovalEmployeeName = "";

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

    public List<ApprovalInfoLists> getApprovalInfoLists() {
        return ApprovalInfoLists;
    }

    public void setApprovalInfoLists(List<ApprovalInfoLists> approvalInfoLists) {
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

    public String getCopyTime() {
        return CopyTime;
    }

    public void setCopyTime(String copyTime) {
        CopyTime = copyTime;
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
