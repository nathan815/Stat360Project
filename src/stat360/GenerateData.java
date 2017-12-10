package stat360;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateData {
	
	private static Connection conn = MysqlConn.getInstance().getConnection();
	private static Statement stmt;
	
	/**
	 * Create the PreTest and PostTest tables for each table size
	 * Data will be generated later on
	 */
	public static void createTables() {
		int startPower = 10;
		int endPower = 30;
		for(int i = startPower; i <= endPower; i++) {
			createTable("PreTest_2^"+i);
			createTable("PostTest_2^"+i);
		}
	}
	
	/**
	 * Insert data into the Pre and PostTest tables for each table size
	 */
	public static void insertDataIntoTables() {
		int startPower = 25;
		int endPower = 30;
		try {
			stmt = conn.createStatement();

			System.out.println("Starting table data creation...");
			for(int i = startPower; i <= endPower; i++) {
				System.out.println("Table size: "+i);
				insertDataForTableSize(i);
			}
			
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertDataForTableSize(int power) throws SQLException {
		long rows = (long) Math.pow(2, power);
		
		conn.setAutoCommit(false);

		insertIntoTable("PreTest_2^"+power, rows);
		insertIntoTable("PostTest_2^"+power, rows);
		
		conn.commit();
		System.out.println("Queries Committed");
		
	}
	
	private static void insertIntoTable(String tableName, long numRows) throws SQLException {
		
		String sqlFormat = "INSERT INTO `%s` (`StudentId`, `Score`) VALUES";
		StringBuilder sql = new StringBuilder();
		int score = 0;
		
		sql = sql.append(String.format(sqlFormat, tableName));
		System.out.println("Starting SQL creation for " + tableName);
		for(long id = 1; id <= numRows; id++) {
			score = (int) Math.round(Math.random() * 100);
			sql.append("(" + id + ", " + score + ") " + (id != numRows ? "," : ""));
			//System.out.print(id + " " + (id%20==0?"\n":""));
		}
		
		System.out.print("\nSQL string created! Executing query for "+tableName+" .... ");
		stmt.execute(sql.toString());
		System.out.println("Inserted "+numRows+" rows into " + tableName);
	}
	
	private static boolean createTable(String name) {
		String sql = String.format("CREATE TABLE %s (StudentId int(11), Score int(11));", name);
		
		try {
			stmt = conn.createStatement();
			boolean exe = stmt.execute(sql);
			System.out.println("Created table: " + name);
			return exe;
		} catch (SQLException e) {
			System.out.println("SQL: "+sql);
			e.printStackTrace();
			return false;
		}
	}

}
