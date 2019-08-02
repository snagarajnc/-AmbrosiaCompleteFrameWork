package com.nectar.ambrosia.utilities.filters;

import org.openqa.selenium.By;

import com.nectar.ambrosia.tests.base.BaseTest;

public class AlertLevelFilter extends FilterWithCheckboxsAndRadio {

	public AlertLevelFilter(BaseTest baseTest, By locator) {
		super(baseTest, locator, "Alert level");
	}
	
	public boolean verifyFilterSelection(String value) {
		//TODO:: Need to implement the verification of selected filters
		return false;
	}

}
