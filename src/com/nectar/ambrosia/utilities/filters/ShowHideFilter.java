package com.nectar.ambrosia.utilities.filters;

import org.openqa.selenium.By;

import com.nectar.ambrosia.tests.base.BaseTest;

public class ShowHideFilter extends FilterWithCheckboxsAndRadio {
	
	public ShowHideFilter(BaseTest baseTest, By locator) {
		super(baseTest, locator, "Show/Hide");
	}

}
