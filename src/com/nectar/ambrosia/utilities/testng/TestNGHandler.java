package com.nectar.ambrosia.utilities.testng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.testng.ITestNGListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.reporting.listeners.SuiteListener;
import com.nectar.ambrosia.reporting.listeners.TestListener;
import com.nectar.ambrosia.runner.helpers.TestCaseData;

public class TestNGHandler {
	static Logger log = Logger.getLogger(TestNGHandler.class.getName());

	public static XmlTest createTestXML(String testcaseid, String testclasses) {
		XmlTest test = new XmlTest();
		test.setClasses(getXmLClassesList(testclasses));
		test.setName(testcaseid);
		return test;
	}

	public static List<XmlClass> getXmLClassesList(String testclasses) {

		return Stream.of(testclasses.split(Consts.TESTS_SPLITTER)).map(tcls -> new XmlClass(tcls))
				.collect(Collectors.toList());
	}

	public static List<XmlClass> getXmLClassesList(List<String> testclasses) {
		return testclasses.stream().map(tc -> new XmlClass(tc)).collect(Collectors.toList());
	}

	public static XmlSuite getSuiteXMLbyModule(List<TestCaseData> tcs, String moduleName) {
		XmlSuite suite = new XmlSuite();
		suite.setName(moduleName);

		if (!tcs.isEmpty()) {
			for (TestCaseData tc : tcs) {
				if (tc.isRunflag() && tc.getModule().contains(moduleName)) {
					XmlTest test = new XmlTest(suite);
					test.setName(tc.getTcId());
					test.setClasses(getXmLClassesList(tc.getFuncstoRun()));
				}
			}
		}
		return suite;
	}
	public static XmlSuite getSuiteXMLbyModule(List<TestCaseData> tcs, String moduleName,String browserName,HashMap<String,String> parameters) {
		XmlSuite suite = new XmlSuite();
		suite.setName(moduleName + "-" + browserName);
		for (TestCaseData tc : tcs) {
			if (tc.isRunflag() && tc.getModule().contains(moduleName)) {
				XmlTest test = new XmlTest(suite);
				test.setName(tc.getTcId());
				test.setParameters(parameters);
				test.setClasses(getXmLClassesList(tc.getFuncstoRun()));
			}
		}
		return suite;
	}

	public static List<XmlSuite> getSuiteXMLsbyModules(List<TestCaseData> tcs, List<String> moduleNames) {
		List<XmlSuite> suites = new ArrayList<XmlSuite>();

		if (!moduleNames.isEmpty()) {
			for (String mod : moduleNames) {
				suites.add(getSuiteXMLbyModule(tcs, mod));
			}
		}
		return suites;
	}

	public static XmlSuite getSuiteXMLForAllTestCase(List<TestCaseData> tcs) {
		XmlSuite suite = new XmlSuite();
		suite.setName("All test cases");

		if (!tcs.isEmpty()) {
			for (TestCaseData tc : tcs) {
				if (tc.isRunflag()) {
					XmlTest test = new XmlTest(suite);
					test.setClasses(getXmLClassesList(tc.getFuncstoRun()));
				}
			}
		}
		return suite;
	}
	public static XmlSuite getSuiteXMLForAllTestCase(List<TestCaseData> tcs,String browserName,HashMap<String,String> parameters) {
		XmlSuite suite = new XmlSuite();
		suite.setName("All test cases - " + browserName);

		if (!tcs.isEmpty()) {
			for (TestCaseData tc : tcs) {
				if (tc.isRunflag()) {
					XmlTest test = new XmlTest(suite);
					test.setName(tc.getTcId());
					test.setParameters(parameters);
					test.setClasses(getXmLClassesList(tc.getFuncstoRun()));
				}
			}
		}
		return suite;
	}

	public static List<XmlSuite> addSuitesByBrowser(List<XmlSuite> suties, List<String> browsers) throws InstantiationException, IllegalAccessException {
		
		List<XmlSuite> finallSuites = new ArrayList<XmlSuite>();

		for (String br : browsers) {
			HashMap<String, String> param = new HashMap<String,String>();
			param.put(Consts.PN_BROWSER, br);
			for(XmlSuite suite : suties) {
				XmlSuite curSuite = (XmlSuite) suite.clone();
				curSuite.setName(suite.getName() + "-" + br);
				curSuite.setParameters(param);
				List<XmlTest> lstTests = new ArrayList<XmlTest>();
				lstTests.addAll(curSuite.getTests());
				lstTests.stream().forEach(t -> t.setParameters(param));
				curSuite.setTests(lstTests);
				finallSuites.add(curSuite);
			}
		}
		return finallSuites;
	}
	public static List<Class<? extends ITestNGListener>>  getListenerClasses(){
		List<Class<? extends ITestNGListener>> listeners = new ArrayList<Class<? extends ITestNGListener>>();
		listeners.add(TestListener.class);
		listeners.add(SuiteListener.class);
		
		return listeners;
	}

}
