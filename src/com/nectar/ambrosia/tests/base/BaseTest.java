package com.nectar.ambrosia.tests.base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.Status;
import com.nectar.ambrosia.reporting.TestCase;
import com.nectar.ambrosia.reporting.TestStep;
import com.nectar.ambrosia.reporting.TestSuite;
import com.nectar.ambrosia.reporting.listeners.SuitesManager;
import com.nectar.ambrosia.runner.StartExecution;
import com.nectar.ambrosia.utilities.CommonUtilities;
import com.nectar.ambrosia.utilities.PageWaiter;
import com.nectar.ambrosia.utilities.UserAction;
import com.nectar.ambrosia.utilities.WebDriverUtility;
import com.nectar.ambrosia.utilities.csv.CSVFileData;

public class BaseTest implements IBaseTest {
	static Logger log = Logger.getLogger(BaseTest.class.getName());

	List<TestStep> testSteps = new ArrayList<TestStep>();
	Map<String, String> additionalInfo = new HashMap<String, String>();
	public WebDriver wDriver;
	public WebDriverWait wDriverWait;
	private WebDriverUtility wDriverUtility;
	public UserAction userAction;
	private CSVFileData testData;
	private TestCase testCase;
	private String browserName;
	public PageWaiter pageWaiter;

	public BaseTest() {

	}

	@BeforeClass
	@Parameters({ "browser" })
	public void beforeTest(String browser, ITestContext result) throws Exception {
		String tcName = result.getName();
		browserName = browser;
		wDriverUtility = new WebDriverUtility(this);
		wDriver = wDriverUtility.getBrowser(browser);
		wDriverWait = wDriverUtility.getWebDriverWait(wDriver);
		userAction = new UserAction(this, wDriver, wDriverWait);
		pageWaiter = new PageWaiter(this, wDriver, wDriverWait);
		try {
			testData = new CSVFileData(
					CommonUtilities.getTestDataPath(StartExecution.getTestCaseDataById(tcName).getDataSheet()));
			if (!testData.setCurRowNumber(Consts.CN_TESTCASEID, tcName)) {
				Fail("no row found in the data sheet for " + tcName);
				throw new Exception("no row found in the data sheet for " + tcName);
			}
		} catch (IOException e) {
			log.error("Unable to locate the test data sheet for the test case " + tcName);
		}
		setTestCase(SuitesManager.getInstance().getTestSuite(result.getSuite().getName()).getTestCase(tcName));

	}

	public String getTestData(String colName) {
		return testData.getValue(colName);
	}

