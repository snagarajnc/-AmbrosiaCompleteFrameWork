package com.nectar.ambrosia.reporting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.Status;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class TestSuite {
	private static final String TESTCASES_FOLDERNAME = "testcases";
	String name;
	Status status;
	int passed;
	int failed;
	int warning;
	int done;
	int unknown;
	int total;

	Date startTime;
	Date endTime;

	Map<String, TestCase> testCases;

	String reportPath;
	String testCasePath;
	String qualifiedHTMLPath;
	String qualifiedJSONPath;

	public TestSuite(String name) {
		this.name = name;
		testCases = new HashMap<String, TestCase>();
		setAndCreateReportPath();
		setQualifiedHTMLPath();
		setQualifiedJSONPath();
		setTestCasePath();
	}

	public synchronized void addTestCase(TestCase tc) {
		testCases.put(tc.getName(), tc);

	}

	public Map<String, TestCase> getTestCases() {
		return testCases;
	}

	public void updateStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setAndCreateReportPath() {
		this.reportPath = CurrentRunProperties.getReportPath() + this.name + "/";
		CommonUtilities.createFolder(this.reportPath);
	}

	public String getTestCasePath() {
		return testCasePath;
	}

	public String getTestCaseRelativePath() {
		return TESTCASES_FOLDERNAME + "/";
	}

	public void setTestCasePath() {
		this.testCasePath = getReportPath() + TESTCASES_FOLDERNAME + "/";
		CommonUtilities.createFolder(this.testCasePath);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void updateStatus() {

		for (String key : testCases.keySet()) {
			int curTestCaseOridinal = testCases.get(key).getStatus().ordinal();
			int curTestSuiteStatus = getStatus().ordinal();
			updateCaseStatusCount(testCases.get(key).getStatus());
			if (curTestSuiteStatus > curTestCaseOridinal) {
				setStatus(testCases.get(key).getStatus());
			}
			this.total++;
		}
		// create HTML and JSON data
		if (CurrentRunProperties.isGenerateJSON()) {
			CommonUtilities.createFileWithContent(getQualifiedJSONPath(), getTestSuiteJSON().toString());
		}
	}

	public TestCase getTestCase(String tcname) {
		return this.testCases.get(tcname);
	}

	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getWarning() {
		return warning;
	}

	public void setWarning(int warning) {
		this.warning = warning;
	}

	public int getDone() {
		return done;
	}

	public void setDone(int done) {
		this.done = done;
	}

	public int getUnknown() {
		return unknown;
	}

	public void setUnknown(int unknown) {
		this.unknown = unknown;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getQualifiedHTMLPath() {
		return qualifiedHTMLPath;
	}

	public String getQualifiedJSONPath() {
		return qualifiedJSONPath;
	}

	public void setQualifiedHTMLPath() {
		this.qualifiedHTMLPath = getReportPath() + this.name + ".html";
	}

	public void setQualifiedJSONPath() {
		this.qualifiedJSONPath = getReportPath() + this.name + ".json";
	}

	public void updateCaseStatusCount(Status status) {
		if (Status.PASS.equals(status)) {
			this.passed++;
		} else if (Status.FAIL.equals(status)) {
			this.failed++;
		} else if (Status.WARNING.equals(status)) {
			this.warning++;
		} else if (Status.DONE.equals(status)) {
			this.done++;
		} else {
			this.unknown++;
		}
	}

	public JsonObject getTestSuiteJSON() {
		JsonObject jo = new JsonObject();
		jo.addProperty(Consts.J_TSNAME, getName());
		jo.addProperty(Consts.J_TSSTARTTIME, CommonUtilities.getFormatedDateAndTime(getStartTime()));
		jo.addProperty(Consts.J_TSENDTIME, CommonUtilities.getFormatedDateAndTime(getEndTime()));
		jo.addProperty(Consts.J_TSSTATUS, getStatus().name());
		jo.addProperty(Consts.J_PASS, getPassed());
		jo.addProperty(Consts.J_FAIL, getFailed());
		jo.addProperty(Consts.J_WARNING, getWarning());
		jo.addProperty(Consts.J_DONE, getDone());
		jo.addProperty(Consts.J_UNKNOWN, getUnknown());
		jo.addProperty(Consts.J_TOTAL, getTotal());
		jo.add(Consts.J_TSTESTCASES, getTestCasesJson());
		return jo;
	}

	private JsonArray getTestCasesJson() {
		JsonArray ja = new JsonArray();

		for (String tc : testCases.keySet()) {
			ja.add(testCases.get(tc).getTestCaseJSON());
		}
		return ja;
	}

}
