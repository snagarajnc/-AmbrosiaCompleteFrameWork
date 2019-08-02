package com.nectar.ambrosia.pom.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.pom.POM_Base;
import com.nectar.ambrosia.tests.base.BaseTest;
import com.nectar.ambrosia.utilities.CommonUtilities;
import com.nectar.ambrosia.utilities.UserAction;

public class POM_Events extends POM_Base {

	public static final String MENU_EVENTS = "Events";
	public static final String TAB_CURRENT = "Current";
	public static final String TAB_PINNED = "Pinned";
	public static final String TAB_DETAILS = "Details";

	private static final String ERR_MESSAGE_NOPINEED = "There are currently no pinned events selected.";

	public POM_Events(BaseTest bTest) {
		super(bTest);
	}

	public static By alertSummary = By.xpath("//div[@class='alert-summary']//div[@class='state-value']");

	public static By getAlertCountFromAlertSummary(String alertName) {
		return By.xpath(String.format(
				"//div[@class='alert-summary']//div[@class='state' and text()='%s']/parent::div/div[@class='state-value']",
				alertName));
	}

	public static By getTab(String tabName) {
		return By.xpath(String.format("//a[@role='tab']//*[text()='%s']", tabName));
	}

	public static By summaryDataTable = By.xpath("//div[@role='tablist' and contains(@class,'events-tree')]");

	public static By summaryDataHolder(String eventName) {
		return By.xpath(String.format(
				"//a//div[@class='event-name' and @title='%s']/parent::p-header/parent::a/parent::div/parent::p-accordiontab",
				eventName));
	}

	public static By summaryEventname = By.xpath("//div[@class='event-name']");
	public static By summaryEventExpender = By.tagName("span");
	public static By summaryEventPinDecider = By.tagName("button");

	public static By eventChildAlerts(String eventName) {
		return By.xpath(String.format(
				"//a//div[@class='event-name' and @title='%s']/ancestor::p-accordiontab//li//div[@class='event-name']",
				eventName));
	}

	public static By eventColumns(String tablename, String colname) {
		return By.xpath(String.format(
				"//app-events-table[@title='" + tablename
						+ "']//div[@ref='headerRoot']//span[@role='columnheader' and text() ='" + colname + "']",
				tablename, colname));
	}

	public static By getEventDetailsRow() {
		return By.xpath("//div[@class='events-panel']//div[@class='row row-line']");
	}

	public static By getEventParam() {
		return By.xpath("//div[@class='events-panel']//div[@class='row row-line']//div[contains(@class,'col-xs-2')]");
	}

	public static By getEventParamValue() {
		return By.xpath("//div[@class='events-panel']//div[@class='row row-line']//div[contains(@class,'col-xs-3')]");
	}

	public static By getEventColumnHeader(String tName, String cName) {
		return By.xpath(String.format(
				"//app-events-table[@title='%s']//div[@role='grid']//div[@ref='eHeaderContainer']//span[@role='columnheader' and text()='%s']",
				tName, cName));
	}
	public static By getEventColumnSortOrder(String tName, String cName) {
		return By.xpath(String.format(
				"//app-events-table[@title='%s']//div[@role='grid']//div[@ref='eHeaderContainer']//span[@role='columnheader' and text()='%s']/ancestor::div[contains(@class,'header-cell-sorted')]",tName, cName));
	}
	public static By getEventColumnValues(String tName, String colID) {
		return By.xpath(String.format(
				"//app-events-table[@title='%s']//div[@role='grid']//div[@ref='eBody']//div[@ref='eBodyViewport']//div[@role='row']//div[@col-id='%s']//div",
				tName, colID));
	}
	public static By getEventColumnDate(String tName, String colID) {
		return By.xpath(String.format(
				"//app-events-table[@title='%s']//div[@role='grid']//div[@ref='eBody']//div[@ref='eBodyViewport']//div[@role='row']//div[@col-id='%s']//app-date-cell",
				tName, colID));
	}

	public static By getEventTable(String tName) {
		return By.xpath(String.format(
				"//app-events-table[@title='%s']//div[@role='grid']",tName));
	}
	
	public static By getEventTableSpinner(String tName) {
		return By.xpath(String.format(
				"//app-events-table[@title='%s']//app-spinner",tName));
	}

