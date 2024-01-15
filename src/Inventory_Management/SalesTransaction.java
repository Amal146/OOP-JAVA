package Inventory_Management;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SalesTransaction {
	private Product product;
    private int quantity;
    private String date;
    private String transactionType;
    private boolean isrecorded;
    
    //read files
    public List<SalesTransaction> readCsvFile() {
        List<SalesTransaction> transactions = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("transactions.csv"))) {
            // Skip the header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0].trim());
                String date = parts[2].trim();
                String transactionType = parts[3].trim();
                int qty = Integer.parseInt(parts[1].trim());
                Product product = getProductById(id);

                SalesTransaction transaction = new SalesTransaction(product ,qty,date, transactionType);
                transactions.add(transaction);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public SalesTransaction(Product product, int quantity, String date, String transactionType) {
        this.setProduct(product);
        this.setQuantity(quantity);
        this.setDate(date);
        this.setTransactionType(transactionType);
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

    public Product getProductById(int id) {
    	Product product1 = new Product(0, "", 0);
		List<Product> products = product1.readCsvFile(); 
    	for(Product p : products) {
    		if(p.getId() == id )
    			product1 = p  ;
    		}
    	return product1;
    }
    
    public void addTransaction(Product product,String date, int quantity, String transactionType) {
        // Update product inventory
        if (transactionType.equalsIgnoreCase("sale")) {
            int updatedStock = product.getStock() - quantity;
            String dateFormatRegex = "\\d{2}-\\d{2}-\\d{4}";
            Pattern pattern = Pattern.compile(dateFormatRegex);
            Matcher matcher = pattern.matcher(date);
            boolean validdate = true;
            if (matcher.matches()) {
                String[] dateParts = date.split("-");
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[0]);
                boolean validMonth = month >= 1 && month <= 12;
                boolean validDay = day >= 1 && day <= 31;
                if (!(validMonth && validDay)) {
                    System.err.println("Invalid month or day! Month should be between 1-12"+month+" and day should be between 1-31."+day);
                    validdate = false; 
                    return;
                   }
                
            } else {
                System.err.println("Invalid date format! Please enter in DD-MM-YYYY format.");
                validdate = false;
                return;
                
            }
            if (updatedStock >= 0 && validdate == true) {
            	product.setStock(updatedStock);
                Product updatedProduct = new Product(product.getId(), product.getName(), product.getCategory(),
                                                     product.getStock(), product.getPrice(), product.getSupplier());
                InventoryManager inventoryManager = new InventoryManager();
				inventoryManager.updateProduct(updatedProduct);
				isrecorded = true ;
				// Add transaction information to transactions.csv file
		        String transactionData = product.getId() + "," + quantity + "," + date + "," + transactionType;

		        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
		            writer.write("\n" + transactionData);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
            } else {
                System.err.println("Insufficient stock available for the transaction.");
                return;
            }
        } else if (transactionType.equalsIgnoreCase("purchase")) {
        	String dateFormatRegex = "\\d{2}-\\d{2}-\\d{4}";
            Pattern pattern = Pattern.compile(dateFormatRegex);
            Matcher matcher = pattern.matcher(date);
            boolean validdate = true;
            if (matcher.matches()) {
                String[] dateParts = date.split("-");
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[0]);
                boolean validMonth = month >= 1 && month <= 12;
                boolean validDay = day >= 1 && day <= 31;
                if (!(validMonth && validDay)) {
                    System.err.println("Invalid month or day! Month should be between 1-12 and day should be between 1-31.");
                    validdate = false; 
                   }
                
            } else {
                System.err.println("Invalid date format! Please enter in DD-MM-YYYY format.");
                validdate = false;
                
            }
            if (validdate == true) {
            	int updatedStock = product.getStock() + quantity;
                product.setStock(updatedStock);
                Product updatedProduct = new Product(product.getId(), product.getName(), product.getCategory(),
                        product.getStock(), product.getPrice(), product.getSupplier());
                InventoryManager inventoryManager = new InventoryManager();
                inventoryManager.updateProduct(updatedProduct);
                isrecorded = true ;
             // Add transaction information to transactions.csv file
                String transactionData = product.getId() + "," + quantity + "," + date + "," + transactionType;

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
                    writer.write("\n" + transactionData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 
            }else {
                System.err.println("Invalid transaction type.");
                return;}
            

        
    }
    
    public boolean isrecorded() {
    	return isrecorded;
    }
    

  }
