package stat360;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateData {
	
	private Connection conn;
	private Statement stmt;
	private int startPower;
	private int endPower;
	
	public GenerateData(int start, int end) {
		this.startPower = start;
		this.endPower = end;
		conn = MysqlConn.getInstance().getConnection();
	}
	
	/**
	 * Create the PreTest and PostTest tables for each table size
	 * Data will be generated later on
	 */
	public void createTables() {
		for(int i = startPower; i <= endPower; i++) {
			createTable("PreTest_2^"+i);
			createTable("PostTest_2^"+i);
		}
	}
	
	private void createTable(String name) {
		String sql = String.format("CREATE TABLE %s (StudentId int(11), Score int(11));", name);
		
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
			System.out.println("Created table: " + name);
		} catch (SQLException e) {
			System.out.println("SQL: "+sql);
			e.printStackTrace();
		}
	}
	
	/**
	 * Insert data into the Pre and PostTest tables for each table size
	 */
	public void insertDataIntoTables() {
		try {
			stmt = conn.createStatement();

			System.out.println("Starting table data insertion...");
			for(int i = startPower; i <= endPower; i++) {
				System.out.println("Table size: 2^"+i);
				insertRowsIntoTable("PreTest_2^"+i, i);
				insertRowsIntoTable("PostTest_2^"+i, i);
			}
			
			stmt.close();
			System.out.println("Finished inserting data into tables.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void insertRowsIntoTable(String tableName, int power) throws SQLException {
		
		int numRows = (int) Math.pow(2, power);
		
		StringBuilder sql = new StringBuilder();
		
		// split into multiple INSERT query strings 
		// (2^power/power^2) i.e. 2^25/25^2 
		// otherwise we run out of memory due to query string being too long
		int rowsPerQuery = (int) Math.ceil(numRows / Math.pow(power, 2));
		int numQueries = numRows/rowsPerQuery;
		int lastStop = 0;
		int stop = 0;
		int score = 0;
		
		for(int j = 0; j < numQueries; j++) {
			System.out.println("\nStarting SQL string #"+j+" creation for " + tableName);
			// clear what sql has right now
			sql.setLength(0);
			sql.append("INSERT INTO `");
			sql.append(tableName);
			sql.append("` (`StudentId`, `Score`) VALUES");
			
			stop = lastStop + rowsPerQuery;
			
			// construct INSERT SQL string for this query
			for(long id = lastStop+1; id <= stop; id++) {
				score = (int) Math.round(Math.random() * 100);
				sql.append("(");
				sql.append(id); 
				sql.append(",");
				sql.append(score);
				sql.append(")");
				
				if(id != stop)
					sql.append(",");
				
				//System.out.print(id + " " + (id%20==0?"\n":""));
			}
			
			System.out.println("SQL string created. Executing query for "+tableName+" .... ");
			stmt.execute(sql.toString());
			System.out.println("Inserted "+rowsPerQuery+" rows into " + tableName);
			lastStop = stop;
		}
		
		
	}

}
