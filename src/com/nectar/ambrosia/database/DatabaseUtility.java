package com.nectar.ambrosia.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtility {

	private String userName;
	private String password;
	private String dbServerWithPort;
	private String dbName;
	private DB_DRIVER dbDriver;
	
	public DatabaseUtility(String dbServer, String dbName, String userName, String password) {

	}

	public static void main(String args[]) throws ClassNotFoundException {

		String url = "jdbc:postgresql://localhost:5432/northwind";
		String user = "postgres";
		String password = "postgres";

		try (Connection con = DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(
						"select cus.company_name as \"Company Name\",count(cus.company_name) as \"Num of customers\"  from customers cus\r\n"
								+ "inner join orders odr on cus.customer_id = odr.customer_id \r\n"
								+ "group by cus.company_name \r\n"
								+ "having count(cus.company_name) between 10 and 20 \r\n"
								+ "order by count(cus.company_name) desc;")) {
			int rowsProcessed = 0;
			while (rs.next()) {
				rowsProcessed++;
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					if (rowsProcessed == 1) {
						System.out.print(rs.getMetaData().getColumnName(i) + "                     |");
					} else {
						System.out.print(rs.getString(i) + "                |");
					}
				}
				System.out.println();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbServerWithPort() {
		return dbServerWithPort;
	}

	public void setDbServerWithPort(String dbServerWithPort) {
		this.dbServerWithPort = dbServerWithPort;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	

}
