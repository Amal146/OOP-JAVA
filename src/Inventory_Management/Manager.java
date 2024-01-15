package Inventory_Management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager extends User{
	private List<Manager> managerList;
	public Manager(String username, String password ) {
		super( username, password);
	}
    private static String[][] managers;

	public void readCsvFile() {
        
        String csvFile = "managers.csv";
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

        managers = new String[rowCount][3];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(csvSplitBy);
                managers[rowIndex][0] = userData[0]; // User ID
                managers[rowIndex][1] = userData[1]; // Username
                managers[rowIndex][2] = userData[2]; // Password
                rowIndex++;
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }
	
	public void Managers() {
        new ArrayList<>();
    }

    public boolean isManagerExists(int managerId) {
        for (Manager manager : managerList) {
            if (manager.getUserId() == getUserId()) {
                return false; 
            }
        }
        return true; 
    }
    

    
    
    public static boolean authenticateUser(String username, String password) {
    	for (String[] mn : managers) {
            if (mn[1].equals(username) && mn[2].equals(password)) {
                return true;
            }
        }
        return false;    }
   

}
