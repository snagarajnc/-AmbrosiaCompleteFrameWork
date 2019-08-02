package com.nectar.ambrosia.utilities;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nectar.ambrosia.tests.base.BaseTest;

public class PageWaiter {
	 
    private static JavascriptExecutor jsExec;
    
    static Logger log = Logger.getLogger(UserAction.class.getName());

	BaseTest baseTest;
	WebDriver jsWaitDriver;
	WebDriverWait jsWait;

	public PageWaiter(BaseTest btest, WebDriver wd, WebDriverWait wdw) {
		this.baseTest = btest;
		this.jsWaitDriver = wd;
		this.jsWait = wdw;
		jsExec = (JavascriptExecutor) jsWaitDriver;
	}
 
   private void ajaxComplete() {
        jsExec.executeScript("var callback = arguments[arguments.length - 1];"
            + "var xhr = new XMLHttpRequest();" + "xhr.open('GET', '/Ajax_call', true);"
            + "xhr.onreadystatechange = function() {" + "  if (xhr.readyState == 4) {"
            + "    callback(xhr.responseText);" + "  }" + "};" + "xhr.send();");
    }
 
    private void waitForJQueryLoad() {
        try {
            ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) jsWaitDriver)
                .executeScript("return jQuery.active") == 0);
 
            boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");
 
            if (!jqueryReady) {
                jsWait.until(jQueryLoad);
            }
        } catch (WebDriverException ignored) {
        }
    }
 
    private void waitForAngularLoad() {
        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
        angularLoads(angularReadyScript);
    }
 
    private void waitUntilJSReady() {
        try {
            ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) jsWaitDriver)
                .executeScript("return document.readyState").toString().equals("complete");
 
            boolean jsReady = jsExec.executeScript("return document.readyState").toString().equals("complete");
 
            if (!jsReady) {
                jsWait.until(jsLoad);
            }
        } catch (WebDriverException ignored) {
        }
    }
 
    private void waitUntilJQueryReady() {
        Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
        if (jQueryDefined) {
            poll(20);
 
            waitForJQueryLoad();
 
            poll(20);
        }
    }
 
    public void waitUntilAngularReady() {
        try {
            Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
            if (!angularUnDefined) {
                Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
                if (!angularInjectorUnDefined) {
                    poll(20);
 
                    waitForAngularLoad();
 
                    poll(20);
                }
            }
        } catch (WebDriverException ignored) {
        }
    }
 
    public void waitUntilAngular5Ready() {
        try {
            Object angular5Check = jsExec.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
            if (angular5Check != null) {
                Boolean angularPageLoaded = (Boolean) jsExec.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1");
                if (!angularPageLoaded) {
//                    poll(20);
 
                    waitForAngular5Load();
 
//                    poll(20);
                }
            }
        } catch (WebDriverException ignored) {
        }
    }
 
    private void waitForAngular5Load() {
        String angularReadyScript = "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1";
        angularLoads(angularReadyScript);
    }
 
    private void angularLoads(String angularReadyScript) {
        try {
            ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
                .executeScript(angularReadyScript).toString());
 
            boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());
 
            if (!angularReady) {
                jsWait.until(angularLoad);
            }
        } catch (WebDriverException ignored) {
        }
    }
 
    public void waitAllRequest() {
    	System.out.println("Wait start - waitUntilJSReady: "+ Calendar.getInstance().getTime());
        waitUntilJSReady();
        System.out.println("Wait end - waitUntilJSReady: "+ Calendar.getInstance().getTime());
        
        
        System.out.println("Wait start - waitUntilJQueryReady: "+ Calendar.getInstance().getTime());
//        ajaxComplete();
        waitUntilJQueryReady();
        System.out.println("Wait end - waitUntilJQueryReady: "+ Calendar.getInstance().getTime());
        
        System.out.println("Wait start - waitUntilAngularReady: "+ Calendar.getInstance().getTime());
        waitUntilAngularReady();
        System.out.println("Wait end - waitUntilAngularReady: "+ Calendar.getInstance().getTime());
        
        
        System.out.println("Wait start - waitUntilAngular5Ready: "+ Calendar.getInstance().getTime());
        waitUntilAngular5Ready();
        System.out.println("Wait end - waitUntilAngular5Ready: "+ Calendar.getInstance().getTime());
    }
    private void waitAll() {
    	waitUntilJSReady();
        ajaxComplete();
        waitUntilJQueryReady();
        waitUntilAngularReady();
        waitUntilAngular5Ready();
    }
 
    /**
     * Method to make sure a specific element has loaded on the page
     *
     * @param by
     * @param expected
     */
    public void waitForElementAreComplete(By by, int expected) {
        ExpectedCondition<Boolean> angularLoad = driver -> {
            int loadingElements = jsWaitDriver.findElements(by).size();
            return loadingElements >= expected;
        };
        jsWait.until(angularLoad);
    }
 
    /**
     * Waits for the elements animation to be completed
     * @param css
     */
    public void waitForAnimationToComplete(String css) {
        ExpectedCondition<Boolean> angularLoad = driver -> {
            int loadingElements = jsWaitDriver.findElements(By.cssSelector(css)).size();
            return loadingElements == 0;
        };
        jsWait.until(angularLoad);
    }
 
    private void poll(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
