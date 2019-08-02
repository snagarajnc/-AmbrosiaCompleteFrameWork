package com.nectar.ambrosia.tests.events;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.Test;

import com.nectar.ambrosia.expections.StopTestCase;
import com.nectar.ambrosia.pom.POM_Home;
import com.nectar.ambrosia.pom.POM_Login;
import com.nectar.ambrosia.pom.events.POM_Events;
import com.nectar.ambrosia.tests.base.BaseTest;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class EventsTest extends BaseTest {

	POM_Login loginPage;
	POM_Home homePage;
	POM_Events eventsPage;

	@Test(priority = 0)
	public void prepareScreen() throws StopTestCase {

		loginPage = new POM_Login(this);
		homePage = new POM_Home(this);
		eventsPage = new POM_Events(this);

		userAction.goToURL("https://ambrosia.lab.nectarvoip.com");

		loginPage.intitatePageObjects();

		loginPage.doLogin("sramasamy@client1.com", "Sramasamy1*");

		if (homePage.waitForLogin()) {
			homePage.intitatePageObjects();

			if (!eventsPage.navigateToEvents()) {
				throw new StopTestCase("Unable to navigate to Events screen");
			} else {
				eventsPage.intitatePageObjects();
			}
		} else {
			throw new StopTestCase("Unable to login");
		}
	}

	@Test(dependsOnMethods = "prepareScreen",priority = 1)
	public void validateAlertSummaryData() {
		Title("Alert summary table validation");
		String expAlertData = getTestData("alertData");
		eventsPage.validateAlertSummaryTable(CommonUtilities.parseDataToMap(expAlertData));
		
	}
	@Test(dependsOnMethods="prepareScreen",priority = 2)
	public void validateCurrentTabData() {
		Title("Current event table validation");
		String currentEventData = getTestData("currentEventData");
		HashMap<String,List<String>> data = CommonUtilities.parseDataToMap(currentEventData,"\\|");
		eventsPage.validateCurrentEventData(data);
		eventsPage.getEventDetails();
		
	}
	@Test(dependsOnMethods="prepareScreen",priority = 3)
	public void validatePinUnPinEventAndData() throws ParseException {
		String pinEventData = getTestData("pinUnpinEventData");
		HashMap<String,List<String>> data = CommonUtilities.parseDataToMap(pinEventData,"\\|");
		eventsPage.validatePinnedEventNoData();
		Title("Pin event and validate pinned event table validation");

		for(String eventName : data.keySet()) {
			eventsPage.pinEvent(eventName);
			homePage.validateFeedbackLabelMessage(eventsPage.addPinnedEventfeedbackMessage(eventName));
			eventsPage.validateCurrentEventData(eventName, data.get(eventName));
			eventsPage.unPinEvent(eventName);
			
		}
		String colToVerify = getTestData("columnsToVerify");
		eventsPage.validateColumnsExists(colToVerify, "Current events");
		eventsPage.validateColumnsExists(colToVerify, "Historic events");
		eventsPage.verifyDefaultOrderByDateTime("Current events", "lastTime");
		if(eventsPage.sortColumnByOrder("Current events", "EVENT DESCRIPTION", false)){
			eventsPage.verifySortOrderByColumnValue("Current events", "description", false);
		}
		eventsPage.filterAlertLevel("Alert level","Critical",false,true);
		
	}
	
}
