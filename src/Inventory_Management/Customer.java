package Inventory_Management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User{
	private List<Customer> customerList;
	private String Address ;
	private String Email ;
	private String Phone ;
	private static String[][] customers;
	
	public Customer(String Address , String Email , String Phone  ,int userId , String Username) {
		super(userId, Username);
		this.setAddress(Address) ;
		this.setEmail(Email) ;
		this.setPhone(Phone) ;
	}


	public String getAddress() {
		return Address;
	}


	public void setAddress(String address) {
		Address = address;
	}


	public String getEmail() {
		return Email;
	}


	public void setEmail(String email) {
		Email = email;
	}


	public String getPhone() {
		return Phone;
	}


	public void setPhone(String phone) {
		Phone = phone;
	}
	
	
	
	static void addCustomer(String name , String phone , String adresse ,String email) {
		int lineNumber = 1;
		try (BufferedReader br = new BufferedReader(new FileReader("customers.csv"))) {
			while ((br.readLine()) != null) {
                lineNumber++;
            }	    
	     } catch (IOException e) {
	    e.printStackTrace();
	   }	
	   try (BufferedWriter pw = new BufferedWriter(new FileWriter("customers.csv", true))) {
        pw.write("\n"  + lineNumber + ","+ name + "," + phone + "," + adresse + "," + email  );
    
        } catch (IOException e) {
    e.printStackTrace();
   }
   }

	public void readCsvFile() {
        
        String csvFile = "customers.csv";
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

        customers = new String[rowCount][5];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(csvSplitBy);
                customers[rowIndex][0] = userData[0]; // User ID
                customers[rowIndex][1] = userData[1]; // Username
                customers[rowIndex][2] = userData[2];
                customers[rowIndex][3] = userData[3]; 
                customers[rowIndex][4] = userData[4]; 
                rowIndex++;
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }
	
	public void Customers() {
        new ArrayList<>();
    }

    public boolean isManagerExists(int managerId) {
        for (Customer c : customerList) {
            if (c.getUserId() == getUserId()) {
                return false; 
            }
        }
        return true; 
    }
	
	
}
	 
	 


