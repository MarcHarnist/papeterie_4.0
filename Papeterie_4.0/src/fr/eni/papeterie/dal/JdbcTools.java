package fr.eni.papeterie.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTools {
	
	//Attribut de la Jdbc Tools
	private static String urldb;
	private static String userdb;
	private static String password;
	
	// Bloc static qui sera utilisé une seule fois
	static {
		
		try {
			Class.forName(Settings.getProperty("driverdb"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		urldb = Settings.getProperty("urldb");
		userdb = Settings.getProperty("userdb");
		password = Settings.getProperty("passworddb");
		
		System.out.println(urldb);
		System.out.println(userdb);
		System.out.println(password);
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(urldb, userdb, password);
	}
	public static void closeConnection(Connection connection) throws SQLException {
		if(connection != null)
		{
			connection.close();
		}
	}
}
