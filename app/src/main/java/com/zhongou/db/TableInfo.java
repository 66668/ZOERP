package com.zhongou.db;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TableInfo implements Serializable{
    /// <summary>
    /// </summary>
	public HashMap<String, ColumnInfo> Columns;


    protected String _tableName;
    /// <summary>
    /// </summary>
    public String GetTableName()
    {
        return _tableName; 
    }

    public TableInfo()
    {
    	Columns = new HashMap<String, ColumnInfo>();
    }

    public void Add(String columnName, ColumnInfo column)
    {
    	SetColumnInfoByName( column,columnName);
    }

    /// <summary>
    /// </summary>
    /// <param name="columnName"></param>
    /// <returns></returns>
    public ColumnInfo GetColumnInfoByName(String columnName)
    {
        return Columns.get(columnName);
    }
    public void SetColumnInfoByName(ColumnInfo value,String columnName)
    {
    	Columns.put(columnName, value);
    }

    protected ArrayList<ColumnInfo> _allColumnInfo;
    /// <summary>
    /// </summary>
    public ArrayList<ColumnInfo> GetAllColumnInfo(){
        if (_allColumnInfo == null)
        {
            _allColumnInfo = new ArrayList<ColumnInfo>();
            for (ColumnInfo ci : Columns.values())
            {
                _allColumnInfo.add(ci);
            }
        }
        return _allColumnInfo;
        
    }

    protected ArrayList<String> _allColumnName;
    /// <summary>
    /// </summary>
    public ArrayList<String> GetAllColumnName()
    {

    	if (_allColumnName == null)
    	{
    		_allColumnName = new ArrayList<String>();
    		for (ColumnInfo ci : Columns.values())
    		{
    			_allColumnName.add(ci.ColumnName);
    		}
    	}
    	return _allColumnName;

    }

    protected ArrayList<ColumnInfo> _keyColumnInfo;
    /// <summary>
    /// </summary>
    public ArrayList<ColumnInfo> GetKeyColumnInfo()
    {

    	if (_keyColumnInfo == null)
    	{
    		_keyColumnInfo = new ArrayList<ColumnInfo>();
    		for (ColumnInfo ci : Columns.values())
    		{
    			if (ci.IsPrimaryKey)
    			{
    				_keyColumnInfo.add(ci);
    			}
    		}
    	}
    	return _keyColumnInfo;

    }

    protected ArrayList<ColumnInfo> _valueColumnInfo;
    /// <summary>
    /// </summary>
    public ArrayList<ColumnInfo> GetValueColumnInfo()
    {
    	if (_valueColumnInfo == null)
    	{
    		_valueColumnInfo = new ArrayList<ColumnInfo>();
    		for (ColumnInfo ci : Columns.values())
    		{
    			if (!ci.IsPrimaryKey)
    			{
    				_valueColumnInfo.add(ci);
    			}
    		}
    	}
    	return _valueColumnInfo;
    }
}
