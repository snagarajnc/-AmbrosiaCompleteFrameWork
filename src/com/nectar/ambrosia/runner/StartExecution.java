package com.nectar.ambrosia.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.RunType;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.runner.helpers.TestCaseData;
import com.nectar.ambrosia.utilities.CommonUtilities;
import com.nectar.ambrosia.utilities.csv.CSVFileData;
import com.nectar.ambrosia.utilities.csv.CSVReader;

public class StartExecution {

	static Logger log = Logger.getLogger(StartExecution.class.getName());

	private static Map<String, TestCaseData> masterData = new HashMap<String, TestCaseData>();
	private static List<TestCaseData> testCaseData = new ArrayList<TestCaseData>();
	private static List<String> browsers = new ArrayList<String>();
	private static List<String> modules = new ArrayList<String>();
	private static boolean isBrowserParallel = false;

	public static void main(String[] args) throws Exception {
		CurrentRunProperties.loadProp();
		updateLog4jConfiguration(CurrentRunProperties.getReportPath());
		prepareDataForExecution();
		// testRun();
		log.info(String.format(
				"Tests ready to run with %s no of modules, %s no of browsers, %s no of test cases and browser parallel run set to %s",
				modules.size(), browsers.size(), testCaseData.size(), Boolean.toString(isBrowserParallel)));
		triggerExecution();

	}

	public static void prepareDataForExecution() throws Exception {
		setMasterData(new CSVFileData(CSVReader.getCSVRecords(CurrentRunProperties.getMasterDataPath())));
		setTestCaseData();
		setModules();
		setBrowsers();
		setBrowserParallel();
		createReportFolder();
		createReportJSONFile();
	}

	public static void triggerExecution() {
		TestRunner tr = new TestRunner(getTestCaseData(), CurrentRunProperties.getRunType(),
				CurrentRunProperties.getRunBy());
		tr.setBrowserParallel(isBrowserParallel());
		tr.setBrowsers(getBrowsers());
		tr.setModules(getModules());
		log.debug("TestRunner is called");
		tr.run();
	}

	public static void createReportFolder() {
		String rp = CurrentRunProperties.getReportPath();
		CommonUtilities.createFolder(rp);
	}

	public static void createReportJSONFile() {
		String filepath = CurrentRunProperties.getReportPath() + Consts.REPORTFILENAME;
		try {
			new File(filepath).createNewFile();
			log.info("JSON file " + filepath + " is created");
		} catch (IOException e) {
			log.error("Unable to create report Json file. error is " + e.getMessage());
		}
	}

	public static List<String> getBrowsers() {
		return browsers;
	}

	public static void setBrowsers() {
		for (String browser : CurrentRunProperties.getBrowserList().keySet()) {
			browsers.add(browser);
		}
	}

	public static List<String> getModules() {
		return modules;
	}

	public static void setModules() {
		StartExecution.modules = CurrentRunProperties.getModuleList();
	}

	public static boolean isBrowserParallel() {
		return isBrowserParallel;
	}

	public static void setBrowserParallel() {
		StartExecution.isBrowserParallel = CurrentRunProperties.getBrowserParallel();
	}

	public static void setMasterData(CSVFileData csvFileData) {

		for (CSVRecord r : csvFileData.getListData()) {
			if (CommonUtilities.getBoolValue(r.get(Consts.CN_RUNFLAG))) {
				masterData.put(r.get(Consts.CN_TESTCASEID),
						new TestCaseData(r.get(Consts.CN_TESTCASEID), r.get(Consts.CN_TESTCASESHORTNAME),
								r.get(Consts.CN_TESTCASEDESCRIPTION), r.get(Consts.CN_MODULENAME),
								r.get(Consts.CN_TESTCLASSES), r.get(Consts.CN_ENVIRONMENT), r.get(Consts.CN_RUNFLAG),
								r.get(Consts.CN_DATASHEET)));
			}
		}
	}

	public Map<String, TestCaseData> getMasterData() {
		return masterData;
	}

	public static List<TestCaseData> getTestCaseData() {
		return testCaseData;
	}

	public static TestCaseData getTestCaseDataById(String tcID) {
		return masterData.get(tcID);
	}

	public static void setTestCaseData() {
		testCaseData.addAll(masterData.values());
	}

	private static void updateLog4jConfiguration(String logFileLocation) {
		Properties props = new Properties();
		try {
			InputStream configStream = new FileInputStream("properties/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			System.out.println("Error: Cannot laod configuration file ");
		}
		props.setProperty("logLocation", logFileLocation);
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);
	}

