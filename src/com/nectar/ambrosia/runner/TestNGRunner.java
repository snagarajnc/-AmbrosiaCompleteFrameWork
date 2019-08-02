package com.nectar.ambrosia.runner;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import com.nectar.ambrosia.utilities.testng.TestNGHandler;

public class TestNGRunner extends TestNG implements Runnable {
	static Logger log = Logger.getLogger(TestNGRunner.class.getName());
	int numOfSuites = 0;
	
	public TestNGRunner(List<XmlSuite> suitesToRun) {
		setXmlSuites(suitesToRun);
		setListenerClasses(TestNGHandler.getListenerClasses());
		numOfSuites = suitesToRun.size();
	}

	public TestNGRunner(XmlSuite suiteToRun) {

		List<XmlSuite> suitesToRun = new ArrayList<XmlSuite>();
		suitesToRun.add(suiteToRun);
		setXmlSuites(suitesToRun);
		setListenerClasses(TestNGHandler.getListenerClasses());
		numOfSuites = suitesToRun.size();
	}
	@Override
	public void run() {
		log.debug(String.format("Started executing TestNGRunner with %s suites",numOfSuites));
		super.run();
	}
}
