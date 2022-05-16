package com.hzerai.db.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static ConnectionManager instance;
	private Connection connection;

	private ConnectionManager() throws SQLException, IOException {
		Configuration cfg = ConfigFileReader.getInstance().getConfiguration();
		try {
			Class.forName(cfg.getDriverClass());
			this.connection = DriverManager.getConnection(cfg.getUrl(), cfg.getUsername(), cfg.getPassword());
		} catch (ClassNotFoundException ex) {
			System.out.println("Database Connection Creation Failed : " + ex.getMessage());
		}
	}

	public static ConnectionManager getInstance() throws SQLException, IOException {
		if (instance == null) {
			instance = new ConnectionManager();
		} else if (instance.getConnection().isClosed()) {
			instance = new ConnectionManager();
		}

		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

}
