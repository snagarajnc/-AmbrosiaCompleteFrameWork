package com.nectar.ambrosia.utilities.filters;

import org.openqa.selenium.By;

import com.nectar.ambrosia.tests.base.BaseTest;

public class GraphModalityFilter extends FilterWithCheckboxsAndRadio {

	public GraphModalityFilter(BaseTest baseTest, By locator) {
		super(baseTest, locator, "Graph");
	}

	@Override
	By getValueCheckbox(String value) {
		return By.xpath(String.format(
				"//app-custom-checkbox-component/div/label/span[normalize-space(text())='%s']/parent::label/preceding-sibling::input",
				value));
	}

	@Override
	By getValueCheckboxClickable(String value) {
		return By.xpath(
				String.format("//app-custom-checkbox-component/div/label/span[normalize-space(text())='%s']", value));
	}
}
