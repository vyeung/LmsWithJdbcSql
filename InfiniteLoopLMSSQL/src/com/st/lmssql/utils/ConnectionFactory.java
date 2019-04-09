package com.st.lmssql.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

	static Properties prop = new Properties();
	static InputStream input = null;
	public static String dbURL;
	public static String user;
	public static String pass;
	
	public static Connection getMyConnection() {
		try {
			input = new FileInputStream("resources/db.properties");
			prop.load(input);
			
			dbURL = prop.getProperty("dbURL");
			user = prop.getProperty("user");
			pass = prop.getProperty("pass");
		}
		catch(IOException e) {
			System.err.println("Unable to find db.properties file!");
		}
		
		Connection dbConnection = null;
		try {
			dbConnection = DriverManager.getConnection(dbURL, user, pass);
			dbConnection.setAutoCommit(false);
			return dbConnection;
		} 
		catch (SQLException e) {
			throw new RuntimeException("Error connecting to the database! Maybe server is off", e);
		}
	}
}