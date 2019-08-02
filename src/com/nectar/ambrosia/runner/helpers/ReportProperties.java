package com.nectar.ambrosia.runner.helpers;

import java.io.FileInputStream;
import java.util.Properties;

public class ReportProperties {
	private static final String filePath = "./properties/report.properties";
	private static final Properties prop = new Properties();;

	public static void loadProp() throws Exception {
		try {
			prop.load(new FileInputStream(filePath));
		} catch (Exception e) {
			throw new Exception("Unable to load propery file" + e.getMessage());
		}
	}
	
	
	
	
}
