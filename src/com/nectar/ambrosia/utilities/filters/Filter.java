package com.nectar.ambrosia.utilities.filters;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.nectar.ambrosia.tests.base.BaseTest;

public class Filter {
	private BaseTest baseTest;
	private By filterHome;

	public Filter(BaseTest baseTest,By locator) {
		setTestCase(baseTest);
		setFilterHomeElement(locator);
	}
	public void setTestCase(BaseTest baseTest) {
		this.baseTest = baseTest;
	}
	
	public void setFilterHomeElement(By locator) {
		filterHome = locator;
	}
	
	public WebElement getFilterHome() {
		return baseTest.userAction.getElement(filterHome);
	}
	public BaseTest getTest() {
		return baseTest;
	}
}
