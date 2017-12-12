package stat360;

public class Main {
	
	// start and end table sizes
	// powers of 2 
	private static final int START_POWER = 10;
	private static final int END_POWER = 25;
	
	// number of trials to run
	// each trial runs the query on every table, in random order
	private static final int NUM_TRIALS = 100;
	
	public static void main(String args[]) {

		GenerateData gen = new GenerateData(START_POWER, END_POWER);
		RunQuery runner = new RunQuery(START_POWER, END_POWER);
		
		// Step 1 - Create the tables for this project
		//gen.createTables();
		// Done
		
		// Step 2 - Actually generate data for those tables
		//gen.insertDataIntoTables();
		// Done
		
		// Step 3 - Run the trials
		// This will record query run times for the table sizes in the 'Stats' table
		runner.runTrials(NUM_TRIALS);
	}
}
