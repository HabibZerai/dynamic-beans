package com.hzerai.db.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.apache.commons.lang3.text.WordUtils;

public class Database {

	private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
	private static final String SQL_ALL = "select * from %s where 1>2";

	public static List<Table> getTables() throws Exception {
		List<Table> tables = new ArrayList<>();
		Connection conn = ConnectionManager.getInstance().getConnection();
		ResultSet rs = null;
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			rs = dbmd.getTables(conn.getCatalog(), dbmd.getUserName(), null, new String[] { "TABLE" });
			ResultSet colRs;
			List<Column> columns;
			List<Column> embeddedId;
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				String tableSchema = rs.getString("TABLE_SCHEM");
				columns = new ArrayList<>();
				embeddedId = new ArrayList<>();
				Set<String> primaryKeys = getPrimaryKeyColumnsForTable(conn, tableSchema, tableName);
				List<MetaEIKey> exportedKeys = getExportedKeyColumnsForTable(conn, tableSchema, tableName);
				List<MetaEIKey> importedKeys = getImportedKeyColumnsForTable(conn, tableSchema, tableName);
				List<UniqueConstraint> uniqueConstraints = getUniqueConstraints(conn, tableSchema, tableName);
				List<String> foreignKeysNames = getForeignKeysNames(importedKeys);
				Statement stmt = conn.createStatement();
				colRs = stmt.executeQuery(String.format(SQL_ALL, tableName));
				final ResultSetMetaData meta = colRs.getMetaData();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					if (foreignKeysNames.contains(meta.getColumnName(i))) {
						continue;
					}
					if (primaryKeys.size() > 1 && primaryKeys.contains(meta.getColumnName(i))) {
						Column column = buildColumn(meta, i);
						if (column != null) {
							embeddedId.add(column);
						}

					} else {
						Column column = buildColumn(meta, i);
						if (primaryKeys.contains(column.getName())) {
							column.setPrimaryKey("true");
						}
						if (column != null) {
							columns.add(column);
						}
					}
				}
				Table table = new Table(tableName, tableSchema, columns, embeddedId, exportedKeys, importedKeys,
						uniqueConstraints);
				tables.add(table);
				close(colRs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(rs);
		close(conn);
		return tables;
	}

	private static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static Set<String> getPrimaryKeyColumnsForTable(Connection connection, String schema, String tableName)
			throws SQLException {
		try (ResultSet pkColumns = connection.getMetaData().getPrimaryKeys(null, schema, tableName);) {
			SortedSet<String> pkColumnSet = new TreeSet<>();
			while (pkColumns.next()) {
				String pkColumnName = pkColumns.getString("COLUMN_NAME");
				Integer pkPosition = pkColumns.getInt("KEY_SEQ");
				pkColumnSet.add(pkColumnName);
			}
			return pkColumnSet;
		}
	}

	private static List<MetaEIKey> getExportedKeyColumnsForTable(Connection connection, String schema, String tableName)
			throws SQLException {
		try (ResultSet fkColumns = connection.getMetaData().getExportedKeys(null, schema, tableName);) {
			List<MetaEIKey> fkColumnSet = new ArrayList<>();
			while (fkColumns.next()) {
				MetaEIKey metaEIKey = new MetaEIKey(fkColumns.getString("PKTABLE_CAT"),
						fkColumns.getString("PKTABLE_SCHEM"), fkColumns.getString("PKTABLE_NAME"),
						fkColumns.getString("PKCOLUMN_NAME"), fkColumns.getString("FKTABLE_CAT"),
						fkColumns.getString("FKTABLE_SCHEM"), fkColumns.getString("FKTABLE_NAME"),
						fkColumns.getString("FKCOLUMN_NAME"), fkColumns.getString("KEY_SEQ"),
						getRule(fkColumns.getShort("UPDATE_RULE")), getRule(fkColumns.getShort("DELETE_RULE")),
						isNullableKey(connection, schema, fkColumns.getString("FKTABLE_NAME"),
								fkColumns.getString("FKCOLUMN_NAME")),
						isUniqueKey(connection, schema, fkColumns.getString("FKTABLE_NAME"),
								fkColumns.getString("FKCOLUMN_NAME")));
				fkColumnSet.add(metaEIKey);
			}
			return fkColumnSet;
		}
	}

	private static List<MetaEIKey> getImportedKeyColumnsForTable(Connection connection, String schema, String tableName)
			throws SQLException {
		try (ResultSet fkColumns = connection.getMetaData().getImportedKeys(null, schema, tableName);) {
			List<MetaEIKey> fkColumnSet = new ArrayList<>();
			while (fkColumns.next()) {
				MetaEIKey metaEIKey = new MetaEIKey(fkColumns.getString("PKTABLE_CAT"),
						fkColumns.getString("PKTABLE_SCHEM"), fkColumns.getString("PKTABLE_NAME"),
						fkColumns.getString("PKCOLUMN_NAME"), fkColumns.getString("FKTABLE_CAT"),
						fkColumns.getString("FKTABLE_SCHEM"), fkColumns.getString("FKTABLE_NAME"),
						fkColumns.getString("FKCOLUMN_NAME"), fkColumns.getString("KEY_SEQ"),
						getRule(fkColumns.getShort("UPDATE_RULE")), getRule(fkColumns.getShort("DELETE_RULE")),
						isNullableKey(connection, schema, fkColumns.getString("FKTABLE_NAME"),
								fkColumns.getString("FKCOLUMN_NAME")),
						isUniqueKey(connection, schema, fkColumns.getString("FKTABLE_NAME"),
								fkColumns.getString("FKCOLUMN_NAME")));
				fkColumnSet.add(metaEIKey);
			}
			return fkColumnSet;
		}
	}

