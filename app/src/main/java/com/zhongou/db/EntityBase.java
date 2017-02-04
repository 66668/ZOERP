package com.zhongou.db;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class EntityBase {

    protected TableInfo _tableSchema;

    public TableInfo GetOringTableSchema() {
        return _tableSchema;
    }

    protected HashMap<String, Object> _data;

    public HashMap<String, Object> GetDataCollection() {
        return _data;
    }

    protected BusinessState State;

    public EntityBase() {
        _data = new HashMap<String, Object>();
        State = BusinessState.Added;
    }

    //0
    public HashMap<String, Object> ToHashtable() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (String key : _data.keySet()) {
            map.put(key, GetData(key));
        }
        return map;
    }

    //0
    public void ConvertEntity(HashMap<String, Object> map) {
        for (String key : map.keySet()) {
            _data.put(key, GetData(key));
        }
    }

    public void SetData(String key, Object value) {
        if (value != null) {
            _data.put(key.trim(), value);
        }
    }

    public Object GetData(String key) {
        return _data.get(key.trim());
    }

    public ArrayList<String> GetKeys() {
        return GetOringTableSchema().GetAllColumnName();
    }

    @Override
    public boolean equals(Object o) {
        EntityBase right = (EntityBase) o;
        if (right == null)
            return false;
        if (this._data.size() != right.GetDataCollection().size())
            return false;
        for (String key : GetKeys()) {
            if (!right.GetDataCollection().containsKey(key))
                return false;
            if (_data.get(key).equals(right.GetDataCollection().get(key)) == false)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String sb = new String();
        for (ColumnInfo column : _tableSchema.GetAllColumnInfo()) {
            sb += "{{";
            sb += column.ColumnName;
            sb += "===";
            Object obj = GetData(column.ColumnName);
            if (obj == null) {
                sb += "";
            } else {
                sb += GetData(column.ColumnName).toString();
            }
            sb += "}}";
        }
        return sb;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        for (ColumnInfo column : _tableSchema.GetAllColumnInfo()) {
            Object obj = GetData(column.ColumnName);
            try {
                jsonObject.put(column.ColumnName, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    //0
    public String toXml() {
        StringBuffer sb = new StringBuffer();
        String leftBracket = "<";
        String rightBracket = ">";
        sb.append(leftBracket).append(_tableSchema._tableName.replace("_", ""))
                .append(rightBracket);
        for (ColumnInfo column : _tableSchema.GetAllColumnInfo()) {
            sb.append(leftBracket).append(column.ColumnName.replace("_", ""))
                    .append(rightBracket);
            Object obj = GetData(column.ColumnName);
            if (obj != null) {
                sb.append(GetData(column.ColumnName));
            }
            sb.append("</").append(column.ColumnName.replace("_", ""))
                    .append(rightBracket);
        }
        sb.append("</").append(_tableSchema._tableName.replace("_", ""))
                .append(rightBracket);

        return sb.toString();
    }

    /**
     * Note: include all parts
     *
     * @return
     */
    public String toXmlLower() {
        StringBuffer sb = new StringBuffer(toXmlLowerNoEnd());
        String rightBracket = ">";

        String tableName = toField(_tableSchema._tableName);
        sb.append("</").append(tableName).append(rightBracket);

        return sb.toString();
    }

    /**
     * Note: there is not end part.
     *
     * @return
     */
    public String toXmlLowerNoEnd() {
        StringBuffer sb = new StringBuffer();
        String leftBracket = "<";
        String rightBracket = ">";

        String tableName = toField(_tableSchema._tableName);
        String field = null;
        sb.append(leftBracket).append(tableName).append(rightBracket);
        for (ColumnInfo column : _tableSchema.GetAllColumnInfo()) {
            field = toField(column.ColumnName);
            sb.append(leftBracket).append(field).append(rightBracket);
            Object obj = GetData(column.ColumnName);
            if (obj != null) {
                if (column.DataType.equalsIgnoreCase("datetime")) {
                    try {
                        sb.append((String) obj);
                    } catch (Exception pe) {
                        Log.d("Entity", pe.getMessage());
                    }
                } else {
                    sb.append(GetData(column.ColumnName));
                }
            }
            sb.append("</").append(field).append(rightBracket);
        }
        return sb.toString();
    }

    public static String toField(String column) {
        column = column.toLowerCase(Locale.CHINA);
        String[] fields = split(column, "_");
        StringBuffer sb = new StringBuffer();
        String firstChar = null;
        for (String str : fields) {
            firstChar = str.substring(0, 1);
            str = str.replaceFirst(firstChar, firstChar.toUpperCase(Locale.CHINA));
            sb.append(str);
        }
        return sb.toString();
    }


    private static String[] split(String scoreOption, String sp) {
        String[] res = {scoreOption};
        if (null == sp || "".equals(sp))
            return res;
        if (null != scoreOption)
            res = scoreOption.split(sp);
        return res;
    }

    //0
    public static <T extends EntityBase> T create(Class<T> c) {
        T instance = null;
        try {
            instance = c.newInstance();
            for (ColumnInfo columnInfo : instance._tableSchema.Columns.values()) {
                instance.SetData(columnInfo.ColumnName, columnInfo.DefaultValue);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * translate json to entity
     *
     * @param <T>
     * @param item
     * @param c
     * @return
     * @throws JSONException
     */
    public static <T extends EntityBase> EntityBase toEntityBase(
            JSONObject item, Class<T> c) throws JSONException {
        //EntityBase obj = null;
        T obj = null;
        try {
            obj = c.newInstance();
            TableInfo table = obj.GetOringTableSchema();
            //String tableName = table.GetTableName();
            //obj = DataAccessBroker.createEntityBase(tableName);
            ArrayList<ColumnInfo> cs = table.GetAllColumnInfo();
            for (ColumnInfo colInfo : cs) {
                if (colInfo.DataType.equalsIgnoreCase("int") || colInfo.DataType.equalsIgnoreCase("Integer")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getInt(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("long")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getLong(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("double")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getDouble(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("string")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getString(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("datetime")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getString(colInfo.ColumnName));
                } else {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getString(colInfo.ColumnName));
                }
            }
        } catch (IllegalAccessException illex) {
            Log.d("SJY", illex.getMessage());
        } catch (InstantiationException inex) {
            Log.d("SJY", inex.getMessage());
        }
        return obj;
    }


}
