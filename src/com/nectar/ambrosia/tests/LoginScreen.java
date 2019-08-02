package com.nectar.ambrosia.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.nectar.ambrosia.pom.POM_Home;
import com.nectar.ambrosia.pom.POM_Login;
import com.nectar.ambrosia.pom.events.POM_Events;
import com.nectar.ambrosia.tests.base.BaseTest;
import com.nectar.ambrosia.utilities.CommonUtilities;
import com.nectar.ambrosia.utilities.filters.FilterWithCheckboxsAndRadio;
import com.nectar.ambrosia.utilities.filters.GraphModalityFilter;
import com.nectar.ambrosia.utilities.filters.RowShowFilter;
import com.nectar.ambrosia.utilities.filters.ShowHideFilter;
import com.nectar.ambrosia.utilities.filters.TimePeriodFilter;

public class LoginScreen extends BaseTest {

	POM_Login loginPage;
	POM_Home homePage;

	@Test(priority = 0)
	public void preparePageObjects() {
		loginPage = new POM_Login(this);
		homePage = new POM_Home(this);
		loginPage.intitatePageObjects();
		
	}

	/**
	 * <h1>Login into application</h1>
	 * <p>
	 * Verify whether user is able to login with valid credentials by checking the
	 * user label on the top right corner once login submitted
	 * <p>
	 * 
	 * @param url
	 *            URL to navigate
	 * @author Shankar Ramasamy
	 * @throws InterruptedException 
	 */
	@Test(priority = 1)
	public void loginIntoApplication() throws InterruptedException {

		String url = getTestData("url");
		String username = getTestData("username");
		String password = getTestData("password");
		String errorMessage = getTestData("errormessage");
		boolean isErrorValidation = CommonUtilities.getBoolValue(getTestData("iserrorvalidation"));


		userAction.goToURL(url);
		loginPage.setUsername(username);
		loginPage.clickPasswordViewHide();
		loginPage.clickPasswordViewHide();
		loginPage.setPassword(password);
		loginPage.clickLogin();

		if (isErrorValidation) {
			loginPage.errorValidation(errorMessage);
		} else {
			if (homePage.waitForLogin()) {
				Pass("Verify whether the user is able to login into applicaiton",
						"User is able to login into the application");
				homePage.validateLoggedInUsername(username);
				
				//validate filter
				POM_Events events = new POM_Events(this);
				events.intitatePageObjects();
//				events.navigateToEvents();
				userAction.navigateToMenu("CALL DETAILS|HISTORIC");
				
				/*ShowHideFilter shf = new ShowHideFilter(this, By.cssSelector("app-global-filter-component > section > div > div.top-container > app-filter-select-component"));
				shf.selectValuesWithCheckbox("Alert Level");*/
				
				ShowHideFilter shf1 = new ShowHideFilter(this, By.cssSelector("app-filter-select-component>div.wrap-dropdown-menu"));
//				shf1.selectValuesWithCheckbox("Sip Response Code","sip");
				shf1.selectAll();
				
				//Time period filter
				TimePeriodFilter tpf = new TimePeriodFilter(this, By.cssSelector("app-filter-time-period-component"));
				tpf.filter("30 Days");
				
				//Modality filter
				FilterWithCheckboxsAndRadio fwc = new FilterWithCheckboxsAndRadio(this, By.xpath("(//app-dropdown-filter-component)[1]"), "Modality");
				fwc.selectValuesWithCheckbox("IM");
				fwc.selectValuesWithCheckbox("Unknown");
				fwc.deSelectAll();
				fwc.selectAll();
				
				//Sip response codes
				fwc.setFilterHomeElement(By.xpath("(//app-dropdown-filter-component)[2]"));
				fwc.deSelectAll();
				fwc.selectValuesWithCheckbox("605");
				fwc.selectValuesWithCheckbox("403");
				fwc.selectAll();
				
				//Protocol
				fwc.setFilterHomeElement(By.xpath("(//app-dropdown-filter-component)[3]"));
				fwc.deSelectAll();
				fwc.selectValuesWithCheckbox("TCP");
				fwc.selectValuesWithCheckbox("UDP");
				fwc.selectAll();

				//Session type
				fwc.setFilterHomeElement(By.xpath("(//app-dropdown-filter-component)[4]"));
				fwc.deSelectAll();
				fwc.selectValuesWithCheckbox("Peer To Peer");
				fwc.selectValuesWithCheckbox("PSTN/External");
				fwc.selectAll();
				
				//Quality
				fwc.setFilterHomeElement(By.xpath("(//app-dropdown-filter-component)[5]"));
				fwc.deSelectAll();
				fwc.selectValuesWithCheckbox("Partially Good (25-50%)");
				fwc.selectValuesWithCheckbox("Poor");
				fwc.selectAll();
				
				//Session scenario
				fwc.setFilterHomeElement(By.xpath("(//app-dropdown-filter-component)[6]"));
				fwc.deSelectAll();
				fwc.selectValuesWithCheckbox("Internal-External");
				fwc.selectValuesWithCheckbox("Internal");
				fwc.selectAll();
				
				//Platform
				fwc.setFilterHomeElement(By.xpath("(//app-dropdown-filter-component)[6]"));
				fwc.deSelectAll();
				fwc.selectValuesWithCheckbox("Skype");
				fwc.selectValuesWithCheckbox("Cisco (CMS)");
				fwc.selectAll();
				
								
				//Graph filter
				GraphModalityFilter gf = new GraphModalityFilter(this, By.cssSelector("app-common-graph"));
				List<String> graphs = new ArrayList<>() ;
				graphs.add("Audio");
				graphs.add("Video");
				graphs.add("Events");
				gf.deSelectValuesWithCheckbox(graphs);
				gf.selectValuesWithCheckbox(graphs);
				
				//Modality without select and deselct
				gf.setFilterHomeElement(By.cssSelector("app-session-quality-select"));
				gf.deSelectValuesWithCheckbox("Video");
				List<String> modlity = new ArrayList<>() ;
				modlity.add("Audio");
				modlity.add("Video");
				modlity.add("Share");
				gf.selectValuesWithCheckbox(modlity);
				
				//Columns
				fwc.setFilterHomeElement(By.cssSelector("app-column-toggler"));
				fwc.selectValuesWithCheckbox("Session ID", "session");
				fwc.deSelectAll();
				fwc.selectAll();
				
				//Show rows
				RowShowFilter rsf = new RowShowFilter(this, By.cssSelector("app-rows-per-page"));
				rsf.selectRows(10);
				
				
				
				/*
//				new TimePeriodFilter(this,By.cssSelector("app-filter-time-period-component")).filter("7 Days");
				AlertLevelFilter alf =  new AlertLevelFilter(this, By.cssSelector("div.filters-holder app-alert-level-filter-component"));
				alf.deSelectAll();
				Thread.sleep(5000);
				alf.selectValuesWithCheckbox("Critical");
				Thread.sleep(5000);
				alf.selectValuesWithCheckbox("Major");
				Thread.sleep(5000);
				alf.selectValuesWithCheckbox("No Activity");
				Thread.sleep(5000);
				alf.selectAll();
				Thread.sleep(5000);
				alf.selectAll();
				Thread.sleep(5000);
				alf.deSelectAll();
				Thread.sleep(5000);
				alf.deSelectAll();
				
				AlertLevelFilter alf1 =  new AlertLevelFilter(this, By.cssSelector("app-summary-tab app-alert-level-filter-component"));
				alf1.selectValueWithRadioButton("Major");*/
				
			} else {
				Fail("Verify whether the user is able to login into applicaiton",
						"User is not able to login into the application");
			}
		}

	}

}
