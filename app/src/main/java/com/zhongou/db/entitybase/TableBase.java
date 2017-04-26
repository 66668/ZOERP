package com.zhongou.db.entitybase;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *表的操作父类
 */
public class TableBase implements Serializable {

    public HashMap<String, ColumnInfo> Columns;

    protected String _tableName;

    /// <summary>
    /// </summary>
    public String GetTableName() {
        return _tableName;
    }

    public TableBase() {
        Columns = new HashMap<String, ColumnInfo>();
    }


    /**
     * 子类添加一条数据使用
     *
     * @param columnName key值
     * @param column     具体的实例对象
     */
    public void Add(String columnName, ColumnInfo column) {
        SetColumnInfoByName(column, columnName);
    }

    public void SetColumnInfoByName(ColumnInfo value, String columnName) {
        Columns.put(columnName, value);
    }

    /**
     * 子类获取一条实例对象ColumnInfo
     *
     * @param columnName
     * @return
     */
    public ColumnInfo GetColumnInfoByName(String columnName) {
        return Columns.get(columnName);
    }


    /**
     * 获取所有的value值，arrayList保存
     */
    protected ArrayList<ColumnInfo> _allColumnInfo;

    public ArrayList<ColumnInfo> GetAllColumnInfo() {
        if (_allColumnInfo == null) {
            _allColumnInfo = new ArrayList<ColumnInfo>();
            for (ColumnInfo ci : Columns.values()) {
                _allColumnInfo.add(ci);
            }
        }
        return _allColumnInfo;

    }

    /**
     * 获取所有的key值，arrayList保存
     */
    protected ArrayList<String> _allColumnName;

    public ArrayList<String> GetAllColumnName() {

        if (_allColumnName == null) {
            _allColumnName = new ArrayList<String>();
            for (ColumnInfo ci : Columns.values()) {
                _allColumnName.add(ci.ColumnName);
            }
        }
        return _allColumnName;

    }

    protected ArrayList<ColumnInfo> _keyColumnInfo;

    public ArrayList<ColumnInfo> GetKeyColumnInfo() {

        if (_keyColumnInfo == null) {
            _keyColumnInfo = new ArrayList<ColumnInfo>();
            for (ColumnInfo ci : Columns.values()) {
                if (ci.IsPrimaryKey) {
                    _keyColumnInfo.add(ci);
                }
            }
        }
        return _keyColumnInfo;

    }

    protected ArrayList<ColumnInfo> _valueColumnInfo;

    public ArrayList<ColumnInfo> GetValueColumnInfo() {
        if (_valueColumnInfo == null) {
            _valueColumnInfo = new ArrayList<ColumnInfo>();
            for (ColumnInfo ci : Columns.values()) {
                if (!ci.IsPrimaryKey) {
                    _valueColumnInfo.add(ci);
                }
            }
        }
        return _valueColumnInfo;
    }
}
