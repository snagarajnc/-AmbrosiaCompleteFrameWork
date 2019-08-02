package com.nectar.ambrosia.reporting.listeners;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.reporting.HTMLHandler;
import com.nectar.ambrosia.reporting.TestCase;
import com.nectar.ambrosia.reporting.TestCaseRow;
import com.nectar.ambrosia.reporting.TestSuite;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class SuitesManager {
	static Logger log = Logger.getLogger(SuitesManager.class.getName());

	private static Map<String, TestSuite> suites = new HashMap<String, TestSuite>();
	private static SuitesManager suiteManager = null;

	private static List<TestCaseRow> testCaseRow = new LinkedList<TestCaseRow>();
	private static String TestCasesHTMLData = "";

	

	private SuitesManager() {
		TestCasesHTMLData = HTMLHandler.createHTMLTable("testcases");
		TestCasesHTMLData += HTMLHandler.createHTMLRow();

		// create columns
		for (String col : HTMLHandler.TESTCASESHEADERS) {
			TestCasesHTMLData += HTMLHandler.addHTMLTableHeaderData(col);
		}

		TestCasesHTMLData += HTMLHandler.endHTMLRow();
	}

	public static SuitesManager getInstance() {
		if (suiteManager == null) {
			synchronized (SuitesManager.class) {
				if (suiteManager == null) {
					suiteManager = new SuitesManager();
				}
			}
		}
		return suiteManager;
	}

	public synchronized void addTestSuite(String name, TestSuite testSuite) {
		suites.put(name, testSuite);
	}

	public synchronized void addTestCaseRow(TestCaseRow tcRow) {
		testCaseRow.add(tcRow);
		updateTestCasesHTML(tcRow);
	}

	public TestSuite getTestSuite(String name) {
		return suites.get(name);
	}

	public Map<String, TestSuite> getAllSuites() {
		return suites;
	}
	
	public void end() {
		generateReport();
		endTestCasesHTML();
		
	}
	

	public JsonObject getAllSuitesJsonObject() {
		JsonArray ja = new JsonArray();
		for (String ts : suites.keySet()) {
			ja.add(suites.get(ts).getTestSuiteJSON());
		}
		JsonObject jo = new JsonObject();
		jo.add(Consts.J_TESTSUITES, ja);

		return jo;
	}

	public void generateReport() {
		String templateContent = CommonUtilities.getFileContent(Consts.HTMLREPORTEMPLPATH);
		templateContent = templateContent.replace(Consts.TESTSUITEJSON, getAllSuitesJsonObject().toString());
		String reportFile = CurrentRunProperties.getReportPath() + "automation-report.html";

		CommonUtilities.createFileWithContent(reportFile, templateContent);
	}
	
	public void endTestCasesHTML() {
		TestCasesHTMLData += HTMLHandler.endHTMLTable();
	}

	public void updateTestCasesHTML(TestCaseRow tcRow) {
		TestCasesHTMLData += HTMLHandler.createHTMLRow();
		HashMap<String, String> data = tcRow.getData();

		for (String key : data.keySet()) {
			TestCasesHTMLData += HTMLHandler.addHTMLRowData(data.get(key), "", "column='" + key + "'");
		}
		
		TestCasesHTMLData += HTMLHandler.endHTMLRow();
	}
	
	public String getTestCasesHTMLData() {
		return TestCasesHTMLData;
	}
}
