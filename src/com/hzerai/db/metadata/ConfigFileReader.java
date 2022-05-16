package com.hzerai.db.metadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFileReader {

	private static ConfigFileReader instance;
	private Configuration cfg;
	private InputStream inputStream;

	private ConfigFileReader() throws IOException {
		Properties prop = new Properties();
		String propFileName = "config.properties";
		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		try {
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			cfg = new Configuration();
			cfg.setDriver(prop.getProperty("connection.driver"));
			cfg.setUrl(prop.getProperty("connection.url"));
			cfg.setUsername(prop.getProperty("connection.username"));
			cfg.setPassword(prop.getProperty("connection.password"));

		} catch (IOException e) {

		} finally {
			inputStream.close();
		}

	}

	public static ConfigFileReader getInstance() throws IOException {
		if (instance == null) {
			instance = new ConfigFileReader();
		}
		return instance;
	}

	public Configuration getConfiguration() {
		return cfg;
	}

}
