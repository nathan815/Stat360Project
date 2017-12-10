package stat360;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;  

public class MysqlConn {

	private static final String DB_CONN     = "jdbc:mysql://localhost:3306/";
	private static final String DB_DRIVER   = "com.mysql.jdbc.Driver";
	private static final String DB_NAME     = "stat360";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "root";
	private static MysqlConn instance;
	private static Connection conn;
	
	MysqlConn() {
		connect();
	}
	
	public static MysqlConn getInstance() {
		if(instance == null) {
			instance = new MysqlConn();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	private Connection connect() {
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(  
				DB_CONN+DB_NAME, DB_USERNAME, DB_PASSWORD);  
			return conn;
		} 
		catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 
}
