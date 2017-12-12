package stat360;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RunQuery {
	
	private Connection conn;
	private int startPower;
	private int endPower;
	
	public RunQuery(int start, int end) {
		this.startPower = start;
		this.endPower = end;
		conn = MysqlConn.getInstance().getConnection();
	}
	
	public void run() {
		try {
			this.runQueryOnTableSize(10);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void runQueryOnTableSize(int power) throws SQLException {
		Statement stmt;
		long startTime = 0;
		long nanosecondsRunTime = 0;
		double millisecondsRunTime = 0;
		String pre  = "PreTest_2^" + power;
		String post = "PostTest_2^" + power;
		
		// This query selects the x students by their improvement
		// from pre to post test score
		String sql = "SELECT Pre.StudentId, "
				   +        "Pre.Score as PreScore, "
				   +        "Post.Score as PostScore, "
				   + 		"(Post.Score - Pre.Score) AS ScoreDiff "
				   +   "FROM `%s` AS Pre "
				   +   "JOIN `%s` AS Post "
				   +     "ON Pre.StudentId = Post.StudentId "
				   +  "ORDER BY ScoreDiff DESC "
				   +  "LIMIT 10 ";
		
		sql = String.format(sql, pre, post);
		System.out.println("Table size: 2^"+power);
		System.out.println("Running: " + sql + "\n");
		
		stmt = conn.createStatement();

		startTime = System.nanoTime();
		ResultSet set = stmt.executeQuery(sql);
		nanosecondsRunTime = System.nanoTime() - startTime;
		millisecondsRunTime = nanoToMilli(nanosecondsRunTime);
		
		while(set.next()) {
			System.out.println(
					"Student: " + set.getInt(1) 
				+ ", Pre: " + set.getInt(2) 
				+ ", Post: " + set.getInt(3)
				+ ", Diff: " + set.getInt(4)
			);
		}
		
		System.out.println("\nQuery took " + millisecondsRunTime + " milliseconds");
		
	}
	
	private int randomTableSizePower() {
		return (int) Math.random() * (startPower - endPower) + startPower;
	}

	private double nanoToMilli(long nano) {
		return nano / 1000000.0;
	}
}
