package com.zhongou.db.entitybase;

/**
 * 缓存的具体对象
 */

public class ColumnInfo {
	// / <summary>
	// / 列名
	// / </summary>
	public String ColumnName;
	// / <summary>
	// / 列备注
	// / </summary>
	public String ColumnCaption;
	// / <summary>
	// / 是否主键
	// / </summary>
	public boolean IsPrimaryKey;

	// / <summary>
	// / 是否允许为空
	// / </summary>
	public boolean AllowNull;

	// / <summary>
	// / 默认值
	// / </summary>
	public Object DefaultValue;

	// / <summary>
	// / 数据类型
	// / </summary>
	public String DataType;

	// / <summary>
	// / 最大长度
	// / </summary>
	public int MaxLength;



	//
	public ColumnInfo(String columnName, String columnCaption,boolean isPrimaryKey, String dataType) {
		Object defaultValue = null; 
		if (dataType.equals("int") || dataType.equals("Integer") ) {
			defaultValue = 0;
		} else if (dataType.equals("long")||dataType.equals("Long")) {
			defaultValue = 0l;
		} else if (dataType.equals("String")) {
			defaultValue = "";
		} else if (dataType.equals("double")||dataType.equals("Double")) {
			double d = 0d;
			defaultValue = d;
		}
		initColumn(columnName, columnCaption, isPrimaryKey, dataType, defaultValue);
	}

	//
	public ColumnInfo(String columnName, String columnCaption, boolean isPrimaryKey, String dataType, Object defaultValue) {
		initColumn(columnName, columnCaption, isPrimaryKey, dataType, defaultValue);
	}

	void initColumn(String columnName, String columnCaption, boolean isPrimaryKey, String dataType, Object defaultValue) {
		ColumnName = columnName;
		ColumnCaption = columnCaption;
		IsPrimaryKey = isPrimaryKey;
		DataType = dataType;
		AllowNull = false;
		DefaultValue = defaultValue;
		MaxLength = -1;
	}
}
