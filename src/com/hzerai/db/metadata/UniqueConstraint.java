package com.hzerai.db.metadata;

import java.util.ArrayList;
import java.util.List;

public class UniqueConstraint {

	private String name;
	private List<String> columns = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "UniqueConstraint [name=" + name + ", columns=" + columns + "]";
	}

}
