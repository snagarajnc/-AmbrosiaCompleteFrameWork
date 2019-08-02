package com.nectar.ambrosia.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Consts {

	
	public final static int TESTDATA_HEADER_ROW = 0;
	public final static int KEY_INDEX = 0;
	public final static int VALUE_INDEX = 1;
	public final static String REPORTFILENAME = "report.json";
	
	public final static String CN_TESTCASEID = "tcid";
	public final static String CN_TESTCASESHORTNAME = "tcshortname";
	public final static String CN_TESTCLASSES = "funcstorun";
	public final static String CN_ENVIRONMENT = "env";
	public final static String CN_MODULENAME = "module";
	public final static String CN_TESTCASEDESCRIPTION = "tcdescription";
	public final static String CN_RUNFLAG = "runflag";
	public final static String CN_DATASHEET = "dataSheetName";

	public final static String COMMA_SPLITTER = "\\,";
	
	public final static String TESTS_SPLITTER = "\\|";
	public final static String SYMBOL_PIPE = "|";

	
	//property values
	public static final String PARALLEL = "parallel";
	public static final String SEQUENCE = "s";
	public static final String BYTESTCASE = "byTestCase";
	public static final String BYMODULE = "byModule";
	public static final String LOCAL = "local";
	public static final String GRID = "grid";
	
	//browser names
	public static final String CHROME = "chrome";
	public static final String FIREFOX = "firefox";
	public static final String IE = "ie";
	//
	
	//property names
	public static final String PN_RUNTYPE = "runType";
	public static final String PN_RUNBY = "runBy";
	public static final String PN_THREADCOUNT = "threadCount";
	public static final String PN_MASTERDATAPATH = "masterSheetPath";
	public static final String PN_MODULES= "modules";
	public static final String PN_ISBROWSERPARALLEL = "isBrowserParallel";
	public static final String PN_USEBDD = "useBDD";
	public static final String PN_FEATUREFILEPATH = "featureFilePath";
	public static final String PN_BROWSER = "browser";
	public static final String PN_BPATH = "path";
	public static final String PN_BNAME = "name";
	public static final String PN_RUNVIA = "runVia";
	public static final String PN_HUBURL = "hubURL";
	public static final String PN_PAGELOADTIMEOUT = "pageLoadTimeOut";
	public static final String PN_WEBDRIVERWAITTIMEOUT = "webdriverWaitTimeout";
	public static final String PN_DELETEALLCOCKIES = "deleteAllCockies";
	public static final String PN_TESTDATAPATH = "testDataPath";
	public static final String PN_REPORTPATH = "reportPath";
	public static final String PN_GENERATEHTML = "generateHTML";
	public static final String PN_GENERATEJSON = "generateJSON";
	
	
	
	public static int maxThreadCount = 20;
	
	
	
	public static final String J_ACUTAL = "actual";
	public static final String J_EXPECTED = "expected";
	public static final String J_STATUS = "status";
	public static final String J_ISTITLE = "isTitile";
	public static final String J_SCREENSHOTPATH = "imgPath";
	
	public static final String J_TCNAME = "tcName";
	public static final String J_TCSHORTNAME = "tcShortName";
	public static final String J_TCDESC = "tcDesc";
	public static final String J_TCMODULE = "tcModule";
	public static final String J_TCENV = "tcEnv";
	public static final String J_TCBROWSER = "tcBrowser";
	public static final String J_TCSTATUS= "tcStatus";
	public static final String J_TCSTARTTIME = "tcStarttime";
	public static final String J_TCENDTIME = "tcEndtime";
	public static final String J_TCTESTSTEPS = "tcTeststeps";
	public static final String J_TCTESTSTEP = "teststep-";
	public static final String J_TCADDINFOS = "tcInformations";
	public static final String J_TCADDINFO = "info";
	public static final String J_TCADDINFOFILE = "info-file";
	public static final String J_TCHOST = "host";
	
	public static final String J_TSNAME = "tsName";
	public static final String J_TSSTARTTIME = "tsStarttime";
	public static final String J_TSENDTIME = "tsEndtime";
	public static final String J_TSSTATUS= "tsStatus";
	public static final String J_TSTESTCASES= "tsTestcases";
	public static final String J_TSTESTCASE= "testcase-";
	
	public static final String J_PASS= "pass";
	public static final String J_FAIL= "fail";
	public static final String J_WARNING= "warning";
	public static final String J_DONE= "done";
	public static final String J_UNKNOWN= "unknown";
	public static final String J_TOTAL= "total";
	
	
	
	public static final String J_TESTSUITES= "testSuites";
	public static final String J_TESTSUITE= "testSuite-";
			
	public static final String DEFAULTDATAPROVIDER = "dataprovider";
	
	
	public static final String H_TESTSETPCLASS = "teststep";
	public static final String H_TESTCASECLASS = "testcase";
	public static final String H_TESTSUITECLASS = "testsuite";
	
	//Test case report consts
	public static final String HTMLREPORTEMPLPATH = "./support/report.html";
	public static final String TESTSUITEJSON = "${TestSuiteJSON}";
	public static final String TESTCASE_HTMLREPORTEMPLPATH = "./support/TestCaseTEmplate.html";
	public static final String HTML_TESTCASENAME = "${TestCaseName}";
	public static final String HTML_TCDESC = "${TestCaseDescription}";
	public static final String HTML_TCENV = "${TestCaseEnvironment}";
	public static final String HTML_TCMACHINE = "${TestCaseMachine}";
	public static final String HTML_TCDATE = "${TestCaseExecutionTime}";
	public static final String HTML_TCSTARTTIME = "${TestCaseExecutionStartTime}";
	public static final String HTML_TCENDTIME = "${TestCaseExecutionEndTime}";
	public static final String HTML_TCDURATION = "${TestCaseDuration}";
	public static final String HTML_TCSTATUS = "${TestCaseStatus}";
	public static final String HTML_TCADDINAL = "${TestCaseAdditionalFiles}";
	public static final String HTML_TCSTEPS = "${TestCaseTestStepsTable}";
	public static final String HTML_TCPASS = "${TestCasePassedSteps}";
	public static final String HTML_TCFAIL = "${TestCaseFailedSteps}";
	public static final String HTML_TCWARNING = "${TestCaseWarningSteps}";
	public static final String HTML_TCDONE = "${TestCaseDoneSteps}";
	public static final String HTML_TCUNKNOWN = "${TestCaseUnkownSteps}";
	public static final int CAPTION_COL_SPAN = 6;
	
	public static final String HTML_SUPPORTFILELOCATION = "./support/support-files";
	
	public static final List<File> getHTMLReportSupportFiles(){
		List<File> files = new ArrayList<File>();
		files.add(new File(TESTCASE_HTMLREPORTEMPLPATH));
		files.add(new File("./support/support-files"));
		return files;
	}

	public static final List<String> ALERTNAMES = Arrays.asList(new String[] {"Critical","Major","Minor","Warning","Good","No Activity"});
	
	
	
}
