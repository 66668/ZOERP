package com.zhongou.db.entity;


import com.zhongou.db.ColumnInfo;
import com.zhongou.db.TableInfo;

/**
 * 数据存储
 */
public class UserTable extends TableInfo {
    public static String C_TableName = "user";


    public static String C_EmployeeID = "employee_id";
    public static String C_storeId = "store_id";//登录的公司账号
    public static String C_StoreID = "StoreID";//返回信息
    public static String C_StoreUserId = "store_userid";
    public static String C_Password = "password";
    public static String C_Name = "Name";
    public static String C_Telephone = "Telephone";
    public static String C_Email = "Email";
    public static String C_StoreName = "StoreName";
    public static String C_DepartmentName = "DepartmentName";
    public static String C_PostName = "PostName";
    public static String C_JobNumber = "JobNumber";
    public static String C_EntryDate = "EntryDate";

    //未使用
    public static String C_WorkId = "work_id";
    public static String C_UserPicture = "userPicture";

    public UserTable() {
        _tableName = "user";
    }

    protected static UserTable _current;

    public static UserTable Current() {
        if (_current == null) {
            Initial();
        }
        return _current;
    }

    private static void Initial() {
        _current = new UserTable();
        _current.Add(C_storeId, new ColumnInfo(C_storeId, "StoreId", false, "String"));
        _current.Add(C_StoreID, new ColumnInfo(C_StoreID, "StoreID", false, "String"));
        _current.Add(C_Password, new ColumnInfo(C_Password, "Password", false, "String"));
        _current.Add(C_EmployeeID, new ColumnInfo(C_EmployeeID, "EmployeeID", false, "String"));
        _current.Add(C_StoreUserId, new ColumnInfo(C_StoreUserId, "StoreuserId", false, "String"));
        _current.Add(C_Name, new ColumnInfo(C_Name, "Name", false, "String"));
        _current.Add(C_Telephone, new ColumnInfo(C_Telephone, "Telephone", false, "String"));
        _current.Add(C_Email, new ColumnInfo(C_Email, "Email", false, "String"));
        _current.Add(C_StoreName, new ColumnInfo(C_StoreName, "StoreName", false, "String"));
        _current.Add(C_DepartmentName, new ColumnInfo(C_DepartmentName, "DepartmentName", false, "String"));
        _current.Add(C_PostName, new ColumnInfo(C_PostName, "PostName", false, "String"));
        _current.Add(C_JobNumber, new ColumnInfo(C_JobNumber, "JobNumber", false, "String"));
        _current.Add(C_EntryDate, new ColumnInfo(C_EntryDate, "EntryDate", false, "String"));

        _current.Add(C_WorkId, new ColumnInfo(C_WorkId, "WorkId", false, "String"));
        _current.Add(C_UserPicture, new ColumnInfo(C_UserPicture, "UserPicture", false, "String"));
    }


    //
    public ColumnInfo EmployeeID() {
        return GetColumnInfoByName(C_EmployeeID);
    }

    //
    public ColumnInfo storeId() {
        return GetColumnInfoByName(C_storeId);
    }

    //
    public ColumnInfo StoreID() {
        return GetColumnInfoByName(C_StoreID);

    }

    //
    public ColumnInfo StoreUserId() {
        return GetColumnInfoByName(C_StoreUserId);
    }

    //
    public ColumnInfo Password() {
        return GetColumnInfoByName(C_Password);
    }

    //
    public ColumnInfo UserPicture() {
        return GetColumnInfoByName(C_UserPicture);
    }

    //
    public ColumnInfo WorkId() {
        return GetColumnInfoByName(C_WorkId);
    }

    //
    public ColumnInfo Name() {
        return GetColumnInfoByName(C_Name);
    }

    //
    public ColumnInfo Telephone() {
        return GetColumnInfoByName(C_Telephone);
    }

    //
    public ColumnInfo Email() {
        return GetColumnInfoByName(C_Email);
    }

    //
    public ColumnInfo StoreName() {
        return GetColumnInfoByName(C_StoreName);
    }

    //
    public ColumnInfo DepartmentName() {
        return GetColumnInfoByName(C_DepartmentName);
    }

    //
    public ColumnInfo PostName() {
        return GetColumnInfoByName(C_PostName);
    }

    //
    public ColumnInfo JobNumber() {
        return GetColumnInfoByName(C_JobNumber);
    }

    //
    public ColumnInfo EntryDate() {
        return GetColumnInfoByName(C_EntryDate);
    }

}
