package com.hzerai.db.metadata;

import java.util.List;

public class Table {

	private String name;
	private String schema;
	private List<Column> columns;
	private List<Column> embeddedId;
	private List<MetaEIKey> exportedKeys;
	private List<MetaEIKey> importedKeys;
	private List<UniqueConstraint> uniqueConstraints;

	public Table() {
		super();
	}

	public Table(String name, String schema, List<Column> columns, List<Column> embeddedId,
			List<MetaEIKey> exportedKeys, List<MetaEIKey> importedKeys, List<UniqueConstraint> uniqueConstraints) {
		super();
		this.name = name;
		this.schema = schema;
		this.columns = columns;
		this.embeddedId = embeddedId;
		this.exportedKeys = exportedKeys;
		this.importedKeys = importedKeys;
		this.uniqueConstraints = uniqueConstraints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Column> getEmbeddedId() {
		return embeddedId;
	}

	public void setEmbeddedId(List<Column> embeddedId) {
		this.embeddedId = embeddedId;
	}

	public List<MetaEIKey> getExportedKeys() {
		return exportedKeys;
	}

	public void setExportedKeys(List<MetaEIKey> exportedKeys) {
		this.exportedKeys = exportedKeys;
	}

	public List<MetaEIKey> getImportedKeys() {
		return importedKeys;
	}

	public void setImportedKeys(List<MetaEIKey> importedKeys) {
		this.importedKeys = importedKeys;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<UniqueConstraint> getUniqueConstraints() {
		return uniqueConstraints;
	}

	public void setUniqueConstraints(List<UniqueConstraint> uniqueConstraints) {
		this.uniqueConstraints = uniqueConstraints;
	}

}
