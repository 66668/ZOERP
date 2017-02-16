package com.zhongou.model;

import java.io.Serializable;

/**
 * 交车提交时使用，该类还需要转成json样式
 * Created by sjy on 2017/2/15.
 */

public class VehicleReturnPostUseModel implements Serializable {
    private String EmployeeID;//申请人ID
    private String Purpose;//原因
    private String Destination; //目的地
    private String Driver;//驾驶人
    private String PlanBorrowTime;//计划用车时间
    private String PlanReturnTime;//计划交车时间

    private String ActualBorrowTime;//实际用车时间
    private String ActualReturnTime;//实际交车时间
    private String Number;//车牌号
    private String StartMileage;//开始里程数
    private String FinishMileage;//交车里程数
    private String Passenger;//乘车人
    private String BackRemark;//交车备注
    private String Remark;//申请备注

    private String ApplicationType;//
    private String ApplicationID;//

    public String getApplicationType() {
        return ApplicationType;
    }

    public void setApplicationType(String applicationType) {
        ApplicationType = applicationType;
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

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getPlanBorrowTime() {
        return PlanBorrowTime;
    }

    public void setPlanBorrowTime(String planBorrowTime) {
        PlanBorrowTime = planBorrowTime;
    }

    public String getPlanReturnTime() {
        return PlanReturnTime;
    }

    public void setPlanReturnTime(String planReturnTime) {
        PlanReturnTime = planReturnTime;
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

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
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

    public String getPassenger() {
        return Passenger;
    }

    public void setPassenger(String passenger) {
        Passenger = passenger;
    }

    public String getBackRemark() {
        return BackRemark;
    }

    public void setBackRemark(String backRemark) {
        BackRemark = backRemark;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
