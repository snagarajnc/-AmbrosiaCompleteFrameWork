package com.nectar.ambrosia.reporting;

import com.nectar.ambrosia.common.Status;

public class HTMLHandler {

	public static final String TESTSTEPTABLE = "teststeps";
	public static final String TESTSTEPHEADER = "teststepheader";
	public static final String TESTSTEPROW = "teststeprow";
	public static final String CAPTION = "teststeptitle";
	public static final String SNO = "sno";
	public static final String PASS = "pass";
	public static final String FAIL = "fail";
	public static final String WARNING = "warning";
	public static final String DONE = "done";
	public static final String UNKNOWN = "unknown";
	public static final String DEFAULT = null;
	public static final String VIEW = "View";
	public static final String[] TESTSTEPHEADERS = new String[] { "Step no", "Expected result", "Actual result",
			"Test step status", "Screen shot" };
	public static final String[] TESTCASESHEADERS = new String[] { "Name", "Description", "Status", "Module",
			"Environment", "Browser", "Suitename", "Duration", "Host", "View report" };

	public static String getStatusClass(Status status) {
		if (Status.PASS.equals(status)) {
			return PASS;
		} else if (Status.FAIL.equals(status)) {
			return FAIL;
		} else if (Status.WARNING.equals(status)) {
			return WARNING;
		} else if (Status.DONE.equals(status) || Status.INFO.equals(status)) {
			return DONE;
		} else {
			return UNKNOWN;
		}
	}

	public static String getDIVTag(String className) {
		return "<div class='" + className + "'>";
	}

	public static String getDIVTag() {
		return "<div>";
	}

	// HTML Utilities
	public static String createHTMLTable(String idValue) {
		return "<table id='" + idValue + "' style='width: 98%'>";
	}

	public static String endHTMLTable() {
		return "</table>";
	}

	// Starting code of theHTML row
	public static String createHTMLRow(String strClass) {
		if (strClass == null || strClass == "") {
			return "<tr>";
		} else {
			return "<tr class = '" + strClass + "'>";
		}
	}

	public static String createHTMLRow() {
		return "<tr>";
	}

	// Ending code of theHTML row
	public static String endHTMLRow() {
		return "</tr>";
	}

	public static String insertHTMLImage(String filePath, String altText) {
		return "<img src='" + filePath + "' alt='" + altText + "' style='width:270px;height:80px;'>";
	}

	// Inserting row data into HTML table
	public static String addHTMLRowData(String strHTMLRowData, String classValue, int colCount) {
		strHTMLRowData = (strHTMLRowData == "") ? "-" : strHTMLRowData;
		if (classValue == null || classValue == "") {
			return "<td class = 'default'>" + strHTMLRowData + "</td>";
		} else if (classValue == "teststeptitle") {
			return "<td class = '" + classValue + "' colspan='" + colCount + "'>" + strHTMLRowData + "</td>";
		} else {
			return "<td class = '" + classValue + "'>" + strHTMLRowData + "</td>";
		}
	}

	public static String addHTMLRowData(String strHTMLRowData, String classValue) {
		strHTMLRowData = (strHTMLRowData == "") ? "-" : strHTMLRowData;
		if (classValue == null || classValue == "") {
			return "<td class = 'default'>" + strHTMLRowData + "</td>";
		} else {
			return "<td class = '" + classValue + "'>" + strHTMLRowData + "</td>";
		}
	}
	public static String addHTMLRowData(String strHTMLRowData, String classValue,String customProp) {
		strHTMLRowData = (strHTMLRowData == "") ? "-" : strHTMLRowData;
		if (classValue == null || classValue == "") {
			return "<td class = 'default' " + customProp + ">" + strHTMLRowData + "</td>";
		} else {
			return "<td class = '" + classValue + "' " + customProp + ">" + strHTMLRowData + "</td>";
		}
	}
	public static String addHTMLTableHeaderData(String headerValue) {
		return "<th>" + headerValue + "</th>";
	}

	public static String addHTMLPopupLink(String HyperLink, String LinkText) {
		return "<a href='" + HyperLink + "' target='_blank' onclick='window.open('" + HyperLink
				+ "', 'popup'); return false;'>" + LinkText + "</a>";
	}

	public static String addHMTLP(String StringValue, String ClassName) {
		StringValue = (StringValue == "") ? "-" : StringValue;
		if (ClassName == null || ClassName == "") {
			return "<p>" + StringValue + "</p>";
		} else {
			return "<p class = '" + ClassName + "'>" + StringValue + "</p>";
		}
	}

	public static String addHTMLOption(String name, String value) {
		return "<option value='" + name + "'>" + value + "</option>";
	}

	public static String addHTMLOptionsforSelect(String[] arrayofValues, boolean noNeedEmptyValue) {
		String htmlText = "";

		if (noNeedEmptyValue) {

		} else {
			htmlText += addHTMLOption("empty", "");
		}
		if (arrayofValues != null) {
			for (int av = 0; av < arrayofValues.length; av++) {
				String currentvalue = arrayofValues[av];
				String name = currentvalue.replace(" ", "_");
				htmlText += addHTMLOption(name, currentvalue);
			}
		}
		return htmlText;
	}

	public static String addHTMLSpan(String tooltipText) {
		return "<span class='tooltiptext'>" + tooltipText + "</span>";
	}

	public static String addDivforTooltip(String mainText, String TooltipText) {
		if (TooltipText == "") {
			TooltipText = mainText;
		}
		return "<div class='tooltip'>" + mainText + " " + addHTMLSpan(TooltipText) + " </div>";
	}

	public static String addHTMLULStart() {
		return "<ul>";
	}

	public static String addHTMLULClose() {
		return "</ul>";
	}

	public static String addHTMLLIItem(String itemvalue) {
		return "<li>" + itemvalue + "</li>";
	}

	public static String getULListHTMLContent(String itemValues, String splitter) {
		String returnHTML = "";
		String[] itemVals = itemValues.split(splitter);

		for (int it = 0; it < itemVals.length; it++) {
			returnHTML += addHTMLLIItem(itemVals[it]);
		}
		return returnHTML;
	}

	public static String getsimpleTableCSSText(String classValue) {
		String txt = "#" + classValue
				+ " {    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;    border-collapse: collapse;    width: 100%;}#";
		txt += classValue + " td, #" + classValue + " th {    border: 1px solid #ddd;    padding: 8px;}#" + classValue
				+ " tr:nth-child(even){background-color: #f2f2f2;}";
		txt += "#" + classValue + " tr:hover {background-color: #ddd;}#" + classValue
				+ " th {    padding-top: 12px;    padding-bottom: 12px;    text-align: left;";
		txt += "    background-color: #4CAF50;    color: white;}";
		return txt;
	}

}
