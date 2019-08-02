package com.nectar.ambrosia.database;

public enum DB_DRIVER {
	
	POSTGRES("org.postgresql.Driver"),
	ORACLE("org.oracle.Driver");
	
	String value;
	
	private DB_DRIVER(String val) {
		value = val;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public String getValue() {
		return value;
	}
	
}
