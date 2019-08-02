package com.nectar.ambrosia.pom;

import org.openqa.selenium.support.PageFactory;

import com.nectar.ambrosia.tests.base.BaseTest;

public class POM_Base{
	
	public BaseTest baseTest;
	
	public POM_Base(BaseTest bTest) {
		baseTest = bTest;
		
	}
	public void intitatePageObjects() {
		PageFactory.initElements(baseTest.wDriver, this);
	}
}
