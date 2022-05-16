package com.hzerai.db.metadata;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

	public static final Map<Integer, String> MAP_TYPES = new HashMap<>();
	static {
		MAP_TYPES.put(Types.CHAR, "String");
		MAP_TYPES.put(Types.VARCHAR, "String");
		MAP_TYPES.put(Types.LONGVARCHAR, "String");
		MAP_TYPES.put(Types.NUMERIC, "BigDecimal");
		MAP_TYPES.put(Types.DECIMAL, "BigDecimal");
		MAP_TYPES.put(Types.BIT, "Boolean");
		MAP_TYPES.put(Types.TINYINT, "Integer");
		MAP_TYPES.put(Types.SMALLINT, "Integer");
		MAP_TYPES.put(Types.INTEGER, "Integer");
		MAP_TYPES.put(Types.BIGINT, "Long");
		MAP_TYPES.put(Types.REAL, "Float");
		MAP_TYPES.put(Types.FLOAT, "Double");
		MAP_TYPES.put(Types.DOUBLE, "Double");
		MAP_TYPES.put(Types.BINARY, "byte[]");
		MAP_TYPES.put(Types.VARBINARY, "byte[]");
		MAP_TYPES.put(Types.LONGVARBINARY, "byte[]");
		MAP_TYPES.put(Types.DATE, "Date");
		MAP_TYPES.put(Types.TIME, "Time");
		MAP_TYPES.put(Types.TIMESTAMP, "Timestamp");
		MAP_TYPES.put(Types.CLOB, "Clob");
		MAP_TYPES.put(Types.BLOB, "Blob");
		MAP_TYPES.put(Types.ARRAY, "Array");
		MAP_TYPES.put(Types.STRUCT, "Struct");
		MAP_TYPES.put(Types.REF, "Ref");
		MAP_TYPES.put(Types.JAVA_OBJECT, "Object");
	}
}
