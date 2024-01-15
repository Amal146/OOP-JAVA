package Inventory_Management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reporter extends User {
	private List<Reporter> reporterList;
	public Reporter(int userId, String username, String password) {
		super(userId, username, password);
	}
	private static String[][] reporters;

	public void readCsvFile() {
        
        String csvFile = "reporters.csv";
        String line;
        String csvSplitBy = ",";
        int rowCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                rowCount++;
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }

        reporters = new String[rowCount][3];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(csvSplitBy);
                reporters[rowIndex][0] = userData[0]; // User ID
                reporters[rowIndex][1] = userData[1]; // Username
                reporters[rowIndex][2] = userData[2]; // Password
                rowIndex++;
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }
	
	public void Reporters() {
        this.reporterList = new ArrayList<>();
    }

    public boolean isReprterExists(int reporterId) {
        for (Reporter reporter : reporterList) {
            if (reporter.getUserId() == getUserId()) {
                return false; 
            }
        }
        return true; 
    }
    
    


    
    public static boolean authenticateUser(String username, String password) {
    	for (String[] rp : reporters) {
            if (rp[1].equals(username) && rp[2].equals(password)) {
                return true;
            }
        }
        return false;    }

	
   

}
