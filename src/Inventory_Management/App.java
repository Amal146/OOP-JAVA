package Inventory_Management;

import java.util.Scanner;

public class App {

	public static void main  (String[] args) {
		
        Manager manager = new Manager("", "");
        manager.readCsvFile();
        
        Reporter reporter = new Reporter(0, "", "");
        reporter.readCsvFile();
        System.out.println("\n========================================================");
		System.out.println("\n>>>>>>>>>>>>> Welcome to our TechStore ^_^ !! <<<<<<<<<<");
		try (Scanner scanner = new Scanner(System.in)) {
			boolean ctrl = true ;
			String r = "";
			while (ctrl) {
				System.out.println("\n°°°Are you customer ? (y/n)");
				r = scanner.nextLine();
			    ctrl = !(r.toLowerCase().equals("y")||r.toLowerCase().equals("n"));
			}
			if(r.toLowerCase().equals("y")) {
				UserInterface.displayMainMenuC();
			}else {
				welcome();
			}
			
		}
	
        
	}
	 public static void welcome() {
	        System.out.println("\n°°°Are you Sales reporter or Inventory Manager ? (IM/SR)");
	        try (Scanner y = new Scanner(System.in)) {
				String role = y.nextLine();
				if (role.toLowerCase().equals("sr") ) {
					welcomeReporter();
					
				}else if (role.toLowerCase().equals("im")) {
				   welcomeManager();
	            } else {
				System.out.print(" >> invalid answer ");
				welcome();
	            }
			}
	 }
     public static void welcomeManager() {
    	 try (Scanner scan = new Scanner(System.in)) {
				System.out.print("Enter your username: ");
				String username = scan.nextLine();
				System.out.print("Enter your password: ");
				String password = scan.nextLine();
				boolean isAuthenticated = Manager.authenticateUser(username,password);
         if (isAuthenticated) {
				System.out.println("\nAuthentication successful\n");
				System.out.println("\n========================================================");
				System.out.println("\n------------------------TechStore-----------------------");
				System.out.println("\n--------------------Manager Interface-------------------");

		 
				UserInterface.displayMainMenuM();     

         } else {
				System.out.println("Authentication failed");
				welcomeManager();
				
                 }
			}
     }
    	 public static void welcomeReporter() {
        	 try (Scanner scan = new Scanner(System.in)) {
    				System.out.print("Enter your username: ");
    				String username = scan.nextLine();
    				System.out.print("Enter your password: ");
    				String password = scan.nextLine();
    				boolean isAuthenticated = Reporter.authenticateUser(username,password);
            if (isAuthenticated) {
    				System.out.println("\nAuthentication successful\n");
    				System.out.println("\n========================================================");
    				System.out.println("\n-------------------------TechStore-----------------------");
    				System.out.println("\n--------------------Reporter Interface-------------------");

    		 
    				UserInterface.displayMainMenuR();     

             } else {
    				System.out.println("Authentication failed");
    				welcomeReporter();
             }
         }
        
	}}