package stat360;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class RunQuery {
	
	private Connection conn;
	private int startPower;
	private int endPower;
	private int[] powers;
	
	public RunQuery(int start, int end) {
		this.startPower = start;
		this.endPower = end;
		conn = MysqlConn.getInstance().getConnection();
		powers = getListOfPowers();
	}
	
	public void runTrials(int numTrials) {
		int[] randomPowers;
		int trial = getLastTrialNumber();
		try {
			Statement stmt = conn.createStatement();
			for(int i = 0; i < numTrials; i++) {
				trial++;
				System.out.println("Trial #" + trial);
				
				// for every trial, re-randomize the list of table sizes
				randomPowers = getRandomizedPowers();
				// run query for each table size
				for(int j = 0; j < randomPowers.length; j++) {
					runQueryOnTableSize(randomPowers[j], trial, stmt);
				}

			}	
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Run the query for a given table size
	 * @param power
	 * @throws SQLException
	 */
	private void runQueryOnTableSize(int power, int trial, Statement stmt) throws SQLException {
		long startTime = 0;
		long nanosecondsRunTime = 0;
		double millisecondsRunTime = 0;
		String pre  = "PreTest_2^" + power;
		String post = "PostTest_2^" + power;
		
		// this query joins the two pre and post tables
		String sql = "SELECT Pre.StudentId, "
				   +        "Pre.Score, "
				   +        "Post.Score "
				   +   "FROM `%s` AS Pre "
				   +   "JOIN `%s` AS Post "
				   +     "ON Pre.StudentId = Post.StudentId ";
		
		sql = String.format(sql, pre, post);
		System.out.println("Trial #"+trial+", Table size: 2^"+power);
		System.out.println("Running: " + sql);

		startTime = System.nanoTime(); // start timer
		stmt.executeQuery(sql); // run query
		nanosecondsRunTime = System.nanoTime() - startTime; // end timer

		millisecondsRunTime = nanoToMilli(nanosecondsRunTime);
		System.out.println("\nQuery took " + millisecondsRunTime + " milliseconds\n");
		
		recordTime(millisecondsRunTime, power, trial);
		
	}
	
	/**
	 * Records query run time for a table size in Stats table
	 * @param time
	 * @param tableSize
	 * @throws SQLException 
	 */
	private void recordTime(double time, int tableSize, int trial) throws SQLException {
		String sql = "INSERT INTO Stats ( TableSizePower, RunTime, Trial ) VALUES ( ?, ?, ? );";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, tableSize);
		stmt.setDouble(2, time);
		stmt.setInt(3, trial);
		stmt.execute();
		stmt.close();
	}
	
	/**
	 * Returns the latest trial number in Stats table
	 * @return
	 */
	private int getLastTrialNumber() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery("SELECT Trial FROM Stats ORDER BY Trial DESC LIMIT 1");
			if(set.next())
				return set.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Returns array of table size powers of 2
	 * @return
	 */
	private int[] getListOfPowers() {
		int length = endPower-startPower+1;
		int[] listOfPowers = new int[length];
		for(int i = startPower; i <= endPower; i++) {
			listOfPowers[i-startPower] = i;
		}
		return listOfPowers;
	}
	
	/**
	 * randomized list of powers 
	 * @return
	 */
	private int[] getRandomizedPowers() {
		Random r = new Random();
		for(int i = powers.length-1; i > 0; i--) {
			int rand = r.nextInt(i);
			int temp = powers[i];
			powers[i] = powers[rand];
			powers[rand] = temp;
		}
		return powers;
	}

	private double nanoToMilli(long nano) {
		return nano / 1000000.0;
	}
}
