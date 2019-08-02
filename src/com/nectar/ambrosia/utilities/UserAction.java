package com.nectar.ambrosia.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.SubmitResponse;
import com.nectar.ambrosia.pom.POM_Home;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.tests.base.BaseTest;

public class UserAction {

	static Logger log = Logger.getLogger(UserAction.class.getName());

	BaseTest baseTest;
	WebDriver webDriver;
	WebDriverWait webDriverWait;
	JavascriptExecutor jsExecutor;

	public UserAction(BaseTest btest, WebDriver wd, WebDriverWait wdw) {
		this.baseTest = btest;
		this.webDriver = wd;
		this.webDriverWait = wdw;
		this.jsExecutor = (JavascriptExecutor) webDriver;
	}

	/**
	 * <h1>Navigate to URL</h1>
	 * 
	 * @param url
	 *            URL to navigate
	 * @author Shankar Ramasamy
	 */
	public void goToURL(String url) {
		this.webDriver.get(url);
		this.webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		baseTest.Info("URL <b>" + url + "</b> should be navigated", "URL <b>" + url + "</b> is navigated");
	}

	/**
	 * <h1>Get title</h1>
	 * <p>
	 * get the title of the current browser's page
	 * </p>
	 * 
	 * @author Shankar Ramasamy
	 */
	public String getCurrentTitle() {
		return webDriver.getTitle();
	}

	/**
	 * <h1>Compare title</h1>
	 * <p>
	 * get the title of the current browser's page and validate against input
	 * parameter
	 * </p>
	 * 
	 * @param expTitle
	 *            expected title
	 * @author Shankar Ramasamy
	 */
	public void comparePageTitle(String expTitle) {
		String currentTitle = getCurrentTitle();
		if (currentTitle.equals(expTitle)) {
			baseTest.Pass("Page title should be " + expTitle, "Page title is " + expTitle);
		} else if (currentTitle.equalsIgnoreCase(expTitle)) {
			baseTest.Warning("Page title should be " + expTitle, "Page title is " + expTitle + " - case mismatched");
		} else {
			baseTest.Fail("Page title should be " + expTitle,
					"Page title is not " + expTitle + " - actual value is " + currentTitle);
		}
	}

	/*
	 * Enter text
	 * start********************************************************************
	 * ****
	 */

	/**
	 * <h1>Enter text in the editable text fields</h1>
	 * <p>
	 * Note: the object name is set to "test" default
	 * 
	 * @param byForElement
	 *            element locator
	 * @param dataToEnter
	 *            data to enter in the editable text field
	 * @author Shankar Ramasamy
	 */
	public void enterText(By byForElement, String dataToEnter) {
		enterText(byForElement, dataToEnter, "test");
	}

	/**
	 * <h1>Enter text in the editable text fields</h1>
	 * 
	 * @param byForElement
	 *            element locator
	 * @param dataToEnter
	 *            data to enter in the editable text field
	 * @param friendlyName
	 *            object name for reference
	 * @author Shankar Ramasamy
	 */
	public void enterText(By byForElement, String dataToEnter, String friendlyName) {
		enterText(getElement(byForElement), dataToEnter, friendlyName);
	}

	/**
	 * <h1>Enter text in the editable text fields</h1>
	 * <p>
	 * Note: the object name is set to "test" default
	 * 
	 * @param webElement
	 *            web element
	 * @param dataToEnter
	 *            data to enter in the editable text field
	 * @author Shankar Ramasamy
	 */
	public void enterText(WebElement webElement, String dataToEnter) {
		enterText(webElement, dataToEnter, "test");
	}

	/**
	 * <h1>Enter text in the editable text fields</h1>
	 * <p>
	 * this function is the actual implementation of entering the text in the
	 * field.
	 * </p>
	 * 
	 * <p>
	 * If the object is not found or not ready for input then a fail step will
	 * be added to the current running test case.
	 * </p>
	 * <p>
	 * If the object is found and ready for input then data get filled and a
	 * done step will be added to the current running test case.
	 * </p>
	 * <p>
	 * If the object is found and ready for input but no data to enter then a
	 * warning step will be added to the current running test case.
	 * </p>
	 * 
	 * @param webElement
	 *            web element
	 * @param dataToEnter
	 *            data to enter in the editable text field
	 * @param friendlyName
	 *            object name for reference
	 * @author Shankar Ramasamy
	 */
	public void enterText(WebElement webElement, String dataToEnter, String friendlyName) {
		if (StringUtils.isBlank(friendlyName)) {
			friendlyName = "test";
		}
		if (webElement != null && webElement.isDisplayed()) {
			if (StringUtils.isNotBlank(dataToEnter)) {
				webElement.click();
				if (dataToEnter.equalsIgnoreCase("null")) {
					webElement.clear();
					baseTest.Done("User should be able to clear text in the <b>" + friendlyName + "</b> web element",
							"User is able to clear text in the <b>" + friendlyName + "</b> web element");
				} else {
					webElement.sendKeys(dataToEnter);
					baseTest.Done(
							"User should be able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
									+ "</b> web element",
							"User is able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
									+ "</b> web element");
				}
			} else {
				baseTest.Warning(
						"User should be able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
								+ "</b> web element",
						"Data is empty and no text entered in the <b>" + friendlyName + "</b> web element");
			}

		} else {
			baseTest.Fail(
					"User should be able to enter <b>" + dataToEnter + "</b> text in the <b>" + friendlyName
							+ "</b> web element",
					"<b>" + friendlyName + "</b> web element is not present/available in the page " + getCurrentTitle()
							+ " to enter text <b>" + dataToEnter + "</b>");
		}
	}
	/*
	 * Enter text
	 * end**********************************************************************
	 * **
	 */