	private static Column buildColumn(ResultSetMetaData meta, int columnId) throws Exception {
		if (meta == null) {
			return null;
		}

		try {
			Column column = new Column();
			column.setCatelogName(meta.getCatalogName(columnId));
			column.setName(meta.getColumnName(columnId));
			column.setLabel(meta.getColumnLabel(columnId));
			column.setClassName(meta.getColumnClassName(columnId));
			column.setType(meta.getColumnType(columnId));
			column.setTableName(meta.getColumnTypeName(columnId));
			column.setDisplaySize(meta.getColumnDisplaySize(columnId));
			column.setPrecision(meta.getPrecision(columnId));
			column.setScale(meta.getScale(columnId));
			column.setSchemaName(meta.getSchemaName(columnId));
			column.setTableName(meta.getTableName(columnId));
			column.setNullable(String.valueOf(meta.isNullable(columnId) == DatabaseMetaData.columnNullable));
			column.setAutoIncrement(String.valueOf(meta.isAutoIncrement(columnId)));
			column.setJavaType(getJavaType(column));
			column.setJavaProperty(toCamelCase(column.getName()));
			return column;
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		}
	}

	private static String toCamelCase(String str) {
		return WordUtils.uncapitalize(WordUtils.capitalizeFully(str, '_').replaceAll("_", ""));
	}

	private static String getJavaType(Column column) {
		if (column == null) {
			return null;
		}

		String javaType = TypeMapper.MAP_TYPES.get(column.getType());
		if (column.getType() == Types.NUMERIC && column.getPrecision() == 1 && column.getScale() == 0) {
			return "Boolean";
		}
		if (column.getType() == Types.NUMERIC && column.getScale() == 0) {
			return "Long";
		}

		return javaType;
	}

	private static String getRule(short deleteRule) {
		if (deleteRule == DatabaseMetaData.importedKeyNoAction) {
			return "importedKeyNoAction";
		} else if (deleteRule == DatabaseMetaData.importedKeyCascade) {
			return "importedKeyCascade";
		} else if (deleteRule == DatabaseMetaData.importedKeySetNull) {
			return "importedKeySetNull";
		} else if (deleteRule == DatabaseMetaData.importedKeySetDefault) {
			return "importedKeySetDefault";
		} else if (deleteRule == DatabaseMetaData.importedKeyRestrict) {
			return "importedKeyRestrict";
		} else {
			return "nobody knows";
		}
	}

	private static List<UniqueConstraint> getUniqueConstraints(Connection conn, String schema, String table)
			throws SQLException {
		Map<String, UniqueConstraint> constraints = new HashMap<>();
		DatabaseMetaData dm = conn.getMetaData();
		ResultSet rs = dm.getIndexInfo(null, schema, table, true, true);
		while (rs.next()) {
			String indexName = rs.getString("index_name");
			String columnName = rs.getString("column_name");
			UniqueConstraint constraint = new UniqueConstraint();
			constraint.setName(indexName);
			constraint.getColumns().add(columnName);

			constraints.compute(indexName, (key, value) -> {
				if (value == null) {
					return constraint;
				}
				value.getColumns().add(columnName);
				return value;
			});

		}

		return new ArrayList<>(constraints.values());
	}

	private static List<String> getForeignKeysNames(List<MetaEIKey> importedKeys) {
		List<String> foreignKeysNames = new ArrayList<>();
		for (MetaEIKey metaEIKey : importedKeys) {
			foreignKeysNames.add(metaEIKey.getFkColumnName());
		}
		return foreignKeysNames;
	}

	private static boolean isNullableKey(Connection connection, String schema, String fkTableName, String fkColumnName)
			throws SQLException {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet columns = meta.getColumns(null, schema, fkColumnName, null);
		while (columns.next()) {
			if (fkColumnName.equals(columns.getString("COLUMN_NAME"))) {
				int nullable = columns.getInt("NULLABLE");
				if (nullable == DatabaseMetaData.columnNoNulls) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean isUniqueKey(Connection connection, String schema, String fkTableName, String fkColumnName)
			throws SQLException {
		List<UniqueConstraint> uniqueConstraints = getUniqueConstraints(connection, schema, fkTableName);
		for (UniqueConstraint uc : uniqueConstraints) {
			if (uc.getColumns().contains(fkColumnName)) {
				return true;
			}
		}
		return false;
	}

}