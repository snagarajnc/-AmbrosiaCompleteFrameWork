package com.nectar.ambrosia.tests.base;

public interface IBaseTest {
	
	
	void Pass(String actual);
	void Pass(String expected,String actual);
	void Pass(String actual,boolean takeScreenshot);
	void Pass(String expected,String actual,boolean takeScreenshot);
	
	void Fail(String actual);
	void Fail(String expected,String actual);
	void Fail(String actual,boolean takeScreenshot);
	void Fail(String expected,String actual,boolean takeScreenshot);
	
	void Warning(String actual);
	void Warning(String expected,String actual);
	void Warning(String actual,boolean takeScreenshot);
	void Warning(String expected,String actual,boolean takeScreenshot);
	
	void Done(String actual);
	void Done(String expected,String actual);
	void Done(String actual,boolean takeScreenshot);
	void Done(String expected,String actual,boolean takeScreenshot);
	
	void Info(String actual);
	void Info(String expected,String actual);
	void Info(String actual,boolean takeScreenshot);
	void Info(String expected,String actual,boolean takeScreenshot);
	
	void Title(String title);
	
	
	

}