	/*
	 * Single click
	 * start********************************************************************
	 * ****
	 */
	/**
	 * <h1>Do a single click on any object</h1>
	 * <p>
	 * Note: the object name is set to "test" default
	 * 
	 * @param byForElement
	 *            element locator
	 * @author Shankar Ramasamy
	 */
	public void buttonClick(By byForElement) {
		buttonClick(byForElement, "test");
	}

	/**
	 * <h1>Do a single click on any object</h1>
	 * 
	 * @param byForElement
	 *            element locator
	 * @param friendlyName
	 *            object name for reference
	 * @author Shankar Ramasamy
	 */
	public void buttonClick(By byForElement, String friendlyName) {
		buttonClick(getElement(byForElement), friendlyName);
	}

	/**
	 * <h1>Do a single click on any object</h1>
	 * <p>
	 * Note: the object name is set to "test" default
	 * </p>
	 * 
	 * @param webElement
	 *            web element
	 * @author Shankar Ramasamy
	 */
	public void buttonClick(WebElement wElement) {
		buttonClick(wElement, "test");
	}

	/**
	 * <h1>Do a single click on any object</h1>
	 * <p>
	 * this function is the actual implementation of clicking the object
	 * </p>
	 * 
	 * <p>
	 * If the object is not found or not ready for input then a fail step will
	 * be added to the current running test case.
	 * </p>
	 * <p>
	 * If the object is found and ready for input then object get clicked and a
	 * done step will be added to the current running test case.
	 * </p>
	 * 
	 * @param webElement
	 *            web element
	 * @param friendlyName
	 *            object name for reference
	 * @author Shankar Ramasamy
	 */
	public void buttonClick(WebElement webElement, String friendlyName) {
		if (StringUtils.isBlank(friendlyName)) {
			friendlyName = "test";
		}
		if (webElement != null && webElement.isDisplayed() && webElement.isEnabled()) {
			try {
				webElement.click();
				baseTest.Done("User should be able to click the <b>" + friendlyName + "</b> web element",
						"User is able to click the <b>" + friendlyName + "</b> web element");
			} catch (Exception e) {
				baseTest.Fail("User should be able to click the <b>" + friendlyName + "</b> web element",
						"<b>" + friendlyName + "</b> web element is present/available in the page " + getCurrentTitle()
								+ " but not available to click");
			}

		} else {
			baseTest.Fail("User should be able to click the <b>" + friendlyName + "</b> web element",
					"<b>" + friendlyName + "</b> web element is not present/available in the page " + getCurrentTitle()
							+ " to click");
		}
	}
	/*
	 * Single click
	 * end**********************************************************************
	 * **
	 */

	/**
	 * <h1>Wait for object(s)</h1>
	 * <p>
	 * waits for an object to be available if found then it will return
	 * SubmitResponse.HIDDEN otherwise return SubmitResponse.FAILURE
	 * </p>
	 * 
	 * @param mainObjectToShow
	 *            locator for the object to be not exists
	 * @author Shankar Ramasamy
	 */
	public SubmitResponse waitForSubmit(By mainObjectToShow) {
		return waitForSubmit(mainObjectToShow, null);
	}

	/**
	 * <h1>Wait for object(s)</h1>
	 * <p>
	 * waits for an object to be available if found then it will return
	 * SubmitResponse.HIDDEN then If the error object is provided then it waits
	 * for error object to displayed if found then it will return
	 * SubmitResponse.ERROR otherwise return SubmitResponse.FAILURE
	 * </p>
	 * 
	 * @param mainObjectToShow
	 *            locator for the object to be not exists
	 * @param errorObject
	 *            locator for the object to be exists if provided
	 * @author Shankar Ramasamy
	 */
	public SubmitResponse waitForSubmit(By mainObjectToShow, By errorObject) {
		WebElement mainObj = getElement(mainObjectToShow);

		if (mainObj != null) {
			return SubmitResponse.HIDDEN;
		}

		if (errorObject == null) {
			return SubmitResponse.FAILURE;
		}
		WebElement errObj = getElement(errorObject);

		if (errObj != null) {
			return SubmitResponse.ERROR;
		}
		return SubmitResponse.FAILURE;
	}

