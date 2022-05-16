package com.hzerai.db.metadata;

public class MetaEIKey {

	private String pkTableCatalog;
	private String pkTableSchema;
	private String pkTableName;
	private String pkColumnName;
	private String fkTableCatalog;
	private String fkTableSchema;
	private String fkTableName;
	private String fkColumnName;
	private String fkSequenceNumber;
	private String updateRule;
	private String deleteRule;
	private boolean fkIsUnique = false;
	private boolean fkIsNullable = true;

	public MetaEIKey(String pkTableCatalog, String pkTableSchema, String pkTableName, String pkColumnName,
			String fkTableCatalog, String fkTableSchema, String fkTableName, String fkColumnName,
			String fkSequenceNumber, String updateRule, String deleteRule, boolean fkIsUnique, boolean fkIsNullable) {
		super();
		this.pkTableCatalog = pkTableCatalog;
		this.pkTableSchema = pkTableSchema;
		this.pkTableName = pkTableName;
		this.pkColumnName = pkColumnName;
		this.fkTableCatalog = fkTableCatalog;
		this.fkTableSchema = fkTableSchema;
		this.fkTableName = fkTableName;
		this.fkColumnName = fkColumnName;
		this.fkSequenceNumber = fkSequenceNumber;
		this.updateRule = updateRule;
		this.deleteRule = deleteRule;
		this.fkIsUnique = fkIsUnique;
		this.fkIsNullable = fkIsNullable;
	}

	public String getPkTableCatalog() {
		return pkTableCatalog;
	}

	public void setPkTableCatalog(String pkTableCatalog) {
		this.pkTableCatalog = pkTableCatalog;
	}

	public String getPkTableSchema() {
		return pkTableSchema;
	}

	public void setPkTableSchema(String pkTableSchema) {
		this.pkTableSchema = pkTableSchema;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String getPkColumnName() {
		return pkColumnName;
	}

	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	public String getFkTableCatalog() {
		return fkTableCatalog;
	}

	public void setFkTableCatalog(String fkTableCatalog) {
		this.fkTableCatalog = fkTableCatalog;
	}

	public String getFkTableSchema() {
		return fkTableSchema;
	}

	public void setFkTableSchema(String fkTableSchema) {
		this.fkTableSchema = fkTableSchema;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}

	public String getFkColumnName() {
		return fkColumnName;
	}

	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}

	public String getFkSequenceNumber() {
		return fkSequenceNumber;
	}

	public void setFkSequenceNumber(String fkSequenceNumber) {
		this.fkSequenceNumber = fkSequenceNumber;
	}

	public String getUpdateRule() {
		return updateRule;
	}

	public void setUpdateRule(String updateRule) {
		this.updateRule = updateRule;
	}

	public String getDeleteRule() {
		return deleteRule;
	}

	public void setDeleteRule(String deleteRule) {
		this.deleteRule = deleteRule;
	}

	public boolean isFkIsUnique() {
		return fkIsUnique;
	}

	public void setFkIsUnique(boolean fkIsUnique) {
		this.fkIsUnique = fkIsUnique;
	}

	public boolean isFkIsNullable() {
		return fkIsNullable;
	}

	public void setFkIsNullable(boolean fkIsNullable) {
		this.fkIsNullable = fkIsNullable;
	}

	@Override
	public String toString() {
		return "MetaEIKey [pkTableCatalog=" + pkTableCatalog + ", pkTableSchema=" + pkTableSchema + ", pkTableName="
				+ pkTableName + ", pkColumnName=" + pkColumnName + ", fkTableCatalog=" + fkTableCatalog
				+ ", fkTableSchema=" + fkTableSchema + ", fkTableName=" + fkTableName + ", fkColumnName=" + fkColumnName
				+ ", fkIsUnique=" + fkIsUnique + ", fkIsNullable=" + fkIsNullable + ", fkSequenceNumber="
				+ fkSequenceNumber + ", updateRule=" + updateRule + ", deleteRule=" + deleteRule + "]";
	}

}
