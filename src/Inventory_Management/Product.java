package Inventory_Management;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Product {
    private int id;
    private String name;
    private String category;
    private int stock;
    private int price;
    private String supplier ; 
    
  //read files
    public List<Product> readCsvFile() {
        List<Product> products = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("Java_Project_data.csv"))) {
            // Skip the header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String category = parts[2].trim();
                int stock = Integer.parseInt(parts[3].trim());
                int price = Integer.parseInt(parts[4].trim());
                String supplier = parts[5].trim();

                Product product = new Product(id, name, category, stock, price , supplier);
                products.add(product);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Product(int id, String name, String category, int stock, int price , String supplier) {
    	this.id = id;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.supplier = supplier ;
    }
    public Product(int id, String name, String category, int stock, int price) {
    	this.id = id;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
       
    }
    
    public Product(int id , String name , int stock) {
    	this.id = id ; 
    	this.name = name ; 
        this.stock = stock;
    }
    
    

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return  " " + name + "\n price : " + price + "DT" + "\n Qty :" + stock ;
    }

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
    
}
