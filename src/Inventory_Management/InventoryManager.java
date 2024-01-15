package Inventory_Management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class InventoryManager {
	
	private static List<Product> productList;
	
	public InventoryManager() {
        InventoryManager.productList = new ArrayList<>(); // Initialize productList
    }
	
	 public static void displayAllProducts() {
		 if (productList.isEmpty()) {
	            System.out.println("No products available.");
	        } else {
	            System.out.println("================ Products in Inventory ============");
	            for (Product product : productList) {
	                System.out.println(product); 
	            }
	        }
	    }



    public boolean isProductExists(int productId , String productName) {
    	Product product1 = new Product(0, "", "", 0, 0);
    	List<Product> productList =  product1.readCsvFile() ;
        boolean ok = false ;
        for (Product p : productList) {
            if (p.getId() == productId || p.getName().toLowerCase().equals(productName.toLowerCase())) {
                ok = true; 
            }
        }
        return ok;
    }

    public void addProduct(Product product) {
        if (!isProductExists(product.getId() , product.getName())) {
        	Product product1 = new Product(0, "", "", 0, 0);
        	List<Product> productList =  product1.readCsvFile() ;
            productList.add(product);
            try (BufferedWriter pw = new BufferedWriter(new FileWriter("Java_Project_data.csv", true))) {
                // Check if name is not empty
                if (!product.getName().isEmpty()) {
                
                    pw.write(product.getId() + "," + product.getName() + "," + product.getCategory() + "," +
                            product.getStock() + "," + product.getPrice()  + "," + product.getSupplier()+"\n");
                } else {
                    System.err.println("Invalid product name");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = today.format(formatter);
            SalesTransaction salesTransaction = new SalesTransaction(null, 0, null, null);
            salesTransaction.addTransaction(product,formattedDate, product.getStock(), "purchase");
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Product with ID " + product.getId() + " or Product's Name " + product.getName() + " already exists.");
            return; 
        }
    }

    public void updateProduct(Product updatedProduct) {
        if (isProductExists(updatedProduct.getId(),updatedProduct.getName())) {
            String filePath = "Java_Project_data.csv";
            File file = new File(filePath);
            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            	String header = reader.readLine(); 
            	lines.add(header);           
            	String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    int id = Integer.parseInt(parts[0].trim());

                    // If the product ID matches, update the line with new product information
                    if (id == updatedProduct.getId()) {
                        String updatedLine = updatedProduct.getId() + "," + updatedProduct.getName() + "," +
                                updatedProduct.getCategory() + "," + updatedProduct.getStock() + "," +
                                updatedProduct.getPrice()+"," + updatedProduct.getSupplier();
                        lines.add(updatedLine);
                    } else {
                        lines.add(line); 
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Write the modified content back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
                System.out.println("Inventory updated successfully!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Product with ID " + updatedProduct.getId() + " does not exist.");
        }
    }

    public void removeProduct(int productId) {
        if (isProductExists(productId,"")) {
        	Product product1 = new Product(0, "", "", 0, 0);
        	List<Product> productList =  product1.readCsvFile() ;
        	
            productList.removeIf(product -> product.getId() == productId);
            
            String filePath = "Java_Project_data.csv";
            
            try {
            	
             File file = new File(filePath);
             
             BufferedReader reader = new BufferedReader(new FileReader(file));
             
             StringBuilder sb = new StringBuilder();
             
             String currentLine;
             String header = reader.readLine(); 
         	 sb.append(header).append(System.getProperty("line.separator")); 

            while ((currentLine = reader.readLine()) != null) {
            	
            	String[] parts = currentLine.split(",");
            	int id = Integer.parseInt(parts[0].trim());
                if (! (id == productId)) {
                    sb.append(currentLine).append(System.getProperty("line.separator"));
                }
            }
            reader.close();

            FileWriter fw = new FileWriter(file);
            fw.write(sb.toString());
            fw.close();
            System.out.println("Product removed successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            
        }} else {
            System.out.println("Product with ID " + productId + " don't exists.");
        }
    
        
    }
    
    public Product getProductById(int productId) {
    	Product product1 = new Product(0, "", "", 0, 0);
    	List<Product> productList =  product1.readCsvFile() ;
    	
        for (Product product : productList) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null; // Return null if product with given ID is not found
    }
}

