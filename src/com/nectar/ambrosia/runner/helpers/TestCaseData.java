package com.nectar.ambrosia.runner.helpers;

import java.util.List;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class TestCaseData {
	String tcId;
	String tcShortname;
	String tcDescription;
	List<String> module;
	List<String> funcstoRun;
	String env;
	boolean runflag;
	String dataSheet;
	
	public TestCaseData(String tcId,String tcShortname,String tcDesc,String module,String funcsToRun,String env,String runflag,String dataSheet) {
		setTcId(tcId);
		setTcShortname(tcShortname);
		setTcDescription(tcDesc);
		setModule(module);
		setFuncstoRun(funcsToRun);
		setEnv(env);
		setRunflag(runflag);
		setDataSheet(dataSheet);
	}
	
	public String getDataSheet() {
		return dataSheet;
	}

	public void setDataSheet(String dataSheet) {
		this.dataSheet = dataSheet;
	}

	public String getTcId() {
		return tcId;
	}

	public void setTcId(String tcId) {
		this.tcId = tcId;
	}

	public String getTcShortname() {
		return tcShortname;
	}

	public void setTcShortname(String tcShortname) {
		this.tcShortname = tcShortname;
	}

	public String getTcDescription() {
		return tcDescription;
	}

	public void setTcDescription(String tcDescription) {
		this.tcDescription = tcDescription;
	}

	public List<String> getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = CommonUtilities.splitStringGetAsList(module, Consts.TESTS_SPLITTER);
	}

	public List<String> getFuncstoRun() {
		return funcstoRun;
	}

	public void setFuncstoRun(String funcstoRun) {
		this.funcstoRun = CommonUtilities.splitStringGetAsList(funcstoRun, Consts.TESTS_SPLITTER);
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public boolean isRunflag() {
		return runflag;
	}

	public void setRunflag(String runflag) {
		this.runflag = CommonUtilities.getBoolValue(runflag);
	}

}