	public static By summaryDataRowReterival(String eventName) {
		return By.xpath(String.format("//a//div[@class='event-name' and @title='%s']/ancestor::a", eventName));
	}

	public static By summaryDataRowAlert(String eventName) {
		return By.xpath(String.format(
				"//a//div[@class='event-name' and @title='%s']/preceding-sibling::app-alert-type-dot/div", eventName));
	}

	public static By summaryChildRowAlert(String subName) {
		return By.xpath(String.format(
				"//a//div[@class='event-name' and @title='%s']/ancestor::p-accordiontab//li//div[@class='event-name']/preceding-sibling::app-alert-type-dot/div",
				subName));
	}

	public static By noEventsData = By.xpath("//div[contains(@class,'alert-danger')]");
	
	public By getFilterButton(String name){
		return By.xpath(String.format("//button//span[text()='%s']", name));
	}

	public String addPinnedEventfeedbackMessage(String eventname) {
		return String.format("%s was added to Pinned events.", eventname);
	}

	public boolean navigateToEvents() {
		return baseTest.userAction.navigateToMenu(MENU_EVENTS);
	}

	public void validateAlertSummaryTable(HashMap<String, String> alertDetails) {

		for (String alertName : alertDetails.keySet()) {
			int expCount = Integer.parseInt(alertDetails.get(alertName));
			try {
				int actCount = Integer
						.parseInt(baseTest.userAction.getElement(getAlertCountFromAlertSummary(alertName)).getText());

				if (actCount == expCount) {
					baseTest.Pass("User should be able to see the total alert as " + expCount + " for " + alertName,
							"User is able to see the total alert as " + expCount + " for " + alertName);
				} else {
					baseTest.Pass("User should be able to see the total alert as " + expCount + " for " + alertName,
							"User is able to see the total alert as " + actCount + " instead of " + expCount
									+ " instead of for " + alertName);
				}
			} catch (NumberFormatException nfe) {
				baseTest.Fail("User should be able to get the total alert of " + expCount + " for " + alertName,
						"Unable to reterive the value from UI");
			}
		}
		baseTest.Info("Alert summary table with values", true);
	}

	public void validateCurrentEventData(HashMap<String, List<String>> eventData) {
		for (String eventname : eventData.keySet()) {
			validateCurrentEventData(eventname, eventData.get(eventname));
		}
	}

	public void validateCurrentEventData(String eventname, List<String> expectedEventNames) {
		baseTest.userAction.buttonClick(getTab(TAB_CURRENT), TAB_CURRENT + " events tab");
		validateEventData(eventname, expectedEventNames);
	}

	public void validateColumnsExists(String cols, String tablename) {
		baseTest.userAction.buttonClick(getTab(TAB_DETAILS), "Events " + TAB_DETAILS + " tab");
		List<String> colObj = CommonUtilities.splitStringGetAsList(cols, "\\|");
		for (String colstr : colObj) {
			if (baseTest.userAction.scrollElementToVisible(eventColumns(tablename, colstr))) {
				WebElement obj = baseTest.userAction.getElement(eventColumns(tablename, colstr));
				if (obj != null) {
					if (obj.isDisplayed()) {
						baseTest.Pass("Verify whether Column : " + colstr + " displayed in " + tablename + " Table.",
								"Column : " + colstr + " displayed in " + tablename + " Table.");
					} else {
						baseTest.Fail("Verify whether Column : " + colstr + " displayed in " + tablename + " Table.",
								"Column : " + colstr + " Not displayed in " + tablename + " Table.");
					}

				} else {
					baseTest.Fail("Verify whether Column : " + colstr + " Found in " + tablename + " Table.",
							"Column : " + colstr + " not Found in " + tablename + " Table.");
				}
			}
		}
	}

