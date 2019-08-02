package com.nectar.ambrosia.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.RunType;
import com.nectar.ambrosia.reporting.listeners.SuitesManager;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.runner.helpers.TestCaseData;
import com.nectar.ambrosia.utilities.CommonUtilities;
import com.nectar.ambrosia.utilities.testng.TestNGHandler;

public class TestRunner {
	static Logger log = Logger.getLogger(TestRunner.class.getName());

	List<TestCaseData> testcases;
	RunType runType;
	RunType runBy;

	boolean isBrowserParallel;
	List<String> modules;
	List<String> browsers;
	List<XmlSuite> runnableSuites;

	public static void main(String[] args) throws Exception {
		CurrentRunProperties.loadProp();
		// create main folder for report
		CommonUtilities.createFolder(CurrentRunProperties.getReportPath());
		// copy report support files into the folder
		try {
			FileUtils.copyDirectory(new File(Consts.HTML_SUPPORTFILELOCATION),
					new File(CurrentRunProperties.getReportPath() + "support-files/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	TestRunner(List<TestCaseData> tcs, RunType rt, RunType runBy) {
		setTestcases(tcs);
		setRunType(rt);
		setRunBy(runBy);
	}

	public void run() {
		// check values
		if (!this.testcases.isEmpty() && runType != null) {
			try {
				preRequisites();
				prepareSuites();
			} catch (IOException e) {
				log.error("Error in preparing test suites run block" + e.getMessage());
			}
		}
	}

	void preRequisites() {
		// create main folder for report
		CommonUtilities.createFolder(CurrentRunProperties.getReportPath());
		// copy report support files into the folder
		try {
			log.debug("Copying support files into the current report folder " + CurrentRunProperties.getReportPath());
			FileUtils.copyDirectory(new File(Consts.HTML_SUPPORTFILELOCATION),
					new File(CurrentRunProperties.getReportPath() + "support-files/"));
		} catch (IOException e) {
			log.error("Error in copying support files into the current report folder "
					+ CurrentRunProperties.getReportPath() + " error is " + e.getMessage());
		}

	}

	void prepareSuites() throws IOException {

		// RunType - Parallel
		// RunBy - ByModule
		// BrowserParallel - true or false
		log.info(String.format("Preparing execution with %s as run type, run by %s and browser parallel is set to %s",
				runType.name(), runBy.name(), Boolean.toString(isBrowserParallel)));

		if (runType.equals(RunType.PARALLEL) && runBy.equals(RunType.BYMODULE)) {

			ExecutorService exeService = Executors.newFixedThreadPool(CurrentRunProperties.getMaxThreadCount());
			for (String mod : modules) {
				List<XmlSuite> suites = new ArrayList<>();
				for (String browser : browsers) {
					HashMap<String, String> param = new HashMap<String, String>();
					param.put(Consts.PN_BROWSER, browser);
					XmlSuite suite = TestNGHandler.getSuiteXMLbyModule(testcases, mod, browser, param);

					if (isBrowserParallel) {
						exeService.execute(new TestNGRunner(suite));
					} else {
						suites.add(suite);
					}
				}
				if(!isBrowserParallel) {
					exeService.execute(new TestNGRunner(suites));
					suites = new ArrayList<>();
				}
			}
			exeService.shutdown();
			try {
				exeService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (exeService.isTerminated()) {
				SuitesManager.getInstance().generateReport();
			}
		}
		// RunType - Sequence
		// RunBy - ByModule
		// BrowserParallel - true or false
		else if (runType.equals(RunType.SEQUENCE) && runBy.equals(RunType.BYMODULE)) {

			ExecutorService exeService = Executors.newFixedThreadPool(CurrentRunProperties.getMaxThreadCount());
			List<XmlSuite> suites = new ArrayList<>();
			for (String browser : browsers) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put(Consts.PN_BROWSER, browser);
				for (String mod : modules) {
					XmlSuite suite = TestNGHandler.getSuiteXMLbyModule(testcases, mod, browser, param);
					suites.add(suite);
				}
				if (isBrowserParallel) {
					exeService.execute(new TestNGRunner(suites));
					suites = new ArrayList<>();
				}
			}
			if (!isBrowserParallel) {
				exeService.execute(new TestNGRunner(suites));
			}
			exeService.shutdown();
			try {
				exeService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (exeService.isTerminated()) {
				SuitesManager.getInstance().generateReport();
			}
		}
		// RunType - Parallel
		// RunBy - ByTestCase
		// BrowserParallel - true or false
		else if (runType.equals(RunType.PARALLEL) && runBy.equals(RunType.BYTESTCASE)) {
			ExecutorService exeService = Executors.newFixedThreadPool(CurrentRunProperties.getMaxThreadCount());

			List<XmlSuite> suites = new ArrayList<>();
			for (String browser : browsers) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put(Consts.PN_BROWSER, browser);
				XmlSuite suite = TestNGHandler.getSuiteXMLForAllTestCase(testcases, browser, param);
				// to run tests parallel
				suite.setParallel(ParallelMode.TESTS);
				if (isBrowserParallel) {
					exeService.execute(new TestNGRunner(suite));
				} else {
					suites.add(suite);
				}
			}
			if (!isBrowserParallel) {
				exeService.execute(new TestNGRunner(suites));
			}

			exeService.shutdown();
			try {
				exeService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (exeService.isTerminated()) {
				SuitesManager.getInstance().generateReport();
			}
		}
		// RunType - Sequence
		// RunBy - ByModule
		// BrowserParallel - true or false
		else if (runType.equals(RunType.SEQUENCE) && runBy.equals(RunType.BYTESTCASE)) {

			ExecutorService exeService = Executors.newFixedThreadPool(CurrentRunProperties.getMaxThreadCount());
			List<XmlSuite> suites = new ArrayList<>();
			for (String browser : browsers) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put(Consts.PN_BROWSER, browser);
				XmlSuite suite = TestNGHandler.getSuiteXMLForAllTestCase(testcases, browser, param);
				suites.add(suite);
				if (isBrowserParallel) {
					exeService.execute(new TestNGRunner(suites));
					suites = new ArrayList<>();
				}
			}
			if (!isBrowserParallel) {
				exeService.execute(new TestNGRunner(suites));
			}
			exeService.shutdown();
			try {
				exeService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (exeService.isTerminated()) {
				SuitesManager.getInstance().generateReport();
			}
		}
	}

	public List<TestCaseData> getTestcases() {
		return testcases;
	}

	public void setTestcases(List<TestCaseData> testcases) {
		this.testcases = testcases;
	}

	public RunType getRunType() {
		return runType;
	}

	public void setRunType(RunType runType) {
		this.runType = runType;
	}

	public boolean isBrowserParallel() {
		return isBrowserParallel;
	}

	public void setBrowserParallel(boolean isBrowserParallel) {
		this.isBrowserParallel = isBrowserParallel;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public RunType getRunBy() {
		return runBy;
	}

	public void setRunBy(RunType runBy) {
		this.runBy = runBy;
	}

	public List<String> getBrowsers() {
		return browsers;
	}

	public void setBrowsers(List<String> browsers) {
		this.browsers = browsers;
	}
}
