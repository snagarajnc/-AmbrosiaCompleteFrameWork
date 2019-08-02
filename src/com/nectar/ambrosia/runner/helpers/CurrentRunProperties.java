package com.nectar.ambrosia.runner.helpers;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.RunType;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class CurrentRunProperties {

	static Logger log = Logger.getLogger(CurrentRunProperties.class.getName());
	
	private static final String filePath = "./properties/run.properties";
	private static final Properties prop = new Properties();
	private static final String EXECUTIONFOLDER = "run-" + CommonUtilities.getDateAndTimeString();

	public static void loadProp() throws Exception {
		try {
			prop.load(new FileInputStream(filePath));
		} catch (Exception e) {
			log.error("Unable to load propery file" + e.getMessage());
			throw new Exception("Unable to load propery file" + e.getMessage());
		}
	}

	// get run type
	public static RunType getRunType() {
		return RunType.valueOf(prop.getProperty(Consts.PN_RUNTYPE));
	}

	// get run by
	public static RunType getRunBy() {
		return RunType.valueOf(prop.getProperty(Consts.PN_RUNBY));
	}

	// get master data path
	public static String getMasterDataPath() {
		return prop.getProperty(Consts.PN_MASTERDATAPATH);
	}

	// get list of browsers with name and path as HashMap
	public static HashMap<String, String> getBrowserList() {
		HashMap<String, String> lst = new HashMap<String, String>();
		String splitter = "\\|";
		for (String pn : prop.stringPropertyNames()) {
			if (pn.startsWith(Consts.PN_BROWSER)) {
				String pv = prop.getProperty(pn);
				if (StringUtils.isNotEmpty(pv)) {
					if (pv.split(splitter).length == 2) {
						lst.put(pv.split(splitter)[0], pv.split(splitter)[1]);
					}
				}
			}
		}
		return lst;
	}

	// get list of modules
	public static List<String> getModuleList() {
		String modules = prop.getProperty(Consts.PN_MODULES);
		return Arrays.asList(modules.split(Consts.COMMA_SPLITTER));
	}

	// get browser parallel
	public static boolean getBrowserParallel() {
		return Boolean.parseBoolean(prop.getProperty(Consts.PN_ISBROWSERPARALLEL, "false"));
	}

	// get BDD useage
	public static boolean isBDDRun() {
		return Boolean.parseBoolean(prop.getProperty(Consts.PN_USEBDD, "false"));
	}

	// get feature file path
	public static String getFeatureFilePath() {
		return prop.getProperty(Consts.PN_FEATUREFILEPATH);
	}

	// get feature file path
	public static int getMaxThreadCount() {
		return Integer.parseInt(prop.getProperty(Consts.PN_THREADCOUNT, "10"));
	}

	// get run via type
	public static String getRunVia() {
		return prop.getProperty(Consts.PN_RUNVIA);
	}

	// get hub URL
	public static URL getHubURL() {
		try {
			return new URL(prop.getProperty(Consts.PN_HUBURL));
		} catch (MalformedURLException e) {
			log.error("URL is not correct. " + e.getMessage());
		}
		return null;
	}

	// get page load timeout
	public static int getPageLoadTimeout() {
		return Integer.parseInt(prop.getProperty(Consts.PN_PAGELOADTIMEOUT));
	}

	// get web driver timeout
	public static int getWebDriverTimeout() {
		return Integer.parseInt(prop.getProperty(Consts.PN_WEBDRIVERWAITTIMEOUT));
	}
	public static Duration getWebDriverTimeoutDuration() {
		return Duration.ofSeconds(getWebDriverTimeout());
	}
	public static Duration getPollEveryTimeoutDuration() {
		return Duration.ofSeconds(3);
	}
	// get web driver timeout
	public static boolean isDeleteAllCokies() {
		return Boolean.parseBoolean(prop.getProperty(Consts.PN_DELETEALLCOCKIES));
	}

	// get the test data path
	public static String getTestDataPath() {
		return prop.getProperty(Consts.PN_TESTDATAPATH);
	}

	// get the report path
	public static String getReportPath() {
		return prop.getProperty(Consts.PN_REPORTPATH) + EXECUTIONFOLDER + "/";
	}

	// get isGenerate json
	public static boolean isGenerateJSON() {
		return Boolean.parseBoolean(prop.getProperty(Consts.PN_GENERATEJSON));
	}

	// get isGenerate html
	public static boolean isGenerateHTML() {
		return Boolean.parseBoolean(prop.getProperty(Consts.PN_GENERATEHTML));
	}
}
