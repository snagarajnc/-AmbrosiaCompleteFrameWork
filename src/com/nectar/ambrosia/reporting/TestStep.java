package com.nectar.ambrosia.reporting;

import com.google.gson.JsonObject;
import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.common.Status;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class TestStep {

	private String expectedResult;
	private String actualResult;
	private String screenShotPath;
	private Status status;
	private boolean isTitle;

	public TestStep(String actualResult, Status status) {
		this.actualResult = actualResult;
		this.status = status;
	}

	public TestStep(String actualResult, String expectedResult, Status status) {
		this(actualResult, status);
		this.expectedResult = expectedResult;
	}

	public TestStep(String actualResult, Status status, String filePath) {
		this.actualResult = actualResult;
		this.status = status;
		this.screenShotPath = filePath;
	}

	public TestStep(String actualResult, String expectedResult, Status status, String filePath) {
		this(actualResult, status, filePath);
		this.expectedResult = expectedResult;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getExpectedResult() {
		if (expectedResult == null)
			return "n/a";

		return CommonUtilities.formattedStringForJSon(expectedResult);
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getActualResult() {
		return CommonUtilities.formattedStringForJSon(actualResult);
	}

	public void setActualResult(String actualResult) {
		this.actualResult = actualResult;
	}

	public String getScreenShotPath() {
		return screenShotPath;
	}

	public void setScreenShotPath(String screenShostPath) {
		this.screenShotPath = screenShostPath;
	}

	public boolean isTitle() {
		return isTitle;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public JsonObject getTestStepJSON() {
		JsonObject jo = new JsonObject();
		jo.addProperty(Consts.J_ACUTAL, getActualResult());
		jo.addProperty(Consts.J_EXPECTED, getExpectedResult());
		jo.addProperty(Consts.J_SCREENSHOTPATH, getScreenShotPath());
		jo.addProperty(Consts.J_STATUS, getStatus().name());
		jo.addProperty(Consts.J_ISTITLE, Boolean.toString(isTitle()));
		return jo;
	}
}
