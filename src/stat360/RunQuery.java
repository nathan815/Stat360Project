package stat360;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RunQuery {

	private Connection conn = MysqlConn.getInstance().getConnection();
	private Statement stmt;
	
	public RunQuery() {
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from pre_1k");
			
			while(rs.next())  
				System.out.println("ID: " + rs.getInt(1)+", Pre Score: "+rs.getInt(2));
			
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
