package Inventory_Management;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class UserInterface {
    private static Scanner scanner = new Scanner(System.in);
    private static InventoryManager inventoryManager = new InventoryManager();
    
    public static void displayMainMenuC() {
    	boolean exit = false;
        while (!exit) {
    	ViewProductsC();
    	System.out.println("\n========================================================");
        System.out.println("1. Buy Product");
        System.out.println("2. Exit");
        
      // Get choice 
        int choice = 0;
        boolean validId = false;
        while (!validId) {
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                validId = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for choice.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
        switch (choice) {
        case 1:
         // Get product id
            int id = 0;
            boolean valid = false;
            while (!valid) {
                System.out.print("Enter product Id: ");
                try {
                    id = scanner.nextInt();
                    valid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for id.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }
            int qty = 0;
            boolean validStock = false;

            // Get product quantity
            while (!validStock) {
                System.out.print("Enter product quantity: ");
                try {
                    qty = scanner.nextInt();
                    validStock = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for quantity.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }


            scanner.nextLine(); // Consume newline
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            
            //Get email
            String email = "";
            boolean ValidEmail = false ;
            while(!ValidEmail) {
                System.out.print("Enter Email : ");
                email = scanner.nextLine();
                if (!(email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$") && !email.contains(","))) {
                   System.err.println("Invalid email format or contains a comma.");
                }else {
                	ValidEmail = true ; 
                }
            }     
            
            //Get phone number
            String phone = "";
            boolean ValidPhone = false ;
            while(!ValidPhone) {
                System.out.print("Enter Phone number: ");
                phone = scanner.nextLine();
            if (!(phone.matches("^\\d{8}$"))) {
                System.err.println("Invalid phone number format. Please enter 8 digits only.");
            }else {
            	ValidPhone = true ; 
            }
            }
            
            System.out.print("Enter adresse : ");
            String adresse = scanner.nextLine();
            
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = today.format(formatter);
            
            Product product = inventoryManager.getProductById(id);
            
            SalesTransaction salesTransaction = new SalesTransaction(null, 0, null, null);
            salesTransaction.addTransaction(product,formattedDate, qty, "sale");
            
            Customer.addCustomer(name , phone , adresse , email);
            
            try (BufferedWriter pw = new BufferedWriter(new FileWriter("orders.csv", true))) {
                
                    pw.write("\n" + name + "," + phone + "," +
                            adresse + "," + email  + "," + qty + " x " + product.getName());
                
               } catch (IOException e) {
                e.printStackTrace();
            }
            
            System.out.println("Thanks For Your Trust ^_^ !! \n");
            break;
        case 2:
        	exit = true;
            break;
        default:
            System.out.println("Invalid choice. Please enter a valid option.");
    }}
        System.out.println("Exiting TechStore Online System. Goodbye!");

    }
    
    public static void displayMainMenuM() {
        boolean exit = false;
        while (!exit) {
        	System.out.println("\n========================================================");
            System.out.println("\n============ Inventory Management System ===============");
            System.out.println("\n========================================================");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Remove Product");
            System.out.println("4. View Products");
            System.out.println("5. Exit");

         // Get choice 
            int choice = 0;
            boolean validId = false;
            while (!validId) {
                System.out.print("Enter your choice: ");
                try {
                    choice = scanner.nextInt();
                    validId = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for choice.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    updateProduct();
                    break;
                case 3:
                    removeProduct();
                    break;
                case 4:
                    ViewProducts();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        System.out.println("Exiting Inventory Management System. Goodbye!");
        App.welcome();
    }
    public static void displayMainMenuR() {
        boolean exit = false;
        while (!exit) {
        	System.out.println("\n========================================================");
            System.out.println("\n============ Inventory Management System ===============");
            System.out.println("\n========================================================");
            System.out.println("1. Record Sales Transaction");
            System.out.println("2. View Products");
            System.out.println("3. View Transactions");
            System.out.println("4. Sales Report ");
            System.out.println("5. Exit");
            
         // Get choice 
            int choice = 0;
            boolean validId = false;
            while (!validId) {
                System.out.print("Enter your choice: ");
                try {
                    choice = scanner.nextInt();
                    validId = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for choice.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }

            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    recordSalesTransaction();
                    break;
                case 2:
                    ViewProducts();
                    break;
                case 3:
                    ViewTransactions();
                    break;   
                case 4:
                    salesReport();
                    break; 
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        System.out.println("Exiting Inventory Management System. Goodbye!");
        App.welcome(); 
    }



    
    public static void ViewTransactions() {
    	SalesTransaction transactions = new SalesTransaction(product,0,"","");
    	List<SalesTransaction> transaction = transactions.readCsvFile();
    	System.out.println("\n========================================================");
    	System.out.println("\n================= View Transactions ====================");
    	System.out.println("\n========================================================");
		System.out.println(" -------------------------Sales---------------------------");
    	for(SalesTransaction t : transaction) {
    		if(t.getTransactionType().toLowerCase().equals("sale"))
    		System.out.println(" Product Id : " + t.getProduct().getId() + "\n Quantity : " + t.getQuantity() + "\n Date : " + t.getDate() + "\n--------------------------------------------------------" );
    	}
    	System.out.println("\n========================================================");
		System.out.println(" ------------------------Purchases-------------------------");
    	for(SalesTransaction t : transaction) {
    		if(t.getTransactionType().toLowerCase().equals("purchase"))
    		System.out.println(" Product Id : " + t.getProduct().getId() + "\n Quantity : " + t.getQuantity() + "\n Date : " + t.getDate() + "\n--------------------------------------------------------" );
    	}
    }
    
    
    
    
    private static Product product = new Product(0, "", "", 0, 0 , "");
    
    
    public static void ViewProducts() {    
    	
    	List<Product> products = product.readCsvFile();
    	System.out.println("\n========================================================");
		System.out.println("\n=================== View Products ======================");
		System.out.println("\n========================================================");
		System.out.println("\n----------------------TechStore-------------------------");
    	System.out.println("\n~~~~~~~~~~~~~~~~~~~~~Smart Phones~~~~~~~~~~~~~~~~~~~~~~~");
    	for (Product product : products) {
    		if(product.getCategory().toLowerCase().equals("smartphone"))
            System.out.println(" id : "+product.getId()+ "\n" + product  + "\n supplier : " + product.getSupplier() + "\n--------------------------------------------------------" );

        }
    	System.out.println("\n~~~~~~~~~~~~~~~~~~~~Smart Watches~~~~~~~~~~~~~~~~~~~~~~~");
    	for (Product product : products) {
    		if(product.getCategory().toLowerCase().equals("smartwatch"))
                System.out.println(" id : "+product.getId()+ "\n" + product  + "\n supplier : " + product.getSupplier() + "\n--------------------------------------------------------" );

        }
    	System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~Tablets~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	for (Product product : products) {
    		if(product.getCategory().toLowerCase().equals("tablet"))
                System.out.println(" id : "+product.getId()+ "\n" + product  + "\n supplier : " + product.getSupplier()  + "\n--------------------------------------------------------" );
    	}

	}
    
    
 public static void ViewProductsC() {    
    	
    	List<Product> products = product.readCsvFile();
    	System.out.println("\n========================================================");
		System.out.println("\n=================== View Products ======================");
		System.out.println("\n========================================================");
		System.out.println("\n----------------------TechStore-------------------------");
    	System.out.println("\n~~~~~~~~~~~~~~~~~~~~~Smart Phones~~~~~~~~~~~~~~~~~~~~~~~");
    
    	for (Product product : products) {
    		if(product.getCategory().toLowerCase().equals("smartphone"))
            System.out.println(" id : "+product.getId()+ "\n" + " " + product.getName() + "\n price : " + String.format("%.3f", product.getPrice() * 0.2 ) + "DT" + "\n Qty :" + product.getStock()  + "\n supplier : " + product.getSupplier() + "\n--------------------------------------------------------" );

        }
    	System.out.println("\n~~~~~~~~~~~~~~~~~~~~Smart Watches~~~~~~~~~~~~~~~~~~~~~~~");
    	for (Product product : products) {
    		if(product.getCategory().toLowerCase().equals("smartwatch"))
                System.out.println(" id : "+product.getId()+ "\n" + " " + product.getName() + "\n price : " + String.format("%.3f", product.getPrice() * 0.2 ) + "DT" + "\n Qty :" + product.getStock()  + "\n supplier : " + product.getSupplier() + "\n--------------------------------------------------------" );

        }
    	System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~Tablets~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	for (Product product : products) {
    		if(product.getCategory().toLowerCase().equals("tablet"))
                System.out.println(" id : "+product.getId()+ "\n" + " " + product.getName() + "\n price : " + String.format("%.3f", product.getPrice() * 0.2 ) + "DT" + "\n Qty :" + product.getStock()  + "\n supplier : " + product.getSupplier() + "\n--------------------------------------------------------" );
    	}

	}




	private static void addProduct() {
		System.out.println("\n========================================================");
        System.out.println("\n=================== Add Product ========================");
        System.out.println("\n========================================================");
     // Get product id
        int id = 0;
        boolean validId = false;
        while (!validId) {
            System.out.print("Enter product Id: ");
            try {
                id = scanner.nextInt();
                validId = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for id.");
                scanner.nextLine(); // Clear the input buffer
            }
        }

        scanner.nextLine(); // Consume newline
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product category : (Tablet/Smartphone/SmartWatch) ");
        String category = scanner.nextLine();
        System.out.print("Enter product supplier: ");
        String supplier = scanner.nextLine();
        int stock = 0;
        int price = 0;
        boolean validStock = false;
        boolean validPrice = false;

        // Get product quantity
        while (!validStock) {
            System.out.print("Enter product quantity: ");
            try {
                stock = scanner.nextInt();
                validStock = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for quantity.");
                scanner.nextLine(); // Clear the input buffer
            }
        }

        // Get product price
        while (!validPrice) {
            System.out.print("Enter product price: ");
            try {
                price = scanner.nextInt();
                validPrice = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for price.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
        
        
        boolean c = ( category.toLowerCase().equals("tablet") || category.toLowerCase().equals("smartphone") ||category.toLowerCase().equals("smartwatch") );
        boolean q = stock  > 0 ;
        boolean p = price > 0 ;
        
        // Create a new Product object and add it to inventory
        if (c & p & q) {
        Product product = new Product( id,name,category , stock, price , supplier );
        inventoryManager.addProduct(product);
       
   
        }else {
        	System.err.println("invalid informations"); 
        }
        
    }

    private static void updateProduct() {
       	 System.out.println("\n========================================================");
    	 System.out.println("\n=================== Update Product =====================");
    	 System.out.println("\n========================================================");
    	// Get product id
         int id = 0;
         boolean validId = false;
         while (!validId) {
             System.out.print("Enter product Id: ");
             try {
                 id = scanner.nextInt();
                 validId = true;
             } catch (InputMismatchException e) {
                 System.out.println("Invalid input. Please enter a valid integer for id.");
                 scanner.nextLine(); // Clear the input buffer
             }
         }
         scanner.nextLine();
         System.out.print("Enter product name: \n");
         String name = scanner.nextLine();
         System.out.print("Enter product category : (Tablet/Smartphone/SmartWatch) \n");
         String category = scanner.nextLine();
         int stock = 0;
         int price = 0;
         boolean validStock = false;
         boolean validPrice = false;

         // Get product quantity
         while (!validStock) {
             System.out.print("Enter product quantity: ");
             try {
                 stock = scanner.nextInt();
                 validStock = true;
             } catch (InputMismatchException e) {
                 System.out.println("Invalid input. Please enter a valid integer for quantity.");
                 scanner.nextLine(); // Clear the input buffer
             }
         }

         // Get product price
         while (!validPrice) {
             System.out.print("Enter product price: ");
             try {
                 price = scanner.nextInt();
                 validPrice = true;
             } catch (InputMismatchException e) {
                 System.out.println("Invalid input. Please enter a valid integer for price.");
                 scanner.nextLine(); // Clear the input buffer
             }
         }
         scanner.nextLine();
         System.out.print("Enter product supplier: \n");
         String supplier = scanner.nextLine();
         
      
         // Create a new Product object and add it to inventory
         boolean c = ( category.toLowerCase().equals("tablet") || category.toLowerCase().equals("smartphone") ||category.toLowerCase().equals("smartwatch") );
         boolean q = stock  > 0 ;
         boolean p = price > 0 ;
         if (c & p & q) {
         Product product = new Product( id , name , category , stock, price , supplier);
         inventoryManager.updateProduct(product);


         }else {
         	System.err.println("invalid informations"); 
         }
    }

    private static void removeProduct() {
    	 System.out.println("\n========================================================");
    	 System.out.println("\n================== Remove Product ======================");
    	 System.out.println("\n========================================================");
         System.out.print("Enter product ID: ");
         int id = scanner.nextInt();
         inventoryManager.removeProduct(id);
    }

    private static void recordSalesTransaction() {
    	System.out.println("\n========================================================");
    	System.out.println("\n========== Record Sales or Purchase Transaction ========");
    	System.out.println("\n========================================================");
    	// Get product id
        int productId = 0;
        boolean validId = false;
        while (!validId) {
            System.out.print("Enter product Id: ");
            try {
                productId = scanner.nextInt();
                validId = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for id.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
        scanner.nextLine(); // Consume newline

        Product product = inventoryManager.getProductById(productId);
        if (product != null) {
        	int quantity = 0;
            boolean validStock = false;

            // Get product quantity
            while (!validStock) {
                System.out.print("Enter quantity: ");
                try {
                    quantity = scanner.nextInt();
                    validStock = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for quantity.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }
            scanner.nextLine(); // Consume newline
            System.out.print("Enter transaction date (DD-MM-YYYY): ");
            String date = scanner.nextLine();
            System.out.print("Enter transaction type (sale/purchase): ");
            String transactionType = scanner.nextLine();
            
			// Record the sales transaction using SalesReportGenerator
            SalesTransaction salesTransaction = new SalesTransaction(null, 0, null, null);
            salesTransaction.addTransaction(product,date, quantity, transactionType);
            boolean ok = salesTransaction.isrecorded();
            if(ok) {System.out.println("Transaction recorded succefully ");}
            
        } else {
            System.out.println("Product with ID " + productId + " not found.");
            boolean ctrl = true ;
            String r="" ;
            while(ctrl) {
            System.out.println("°°You want to add a new product ? (y/n)");
            r = scanner.nextLine();
            ctrl = !(r.toLowerCase().equals("y")||r.toLowerCase().equals("n"));
            }
            if(r.toLowerCase().equals("y")) {
            System.out.print("\nEnter product name: \n");
            String name = scanner.nextLine();
            System.out.print("Enter product category : (Tablet/Smartphone/SmartWatch) \n");
            String category = scanner.nextLine();
            int stock = 0;
            int price = 0;
            boolean validStock = false;
            boolean validPrice = false;

            // Get product quantity
            while (!validStock) {
                System.out.print("Enter product quantity: ");
                try {
                    stock = scanner.nextInt();
                    validStock = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for quantity.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }

            // Get product price
            while (!validPrice) {
                System.out.print("Enter product price: ");
                try {
                    price = scanner.nextInt();
                    validPrice = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for price.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }
            scanner.nextLine();
            System.out.print("Enter product supplier: \n");
            String supplier = scanner.nextLine();
            System.out.print("Enter transaction date (DD-MM-YYYY): ");
            String date = scanner.nextLine();
            Product newproduct = new Product( productId,name,category , stock, price , supplier );
            inventoryManager.addProduct(newproduct);
            SalesTransaction salesTransaction = new SalesTransaction(null, 0, null, null);
            salesTransaction.addTransaction(newproduct,date, stock, "purchase");
            
        
        }} }
    private static void salesReport() {
    	System.out.println("\n========================================================");
    	System.out.println("\n==================== Sales Report ======================");
    	System.out.println("\n========================================================");
    	
    	SalesReportGenerator statisticsGenerator = new SalesReportGenerator();

        // Calculate revenue
        int revenue = statisticsGenerator.calculateRevenue();
        System.out.println("Revenue: "+revenue+" DT");

        // Calculate the most popular products
        String popularProducts = statisticsGenerator.getMostSoldProduct();
        System.out.println("Most popular products: " + popularProducts);

        // Calculate the most sold category (example)
        String mostSoldCategory = statisticsGenerator.getMostSoldCategory();
        System.out.println("Most sold category: " + mostSoldCategory);

        // Calculate the most profitable supplier (example)
        String mostProfitableSupplier = statisticsGenerator.getMostProfitableSupplier();
        System.out.println("Most profitable supplier: " + mostProfitableSupplier);
     // Calculate average sales
        double averageSales = statisticsGenerator.calculateAverageSales();
        System.out.printf("Average Sales: %.3f DT",averageSales);
        System.out.println();
        // Identify peak sales periods
        List<LocalDate> peakSalesPeriods = statisticsGenerator.identifyPeakSalesPeriods();
        System.out.println("Peak Sales Periods: " + peakSalesPeriods);
        SalesReportGenerator salesReportGenerator = new SalesReportGenerator();
		salesReportGenerator.generateSalesReportToExcel();
        System.out.println("\n========================================================");
        System.out.println("\n Excel Report Created Successfully in Your Directory ^_^  ");
        System.out.println("\n========================================================");

    }
}