	/**
	 * <h1>Find an element</h1>
	 * <p>
	 * Find the element by using locator from the web driver
	 * </p>
	 * 
	 * @param element
	 *            locator for the object to find
	 * @author Shankar Ramasamy
	 */
	public WebElement getElement(By element) {
		try {
			return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}
	
	public WebElement getVisibleElement(WebElement element) {
		try {
			return webDriverWait.until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}

	/**
	 * <h1>Find an element from a parent element</h1>
	 * <p>
	 * Find the element by using locator from the given web element
	 * </p>
	 * 
	 * @param parent
	 *            parent webelement
	 * @param element
	 *            locator for the object to find
	 * @author Shankar Ramasamy
	 */
	public WebElement getElementFromParent(WebElement parent, By element) {
		try {
			return webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}
	
	public Boolean getInvisbileElement(By element){
		try{
			return webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(element));
		}catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
			"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}

	/**
	 * <h1>Find an element from a parent element</h1>
	 * <p>
	 * Find the element by using locator from the given web element
	 * </p>
	 * 
	 * @param parent
	 *            parent webelement
	 * @param element
	 *            locator for the object to find
	 * @author Shankar Ramasamy
	 */
	public List<WebElement> getElementsFromParent(WebElement parent, By element) {
		try {
			return webDriverWait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parent, element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}
	/**
	 * <h1>Find an element from a parent locator</h1>
	 * <p>
	 * Find the element by using locator from the given web element
	 * </p>
	 * 
	 * @param parent
	 *            parent element locator
	 * @param element
	 *            locator for the object to find
	 * @author Shankar Ramasamy
	 */
	public WebElement getElementFromParent(By parent, By element) {
		try {
			return webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}

	public WebElement getElementFromParentStatic(WebElement parent, By locator) {
		if (parent != null) {
			return parent.findElement(locator);
		}
		return null;
	}

	public List<WebElement> getElementsFromParentStatic(WebElement parent, By locator) {
		if (parent != null) {
			return parent.findElements(locator);
		}
		return null;
	}

	/**
	 * <h1>Find the elements</h1>
	 * <p>
	 * Find the elements by using locator from the web driver
	 * </p>
	 * 
	 * @param element
	 *            locator for the object to find
	 * @author Shankar Ramasamy
	 */
	public List<WebElement> getElements(By element) {
		try {
			return webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
		} catch (TimeoutException e) {
			baseTest.Fail("User should be able to find a element using locator <b>" + element.toString() + "</b>",
					"Useris not able to find a element using locator <b>" + element.toString() + "</b>");
			return null;
		}
	}

	/**
	 * <h1>Click an element</h1>
	 * <p>
	 * click the element
	 * </p>
	 * 
	 * @param elementPath
	 *            locator for the object to click
	 * @author Shankar Ramasamy
	 */
	public Boolean clickElement(By elementPath) {
		return clickElement(elementPath, "test");
	}

	/**
	 * <h1>Click an element</h1>
	 * <p>
	 * click the element
	 * </p>
	 * 
	 * @param elementPath
	 *            locator for the object to click
	 * @param elementName
	 *            friendly name of the element to be clicked
	 * @author Shankar Ramasamy
	 */
	public Boolean clickElement(By elementPath, String elementName) {
		WebElement obj = getElement(elementPath);
		return clickElement(obj, elementName);
	}

	/**
	 * <h1>Click an element</h1>
	 * <p>
	 * click the element. Actual implementation of the click action
	 * </p>
	 * 
	 * @param obj
	 *            locator for the object to click
	 * @param elementName
	 *            friendly name of the element to be clicked
	 * @author Shankar Ramasamy
	 */
	public Boolean clickElement(WebElement obj, String elementName) {
		if (obj != null) {
			if (obj.isDisplayed()) {
				obj.click();
				baseTest.Done("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
						"User able to click Specified Element : <b>" + elementName + "</b>");
				return true;
			} else {
				baseTest.Fail("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
						"User Unable to click Specified Element : <b>" + elementName + "</b>");
				return false;
			}

		} else {
			baseTest.Fail("Verify User able to find Specified Element :  <b>" + elementName + "</b>",
					"User Unable to find Specified Element : <b>" + elementName + "</b>");
			return false;
		}
	}

	public Boolean click(By elementPath) {
		return click(elementPath, "test");
	}

	public Boolean click(By elementPath, String elementName) {
		WebElement obj = getElement(elementPath);
		return click(obj, elementName);
	}

	public Boolean click(WebElement obj, String elementName) {
		try {
			obj.click();
			baseTest.Done("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
					"User able to click Specified Element : <b>" + elementName + "</b>");
			return true;
		} catch (ElementNotVisibleException e) {

			try {
				jsExecutor.executeScript("arguments[0].click();", obj);
				baseTest.Done("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
						"User able to click Specified Element : <b>" + elementName + "</b>");
			} catch (JavascriptException jse) {
				baseTest.Fail("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
						"User Unable to click Specified Element : <b>" + elementName + "</b> and expection occured "
								+ jse.getLocalizedMessage());
			}
		} catch (Exception e) {
			baseTest.Fail("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
					"User Unable to click Specified Element : <b>" + elementName + "</b> and expection occured "
							+ e.getLocalizedMessage());
		}
		return false;
	}

	public boolean moveToElementAndClick(By obj) {
		return moveToElementAndClick(getElement(obj));
	}

	public boolean moveToElementAndClick(WebElement obj) {
		return moveToElementAndClick(obj, "test");
	}

	public boolean moveToElementAndClick(By obj, String elementName) {
		return moveToElementAndClick(getElement(obj), elementName);
	}

	public boolean moveToElementAndClick(WebElement obj, String elementName) {
		if (obj != null) {
			if (obj.isEnabled()) {
				Actions action = new Actions(webDriver);
				action.moveToElement(obj);
				action.click();
				action.build().perform();
				baseTest.Done("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
						"User able to click Specified Element : <b>" + elementName + "</b>");
				return true;
			} else {
				baseTest.Fail("Verify User able to click Specified Element :  <b>" + elementName + "</b>",
						"User Unable to click Specified Element : <b>" + elementName + "</b>");
				return false;
			}

		} else {
			baseTest.Fail("Verify User able to find Specified Element :  <b>" + elementName + "</b>",
					"User Unable to find Specified Element : <b>" + elementName + "</b>");
			return false;
		}
	}

	/**
	 * <h1>Element's enabled state</h1>
	 * <p>
	 * Get the element enabled state
	 * </p>
	 * 
	 * @param elementPath
	 *            friendly name of the element to be clicked
	 * @author Shankar Ramasamy
	 */
	public Boolean isElementEnable(By elementPath) {

		WebElement element = getElement(elementPath);
		if (element != null && element.isDisplayed() && element.isEnabled()) {
			return true;
		}
		return false;
	}

	/**
	 * <h1>Navigate to menu</h1>
	 * <p>
	 * Navigate to the menu on the server level view
	 * </p>
	 * 
	 * @param menuName
	 *            menu name to navigate, if it is nested menu split with pipe
	 *            (|) symbol
	 * @author Shankar Ramasamy
	 */
	public boolean navigateToMenu(String menuName) {
		return navigateToMenu(menuName, null);
	}

	/**
	 * <h1>Navigate to menu</h1>
	 * <p>
	 * Navigate to the menu on the server level view
	 * </p>
	 * 
	 * @param menuName
	 *            menu name to navigate, if it is nested menu split with pipe
	 *            (|) symbol
	 * @param webElement
	 *            if not null then consider menu navigation successful by
	 *            checking webElement given otherwise just return after
	 *            navigating to menu
	 * @author Shankar Ramasamy
	 */
	public boolean navigateToMenu(String menuName, By webElement) {

		if (StringUtils.isNotEmpty(menuName)) {
			if (menuName.contains(Consts.SYMBOL_PIPE)) {// if | found in the
														// string then consider
														// it is nested menu
				String[] menus = menuName.split(Consts.TESTS_SPLITTER);

				for (int i = 0; i < menus.length; i++) {
					String curMenu = menus[i];
					if (i == 0) {
						clickElement(POM_Home.getNestedMenu(curMenu), String.format("Main menu <b>%s</b>", curMenu));
					} else {
						clickElement(POM_Home.getNestedSubMenu(curMenu), String.format("Sub menu <b>%s</b>", curMenu));
					}
				}
			} else {// direct menu
				clickElement(POM_Home.getNonNestedMenu(menuName), String.format("Direct menu <b>%s</b>", menuName));
			}
		}
		// wait for all page objects to load
		baseTest.pageWaiter.waitAllRequest();
		if (webElement != null) {
			WebElement webEle = getElement(webElement);
			return webEle.isDisplayed();
		} else {
			return true;
		}
	}

	public boolean waitForElementDisappearAfterGivenTime(By identifier, int DelayTime) {
		WebElement obj = getElement(identifier);
		if (obj != null) {
			if (obj.isDisplayed()) {
				try {
					Thread.sleep(DelayTime * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				WebElement	objexists = null;
				try {
				     objexists = webDriver.findElement(identifier);
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (objexists == null) {
					return true;
				} else if (!objexists.isDisplayed()) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
		return false;

	}

	public boolean waitForAnElement(By identifier) {
		return waitForAnElement(identifier, "unknown");
	}

	public boolean waitForAnElement(By identifier, String friendlyName) {
		try {
			Function<WebDriver, Boolean> predicate = new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver wd) {
					try {
						return wd.findElement(identifier).isDisplayed();
					} catch (NoSuchElementException e) {
						return false;
					}
				}
			};
			if (waitByFluent(predicate)) {
				baseTest.Pass(friendlyName + " should be displayed on the screen",
						friendlyName + " is displayed on the screen");
				return true;
			} else {
				baseTest.Fail(friendlyName + " should be displayed on the screen",
						friendlyName + " is displayed on the screen");
				return false;
			}
		} catch (TimeoutException te) {
			baseTest.Fail(friendlyName + " should be displayed on the screen",
					friendlyName + " is not displayed on the screen");
			return false;
		}
	}

	public boolean waitForAttributeToBe(WebElement element, String attributeName, String attributeValue) {
		Function<WebDriver, Boolean> predicate = new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver wd) {
				try {
					if (element != null) {
						return element.getAttribute(attributeName).equalsIgnoreCase(attributeValue);
					} else {
						return false;
					}
				} catch (NoSuchElementException e) {
					return false;
				}
			}
		};
		return waitByFluent(predicate);
	}

	public boolean waitByFluent(Function<WebDriver, Boolean> predicate) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(webDriver);
		wait.pollingEvery(CurrentRunProperties.getPollEveryTimeoutDuration());
		wait.withTimeout(CurrentRunProperties.getWebDriverTimeoutDuration());

		Boolean isDisplayed = wait.until(predicate);
		if (isDisplayed != null) {
			return isDisplayed;
		}
		return false;
	}
	public boolean scrollElementToVisible(By identifier){
		//create instance of javascript executor
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		WebElement ele = getElement(identifier);
		js.executeScript("arguments[0].scrollIntoView(true);",ele);
		return true;
		
	}
	public boolean scrollToTop(){
		//create instance of javascript executor
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("window.scrollTo(0, 0)");
		return true;
	}
	public boolean scrollToDown(){
		//create instance of javascript executor
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
		return true;
	}
	
	public void waitforPageLoaded(){
		ExpectedCondition<Boolean> expectedLoad = new ExpectedCondition<Boolean>() {
			
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
			}
			
		};
		try{
			Thread.sleep(250);
			 webDriverWait.until(expectedLoad);
			
		}catch (Exception e) {
			// TODO: handle exception
			baseTest.Fail("Timeout waiting for Page Load Request to complete.");
		}
			
		
	}
	public void waitforAjaxFinished(){
		ExpectedCondition<Boolean> expectedLoad = new ExpectedCondition<Boolean>() {
			
			public Boolean apply(WebDriver driver) {
				return ((Boolean)((JavascriptExecutor) driver).executeScript("return jQuery.active == 0"));
			}
			
		};
		try{
			Thread.sleep(250);
			 webDriverWait.until(expectedLoad);
			
		}catch (Exception e) {
			// TODO: handle exception
			baseTest.Fail("Timeout waiting for Ajax Finished to complete.");
		}
			
		
	}
	/*
	 * Below methods yet to be considered as a common and whether it is belongs
	 * to this class or not
	 */

	public By ddm_showHideButton = By.xpath("//div[@class='wrap-dropdown-menu']");
	public By ddm_MenuObject = By.xpath("//div[contains(@class,'dropdown-menu') and contains(@style,'block')]");
	public By ddm_MO_SelectAll = By.xpath("//button[text()='Select all']");
	public By ddm_MO_DeselectAll = By.xpath("//button[text()='Deselect all']");
	public By ddm_MO_ML_Input = By.tagName("input");
	public By ddm_SearchTextbox = By.xpath("//input[@placeholder='Search']");
	
	public By getFilterObject(String objName){
		return By.xpath(String.format("//div[@class='filters-holder']//span[contains(text(),'%s')]", objName));
	}

	private By get_ddm_MO_ML_div() {
		return By.xpath("//app-custom-checkbox-component/div");
	}

	private By get_listitem(String listItem) {
		return By.xpath(String.format("//li[contains(text(),'%s')]", listItem));
	}

	public void selectDropDownMenuCheckbox(By prtObjDropDownMenu, String itemToSelect, boolean isSearch,
			boolean isCheck) {
		selectDropDownMenuCheckbox(getElement(prtObjDropDownMenu), itemToSelect, isSearch, isCheck);
	}

	public void selectDropDownItem(By prtObjDropDownMenu, String itemToSelect) {
		selectDropDownItem(getElement(prtObjDropDownMenu), itemToSelect);
	}

	public void selectDropDownItem(WebElement prtObjDropDownMenu, String itemToSelect) {

		if (prtObjDropDownMenu != null && prtObjDropDownMenu.isDisplayed()) {

			if (clickElement(prtObjDropDownMenu, "Show Combobox")) {
				WebElement dropDownMenuElement = getElementFromParent(prtObjDropDownMenu, get_listitem(itemToSelect));

				if (!clickElement(dropDownMenuElement, itemToSelect)) {
					baseTest.Fail(itemToSelect + " item checked is not selected");
				}
			}
		}

	}

	public void selectRadioButton(By dropDownObject, String buttontoSelect) {
		try {
			webDriver.switchTo().frame(getElement(getReportFrame()));
			selectRadioButton(getElement(dropDownObject), buttontoSelect);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			webDriver.switchTo().defaultContent();
		}

	}

	public void selectDropDownCheckbox(By prtObjDropDownMenu, String itemToSelect, boolean isSelectAll,
			boolean isDeselectAll, boolean isCheck) {
		try {
			webDriver.switchTo().frame(getElement(getReportFrame()));
			selectDropDownCheckbox(getElement(prtObjDropDownMenu),
					Arrays.asList(itemToSelect.split(Consts.TESTS_SPLITTER)), isSelectAll, isDeselectAll, isCheck);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			webDriver.switchTo().defaultContent();
		}
	}

	public boolean selectDropDownCheckbox(WebElement popupmenu, List<String> listtoselect, Boolean isSelectAll,
			Boolean isDeselectAll, Boolean isCheck) {
		try {
			if (popupmenu != null && !popupmenu.isDisplayed()) {
				if (clickElement(getAnnunciatorIcon(), "Annunciator Arrow Icon")) {
					if (isSelectAll) {
						if (getElement(getCheckName("All")).isSelected()) {
							baseTest.Done("All Checkbox Already Checked state");
						} else {
							WebElement cobj = getElement(getCheckboxParent("All"));
							if (cobj != null && cobj.isDisplayed()) {
								cobj.click();
								baseTest.Done("All Checkbox Checked successfully");
							}

						}
						return true;
					} else {
						if (getElement(getCheckName("All")).isSelected()) {

							WebElement cobj = getElement(getCheckboxParent("All"));
							if (cobj != null && cobj.isDisplayed()) {
								cobj.click();
								if (isDeselectAll) {
									baseTest.Done("All Checkbox UnChecked successfully");
									return true;
								}
							}
							for (String chkname : listtoselect) {
								WebElement obj = getElement(getCheckName(chkname));
								if (isCheck) {

									if (obj != null && !obj.isSelected()) {
										WebElement cbj = getElement(getCheckboxParent(chkname));
										if (cbj != null && cbj.isDisplayed()) {
											cbj.click();
											baseTest.Done("Checkbox : " + chkname + " Checked successfully");
										}
									}
								}
							}
							return true;
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// inorder to close dropdown list
			clickElement(getAnnunciatorIcon(), "Annunciator Arrow Icon");
		}
		return false;
	}

	private By getCheckName(String name) {
		return By.xpath("//div[@id='inptAnnunciatorTrunk']//input[@value='" + name + "' or @title ='" + name + "']");
	}

	private By getCheckboxParent(String name) {
		return By.xpath("//div[@id='inptAnnunciatorTrunk']//input[@value='" + name + "' or @title ='" + name
				+ "']//ancestor::label");
	}

	private By getDateRangeIcon() {
		return By.xpath("//span[@id='dateRangeOuter']//span[@class='rd-checkboxlist-icon']");
	}

	private By getAnnunciatorIcon() {
		return By.xpath("//span[@id='AnnunciatorOuter']//span[@class='rd-checkboxlist-icon']");
	}

	private By getLabel(String name) {
		return By.xpath("//label[text()='" + name + "']");
	}

	public boolean selectRadioButton(WebElement dropDownObject, String buttontoSelect) {

		if (dropDownObject != null && !dropDownObject.isDisplayed()) {
			if (clickElement(getDateRangeIcon(), "Date Range Arrow Icon")) {
				return clickElement(getElementFromParent(dropDownObject, getLabel(buttontoSelect)),
						buttontoSelect + " Radio Button");
			}
		} else {
			return clickElement(getElementFromParent(dropDownObject, getLabel(buttontoSelect)),
					buttontoSelect + " Radio Button");
		}
		return false;

	}

	public List<String> getStringList(String items, String splitter) {
		if (StringUtils.isNotBlank(items) && StringUtils.isNotBlank(splitter)) {
			return Arrays.asList(items.split(splitter));
		}
		return new ArrayList<String>();
	}

	public void selectDropDownMenuCheckbox(WebElement prtObjDropDownMenu, String itemToSelect, boolean isSearch,
			boolean isCheck) {

		selectDropDownMenuCheckbox(prtObjDropDownMenu, getStringList(itemToSelect, "\\|"), false, false, isSearch,
				isCheck);
	}

	public void selectDropDownMenuSelectAll(WebElement prtObjDropDownMenu) {

		selectDropDownMenuCheckbox(prtObjDropDownMenu, new ArrayList<>(), true, false, false, false);
	}

	public void selectDropDownMenuDeselectAll(WebElement prtObjDropDownMenu) {

		selectDropDownMenuCheckbox(prtObjDropDownMenu, new ArrayList<>(), false, true, false, false);
	}

	public void selectDropDownMenuCheckbox(WebElement prtObjDropDownMenu, List<String> itemsToSelect,
			boolean isSelectAll, boolean isDeSellectAll, boolean isSearch, boolean isCheck) {

		if (prtObjDropDownMenu != null && prtObjDropDownMenu.isDisplayed()) {

			if (ddm_ShowDropDownMenu(prtObjDropDownMenu)) {
				
				WebElement dropDownMenuElement = getElementFromParent(prtObjDropDownMenu, ddm_MenuObject);
				
				if (isSelectAll) {
					ddm_SelectAll(dropDownMenuElement);
					return;
				}
				if (isDeSellectAll) {
					ddm_DeselectAll(dropDownMenuElement);
					return;
				}
				if (itemsToSelect != null && itemsToSelect.size() > 0) {
					ddm_DeselectAll(dropDownMenuElement);

					for (String menuName : itemsToSelect) {
						if (isSearch) {
							WebElement search = getElementFromParent(prtObjDropDownMenu, ddm_SearchTextbox);
							if (search != null && search.isDisplayed()) {
								search.clear();
								search.sendKeys(menuName);
							}
						}
						if (!checkUnCheckLiItem(dropDownMenuElement, menuName, isCheck)) {
							baseTest.Fail(menuName + " item checked is not selected");
						}
					}
				}
				ddm_HideDropDownMenu(prtObjDropDownMenu);
			}else{
				baseTest.Fail("DropDown Object not Found");
			}
		}
	}

	private boolean checkUnCheckLiItem(WebElement dropDownMenu, String name, boolean isCheck) {
		WebElement divMenu = getElementFromParent(dropDownMenu, get_ddm_MO_ML_div());

		if (divMenu != null && divMenu.isDisplayed()) {
			WebElement chkBox = getElementFromParent(divMenu, ddm_MO_ML_Input);
			boolean isItemChecked = chkBox.isSelected();
			if (isCheck == isItemChecked) {
				baseTest.Done(name + " item checked is set to " + Boolean.toString(isItemChecked));
			} else {
				clickElement(getElementFromParent(divMenu, By.tagName("label")), name);
			}
			return getElementFromParent(divMenu, ddm_MO_ML_Input).isSelected() == isCheck;

		}
		return false;
	}

	private boolean ddm_ShowDropDownMenu(WebElement paretMenuObject) {
		if (paretMenuObject != null) {
			WebElement dropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			if (dropDownMenu != null) {
				if (dropDownMenu.isDisplayed()) {
					return true;
				}
			}
			buttonClick(getElementFromParent(paretMenuObject, ddm_showHideButton));
			WebElement afterDropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			return afterDropDownMenu != null && afterDropDownMenu.isDisplayed();
		}
		return false;
	}

	private boolean ddm_HideDropDownMenu(WebElement paretMenuObject) {

		if (paretMenuObject != null) {
			WebElement dropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			if (dropDownMenu != null) {
				if (dropDownMenu.isDisplayed()) {
					buttonClick(getElementFromParent(paretMenuObject, ddm_showHideButton));
				}
			}

			WebElement afterDropDownMenu = getElementFromParent(paretMenuObject, ddm_MenuObject);
			if (afterDropDownMenu != null) {
				if (!afterDropDownMenu.isDisplayed()) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	private void ddm_SelectAll(WebElement objMenuList) {
		if (objMenuList != null && objMenuList.isDisplayed()) {
			WebElement selectAll = getElementFromParent(objMenuList, ddm_MO_SelectAll);
			if (selectAll != null && !selectAll.isEnabled()) {
				buttonClick(getElementFromParent(objMenuList, ddm_MO_DeselectAll));
			}
			buttonClick(selectAll);
		}
	}

	private void ddm_DeselectAll(WebElement objMenuList) {
		if (objMenuList != null && objMenuList.isDisplayed()) {
			WebElement DeselectAll = getElementFromParent(objMenuList, ddm_MO_DeselectAll);
			if (DeselectAll != null && !DeselectAll.isEnabled()) {
				buttonClick(getElementFromParent(objMenuList, ddm_MO_SelectAll));
			}
			buttonClick(DeselectAll);
		}
	}

	public Boolean clickTab(By tabPanel, String tabName) {
		return clickTab(getElement(tabPanel), tabName);
	}

	private Boolean clickTab(WebElement tbs, String tabName) {

		if (tbs != null && tbs.isDisplayed()) {
			WebElement tab = getElementFromParent(tbs, get_obj_byname(tabName));
			if (tab.isDisplayed() && tab.isEnabled()) {
				tab.click();
				baseTest.Done("Verify User able to click tab : " + tabName, "User able to click tab : " + tabName);
				return true;
			}

		}
		baseTest.Fail("Verify User able to click tab : " + tabName, "User Wouldn't able to click tab : " + tabName);
		return false;
	}

	private By get_obj_byname(String name) {
		return By.xpath("//*[text() ='" + name + "']");
	}

	public Boolean clickShareLink() {
		WebElement lnk = getElement(By.xpath("//header//app-share-link//a[contains(text(),'Share a link')]"));
		if (lnk != null && lnk.isDisplayed()) {
			return clickElement(lnk, "Share Link");
		}
		return false;
	}

	private By getEditIconByIndex(String rowIndex) {
		return By.xpath(String.format(
				"//div[@role='grid']//div[@ref='eLeftContainer']//div[@row-index='%s']//div[@class='edit-icon']",
				rowIndex));
	}

	private By getRemoveIconByIndex(String rowIndex) {
		return By.xpath(String.format(
				"//div[@role='grid']//div[@ref='eLeftContainer']//div[@row-index='%s']//div[@class='remove-icon']",
				rowIndex));
	}

	private By getRows() {
		return By.xpath("//div[@role='grid']//div[@ref='eBodyViewport']//div[@row-index]");
	}

	private By getColValByIndex(String rowIndex, String colName) {
		return By.xpath(
				String.format("//div[@role='grid']//div[@ref='eBodyViewport']//div[@row-index='%s']/div[@col-id='%s']",
						rowIndex, colName));
	}

	private By getcolumnValue(String colName) {
		return By.xpath(String.format("div[@col-id='%s']", colName));
	}

	private By getRow(String colName, String colValue) {
		return By.xpath(String.format(
				"//div[@role='grid']//div[@ref='eBodyViewport']//div[@col-id='%s' and text()='%s']/parent::div",
				colName, colValue));
	}

	public boolean verifyAdminTable(String colName, String colValue, String colNames, String colValues) {
		List<String> colList = Arrays.asList(colNames.split(Consts.TESTS_SPLITTER));
		List<String> valList = Arrays.asList(colValues.split(Consts.TESTS_SPLITTER));

		return verifyAdminTable(colName, colValue, colList, valList, true, false);
	}

	private By getColumns() {
		return By.xpath("//div[@class='wrap-status-table']//th");
	}

	private By getTotalRows() {
		return By.xpath("//div[@class='wrap-status-table']//tr");
	}

	private By getCellValue(int i, int j) {
		return By.xpath("//div[@class='wrap-status-table']//tr[" + i + "]//td[" + i + "]");
	}

	private By getColValue(int ind) {
		return By.xpath("//div[@class='wrap-status-table']//tr[1]//th[" + ind + "]");
	}

	public boolean verifyPlatformTable(String colName, String colValue, String colNames, String colValues) {
		List<String> colList = Arrays.asList(colNames.split(Consts.TESTS_SPLITTER));
		List<String> valList = Arrays.asList(colValues.split(Consts.TESTS_SPLITTER));

		return verifyPlatformTable(colName, colValue, colList, valList, true);
	}

	public boolean verifyPlatformTable(By parentObject, HashMap<String, String> rowIndentifier,
			HashMap<String, String> validations) {
		return verifyPlatformTable(getElement(parentObject), rowIndentifier, validations);
	}

	public boolean verifyPlatformTable(WebElement parentObject, HashMap<String, String> rowIndentifier,
			HashMap<String, String> validations) {

		By table = By.tagName("table");

		if (parentObject != null && parentObject.isDisplayed()) {
			WebElement objTable = getElementFromParent(parentObject, table);
			HashMap<String, Integer> colNumbers = new HashMap<String, Integer>();
			if (objTable != null && objTable.isDisplayed()) {
				// store column numbers
				String xpath = String.format("//th");
				List<WebElement> columns = objTable.findElements(By.xpath(xpath));
				if (columns != null && columns.size() > 0) {
					for (int i = 0; i < columns.size(); i++) {
						String columnName = columns.get(i).getText().toLowerCase();
						colNumbers.put(columnName, i);
					}
				}
				// row indentification
				List<WebElement> rows = objTable.findElements(By.xpath("//tr"));

				for (int r = 0; r < rows.size(); r++) {
					WebElement curRow = rows.get(r);
					int matchCounter = 0;
					List<WebElement> cell = curRow.findElements(By.tagName("td"));
					if (cell.size() > 0) {
						for (String colName : rowIndentifier.keySet()) {
							int colNumber = colNumbers.get(colName.toLowerCase());
							String actColValue = cell.get(colNumber).getText();
							if (rowIndentifier.get(colName).equalsIgnoreCase(actColValue)) {
								matchCounter++;
								if (rowIndentifier.size() == matchCounter) {
									// start validate actual values
									System.out.println("Row: " + r);
									baseTest.Pass(rowIndentifier.toString() + "  Exists in Platform Table");
									return true;
								} else {
									baseTest.Fail(rowIndentifier.toString() + "  Not Exists in Platform Table");
									return false;
								}
							}
						}
					}
				}

			} else {
				baseTest.Fail("Table not exists");
			}

		}

		return false;
	}

	public boolean verifyPlatformTable(String uColumn, String uValue, List<String> colNames, List<String> colValues,
			boolean isClick) {
		Boolean isPass = false;
		int findIndex = -1;
		List<String> appColumns = getColumnArray(getColumns());
		List<WebElement> rows = getElements(getTotalRows());
		List<WebElement> columns = getElements(getColumns());

		if (rows.size() > 1) {
			for (int i = 1; i < rows.size() - 1; i++) {

				for (int j = 1; j < columns.size() - 1; j++) {
					WebElement tc = getElement(getCellValue(i, j));
					WebElement th = getElement(getColValue(j));
					if (th != null && tc != null && th.isDisplayed() && tc.isDisplayed()) {
						if (th.getText().equals(uColumn) && tc.getText().equals(uValue)) {
							isPass = true;
							findIndex = i;
							baseTest.Pass(
									"Verify table Column Name : [ " + uColumn + " ] Exists with Column value : [ "
											+ uValue + " ]",
									"table Column Name : [ " + uColumn + " ] Not Exists with Column value : [ " + uValue
											+ " ]");
							tc.click();
							if (isClick) {
								return isPass;
							}
							break;
						}
					}
				}
			}
			if (isPass) {
				isPass = false;
				int ind = -1;
				for (String col : colNames) {
					ind++;
					int colind = getColumnIndex(appColumns, col);
					WebElement tc = getElement(getCellValue(findIndex, colind));
					WebElement th = getElement(getColValue(colind));
					if (th != null && tc != null && th.isDisplayed() && tc.isDisplayed()) {
						if (th.getText().equals(col) && tc.getText().equals(colValues.get(ind))) {
							isPass = true;
						} else {
							return false;
						}
					}

				}

				baseTest.Pass(
						"Verify table Column Names : [ " + colNames + " ] Exists with Column values : [ " + colValues
								+ " ]",
						"table Column Names : [ " + colNames + " ] Exists with Column values : [ " + colValues + " ]");

			} else {
				baseTest.Fail(
						"Verify table Column Name : [ " + uColumn + " ] Exists with Column value : [ " + uValue + " ]",
						"table Column Name : [ " + uColumn + " ] Not Exists with Column value : [ " + uValue + " ]");
				return false;
			}
		}
		return false;
	}

	public List<String> getColumnArray(By columnpath) {

		List<WebElement> cols = getElements(columnpath);
		List<String> colArr = new ArrayList<String>();
		for (WebElement col : cols) {

			colArr.add(col.getText());

		}

		return colArr;

	}

	public int getColumnIndex(List<String> cols, String name) {
		int ind = -1;
		for (String col : cols) {
			ind++;
			if (col.equals(name)) {
				return ind;
			}

		}
		return -1;
	}

	public boolean verifyAdminTable(String uColumn, String uValue, List<String> colNames, List<String> colValues,
			boolean isEdit, boolean isRemove) {

		WebElement tr = getElement(getRow(uColumn, uValue));
		boolean isPass = false;
		if (tr != null && tr.isDisplayed()) {
			String ri = tr.getAttribute("row-index");
			int ind = -1;
			for (String col : colNames) {
				ind++;
				tr.findElement(By.xpath("div[@col-id='" + col + "']")).getText().equals(colValues.get(ind));
				WebElement actVal = getElementFromParent(tr, getcolumnValue(col));
				if (actVal.getText().equals(colValues.get(ind))) {
					isPass = true;
				} else {
					isPass = false;
					break;
				}
			}

			if (isPass) {
				baseTest.Pass(
						"Verify table Column Names : [ " + colNames + " ] Exists with Column values : [ " + colValues
								+ " ]",
						"table Column Names : [ " + colNames + " ] Exists with Column values : [ " + colValues + " ]");
				if (isEdit) {
					WebElement edit = getElement(getEditIconByIndex(ri));
					if (edit != null && edit.isDisplayed()) {
						edit.click();
						return true;
					}
					return false;

				} else if (isRemove) {
					WebElement remove = getElement(getRemoveIconByIndex(ri));
					if (remove != null && remove.isDisplayed()) {
						remove.click();
						return true;
					}
					return false;
				}
				return true;
			} else {
				baseTest.Fail(
						"Verify table Column Names : [ " + colNames + " ] Exists with Column values : [ " + colValues
								+ " ]",
						"table Column Names : [ " + colNames + " ] Not Exists with Column values : [ " + colValues
								+ " ]");
				return false;
			}
		}
		return false;
	}

	public boolean verifyReportsTable(String colName, String colValue, String colNames, String colValues) {
		List<String> colList = Arrays.asList(colNames.split(Consts.TESTS_SPLITTER));
		List<String> valList = Arrays.asList(colValues.split(Consts.TESTS_SPLITTER));

		return verifyReportsTable(colName, colValue, colList, valList, true, false, false);
	}

	private By getUniqueRow(String colName, String colValue) {
		return By.xpath(String.format(
				"//table[@id='HomeReportTable']//tbody//span[contains(translate(@id, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'%s')  and text()= '%s']",
				colName.toLowerCase(), colValue));
	}

	private By getParentByRow(String colName, String colValue) {
		return By.xpath(String.format(
				"//table[@id='HomeReportTable']//tbody//span[contains(translate(@id, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'%s')  and text()= '%s']//ancestor::tr",
				colName.toLowerCase(), colValue));
	}

	private By getColumnValue(String colName, String colValue) {
		return By.xpath(String.format(
				"//span[contains(translate(@id, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'%s')  and text()= '%s']",
				colName.toLowerCase(), colValue));
	}

	private By getScheduleIcon() {
		return By.xpath("//td[contains(@id,'Actions')]//img[@class='schedule-icon']");
	}

	private By getPinReport() {
		return By.xpath("//td[contains(@id,'Actions')]//img[@class='plus-icon']");
	}

	private By getReportFrame() {
		return By.xpath("//iframe[contains(@src,'HistoricalReporting')]");
	}

	public boolean verifyReportsTable(String uColumn, String uValue, List<String> colNames, List<String> colValues,
			boolean isClick, boolean isSchedule, boolean isPin) {
		try {
			// switch parent frame
			webDriver.switchTo().frame(getElement(getReportFrame()));
			WebElement tr = getElement(getUniqueRow(uColumn, uValue));
			boolean isPass = false;
			if (tr != null && tr.isDisplayed()) {
				WebElement fRow = getElement(getParentByRow(uColumn, uValue));
				if (fRow != null && fRow.isDisplayed()) {
					int ind = -1;
					for (String col : colNames) {
						ind++;
						WebElement expVal = getElementFromParent(fRow, getColumnValue(col, colValues.get(ind)));
						if (expVal != null || expVal.isDisplayed()) {
							isPass = true;
						} else {
							isPass = false;
							break;
						}
					}
				}

				if (isPass) {
					baseTest.Pass(
							"Verify table Column Names : [ " + colNames + " ] Exists with Column values : [ "
									+ colValues + " ]",
							"table Column Names : [ " + colNames + " ] Exists with Column values : [ " + colValues
									+ " ]");
					if (isClick) {
						tr.click();
						return true;
					}
					if (isSchedule) {
						WebElement sch = getElementFromParent(fRow, getScheduleIcon());
						if (sch != null && sch.isDisplayed()) {
							sch.click();
							return true;
						}
						return false;

					} else if (isPin) {
						WebElement pin = getElementFromParent(fRow, getPinReport());
						if (pin != null && pin.isDisplayed()) {
							pin.click();
							return true;
						}
						return false;
					}
					return true;
				} else {
					baseTest.Fail(
							"Verify table Column Names : [ " + colNames + " ] Exists with Column values : [ "
									+ colValues + " ]",
							"table Column Names : [ " + colNames + " ] Not Exists with Column values : [ " + colValues
									+ " ]");
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			webDriver.switchTo().defaultContent();
		}
		return false;
	}

}
