package com.nectar.ambrosia.reporting;

import java.util.HashMap;

import com.nectar.ambrosia.common.Status;

public class TestCaseRow {
	private String name;
	private String description;
	private Status status;
	private String module;
	private String environment;
	private String browser;
	private String suitename;
	private String duration;
	private String host;
	private String reportPath;
	
	public TestCaseRow(String name) {
		setName(name);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getSuitename() {
		return suitename;
	}
	public void setSuitename(String suitename) {
		this.suitename = suitename;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}
	
	public HashMap<String, String> getData(){
		HashMap<String, String> data = new HashMap<>();
		
		data.put("Name", getName());
		data.put("Description", getDescription());
		data.put("Status",getStatus().name());
		data.put("Module", getModule());
		data.put("Environment", getEnvironment());
		data.put("Browser", getBrowser());
		data.put("Suitename", getSuitename());
		data.put("Duration", getDuration());
		data.put("Host", getHost());
		data.put("Report", getReportPath());
		
		return data;
	}
	
	
	

}