	@AfterClass
	public void afterTest(ITestContext result) {
		TestSuite s = SuitesManager.getInstance().getTestSuite(result.getSuite().getName());
		TestCase tc = s.getTestCase(result.getName());
		tc.addStep(testSteps);
		tc.setAdditionalInfo(additionalInfo);
		tc.setBrowser(browserName);
		// wDriver.quit();
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	public void takeScreenShot(String fileName) {
		// Take screenshot and store as a file format
		File src = ((TakesScreenshot) wDriver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(src, new File(fileName));
		} catch (IOException e) {
			log.error("Unable to take screenshot");
		}
	}

	public void addAdditionalInfo(String info) {
		log.info("Additional info: " + info);
		additionalInfo.put(info, null);
	}

	public void addAdditionalFile(String displayName, String relativeFilePath) {
		log.info("Addtional file : path > " + relativeFilePath + " | displayName > " + displayName);
		additionalInfo.put(displayName, relativeFilePath);
	}

	@Override
	public void Pass(String actual) {
		log.info("PASS :: Acutal result- " + actual);
		testSteps.add(new TestStep(actual, Status.PASS));
	}

	@Override
	public void Pass(String expected, String actual) {
		log.info("PASS :: Expected result- " + expected + " | Acutal result- " + actual);
		testSteps.add(new TestStep(actual, expected, Status.PASS));
	}

	@Override
	public void Fail(String actual) {
		log.error("FAIL :: Acutal result- " + actual);
		testSteps.add(new TestStep(actual, Status.FAIL));
	}

	@Override
	public void Fail(String expected, String actual) {
		log.error("FAIL :: Expected result- " + expected + " | Acutal result- " + actual);
		testSteps.add(new TestStep(actual, expected, Status.FAIL));
	}

	@Override
	public void Warning(String actual) {
		log.info("WARNING :: Acutal result- " + actual);
		testSteps.add(new TestStep(actual, Status.WARNING));
	}

	@Override
	public void Warning(String expected, String actual) {
		log.info("WARNING :: Expected result- " + expected + " | Acutal result- " + actual);
		testSteps.add(new TestStep(actual, expected, Status.WARNING));
	}

	@Override
	public void Done(String actual) {
		log.info("DONE :: Acutal result- " + actual);
		testSteps.add(new TestStep(actual, Status.DONE));

	}

	@Override
	public void Done(String expected, String actual) {
		log.info("DONE :: Expected result- " + expected + " | Acutal result- " + actual);
		testSteps.add(new TestStep(actual, expected, Status.DONE));
	}

	@Override
	public void Info(String actual) {
		log.info("INFO :: Acutal result- " + actual);
		testSteps.add(new TestStep(actual, Status.INFO));
	}

	@Override
	public void Info(String expected, String actual) {
		log.info("INFO :: Expected result- " + expected + " | Acutal result- " + actual);
		testSteps.add(new TestStep(actual, expected, Status.INFO));
	}

	@Override
	public void Pass(String actual, boolean takeScreenshot) {
		log.info("PASS :: Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, Status.PASS, stepFileName.getRelativePath()));
	}

	@Override
	public void Pass(String expected, String actual, boolean takeScreenshot) {
		log.info("PASS :: Expected result- " + expected + " | Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, expected, Status.PASS, stepFileName.getRelativePath()));
	}

	@Override
	public void Fail(String actual, boolean takeScreenshot) {
		log.info("FAIL :: Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, Status.FAIL, stepFileName.getRelativePath()));

	}

	@Override
	public void Fail(String expected, String actual, boolean takeScreenshot) {
		log.info("FAIL :: Expected result- " + expected + " | Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, expected, Status.FAIL, stepFileName.getRelativePath()));
	}

	@Override
	public void Warning(String actual, boolean takeScreenshot) {
		log.info("WARNING :: Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, Status.WARNING, stepFileName.getRelativePath()));

	}

	@Override
	public void Warning(String expected, String actual, boolean takeScreenshot) {
		log.info("WARNING :: Expected result- " + expected + " | Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, expected, Status.WARNING, stepFileName.getRelativePath()));
	}

	@Override
	public void Done(String actual, boolean takeScreenshot) {
		log.info("DONE :: Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, Status.DONE, stepFileName.getRelativePath()));

	}

	@Override
	public void Done(String expected, String actual, boolean takeScreenshot) {
		log.info("DONE :: Expected result- " + expected + " | Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, expected, Status.DONE, stepFileName.getRelativePath()));
	}

	@Override
	public void Info(String actual, boolean takeScreenshot) {
		log.info("INFO :: Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, Status.INFO, stepFileName.getRelativePath()));

	}

	@Override
	public void Info(String expected, String actual, boolean takeScreenshot) {
		log.info("INFO :: Expected result- " + expected + " | Acutal result- " + actual + " | with screenshost");
		StepFileName stepFileName = new StepFileName();

		if (takeScreenshot) {
			takeScreenShot(stepFileName.getAbolutePath());
		}
		testSteps.add(new TestStep(actual, expected, Status.INFO, stepFileName.getRelativePath()));
	}

	class StepFileName {
		String absolutePath;
		String relativePath;

		public StepFileName() {
			String fileName = "img-" + CommonUtilities.getDateAndTimeStringForFile() + ".png";
			this.absolutePath = getTestCase().getScreenShotPath() + fileName;
			this.relativePath = getTestCase().getScreenShotRelativePath() + fileName;
		}

		public String getAbolutePath() {
			return this.absolutePath;
		}

		public String getRelativePath() {
			return this.relativePath;
		}
	}

	@Override
	public void Title(String title) {
		log.info("TITLE :: " + title);
		TestStep ts = new TestStep(title, Status.INFO);
		ts.setTitle(true);
		testSteps.add(ts);
	}
}
