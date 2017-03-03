package com.zhongou.db.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zhongou.application.MyApplication;
import com.zhongou.model.ContactsDeptModel;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.model.ContactsSonCOModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录存储操作类
 * <p>
 * 用于 应用-审批-我的审批-详情-抄送功能
 * <p>
 * Created by sjy on 2017/2/10.
 */

public class SQLiteCopytoContactdb extends SQLiteOpenHelper {
    private static Context context;

    private static final int Db_CO_VERSION = 1;//数据库version
    private static final String DB_NAME = "contactcopy.db";//数据库

    private static final String DB_CO_TABLE_NAME = "contactsofco";//子公司表名
    private static final String DB_DEPT_TABLE_NAME = "contactsofdept";//子公司下的部门表名
    private static final String DB_EMPL_TABLE_NAME = "contactsofempl";//部门下的通讯录表名


    //表属性
    private static final String STOREID = "storeid";//子公司id
    private static final String STORENAME = "storename";

    private static final String DEPTID = "deptid";//部门id，区别于个人的部门
    private static final String DEPTNAME = "deptname";

    private static final String EMPLNAME = "emplname";//联系人
    private static final String EMPLID = "emplid";//联系人对应id
    private static final String GENDER = "gender";//性别

    private static final String PHONE = "phone";//手机号
    private static final String EMAIL = "email";//邮箱
    private static final String EMPL_DEPTNAME = "empldeptname";//联系人部门
    private static final String ENTRYTIME = "entrytime";//入职时间
    private static final String LEVEL = "level";//个人级别


    //创建子公司表
    private static final String CREATE_CO_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DB_CO_TABLE_NAME//表名
            + " ( "
            + STOREID + " text,"//子公司id 1
            + STORENAME + " text"// 子公司 2
            + " );";

    //创建部门表
    private static final String CREATE_DEPT_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DB_DEPT_TABLE_NAME//表名
            + " ( "
            + STOREID + " text,"
            + DEPTID + " text,"
            + DEPTNAME + " text"
            + " );";

    //创建部门下的联系人表
    private static final String CREATE_EMPL_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DB_EMPL_TABLE_NAME//表名
            + " ( "
            + DEPTID + " text ,"//标记 0
            + EMPLID + " text,"//员工id
            + EMPLNAME + " text,"//姓名
            + GENDER + " text,"//性别
            + PHONE + " text,"//手机号 6
            + EMAIL + " text,"//邮箱 7

            + EMPL_DEPTNAME + " text,"//所属部门 8
            + ENTRYTIME + " text,"//入职时间 9
            + LEVEL + " text"//个人级别 10
            + " );";

