package com.zhongou.model;

import java.io.Serializable;

/**
 * Created by sjy on 2016/11/29.
 */

public class MapAttendModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String attendID;
    private String EmployeeID;
    private String DepartmentID;
    private String attendUserName;
    private String attendCardNo;
    private String attendCapTime;
    private String address = "没有获取地址";
    private String OperatorName;
    private String StoreID;

    public String getAttendID() {
        return attendID;
    }

    public void setAttendID(String attendID) {
        this.attendID = attendID;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getAttendUserName() {
        return attendUserName;
    }

    public void setAttendUserName(String attendUserName) {
        this.attendUserName = attendUserName;
    }

    public String getAttendCardNo() {
        return attendCardNo;
    }

    public void setAttendCardNo(String attendCardNo) {
        this.attendCardNo = attendCardNo;
    }

    public String getAttendCapTime() {
        return attendCapTime;
    }

    public void setAttendCapTime(String attendCapTime) {
        this.attendCapTime = attendCapTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
