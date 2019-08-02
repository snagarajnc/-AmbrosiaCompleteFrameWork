package com.nectar.ambrosia.reporting;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.Status;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class TestCase {

	private static final String SCREENSHOT_FOLDERNAME = "screenhosts";

	private String name;
	private String shortName;
	private String description;

	private String module;
	private String environment;
	private String browser;
	private Status status;

	private Date startTime;
	private Date endTime;

	private HashMap<String, TestStep> testSteps;
	private Map<String, String> additionalInfo;

	private String host;

	private int passed;
	private int failed;
	private int warning;
	private int done;
	private int unknown;
	private int total;

	private String reportPath;
	private String qualifiedHTMLPath;
	private String qualifiedJSONPath;
	private String screenShotPath;

	TestSuite testSuite;

	public TestCase(String name, TestSuite testSuite) {
		prepareData(name, testSuite);
	}

	public TestCase(String name, TestSuite testSuite, String shortname) {
		prepareData(name, testSuite, shortname);
	}

	public TestCase(String name, String module, TestSuite testSuite) {
		prepareData(name, module, testSuite);
	}

	public TestCase(String name, String module, String environment, TestSuite testSuite) {
		prepareData(name, module, environment, testSuite);
	}

	private void prepareData(String name, TestSuite testSuite) {
		this.name = name;
		testSteps = new LinkedHashMap<String, TestStep>();
		additionalInfo = new HashMap<String, String>();
		setTestSuite(testSuite);
		setAndCreateReportPath();
		setQualifiedHTMLPath();
		setQualifiedJSONPath();
		setScreenShotPath();
	}

	private void prepareData(String name, TestSuite testSuite, String shortname) {
		this.shortName = shortname;
		prepareData(name, testSuite);
	}

	private void prepareData(String name, String module, TestSuite testSuite) {
		this.module = module;
		prepareData(name, testSuite);
	}

	private void prepareData(String name, String module, String environment, TestSuite testSuite) {
		this.environment = environment;
		prepareData(name, module, testSuite);
	}

	public void updateStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHost() {
		if (host == null)
			return "running locally";

		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getReportPath() {
		return reportPath;
	}

	public String getReportRelativePath() {
		return this.testSuite.getTestCaseRelativePath() + getShortName() + "/";
	}

	public void setAndCreateReportPath() {
		this.reportPath = this.testSuite.getTestCasePath() + getShortName() + "/";
		CommonUtilities.createFolder(this.reportPath);
	}

	public TestSuite getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(TestSuite testSuite) {
		this.testSuite = testSuite;
	}

	public String getQualifiedHTMLPath() {
		return qualifiedHTMLPath;
	}

	public String getQualifiedJSONPath() {
		return qualifiedJSONPath;
	}

	public void setQualifiedHTMLPath() {
		this.qualifiedHTMLPath = getReportPath() + getShortName() + ".html";
	}

	public void setQualifiedJSONPath() {
		this.qualifiedJSONPath = getReportPath() + getShortName() + ".json";
	}

	public String getScreenShotPath() {
		return screenShotPath;
	}

	public String getScreenShotRelativePath() {
		return SCREENSHOT_FOLDERNAME + "/";
	}

	public void setScreenShotPath() {
		this.screenShotPath = getReportPath() + SCREENSHOT_FOLDERNAME + "/";
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void updateStatus() {
		for (String key : testSteps.keySet()) {
			int curStepOridinal = testSteps.get(key).getStatus().ordinal();
			int curTestCaseStatus = getStatus().ordinal();
			if (!testSteps.get(key).isTitle()) {
				updateStepStatusCount(testSteps.get(key).getStatus());
				if (curTestCaseStatus > curStepOridinal) {
					setStatus(testSteps.get(key).getStatus());
				}
			}
			this.total++;
		}
		// create HTML and JSON data
		if (CurrentRunProperties.isGenerateJSON()) {
			CommonUtilities.createFileWithContent(getQualifiedJSONPath(), getTestCaseJSON().toString());
		}
		if (CurrentRunProperties.isGenerateHTML()) {
			generateTestCaseReport();
		}
	}

	public void updateStepStatusCount(Status status) {
		if (Status.PASS.equals(status)) {
			this.passed++;
		} else if (Status.FAIL.equals(status)) {
			this.failed++;
		} else if (Status.WARNING.equals(status)) {
			this.warning++;
		} else if (Status.DONE.equals(status) || Status.INFO.equals(status)) {
			this.done++;
		} else {
			this.unknown++;
		}

	}

	public void generateTestCaseReport() {
		String htmlData = getTestCaseReportHTMLData();
		CommonUtilities.createFileWithContent(getQualifiedHTMLPath(), htmlData);
	}

	public void addStep(TestStep testStep) {
		int stepNumber = testSteps.size() + 1;
		testSteps.put(Integer.toString(stepNumber), testStep);
	}

	public void addStep(List<TestStep> testSteps) {
		for (TestStep testStep : testSteps) {
			int stepNumber = this.testSteps.size() + 1;
			this.testSteps.put(Integer.toString(stepNumber), testStep);
		}
	}

	public void addAdditionalInfo(String additionalInformation) {
		if (!this.additionalInfo.containsKey(additionalInformation)) {
			this.additionalInfo.put(additionalInformation, null);
		}
	}

	public void addAdditionalInfo(String displayText, String relativeFilepath) {
		if (!this.additionalInfo.containsKey(displayText)) {
			this.additionalInfo.put(displayText, relativeFilepath);
		}
	}

	public void setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	public String getName() {
		return this.name;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Map<String, TestStep> getTestSteps() {
		return testSteps;
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

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
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

	public String getDurationString() {
		return CommonUtilities.getFormattedDateAndTimeDifference(getStartTime(), getEndTime());
	}
	
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public JsonObject getTestCaseJSON() {
		JsonObject jo = new JsonObject();
		jo.addProperty(Consts.J_TCNAME, getName());
		jo.addProperty(Consts.J_TCSHORTNAME, getShortName());
		jo.addProperty(Consts.J_TCDESC, getDescription());
		jo.addProperty(Consts.J_TCMODULE, getModule());
		jo.addProperty(Consts.J_TCENV, getEnvironment());
		jo.addProperty(Consts.J_TCBROWSER, getBrowser());
		jo.addProperty(Consts.J_TCHOST, getHost());
		jo.addProperty(Consts.J_TCSTATUS, getStatus().name());
		jo.addProperty(Consts.J_TCSTARTTIME, CommonUtilities.getFormatedDateAndTime(getStartTime()));
		jo.addProperty(Consts.J_TCENDTIME, CommonUtilities.getFormatedDateAndTime(getEndTime()));
		jo.addProperty(Consts.J_PASS, getPassed());
		jo.addProperty(Consts.J_FAIL, getFailed());
		jo.addProperty(Consts.J_WARNING, getWarning());
		jo.addProperty(Consts.J_DONE, getDone());
		jo.addProperty(Consts.J_UNKNOWN, getUnknown());
		jo.addProperty(Consts.J_TOTAL, getTotal());
		jo.add(Consts.J_TCTESTSTEPS, getTestStepsJson());
		jo.add(Consts.J_TCADDINFOS, getAdditionalInfoJson());
		return jo;
	}

	private JsonArray getTestStepsJson() {

		JsonArray ja = new JsonArray();

		for (String ts : testSteps.keySet()) {
			ja.add(testSteps.get(ts).getTestStepJSON());
		}
		return ja;
	}

	private JsonObject getAdditionalInfoJson() {
		JsonObject jo = new JsonObject();

		for (String info : additionalInfo.keySet()) {
			JsonObject joInfo = new JsonObject();
			if (additionalInfo.get(info) == null) {
				joInfo.addProperty(Consts.J_TCADDINFO, info);
			} else {
				joInfo.addProperty(Consts.J_TCADDINFOFILE, additionalInfo.get(info));
			}
		}
		return jo;
	}

	public String getTestCaseReportHTMLData() {
		String templateContent = CommonUtilities.getFileContent(Consts.TESTCASE_HTMLREPORTEMPLPATH);
		
		templateContent = templateContent.replace(Consts.HTML_TESTCASENAME, getName());
		templateContent = templateContent.replace(Consts.HTML_TCDESC, getDescription());
		templateContent = templateContent.replace(Consts.HTML_TCENV, getEnvironment());
		templateContent = templateContent.replace(Consts.HTML_TCMACHINE, getHost());
		templateContent = templateContent.replace(Consts.HTML_TCDATE, CommonUtilities.getFormatedDate(getStartTime()));
		templateContent = templateContent.replace(Consts.HTML_TCSTARTTIME,
				CommonUtilities.getFormatedDateAndTime(getStartTime()));
		templateContent = templateContent.replace(Consts.HTML_TCENDTIME,
				CommonUtilities.getFormatedDateAndTime(getEndTime()));
		templateContent = templateContent.replace(Consts.HTML_TCDURATION, getDurationString());
		templateContent = templateContent.replace(Consts.HTML_TCSTATUS, getStatus().name());
		templateContent = templateContent.replace(Consts.HTML_TCDURATION, getDurationString());
		// TODO add additional info html
		// templateContent = templateContent.replace(Consts.HTML_TCADDINAL,null);

		templateContent = templateContent.replace(Consts.HTML_TCSTEPS, getTestStepTableHTML());
		templateContent = templateContent.replace(Consts.HTML_TCPASS, Integer.toString(getPassed()));
		templateContent = templateContent.replace(Consts.HTML_TCFAIL, Integer.toString(getFailed()));
		templateContent = templateContent.replace(Consts.HTML_TCWARNING, Integer.toString(getWarning()));
		templateContent = templateContent.replace(Consts.HTML_TCDONE, Integer.toString(getDone()));
		templateContent = templateContent.replace(Consts.HTML_TCUNKNOWN, Integer.toString(getUnknown()));

		return templateContent;
	}

	public String getTestStepsAsHTMLTableRow(String sno, TestStep testStep) {
		String testStepHTMLTablerow = HTMLHandler.createHTMLRow(HTMLHandler.TESTSTEPROW);
		if (testStep.isTitle()) {
			testStepHTMLTablerow += HTMLHandler.addHTMLRowData(testStep.getActualResult(), HTMLHandler.CAPTION,
					Consts.CAPTION_COL_SPAN);
		} else {
			// update step no
			testStepHTMLTablerow += HTMLHandler.addHTMLRowData(sno, HTMLHandler.SNO);
			if (testStep.getExpectedResult() != null) {
				testStepHTMLTablerow += HTMLHandler.addHTMLRowData(testStep.getExpectedResult(), HTMLHandler.DEFAULT);
			} else {
				testStepHTMLTablerow += HTMLHandler.addHTMLRowData("--na--", HTMLHandler.DEFAULT);
			}
			testStepHTMLTablerow += HTMLHandler.addHTMLRowData(testStep.getActualResult(), HTMLHandler.DEFAULT);
			testStepHTMLTablerow += HTMLHandler.addHTMLRowData(testStep.getStatus().name(),
					HTMLHandler.getStatusClass(testStep.getStatus()));
			if (StringUtils.isNotBlank(testStep.getScreenShotPath())) {
				testStepHTMLTablerow += HTMLHandler.addHTMLRowData(
						HTMLHandler.addHTMLPopupLink(testStep.getScreenShotPath(), HTMLHandler.VIEW),
						HTMLHandler.DEFAULT);
			} else {
				testStepHTMLTablerow += HTMLHandler.addHTMLRowData("--na--", HTMLHandler.DEFAULT);
			}
		}
		testStepHTMLTablerow += HTMLHandler.endHTMLRow();
		return testStepHTMLTablerow;
	}

	public String getTestStepsHeaderHTML() {
		String htmlTable = HTMLHandler.createHTMLRow(HTMLHandler.TESTSTEPHEADER);
		// Adding Test Steps columns
		for (String header : HTMLHandler.TESTSTEPHEADERS) {
			htmlTable += HTMLHandler.addHTMLTableHeaderData(header);
		}
		htmlTable += HTMLHandler.endHTMLRow();
		return htmlTable;
	}

	public String getTestStepTableHTML() {
		String testStepHTML = HTMLHandler.createHTMLTable(HTMLHandler.TESTSTEPTABLE);
		testStepHTML += getTestStepsHeaderHTML();

		for (String key : testSteps.keySet()) {
			testStepHTML += getTestStepsAsHTMLTableRow(key, testSteps.get(key));
		}
		testStepHTML += HTMLHandler.endHTMLTable();
		return testStepHTML;
	}
}