	public void validateEventData(String eventname, List<String> expectedEventNames) {
		// click current tab
		WebElement sumDataTable = baseTest.userAction.getElement(summaryDataTable);
		WebElement dataHolder = baseTest.userAction.getElementFromParent(sumDataTable, summaryDataHolder(eventname));
		if (dataHolder != null && dataHolder.isDisplayed()) {
			WebElement pAlert = baseTest.userAction.getElement(summaryDataRowAlert(eventname));
			if (pAlert != null && pAlert.isDisplayed()) {
				String pname = pAlert.getAttribute("title");
				// expend event to see actual alerts
				baseTest.userAction.buttonClick(dataHolder, eventname + " event expender");
				WebElement aData = baseTest.userAction.getElementFromParent(dataHolder,
						summaryDataRowReterival(eventname));
				// wait for the event to be expended.
				baseTest.userAction.waitForAttributeToBe(aData, "aria-expanded", "true");
				// Retrieve the list of li items
				List<WebElement> childAlerts = baseTest.wDriverWait
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(eventChildAlerts(eventname)));
				HashMap<String, String> eventDetails = new HashMap<String, String>();

				for (WebElement event : childAlerts) {
					if (event != null && event.isDisplayed()) {
						event.click();
						String actEventName = event.getText().trim();
						String parent = event.getAttribute("title");
						WebElement alert = baseTest.userAction.getElement(summaryChildRowAlert(eventname));
						if (alert != null && alert.isDisplayed()) {
							String actAlert = alert.getAttribute("title");
							if (parent.equalsIgnoreCase(eventname) && expectedEventNames.toString().contains(pname)) {
								eventDetails.put(actEventName, actAlert);
							} else {
								baseTest.Fail("Verify whether the " + actEventName + " is part of " + eventname,
										actEventName + " is part of " + parent + " instead of " + eventname + " nor "
												+ pname + " alert not matched");
							}

						}

					}
				}
				// verify the data against input
				for (String e : expectedEventNames) {
					List<String> exp = Arrays.asList(e.split(Consts.COMMA_SPLITTER));
					if (eventDetails.containsKey(exp.get(Consts.KEY_INDEX))
							&& eventDetails.containsValue(exp.get(Consts.VALUE_INDEX))) {
						baseTest.Pass(
								"Verify whether the event " + exp.get(Consts.KEY_INDEX) + " exists with Alert as "
										+ exp.get(Consts.VALUE_INDEX) + " under the " + eventname,
								"Event " + exp.get(Consts.KEY_INDEX) + " is exists with Alert as "
										+ exp.get(Consts.VALUE_INDEX) + " under the " + eventname);
					} else {
						baseTest.Fail(
								"Verify whether the event " + exp.get(Consts.KEY_INDEX) + " exists with Alert as "
										+ exp.get(Consts.VALUE_INDEX) + " under the " + eventname,
								"Event " + exp.get(Consts.KEY_INDEX) + " is not exists with Alert as "
										+ exp.get(Consts.VALUE_INDEX) + " under the " + eventname);
					}
				}

			} else {
				baseTest.Fail("User should be able to see the " + eventname + " Alert Icon on the current event tab",
						eventname + " Alert Icon is not available on the current event tab", true);
			}
		} else {
			baseTest.Fail("User should be able to see the " + eventname + " on the current event tab",
					eventname + " is not available on the current event tab", true);
		}

	}

	public HashMap<String, String> getEventDetails() {

		HashMap<String, String> eventDetails = new HashMap<String, String>();

		List<WebElement> pName = baseTest.userAction.getElements(getEventParam());
		List<WebElement> pValue = baseTest.userAction.getElements(getEventParamValue());
		for (int j = 0; j < pName.size(); j++) {
			String parValue = "";
			String parName = "";
			if (pName.get(j) != null && pValue.get(j) != null) {
				parName = pName.get(j).getText().trim();
				if (parName.contains("Alert")) {
					WebElement alert = baseTest.userAction.getElementFromParentStatic(pValue.get(j),
							By.xpath("//div[contains(@class,'circle')]"));
					if (alert != null) {
						parValue = alert.getAttribute("title").trim();
					}
				} else {

					parValue = pValue.get(j).getText().trim();
				}
				System.out.println(parName.replace(":", ""));
				System.out.println(parValue);
				eventDetails.put(parName.replace(":", ""), parValue);
			}

		}

		return eventDetails;

	}

	public Boolean sortColumnByOrder(String tableName, String columnName, Boolean isAsc) {

		if (baseTest.userAction.scrollElementToVisible(getEventTable(tableName))) {
			WebElement colObj = baseTest.userAction.getElement(getEventColumnHeader(tableName, columnName));
			if (colObj != null && colObj.isDisplayed()) {
				WebElement pObj = baseTest.userAction.getElement(getEventColumnSortOrder(tableName, columnName));
				if (pObj != null) {
					String state = pObj.getAttribute("class").trim();
					if (state.contains("none")) {
						if (isAsc) {
							colObj.click();
							baseTest.userAction.getInvisbileElement(getEventTableSpinner(tableName));
						} else {
							colObj.click();
							if(baseTest.userAction.getInvisbileElement(getEventTableSpinner(tableName))){
								colObj.click();
								baseTest.userAction.getInvisbileElement(getEventTableSpinner(tableName));
							}
						}
					} else if (state.contains("asc")) {
						if (!isAsc) {
							colObj.click();
							baseTest.userAction.getInvisbileElement(getEventTableSpinner(tableName));
						}

					} else if (state.contains("desc")) {
						if (isAsc) {
							colObj.click();
							baseTest.userAction.getInvisbileElement(getEventTableSpinner(tableName));
						}

						
					}
				}
				baseTest.Pass("Verify Whether "+columnName+" Column  Clicked for "+(isAsc ? "Ascending" :  "Descending")+" Order", columnName+" Column  Clicked for "+(isAsc ? "Ascending" :  "Descending")+" Order");
				return true;
			}

		}
		baseTest.Pass("Verify Whether "+columnName+" Column  Clicked for "+(isAsc ? "Ascending" :  "Descending")+" Order", columnName+" Column  not Clicked for "+(isAsc ? "Ascending" :  "Descending")+" Order");
		return false;
	}

	public void verifySortOrderByColumnValue(String tableName, String columnID, Boolean isAsc) {
		Boolean status = null;
		if (baseTest.userAction.scrollElementToVisible(getEventTable(tableName))) {
			List<WebElement> colVal = baseTest.userAction.getElements(getEventColumnValues(tableName, columnID));
			if (colVal.size() > 0) {

				for (int i = 0; i < colVal.size() - 1; i++) {

					if (colVal.get(i) != null && colVal.get(i + 1) != null) {
						String present = colVal.get(i).getText().trim();
						String next = colVal.get(i + 1).getText().trim();
						if (isAsc) {
							if (present.compareToIgnoreCase(next) <= 0) {
								status = true;
							} else {
								status = false;
								break;
							}

						} else {
							if (present.compareToIgnoreCase(next) >= 0) {
								status = true;
							} else {
								status = false;
								break;

							}
						}
					}

				}
				if(status){
					baseTest.Pass("Verify Whether "+columnID+" Column Values Sorted in "+(isAsc ? "Ascending" :  "Descending")+" Order", columnID+" Column Values Sorted in "+(isAsc ? "Ascending" :  "Descending")+" Order");
				}else{
					baseTest.Fail("Verify Whether "+columnID+" Column Values Sorted in "+(isAsc ? "Ascending" :  "Descending")+" Order", columnID+" Column Values Not Sorted in "+(isAsc ? "Ascending" :  "Descending")+" Order");
				}

			}else{
				baseTest.Fail("Verify whether Table has Rows", "Verify whether Table has no Rows Hence Can't Validate Sorting Order");
			}
		}

	}
	
	public void verifyDefaultOrderByDateTime(String tableName, String columnID) throws ParseException {
		Boolean status = null;
		if (baseTest.userAction.scrollElementToVisible(getEventTable(tableName))) {
			List<WebElement> colVal = baseTest.userAction.getElements(getEventColumnDate(tableName, columnID));
			if (colVal.size() > 0) {

				for (int i = 0; i < colVal.size() - 1; i++) {

					if (colVal.get(i) != null && colVal.get(i + 1) != null) {
						String present = colVal.get(i).getText().trim();
						String  next = colVal.get(i+1).getText().trim();
						SimpleDateFormat dateformat =   new SimpleDateFormat("hh:mm a, MM/dd/yyyy");
						Date time1 = dateformat.parse(present);
						Date time2 = dateformat.parse(next);
						if(time1.compareTo(time2) <= 0){
							status = true;
						}else{
							status = false;
							break;
						}
						
					}

				}
				if(status){
					baseTest.Pass("Verify Whether "+columnID+" Column Values Sorted By Default Order(Date and Time)",columnID+" Column Values Sorted By Default Order(Date and Time)");
				}else{
					baseTest.Fail("Verify Whether "+columnID+" Column Values Sorted By Default Order(Date and Time)", columnID+" Column Values Not Sorted By Default Order(Date and Time)");
				}

			}else{
				baseTest.Fail("Verify whether Table has Rows", "Verify whether Table has no Rows Hence Can't Validate Default Sort Order");
			}
		}

	}
	
	public boolean filterAlertLevel(String filterBy,String alertToSelect,boolean isSearch,boolean isCheck){
		
		if(baseTest.userAction.scrollToTop()){
			
	       baseTest.userAction.selectDropDownMenuCheckbox(By.xpath("//app-filter-select-component"), filterBy, isSearch,isCheck);
	       baseTest.userAction.selectDropDownMenuCheckbox(baseTest.userAction.getFilterObject(filterBy), alertToSelect, isSearch,isCheck);
	       baseTest.userAction.buttonClick(getFilterButton("Apply"), "Apply");
	    	   
	       }
		
		
		
		
		return false;
	}

	public void pinEvent(List<String> eventNames) {
		for (String eventname : eventNames) {
			pinEvent(eventname);
		}
	}

	public void unPinEvent(List<String> eventNames) {
		for (String eventname : eventNames) {
			unPinEvent(eventname);
		}
	}

	public void pinEvent(String eventName) {

		pinUnEvent(eventName, true);
	}

	public void unPinEvent(String eventName) {

		pinUnEvent(eventName, false);
	}

	public void pinUnEvent(String eventname, boolean isPinEvent) {
		if (isPinEvent) {
			// click current tab
			baseTest.userAction.buttonClick(getTab(TAB_CURRENT), TAB_CURRENT + " events tab");
		} else {
			// click pinned tab
			baseTest.userAction.buttonClick(getTab(TAB_PINNED), TAB_PINNED + " events tab");
		}
		WebElement sumDataTable = baseTest.userAction.getElement(summaryDataTable);
		WebElement dataHolder = baseTest.userAction.getElementFromParent(sumDataTable, summaryDataHolder(eventname));
		if (dataHolder != null && dataHolder.isDisplayed()) {
			baseTest.userAction.moveToElementAndClick(
					baseTest.userAction.getElementFromParent(dataHolder, summaryEventPinDecider),
					(isPinEvent ? "Pin" : "Unpin") + " event " + eventname);
		}

	}

	public void validatePinnedEventData(HashMap<String, List<String>> eventData) {
		for (String eventname : eventData.keySet()) {
			validateCurrentEventData(eventname, eventData.get(eventname));
		}
	}

	public void validatePinnedEventData(String eventname, List<String> expectedEventNames) {
		baseTest.userAction.buttonClick(getTab(TAB_PINNED), TAB_PINNED + " events tab");
		validateCurrentEventData(eventname, expectedEventNames);
		baseTest.Info("Pinned event data", true);
	}

	public void validatePinnedEventNoData() {
		baseTest.userAction.buttonClick(getTab(TAB_PINNED), TAB_PINNED + " events tab");
		WebElement errMes = baseTest.userAction.getElement(noEventsData);
		if (errMes != null && errMes.isDisplayed()) {
			if (getErrMessageNopineed().equals(errMes.getText())) {
				baseTest.Pass(
						"Verify whether the user is able to see " + getErrMessageNopineed()
								+ " error message when ther are no event(s) pinned",
						"User is able to see " + getErrMessageNopineed()
								+ " error message when ther are no event(s) pinned");
			} else {
				baseTest.Fail(
						"Verify whether the user is able to see " + getErrMessageNopineed()
								+ " error message when ther are no event(s) pinned",
						"User is displayed with the " + errMes.getText() + " instead of " + getErrMessageNopineed()
								+ " error message when ther are no event(s) pinned");
			}
		}
	}

	public static String getErrMessageNopineed() {
		return ERR_MESSAGE_NOPINEED;
	}
}
