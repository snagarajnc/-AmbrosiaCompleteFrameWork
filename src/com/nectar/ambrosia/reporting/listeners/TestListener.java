package com.nectar.ambrosia.reporting.listeners;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.nectar.ambrosia.common.Status;
import com.nectar.ambrosia.reporting.TestCase;
import com.nectar.ambrosia.reporting.TestCaseRow;
import com.nectar.ambrosia.reporting.TestStep;
import com.nectar.ambrosia.reporting.TestSuite;
import com.nectar.ambrosia.runner.StartExecution;
import com.nectar.ambrosia.runner.helpers.TestCaseData;

public class TestListener implements ITestListener {
	static Logger log = Logger.getLogger(TestListener.class.getName());

	@Override
	public void onTestStart(ITestResult result) {

	}

	@Override
	public void onTestSuccess(ITestResult result) {
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if (result.getThrowable() != null) {
			TestStep testStep = new TestStep("Test should not have any exceptions",
					"Test is having a exception. <br>" + result.getThrowable().toString(), Status.FAIL);
			TestCase testCase = SuitesManager.getInstance().getTestSuite(result.getTestContext().getSuite().getName())
					.getTestCase(result.getTestContext().getName());
			testCase.addStep(testStep);
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {

		TestStep testStep = new TestStep("Test should be run successfully", "Test is skipped", Status.WARNING);
		TestCase testCase = SuitesManager.getInstance().getTestSuite(result.getTestContext().getSuite().getName())
				.getTestCase(result.getTestContext().getName());
		testCase.addStep(testStep);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		if (result.getThrowable() != null) {
			TestStep testStep = new TestStep("Test should not have any exceptions",
					"Test is having a exception. <br>" + result.getThrowable().getMessage(), Status.FAIL);
			TestCase testCase = SuitesManager.getInstance().getTestSuite(result.getTestContext().getSuite().getName())
					.getTestCase(result.getTestContext().getName());
			testCase.addStep(testStep);
		}
	}

	@Override
	public void onStart(ITestContext context) {
		String tcName = context.getName();
		TestSuite testSuite = SuitesManager.getInstance().getTestSuite(context.getSuite().getName());
		TestCaseData tcd = StartExecution.getTestCaseDataById(tcName);
		TestCase testCase = new TestCase(tcName, testSuite, tcd.getTcShortname());

		testCase.setDescription(tcd.getTcDescription());
		testCase.setModule(tcd.getModule().toString());
		testCase.setEnvironment(tcd.getEnv());
		testCase.setStartTime(context.getStartDate());
		testCase.setStatus(Status.RUNNING);
		testCase.setHost(context.getHost());

		testSuite.addTestCase(testCase);
		log.info(tcName + " test started runing");

	}

	@Override
	public void onFinish(ITestContext context) {
		TestCase testCase = SuitesManager.getInstance().getTestSuite(context.getSuite().getName())
				.getTestCase(context.getName());
		testCase.setEndTime(context.getEndDate());
		testCase.updateStatus();
		
		
		TestCaseRow tcr = new TestCaseRow(context.getName());
		tcr.setDescription(testCase.getDescription());
		tcr.setStatus(testCase.getStatus());
		tcr.setModule(testCase.getModule());
		tcr.setEnvironment(testCase.getEnvironment());
		tcr.setBrowser(testCase.getBrowser());
		tcr.setSuitename(context.getSuite().getName());
		tcr.setDuration(testCase.getDurationString());
		tcr.setHost(testCase.getHost());
		tcr.setReportPath(testCase.getReportRelativePath());
		
		SuitesManager.getInstance().addTestCaseRow(tcr);
		
		log.info(context.getName() + " test compelted");
	}

}
