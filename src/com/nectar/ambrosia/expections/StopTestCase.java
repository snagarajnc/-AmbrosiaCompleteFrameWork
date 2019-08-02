package com.nectar.ambrosia.expections;

public class StopTestCase extends Exception{

	private static final long serialVersionUID = 1L;
	
	String errorMessage;
	
	public StopTestCase(String erroMessage) {
		this.errorMessage = erroMessage;
	}
	@Override
	public String getMessage() {
		return errorMessage;
	}

}
