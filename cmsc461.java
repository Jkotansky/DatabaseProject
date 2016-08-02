import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class cmsc461 {
	public static void main(String args[]) {
		System.out.println("\n-----------------------------------------");
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager
					.getConnection("jdbc:sqlite:C:/Users/joshuak/Desktop/COMP 521/hold/gsa.db");
			while (true) {
				System.out.println("   Welcome to the GSA database system");
				System.out
						.println("please choose from the following commands:");
				System.out.println("1. Update/Insert SQl queries");
				System.out.println("2. View/Show SQl queries");
				System.out.println("3. Load new information");
				System.out.println("4. Quit");
				System.out.println("Enter the number of the command.");
				System.out.print("-----------------------------------------\n");
				Scanner sc = new Scanner(System.in);
				int choice = sc.nextInt();
				String userCmd = "";
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(System.in));
				switch (choice) {
				case 1:
					System.out
							.print("Enter the valid sql update/insert command"
									+ "\n");
					try {
						userCmd = reader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					gmrUpdate(c, userCmd, 'M');

					break;
				case 2:
					System.out.print("Enter the valid sql query" + "\n");
					try {
						userCmd = reader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					gmrShow(c, userCmd, 'S');

					break;
				case 3:
					System.out
							.println("Please input the name of the file you would like to inout from:");
					Scanner sc2 = new Scanner(System.in);
					String fileName = sc2.nextLine();

					try {
						// FileReader reads text files in the default encoding.
						FileReader fileReader = new FileReader(fileName);

						// Always wrap FileReader in BufferedReader.
						BufferedReader bufferedReader = new BufferedReader(
								fileReader);

						String line;
						while ((line = bufferedReader.readLine()) != null) {
							Parse(line, c);
						}

						// Always close files.
						bufferedReader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 4:
					System.out.println("Quiting...");
					c.close();
					System.out.println("Program Exited.");
					return;
				default:
					System.out.println("Try again");
					break;
				}
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	private static void Parse(String input, Connection c) {
		ArrayList<String> tokens = new ArrayList<String>();
		StringTokenizer tokenizedLine = new StringTokenizer(input);

		// adds every token into the ArrayList to be parsed.
		while (tokenizedLine.hasMoreTokens()) {
			tokens.add(tokenizedLine.nextToken());
		}

		String tableName = tokens.get(0);
		
		if(tableName.equalsIgnoreCase("Office")){
			String insert = "INSERT INTO " + tableName + " VALUES ('" + tokens.get(1) +"', '"+ tokens.get(2) + "', " + tokens.get(3)+" )";
			gmrUpdate(c, insert, 'M');		
		}else if(tableName.equalsIgnoreCase("Rental_Agreement")){
			String insert = "INSERT INTO " + tableName + " VALUES ( "+ tokens.get(1) + ", "+ tokens.get(2)+ ", '"+ tokens.get(3) + "', '"+ tokens.get(4) + "', '"+ tokens.get(5) + "')";
			gmrUpdate(c, insert, 'M');
		}else if(tableName.equalsIgnoreCase("Agency")){
			String insert = "INSERT INTO " + tableName + " VALUES ( "+ tokens.get(1) + ", '"+ tokens.get(2)+ "', '"+ tokens.get(3) + "', '"+ tokens.get(4) + "', '"+ tokens.get(5) + "')";
			gmrUpdate(c, insert, 'M');
		}else if(tableName.equalsIgnoreCase("Agency_Location")){
			String insert = "INSERT INTO " + tableName + " VALUES ('" + tokens.get(1) +"', '"+ tokens.get(2) + "')";
			gmrUpdate(c, insert, 'M');
			
		}else{
			System.out.println("ERROR -- Invalid Table");
			return;
		}		
	}

	private static void gmrUpdate(Connection c, String sqlStmt, char d) {
		int i;
		System.out.println("Results for " + sqlStmt);
		try {
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sqlStmt);
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("SQL error in Update.");
			ex.printStackTrace(System.err);
			// System.exit(1);
		}

	}

	public static void gmrShow(Connection con, String sqlStmt, char sqlType) {
		int i;
		System.out.println("Results for " + sqlStmt);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStmt);
			if (sqlType == 'M')
				return;
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			for (i = 1; i <= numCols; i++) {
				if (i > 1)
					System.out.print(",");
				System.out.print(rsmd.getColumnLabel(i));
			}
			System.out.println("\n-------------------------------------");
			while (rs.next()) {
				for (i = 1; i <= numCols; i++) {
					if (i > 1)
						System.out.print(",");
					System.out.print(rs.getString(i));
				}
				System.out.println("");
			}
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("SQL error in getQuery.");
			ex.printStackTrace(System.err);
		}
	}
}