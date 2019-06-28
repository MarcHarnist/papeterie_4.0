package fr.eni.papeterie.dal.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.eni.papeterie.dal.Settings;



public class JdbcTools {
	private static String urldb;
	private static String userdb;
	private static String password;
	
	
	// bloc d'initialisation static
	static {
		try {
			Class.forName(Settings.getProperty("driverdb"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		urldb = Settings.getProperty("urldb");
		userdb = Settings.getProperty("userdb");
		password = Settings.getProperty("passworddb");
		
		System.out.println("urldb = " + urldb);
		System.out.println("userdb = " + userdb);
		System.out.println("password = " + password);
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(urldb, userdb, password);
	}
	
	public static void closeConnection(Connection connection) throws SQLException {
		if(connection!=null) {
			connection.close();
		}
	}

}
