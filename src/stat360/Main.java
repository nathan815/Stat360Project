package stat360;

public class Main {
	
	// start and end table sizes
	// powers of 2 
	private static final int START_POWER = 10;
	private static final int END_POWER = 25;
	
	public static void main(String args[]) {
		// Step 1 - Create the tables for this project
		//GenerateData gen = new GenerateData(START_POWER, END_POWER);
		//gen.createTables();
		// Done
		
		// Step 2 - Actually generate data for those tables
		//gen.insertDataIntoTables();
		// Done
		
		// Step 3 - Run the queries on each table size, in random order
		RunQuery runner = new RunQuery(START_POWER, END_POWER);
		runner.run();
	}
}
