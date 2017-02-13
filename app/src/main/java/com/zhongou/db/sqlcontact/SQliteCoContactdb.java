package com.zhongou.db.sqlcontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.model.ContactsSonCOModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录存储操作类
 * <p>
 * 通讯录 子公司+联系人 存储
 * 由于该表存储是子公司集合和员工集合的两部分数据，所以索引用有8个参数的query
 * Created by sjy on 2017/2/10.
 */

public class SQLiteCoContactdb extends SQLiteOpenHelper {

    private static final int Db_CO_VERSION = 1;//公司数据库version

    private static final String DB_CO_NAME = "coContacts.db";//数据库名

    private static final String DB_CO_TABLE_NAME = "contactsofco";//表名


    //表属性
    private static final String FLAG = "flag";//区分标记

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

    //创建表SQL语句
    private static final String CREATE_CO_CONTACT_TABLE = "CREATE TABLE "
            + DB_CO_TABLE_NAME//表名
            + " ("
            + FLAG + " TEXT,"//标记 0
            + STOREID + " TEXT,"//子公司id 1
            + STORENAME + " TEXT,"// 子公司 2

            + EMPLNAME + "TEXT,"//联系人 3
            + EMPLID + "TEXT,"//联系人id 4

            + GENDER + "TEXT,"//性别 5
            + PHONE + "TEXT,"//手机号 6
            + EMAIL + "TEXT,"//邮箱 7

            + EMPL_DEPTNAME + "TEXT,"//所属部门 8
            + ENTRYTIME + "TEXT,"//入职时间 9
            + LEVEL + "TEXT"//个人级别 10
            + ");";


    //标记值
    public static final String SONCOFLAG = "sonOfCO_Flag";
    public static final String EMPLOYEEFLAG = "employee_Contact_Flag";

    public SQLiteCoContactdb(Context context) {
        super(context, DB_CO_NAME, null, Db_CO_VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建子公司表
        db.execSQL(CREATE_CO_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_CO_TABLE_NAME);
        onCreate(db);

    }

    //添加一条联系人数据
    public void addEmplContact(ContactsEmployeeModel model, String flag) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FLAG, flag);//标记

        values.put(EMPLNAME, model.getsEmployeeName());//联系人
        values.put(EMPLID, model.getsEmployeeID());//联系人id
        values.put(GENDER, model.getsGender());//性别

        values.put(PHONE, model.getsTelephone());//手机号
        values.put(EMAIL, model.getsEmail());//邮箱
        values.put(EMPL_DEPTNAME, model.getsDepartmentName());//所属部门
        values.put(ENTRYTIME, model.getsEntryDate());//入职时间
        values.put(LEVEL, model.getsPostLevelName());//级别

        //导入数据库中
        db.insert(DB_CO_TABLE_NAME, null, values);
        db.close();

    }

    //添加联系人集合
    public void addEmplContactList(List<ContactsEmployeeModel> list, String flag) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(FLAG, flag);//标记

            values.put(EMPLNAME, list.get(i).getsEmployeeName());//联系人
            values.put(EMPLID, list.get(i).getsEmployeeID());//联系人id
            values.put(GENDER, list.get(i).getsGender());//性别

            values.put(PHONE, list.get(i).getsTelephone());//手机号
            values.put(EMAIL, list.get(i).getsEmail());//邮箱
            values.put(EMPL_DEPTNAME, list.get(i).getsDepartmentName());//所属部门
            values.put(ENTRYTIME, list.get(i).getsEntryDate());//入职时间
            values.put(LEVEL, list.get(i).getsPostLevelName());//级别

            //导入该条数据
            db.insert(DB_CO_TABLE_NAME, null, values);
        }
        db.close();
    }

    //添加子公司集合数据
    public void addSonCoList(List<ContactsSonCOModel> list, String flag) {
        Log.d("SJY", "db添加子公司集合数据");
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(FLAG, flag);//标记
            values.put(STOREID, list.get(i).getsStoreID());
            values.put(STORENAME, list.get(i).getsStoreName());
            Log.d("SJY", "db添加子公司集合数据list" + i);
            //导入该条数据
            db.insert(DB_CO_TABLE_NAME, null, values);
        }
        db.close();
    }

    //获取 所有联系人数据
    public List<ContactsEmployeeModel> getEmpContactList(String flag) {
        List<ContactsEmployeeModel> emplList = new ArrayList<ContactsEmployeeModel>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            //筛选
            Cursor cursor = db.query(DB_CO_TABLE_NAME//表名
                    , new String[]{EMPLNAME, EMPLID, GENDER, PHONE, EMAIL, EMPL_DEPTNAME, ENTRYTIME, LEVEL}
                    , FLAG + "=?"
                    , new String[]{flag}//, new String[]{flag}
                    , null
                    , null
                    , null);
            Log.d("SJY", "getEmpContactList try中03");
            if (cursor == null) {
                Log.d("SJY", "获取联系人cursor为空");
                return emplList;
            }
            Log.d("SJY", "获取联系人cursor不为空");
            cursor.moveToFirst();
            do {
                ContactsEmployeeModel model = new ContactsEmployeeModel();

                model.setsEmployeeName(cursor.getString(3));//联系人 3
                model.setsEmployeeID(cursor.getString(4));//联系人id 4

                model.setsGender(cursor.getString(5));//性别 5
                model.setsTelephone(cursor.getString(6));//手机号 6
                model.setsEmail(cursor.getString(7));//邮箱 7

                model.setsDepartmentName(cursor.getString(8));//所属部门 8
                model.setsEntryDate(cursor.getString(9));//入职时间 9
                model.setsPostLevelName(cursor.getString(10));//等级 10

                emplList.add(model);
                Log.d("SJY", "do while循环取联系人值");
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            Log.d("SJY", "do while循环后 关闭cursor,db");
            return emplList;
        } catch (Exception e) {
            Log.d("SJY", "" + "异常"+e.getMessage());
        }

        Log.d("SJY", "do while循环后 关闭cursor,db");
        return emplList;
    }

    //获取所有子公司集合
    public List<ContactsSonCOModel> getSonCOList(String flag) {
        List<ContactsSonCOModel> sonList = new ArrayList<ContactsSonCOModel>();

        try {

            SQLiteDatabase db = this.getReadableDatabase();

            //筛选
            Cursor cursor = db.query(DB_CO_TABLE_NAME//表名
                    , new String[]{STOREID, STORENAME}
                    , FLAG + "=?"
                    , new String[]{flag}
                    , null
                    , null
                    , null);

            if (cursor == null) {
                return sonList;
            } else {
                cursor.moveToFirst();
                do {
                    ContactsSonCOModel model = new ContactsSonCOModel();

                    model.setsStoreID(cursor.getString(1));//子公司id 1
                    model.setsStoreName(cursor.getString(2));//子公司 2

                    sonList.add(model);
                } while (cursor.moveToNext());

            }
            cursor.close();
            db.close();

            return sonList;
        } catch (Exception e) {
            Log.e("all_contact", "" + e.getMessage());
        }
        return sonList;
    }
}







































