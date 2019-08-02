package com.nectar.ambrosia.utilities.filters;

import org.openqa.selenium.By;

import com.nectar.ambrosia.tests.base.BaseTest;

public class RowShowFilter extends FilterWithCheckboxsAndRadio {

	public RowShowFilter(BaseTest baseTest, By locator) {
		super(baseTest, locator, "Row show");
	}

	public void selectRows(int rowCount) {
		selectValueWithRadioButton(Integer.toString(rowCount));
	}
	@Override
	By getValueRadioButtonClickable(String value) {
		return By.xpath(String.format("//ul/li[normalize-space(text())='%s']", value));
	}
}
