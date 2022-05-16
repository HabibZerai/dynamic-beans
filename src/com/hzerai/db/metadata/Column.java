package com.hzerai.db.metadata;

public class Column {

	private String catelogName;
	private String name;
	private String columnName;
	private String methodName;
	private String label;
	private int displaySize;
	private int type;
	private String nullable;
	private String autoIncrement = "false";
	private String primaryKey = "false";
	private String typeName;
	private int precision;
	private int scale;
	private String schemaName;
	private String tableName;
	private String className;
	private String javaType;
	private String javaProperty;

	public String getCatelogName() {
		return catelogName;
	}

	public void setCatelogName(String catelogName) {
		this.catelogName = catelogName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getJavaProperty() {
		return javaProperty;
	}

	public void setJavaProperty(String javaProperty) {
		this.javaProperty = javaProperty;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(String autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String toString() {
		return "Column [catelogName=" + catelogName + ", name=" + name + ", columnName=" + columnName + ", methodName="
				+ methodName + ", label=" + label + ", displaySize=" + displaySize + ", type=" + type + ", nullable="
				+ nullable + ", autoIncrement=" + autoIncrement + ", primaryKey=" + primaryKey + ", typeName="
				+ typeName + ", precision=" + precision + ", scale=" + scale + ", schemaName=" + schemaName
				+ ", tableName=" + tableName + ", className=" + className + ", javaType=" + javaType + ", javaProperty="
				+ javaProperty + "]";
	}

}
