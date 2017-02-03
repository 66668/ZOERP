package com.zhongou.model;

import java.io.Serializable;

/**
 * 获取部门员工信息
 * <p>
 * Created by sjy on 2017/1/6.
 */

public class ContactsEmployeeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sEmployeeID;//员工ID
    private String sPostLevelName;//级别

    private String sEmployeeName;//员工姓名
    private String sEmail;//
    private String sGender;//
    private String sTelephone;//
    private String sEntryDate;//
    private String sDepartmentName;//


    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsGender() {
        return sGender;
    }

    public void setsGender(String sGender) {
        this.sGender = sGender;
    }

    public String getsTelephone() {
        return sTelephone;
    }

    public void setsTelephone(String sTelephone) {
        this.sTelephone = sTelephone;
    }

    public String getsEntryDate() {
        return sEntryDate;
    }

    public void setsEntryDate(String sEntryDate) {
        this.sEntryDate = sEntryDate;
    }

    public String getsDepartmentName() {
        return sDepartmentName;
    }

    public void setsDepartmentName(String sDepartmentName) {
        this.sDepartmentName = sDepartmentName;
    }

    public String getsPostLevelName() {
        return sPostLevelName;
    }

    public void setsPostLevelName(String sPostLevelName) {
        this.sPostLevelName = sPostLevelName;
    }


    private String FirstLetter;//显示数据拼音的首字母，获取服务端数据后，后期处理使用

    public String getFirstLetter() {
        return FirstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        FirstLetter = firstLetter;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getsEmployeeID() {
        return sEmployeeID;
    }

    public void setsEmployeeID(String sEmployeeID) {
        this.sEmployeeID = sEmployeeID;
    }

    public String getsEmployeeName() {
        return sEmployeeName;
    }

    public void setsEmployeeName(String sEmployeeName) {
        this.sEmployeeName = sEmployeeName;
    }
}
