package com.nectar.ambrosia.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.nectar.ambrosia.tests.base.BaseTest;

public class POM_Login extends POM_Base {

	public POM_Login(BaseTest bTest) {
		super(bTest);
	}

	@FindBy(name = "login")
	public WebElement txtUserName;

	@FindBy(name = "password")
	public WebElement txtPassword;

	@FindBy(xpath = "//div[@class='dui-login-inputs-password']/div")
	public WebElement divPasswordVi;

	public By byErrorMessage = By.xpath("//div[contains(@class,'dui-login-error-message')]");

	@FindBy(xpath = "//button[@type='submit']")
	public WebElement btnSubmit;

	public void setUsername(String userName) {
		baseTest.userAction.enterText(txtUserName, userName, "User name");
	}

	public void clickPasswordViewHide() {
		baseTest.userAction.clickElement(divPasswordVi, "View/Hide password");
	}

	public void setPassword(String password) {
		baseTest.userAction.enterText(txtPassword, password, "Password");
	}

	public void clickLogin() {
		baseTest.userAction.buttonClick(btnSubmit, "Login");
	}

	public boolean errorValidation(String expectedErrorMessage) {
		WebElement lblErrorMessage = baseTest.userAction.getElement(byErrorMessage);
		if (lblErrorMessage != null && lblErrorMessage.isDisplayed()) {
			String actErrorMessage = lblErrorMessage.getText();
			if (expectedErrorMessage.equals(actErrorMessage)) {
				baseTest.Pass(
						"Verify whether the error message <b>" + expectedErrorMessage
								+ "</b> is displayed on the login page",
						"Error message <b>" + expectedErrorMessage + "</b> is displayed on the login page");
				return true;
			} else {
				baseTest.Fail(
						"Verify whether the error message <b>" + expectedErrorMessage
								+ "</b> is displayed on the login page",
						"Error message <b>" + actErrorMessage + "</b> is displayed on the login page instead of <b>"
								+ expectedErrorMessage + "</b>");
			}
		} else {
			baseTest.Fail("Verify whether the error message <b>" + expectedErrorMessage
					+ "</b> is displayed on the login page", "Error label is not displayed on the login page");
		}
		return false;
	}

	public void doLogin(String userName, String password) {
		setUsername(userName);
		clickPasswordViewHide();
		clickPasswordViewHide();
		setPassword(password);
		clickLogin();
	}

}
