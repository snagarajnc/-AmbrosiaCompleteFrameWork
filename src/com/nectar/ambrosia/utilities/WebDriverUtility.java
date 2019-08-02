package com.nectar.ambrosia.utilities;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.tests.base.BaseTest;

public class WebDriverUtility {
	static Logger log = Logger.getLogger(WebDriverUtility.class.getName());
	
	public BaseTest baseTest;
	

	public WebDriverUtility(BaseTest test) {
		this.baseTest = test;
	}

	public ChromeDriver getChromeDriver() {
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		ChromeDriver cd = new ChromeDriver();
		baseTest.Info("Chrome session should be created successfully","Chrome session is created successfully");
		return cd;
	}

	public FirefoxDriver getFirefoxDriver() {
		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "./firefoxLog.log");
		FirefoxDriver ff = new FirefoxDriver();
		baseTest.Info("Firefox session should be created successfully","Firefox session is created successfully");
		return ff;
	}

	public InternetExplorerDriver getIEDriver() {
		System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer.exe");
		baseTest.Info("IE session should be created successfully","IE session is created successfully");
		return new InternetExplorerDriver();
	}

	public WebDriver getBrowser(String browser) {
		WebDriver wd = null;
		if (CurrentRunProperties.getRunVia().equalsIgnoreCase(Consts.LOCAL)) {
			if (browser.equalsIgnoreCase(Consts.FIREFOX)) {
				wd = getFirefoxDriver();
			} else if (browser.equalsIgnoreCase(Consts.IE)) {
				wd = getIEDriver();
			} else {
				wd = getChromeDriver();
			}
		} else {
			wd = getRemoteWebDriver(browser);
		}
		
		wd.manage().timeouts().pageLoadTimeout(CurrentRunProperties.getPageLoadTimeout(), TimeUnit.SECONDS);
		wd.manage().window().maximize();
		
		if(CurrentRunProperties.isDeleteAllCokies()) {
			wd.manage().deleteAllCookies();
		}
		return wd;
	}

	public RemoteWebDriver getRemoteWebDriver(String browser) {
		RemoteWebDriver rwd = null;
		DesiredCapabilities dc = null;

		if (browser.equalsIgnoreCase(Consts.FIREFOX)) {
			dc = DesiredCapabilities.firefox();

		} else if (browser.equalsIgnoreCase(Consts.IE)) {
			dc = DesiredCapabilities.internetExplorer();
		} else {
			dc = DesiredCapabilities.chrome();
		}
		if (CurrentRunProperties.getHubURL() != null) {
			rwd = new RemoteWebDriver(CurrentRunProperties.getHubURL(), dc);
		}
		baseTest.Info(browser + " session should be created successfully",browser + " session is created successfully in remote mode");
		return rwd;
	}
	
	public WebDriverWait getWebDriverWait(WebDriver wd) {
		return new WebDriverWait(wd, CurrentRunProperties.getWebDriverTimeout());
	}
	
	

}
