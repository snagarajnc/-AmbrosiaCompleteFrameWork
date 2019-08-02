package com.nectar.ambrosia.pom;

import org.openqa.selenium.By;

import com.nectar.ambrosia.tests.base.BaseTest;

public class POM_Home extends POM_Base {
	By byLoggedInUsername = By.xpath("//*[name()='svg' and @class='svg-user-dims']/following-sibling::span");
	public static By feedbackLabel = By.xpath("//div[@class='ui-toast-summary']");

	public POM_Home(BaseTest bTest) {
		super(bTest);
	}

	public static By getNestedMenu(String topLevelMenuName) {
		return By.xpath(String.format(
				"//nav[@class='dui-header-nav']//app-extended-dropdown-menu//div[@class='btn-wrap']//button[translate( text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='%s']",
				topLevelMenuName.toLowerCase()));
	}

	public static By getNonNestedMenu(String menuName) {
		return By.xpath(String.format(
				"//nav[@class='dui-header-nav']//li/a[translate( text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='%s']",
				menuName.toLowerCase()));
	}

	public static By getNestedSubMenu(String subMenuName) {
		return By.xpath(String.format(
				"//nav[@class='dui-header-nav']//p-menu[@styleclass='extended-dropdown-menu']//a/span[translate( text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='%s']",
				subMenuName.toLowerCase()));
	}

	public boolean waitForLogin() {
		return baseTest.userAction.waitForAnElement(byLoggedInUsername);
	}

	public boolean validateLoggedInUsername(String username) {
		String loggedInUserName = baseTest.userAction.getElement(byLoggedInUsername).getText();
		if (username.equals(loggedInUserName)) {
			baseTest.Pass(
					"Verify whether the user <b> " + username
							+ "</b> is logged in by checking the top right corner label",
					"<b> " + username + "</b> is logged in", true);
			return true;
		} else {
			baseTest.Fail(
					"Verify whether the user <b> " + username
							+ "</b> is logged in by checking the top right corner label",
					"<b> " + loggedInUserName + "</b> is logged in instead of <b>" + username + "</b>", true);
			return false;
		}

	}

	public boolean validateFeedbackLabelMessage(String messageToVerify) {
		if (baseTest.userAction.waitForAnElement(feedbackLabel, "Feedback label")) {

			String actErrorMessage = baseTest.userAction.getElement(feedbackLabel).getText();
			if (actErrorMessage.equals(messageToVerify)) {
				baseTest.Pass("Verify whether the " + messageToVerify + " is displayed on the screen",
						messageToVerify + " is displayed on the screen");
				return true;
			} else {
				baseTest.Fail("Verify whether the " + messageToVerify + " is displayed on the screen",
						actErrorMessage + " is displayed instead of " + messageToVerify + " on the screen");
				return false;
			}
		} else {
			baseTest.Fail("Verify whether the " + messageToVerify + " is displayed on the screen",
					messageToVerify + " is not displayed on the screen");
			return false;
		}

	}
}