	@SuppressWarnings("unused")
	private static void testRun() throws IOException {
		// collect all test cases from the sheet
		CSVFileData csvFileData = new CSVFileData(CSVReader.getCSVRecords(CurrentRunProperties.getMasterDataPath()));

		List<CSVRecord> lstAllData = csvFileData.getListData();
		List<TestCaseData> lstTCs = new ArrayList<TestCaseData>();

		for (CSVRecord r : lstAllData) {
			lstTCs.add(new TestCaseData(r.get(Consts.CN_TESTCASEID), r.get(Consts.CN_TESTCASESHORTNAME),
					r.get(Consts.CN_TESTCASEDESCRIPTION), r.get(Consts.CN_MODULENAME), r.get(Consts.CN_TESTCLASSES),
					r.get(Consts.CN_ENVIRONMENT), r.get(Consts.CN_RUNFLAG), r.get(Consts.CN_DATASHEET)));
		}

		boolean s1 = false;
		boolean s2 = false;
		boolean s3 = false;
		boolean s4 = false;
		boolean s5 = false;
		boolean s6 = true;
		boolean s7 = false;
		boolean s8 = false;

		if (s1) {
			// RunType - Parallel
			// RunBy - ByTestCase
			// BrowserParallel - true
			TestRunner tr = new TestRunner(lstTCs, RunType.PARALLEL, RunType.BYTESTCASE);
			tr.setBrowserParallel(true);
			tr.setBrowsers(Arrays.asList(new String[] { "chrome", "firefox" }));
			tr.setModules(Arrays.asList(new String[] { "test01", "test02" }));
			tr.run();
		}
		if (s2) {
			// RunType - Parallel
			// RunBy - ByTestCase
			// BrowserParallel - false
			TestRunner tr1 = new TestRunner(lstTCs, RunType.PARALLEL, RunType.BYTESTCASE);
			tr1.setBrowserParallel(false);
			tr1.setBrowsers(Arrays.asList(new String[] { "chrome", "firefox" }));
			tr1.setModules(Arrays.asList(new String[] { "test01", "test02" }));
			tr1.run();
		}
		if (s3) {
			// RunType - Sequence
			// RunBy - ByTestCase
			// BrowserParallel - true
			TestRunner tr2 = new TestRunner(lstTCs, RunType.SEQUENCE, RunType.BYTESTCASE);
			tr2.setBrowserParallel(true);
			tr2.setBrowsers(Arrays.asList(new String[] { "chrome", "firefox", "ie" }));
			tr2.setModules(Arrays.asList(new String[] { "test01", "test02" }));
			tr2.run();
		}
		if (s4) {
			// RunType - Sequence
			// RunBy - ByTestCase
			// BrowserParallel - false
			TestRunner tr3 = new TestRunner(lstTCs, RunType.SEQUENCE, RunType.BYTESTCASE);
			tr3.setBrowserParallel(false);
			tr3.setBrowsers(Arrays.asList(new String[] { "chrome", "firefox" }));
			tr3.setModules(Arrays.asList(new String[] { "test01", "test02" }));
			tr3.run();
		}
		if (s5) {
			// RunType - Parallel
			// RunBy - ByModule
			// BrowserParallel - true
			TestRunner tr4 = new TestRunner(lstTCs, RunType.PARALLEL, RunType.BYMODULE);
			tr4.setBrowserParallel(true);
			tr4.setBrowsers(Arrays.asList(new String[] { "ie" }));
			tr4.setModules(Arrays.asList(new String[] { "test01", "test02" }));
			tr4.run();
		}
		if (s6) {
			// RunType - Parallel
			// RunBy - ByModule
			// BrowserParallel - false
			TestRunner tr5 = new TestRunner(lstTCs, RunType.PARALLEL, RunType.BYMODULE);
			tr5.setBrowserParallel(false);
			tr5.setBrowsers(Arrays.asList(new String[] { "chrome", "firefox" }));
			tr5.setModules(Arrays.asList(new String[] { "module1", "module2" }));
			tr5.run();
		}
		if (s7) {
			// RunType - Sequence
			// RunBy - ByModule
			// BrowserParallel - true
			TestRunner tr6 = new TestRunner(lstTCs, RunType.SEQUENCE, RunType.BYMODULE);
			tr6.setBrowserParallel(true);
			tr6.setBrowsers(Arrays.asList(new String[] { "chrome", "firefox" }));
			tr6.setModules(Arrays.asList(new String[] { "test01", "test02" }));
			tr6.run();
		}
		if (s8) {
			// RunType - Sequence
			// RunBy - ByModule
			// BrowserParallel - false
			TestRunner tr7 = new TestRunner(lstTCs, RunType.SEQUENCE, RunType.BYMODULE);
			tr7.setBrowserParallel(false);
			tr7.setBrowsers(Arrays.asList(new String[] { "chrome", "firefox" }));
			tr7.setModules(Arrays.asList(new String[] { "test01", "test02" }));
			tr7.run();
		}
	}

}
