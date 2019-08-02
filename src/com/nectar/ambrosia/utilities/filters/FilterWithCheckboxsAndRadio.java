package com.nectar.ambrosia.utilities.filters;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.nectar.ambrosia.tests.base.BaseTest;

public class FilterWithCheckboxsAndRadio extends Filter {
	String filterName;

	By dropDownContainer = By.cssSelector("div[class^='dropdown-menu']");

	By searchTextbox = By.cssSelector("input[formcontrolname='search']");

	By getValueCheckbox(String value) {
		return By.xpath(String.format(
				"//app-custom-checkbox-component/div/label[normalize-space(text())='%s']/preceding-sibling::input",
				value));
	}

	By getValueCheckboxClickable(String value) {
		return By
				.xpath(String.format("//app-custom-checkbox-component/div/label[normalize-space(text())='%s']", value));
	}

	By getValueRadioButtonClickable(String value) {
		return By.xpath(
				String.format("//app-custom-radio-button/div/label[normalize-space(text())='%s']", value.replaceAll(" ", "_")));
	}

	By buttons = By.cssSelector("button");

	public FilterWithCheckboxsAndRadio(BaseTest baseTest, By locator, String filterName) {
		super(baseTest, locator);
		this.filterName = filterName;
		getTest().Title(filterName + " filter");
	}

	public String getFilterName() {
		return this.filterName;
	}

	private void expendMenu() {
		if (!getFilterHome().findElement(dropDownContainer).isDisplayed()) {
			getTest().userAction.buttonClick(getFilterHome());
		}
	}

	private void collapseMenu() {
		if (getFilterHome().findElement(dropDownContainer).isDisplayed()) {
			getTest().userAction.buttonClick(getFilterHome());
		}
	}

	public void searchFilter(String search) {
		getTest().userAction.enterText(getTest().userAction.getElementFromParent(getFilterHome(), dropDownContainer)
				.findElement(searchTextbox), search, "Search in fitler");
	}

	public void selectValuesWithCheckbox(String value) {
		List<String> alerts = new ArrayList<String>();
		alerts.add(value);
		selectOrDeSelectValuesWithCheckbox(alerts, null, true);
	}

	public void selectValuesWithCheckbox(String value, String searchString) {
		List<String> alerts = new ArrayList<String>();
		alerts.add(value);
		selectOrDeSelectValuesWithCheckbox(alerts, searchString, true);
	}

	public void selectValuesWithCheckbox(List<String> values) {
		selectOrDeSelectValuesWithCheckbox(values, null, true);
	}

	public void selectValuesWithCheckbox(List<String> values, String searchString) {
		selectOrDeSelectValuesWithCheckbox(values, searchString, true);
	}

	public void deSelectValuesWithCheckbox(String value) {
		List<String> alerts = new ArrayList<String>();
		alerts.add(value);
		selectOrDeSelectValuesWithCheckbox(alerts, null, false);
	}

	public void deSelectValuesWithCheckbox(String value, String searchString) {
		List<String> alerts = new ArrayList<String>();
		alerts.add(value);
		selectOrDeSelectValuesWithCheckbox(alerts, searchString, false);
	}

	public void deSelectValuesWithCheckbox(List<String> values) {
		selectOrDeSelectValuesWithCheckbox(values, null, false);
	}

	public void deSelectValuesWithCheckbox(List<String> values, String searchString) {
		selectOrDeSelectValuesWithCheckbox(values, searchString, false);
	}

	protected void selectOrDeSelectValuesWithCheckbox(List<String> values, String searchString, boolean isSelect) {
		expendMenu();
		if (searchString != null) {
			searchFilter(searchString);
		}
		for (String value : values) {
			try {
				WebElement checkbox = getTest().userAction.getElementFromParent(getFilterHome(), dropDownContainer)
						.findElement(getValueCheckbox(value));
				if (isSelect != Boolean.parseBoolean(checkbox.getAttribute("checked"))) {
					getTest().userAction
							.clickElement(getTest().userAction.getElementFromParent(getFilterHome(), dropDownContainer)
									.findElement(getValueCheckboxClickable(value)), value);
				} else {
					getTest().Done(
							"User should be able to select the " + value + " from the " + getFilterName() + " filter",
							value + " selected state is already " + isSelect + " in the " + getFilterName()
									+ " filter");
				}
			} catch (NoSuchElementException nse) {
				getTest().Fail(
						"User should be able to select the " + value + " from the " + getFilterName() + " filter",
						value + " is not existis in the " + getFilterName() + " filter");
			}
		}
		collapseMenu();
	}

	public void selectValueWithRadioButton(String value) {
		expendMenu();
		getTest().userAction.click(getTest().userAction.getElementFromParent(getFilterHome(), dropDownContainer)
				.findElement(getValueRadioButtonClickable(value)), value);
		collapseMenu();
	}

	public void selectAll() {
		expendMenu();
		WebElement selAll = getSelectAllButton();

		if (!Boolean.parseBoolean(selAll.getAttribute("disabled"))) {
			getTest().userAction.click(selAll, "Select all");
		} else {
			getTest().Done("User should be able to click the Select all under " + getFilterName() + " filter drop down",
					"All values already selected");
		}
		collapseMenu();
	}

	public void deSelectAll() {
		expendMenu();
		WebElement deSelAll = getDeselectAllButton();
		if (!Boolean.parseBoolean(deSelAll.getAttribute("disabled"))) {
			getTest().userAction.click(deSelAll, "Deselect all");
		} else {
			getTest().Done(
					"User should be able to click the Deselect all under " + getFilterName() + " filter drop down",
					"All values already de-selected");
		}
		collapseMenu();
	}

	private WebElement getSelectAllButton() {
		List<WebElement> lstButtons = getTest().userAction.getElementFromParent(getFilterHome(), dropDownContainer)
				.findElements(buttons);

		for (WebElement ele : lstButtons) {
			if (ele.getText().equalsIgnoreCase("Select all")) {
				return ele;
			}
		}
		return null;
	}

	private WebElement getDeselectAllButton() {
		List<WebElement> lstButtons = getTest().userAction.getElementFromParent(getFilterHome(), dropDownContainer)
				.findElements(buttons);

		for (WebElement ele : lstButtons) {
			if (ele.getText().equalsIgnoreCase("Deselect all")) {
				return ele;
			}
		}
		return null;
	}

}