    //构造
    public SQLiteCopytoContactdb(Context context) {
        super(context, DB_NAME, null, Db_CO_VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CO_CONTACT_TABLE);//建子公司表
        db.execSQL(CREATE_DEPT_CONTACT_TABLE);//建部门表
        db.execSQL(CREATE_EMPL_CONTACT_TABLE);//建联系人
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_CO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_DEPT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_EMPL_TABLE_NAME);
        onCreate(db);

    }

    //01添加子公司集合数据
    public void addSonCoList(List<ContactsSonCOModel> list) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(STOREID, list.get(i).getsStoreID());
            values.put(STORENAME, list.get(i).getsStoreName());

            //导入该条数据
            db.insert(DB_CO_TABLE_NAME, null, values);
        }
        db.close();
    }


    //02获取所有子公司集合
    public List<ContactsSonCOModel> getSonCOList() {
        List<ContactsSonCOModel> sonList = new ArrayList<ContactsSonCOModel>();

        try {

            SQLiteDatabase db = this.getReadableDatabase();

            //筛选
            Cursor cursor = db.query(DB_CO_TABLE_NAME//表名
                    , new String[]{STOREID, STORENAME}
                    , null
                    , null
                    , null
                    , null
                    , STOREID + " desc");

            if (cursor == null) {
                return sonList;
            } else {
                while (cursor.moveToNext()) {
                    ContactsSonCOModel model = new ContactsSonCOModel();

                    model.setsStoreID(cursor.getString(cursor.getColumnIndex(STOREID)));
                    model.setsStoreName(cursor.getString(cursor.getColumnIndex(STORENAME)));

                    sonList.add(model);
                }
            }
            cursor.close();
            db.close();

            return sonList;
        } catch (Exception e) {
            Log.e("co_contact异常", "" + e.getMessage());
        }
        return sonList;
    }

    //03添加部门集合 不同公司对应不同部门
    public void addDeptList(List<ContactsDeptModel> list, String storeID) {


        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(STOREID, storeID);
            values.put(DEPTID, list.get(i).getsDeptID());
            values.put(DEPTNAME, list.get(i).getsDeptName());

            //导入该条数据
            db.insert(DB_DEPT_TABLE_NAME, null, values);
        }
        db.close();
    }

    //04获取子公司下的所有部门集合
    public List<ContactsDeptModel> getDeptList(String storeID) {

        List<ContactsDeptModel> deptList = new ArrayList<ContactsDeptModel>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            //筛选
            Cursor cursor = db.query(DB_DEPT_TABLE_NAME//表名
                    , new String[]{STOREID, DEPTID, DEPTNAME}
                    , STOREID + "=?"
                    , new String[]{storeID}
                    , null
                    , null
                    , STOREID + " desc");

            if (cursor == null) {
                return deptList;
            } else {
                while (cursor.moveToNext()) {
                    ContactsDeptModel model = new ContactsDeptModel();

                    model.setsDeptID(cursor.getString(cursor.getColumnIndex(DEPTID)));
                    model.setsDeptName(cursor.getString(cursor.getColumnIndex(DEPTNAME)));

                    deptList.add(model);
                }
            }
            cursor.close();
            db.close();

            return deptList;
        } catch (Exception e) {
            Log.e("dept_contact异常", "" + e.getMessage());
        }
        return deptList;
    }

    //05添加部门联系人集合
    public void addEmplList(List<ContactsEmployeeModel> list, String DeptID) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(DEPTID, DeptID);//标记

            values.put(EMPLID, list.get(i).getsEmployeeID());//
            values.put(EMPLNAME, list.get(i).getsEmployeeName());//联系人

            values.put(GENDER, list.get(i).getsGender());//性别
            values.put(PHONE, list.get(i).getsTelephone());//手机号
            values.put(EMAIL, list.get(i).getsEmail());//邮箱

            values.put(EMPL_DEPTNAME, list.get(i).getsDepartmentName());//所属部门
            values.put(ENTRYTIME, list.get(i).getsEntryDate());//入职时间
            values.put(LEVEL, list.get(i).getsPostLevelName());//级别

            //导入该条数据
            db.insert(DB_EMPL_TABLE_NAME, null, values);
        }

        db.close();

    }


    //06获取 部门下联系人数据
    public List<ContactsEmployeeModel> getEmpList(String deptID) {
        List<ContactsEmployeeModel> emplList = new ArrayList<ContactsEmployeeModel>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            //筛选
            Cursor cursor = db.query(DB_EMPL_TABLE_NAME//表名
                    , new String[]{EMPLID, EMPLNAME, GENDER, PHONE, EMAIL, EMPL_DEPTNAME, ENTRYTIME, LEVEL}
                    , DEPTID + "=?"
                    , new String[]{deptID}//, new String[]{flag}
                    , null
                    , null
                    , null);
            if (cursor == null) {
                return emplList;
            }
            cursor.moveToFirst();
            do {
                ContactsEmployeeModel model = new ContactsEmployeeModel();

                model.setsEmployeeName(cursor.getString(cursor.getColumnIndex(EMPLNAME)));//联系人 3
                model.setsEmployeeID(cursor.getString(cursor.getColumnIndex(EMPLID)));//联系人id 4

                model.setsGender(cursor.getString(cursor.getColumnIndex(GENDER)));//性别 5
                model.setsTelephone(cursor.getString(cursor.getColumnIndex(PHONE)));//手机号 6
                model.setsEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));//邮箱 7

                model.setsDepartmentName(cursor.getString(cursor.getColumnIndex(EMPL_DEPTNAME)));//所属部门 8
                model.setsEntryDate(cursor.getString(cursor.getColumnIndex(ENTRYTIME)));//入职时间 9
                model.setsPostLevelName(cursor.getString(cursor.getColumnIndex(LEVEL)));//等级 10

                emplList.add(model);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return emplList;
        } catch (Exception e) {
            Log.d("SJY", "db_empl 异常=" + e.getMessage());
        }
        return emplList;
    }

    //程序退出，清空所有表
    public static void clearDb() {

        SQLiteDatabase db = new SQLiteCopytoContactdb(MyApplication.getInstance()).getWritableDatabase();

        db.execSQL("DELETE FROM " + DB_CO_TABLE_NAME);
        db.execSQL("DELETE FROM " + DB_DEPT_TABLE_NAME);
        db.execSQL("DELETE FROM " + DB_EMPL_TABLE_NAME);

        db.close();
    }

}





























