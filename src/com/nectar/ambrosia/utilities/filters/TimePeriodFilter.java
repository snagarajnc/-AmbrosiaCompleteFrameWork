package com.nectar.ambrosia.utilities.filters;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.nectar.ambrosia.tests.base.BaseTest;

public class TimePeriodFilter extends Filter {

	By dropDownContainer = By.cssSelector("div[class^='time-period-container']");

	By getFilterValueLocator(String period) {
		return By.cssSelector(String.format("input[name='timePeriod'] ~ label[for='period-%s' i]", period));
	}

	By selectButton = By.cssSelector("div[class$='time-period-filter'] button[type='Button']");

	By filteredValue = By.cssSelector("span.selected-options");

	public TimePeriodFilter(BaseTest baseTest,By locator) {
		super(baseTest,locator);
		getTest().Title("Time period filter");
	}

	public void filter(String periodValue) {
		if (StringUtils.isNotBlank(periodValue)) {
			try {
				expendMenu();
				clickFilterValue(periodValue);
				selectFilter();
				if (verifyFilteredValue(periodValue)) {
					getTest().Pass("User should be able to filter as " + periodValue + " value in Time period filter",
							"User is able to filter as " + periodValue + " value in Time period filter");
				} else {
					getTest().Fail("User should be able to filter as " + periodValue + " value in Time period filter",
							"User is not able to filter as " + periodValue + " value in Time period filter", true);
				}
			} catch (Exception e) {
				getTest().Fail("User should be able to filter as " + periodValue + " value in Time period filter",
						"User is not able to filter as " + periodValue + " value in Time period filter. Exception occred " + e.getMessage());
			}
		} else {
			getTest().Fail("User should be able to filter the time in Time period filter", "No value found for filter");
		}
	}

	private void expendMenu() {
		if (!getFilterHome().findElement(dropDownContainer).isDisplayed()) {
			getTest().userAction.buttonClick(getFilterHome());
		}
	}

	private void clickFilterValue(String period) {
		getTest().userAction.getElementFromParent(getFilterHome(), dropDownContainer)
				.findElement(getFilterValueLocator(period)).click();
	}

	private void selectFilter() {
		getFilterHome().findElement(dropDownContainer).findElement(selectButton).click();
	}

	private boolean verifyFilteredValue(String periodValue) {
		String actualValue = getTest().userAction.getElementFromParent(getFilterHome(), filteredValue).getText();
		return actualValue.equalsIgnoreCase(periodValue);
	}
}
