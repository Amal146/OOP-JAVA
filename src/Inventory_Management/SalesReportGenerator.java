package Inventory_Management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import jxl.Cell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;



public class SalesReportGenerator {
    private List<SalesTransaction> salesTransactions;

    public SalesReportGenerator() {
        salesTransactions = new ArrayList<>();
    }

    public String getMostSoldProduct() {
        Map<Integer, Integer> productQuantities = getProductQuantities();

        int maxQuantity = 0;
        int mostSoldProductId = -1;
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            if (entry.getValue() > maxQuantity) {
                maxQuantity = entry.getValue();
                mostSoldProductId = entry.getKey();
            }
        }

        if (mostSoldProductId != -1) {
            Product mostSoldProduct = getProductById(mostSoldProductId);
            return mostSoldProduct != null ? mostSoldProduct.getName() : "";
        } else {
            return "";
        }
    }

    public String getMostSoldCategory() {
        Map<String, Integer> categoryQuantities = getCategoryQuantities();

        int maxQuantity = 0;
        String mostSoldCategory = "";
        for (Map.Entry<String, Integer> entry : categoryQuantities.entrySet()) {
            if (entry.getValue() > maxQuantity) {
                maxQuantity = entry.getValue();
                mostSoldCategory = entry.getKey();
            }
        }

        return mostSoldCategory;
    }

    public String getMostProfitableSupplier() {
        Map<String, Double> supplierProfits = getSupplierProfits();

        double maxProfit = 0;
        String mostProfitableSupplier = "";
        for (Map.Entry<String, Double> entry : supplierProfits.entrySet()) {
            if (entry.getValue() > maxProfit) {
                maxProfit = entry.getValue();
                mostProfitableSupplier = entry.getKey();
            }
        }

        return mostProfitableSupplier;
    }

    public int calculateRevenue() {
    	int expences = 0;
    	int sales = 0;
        int revenue = 0;
        SalesTransaction salesTransaction = new SalesTransaction(null, 0, null, null);
        salesTransactions = salesTransaction.readCsvFile();
        for (SalesTransaction transaction : salesTransactions) {
        	if(transaction.getTransactionType().toLowerCase().equals("purchase"))
            expences += transaction.getQuantity() * transaction.getProduct().getPrice();
        	else {
            sales += transaction.getQuantity() * transaction.getProduct().getPrice()*.2;
        	}
        }
        revenue = sales - expences ;
        return revenue;
    }

    public int calculateExpenses() {
        int expenses = 0;
        SalesTransaction salesTransaction = new SalesTransaction(null, 0, null, null);
        salesTransactions = salesTransaction.readCsvFile();
        for (SalesTransaction transaction : salesTransactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("purchase")) {
                expenses += transaction.getQuantity() * transaction.getProduct().getPrice();
            }
        }
        return expenses;
    }

    // Helper methods to calculate statistics

    private Map<Integer, Integer> getProductQuantities() {
        Map<Integer, Integer> productQuantities = new HashMap<>();
        for (SalesTransaction transaction : salesTransactions) {
            int productId = transaction.getProduct().getId();
            productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + transaction.getQuantity());
        }
        return productQuantities;
    }

    private Map<String, Integer> getCategoryQuantities() {
        Map<String, Integer> categoryQuantities = new HashMap<>();
        for (SalesTransaction transaction : salesTransactions) {
            String category = transaction.getProduct().getCategory();
            categoryQuantities.put(category, categoryQuantities.getOrDefault(category, 0) + transaction.getQuantity());
        }
        return categoryQuantities;
    }

    private Map<String, Double> getSupplierProfits() {
        Map<String, Double> supplierProfits = new HashMap<>();
        for (SalesTransaction transaction : salesTransactions) {
            String supplier = transaction.getProduct().getSupplier();
            double transactionAmount = transaction.getQuantity() * transaction.getProduct().getPrice();
            supplierProfits.put(supplier, supplierProfits.getOrDefault(supplier, 0.0) + transactionAmount);
        }
        return supplierProfits;
    }

    // Methods to interact with Products and Transactions

    private Product getProductById(int id) {
        SalesTransaction salesTransaction = new SalesTransaction(null, id, null, null);
        salesTransactions = salesTransaction.readCsvFile();
        for (SalesTransaction transaction : salesTransactions) {
            if (transaction.getProduct().getId() == id) {
                return transaction.getProduct();
            }
        }
        return null;
    }

    public double calculateAverageSales() {
        double totalSales = 0;
        int totalTransactions = 0;

        for (SalesTransaction transaction : salesTransactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("sale")) {
                totalSales += transaction.getQuantity() * transaction.getProduct().getPrice();
                totalTransactions++;
            }
        }

        return totalTransactions > 0 ? totalSales / totalTransactions : 0;
    }

    public List<LocalDate> identifyPeakSalesPeriods() {
        Map<LocalDate, Integer> salesByDate = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (SalesTransaction transaction : salesTransactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("sale")) {
            	String dateString = transaction.getDate(); // Replace this with the method that retrieves the date string
                LocalDate date = LocalDate.parse(dateString, formatter); // Adjust the parsing format
                salesByDate.put(date, salesByDate.getOrDefault(date, 0) + transaction.getQuantity());
            }
        }

        // Find dates with the maximum sales
        int maxSales = 0;
        for (int sales : salesByDate.values()) {
            if (sales > maxSales) {
                maxSales = sales;
            }
        }

        // Collect dates with maximum sales
        List<LocalDate> peakSalesDates = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : salesByDate.entrySet()) {
            if (entry.getValue() == maxSales) {
                peakSalesDates.add(entry.getKey());
            }
        }

        return peakSalesDates;
    }
    public void generateSalesReportToExcel() {
        try {
        	 WritableWorkbook workbook = Workbook.createWorkbook(new File("SalesReport.xls"));
        	 WritableSheet sheet = workbook.createSheet("SalesReport", 0);
        	 // Create a cell format with centered alignment
             WritableCellFormat cellFormat = new WritableCellFormat();
             cellFormat.setAlignment(Alignment.CENTRE);
           

            // Add Header
        	 sheet.mergeCells(0, 0, 9, 0); // Merge cells from (0, 0) to (4, 0)
             addCellH(sheet, 0, 0, "Sales Report", cellFormat);
             
             
          // Add Sales Report Data Section
             sheet.mergeCells(0, 1, 4, 1); 
        	 addCellH(sheet, 0, 1, "Statistics",cellFormat);
             generateSalesReportDataSection(sheet);

            // Add Inventory Section
             sheet.mergeCells(0, 8, 4, 8); 
        	 addCellH(sheet, 0, 8, "Inventory",cellFormat);
             addInventorySection(sheet);
            

            // Add Recent Transactions Section
             sheet.mergeCells(5, 1, 9, 1); 
        	 addCellH(sheet, 5, 1, "Transactions",cellFormat);
             addRecentTransactionsSection(sheet);

             // Add Sales Evolution Graph Placeholder
             sheet.mergeCells(10, 0, 16, 0); // Merge cells to hold the graph (adjust as needed)
             addCellH(sheet, 10, 0, "Sales Evolution", cellFormat);

             // Generate bar chart for sales evolution
             List<LocalDate> salesDates = getSalesDates(); // Replace this with your method to get sales dates
             BarChart(sheet, salesDates);
             
          // Add Pie Chart for sales of each product category
             addPieChart(sheet);

            
             autoResizeCells(sheet);

             workbook.write();
             workbook.close();           

        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
    }
    
    

    private List<LocalDate> getSalesDates() {
        SalesTransaction transactions = new SalesTransaction(product, 0, "", "");
        List<SalesTransaction> transaction = transactions.readCsvFile();
        List<LocalDate> salesDates = new ArrayList<>();
        for (SalesTransaction t : transaction) {
            if (t.getTransactionType().equalsIgnoreCase("sale")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate d = LocalDate.parse(t.getDate(), formatter);
                salesDates.add(d);
            }
        }
        return salesDates;
    }


	private void generateSalesReportDataSection(WritableSheet sheet) {
        try {
        	for(int i = 2 ; i <8 ; i++) {
        		sheet.mergeCells(0, i, 2, i); 
        		sheet.mergeCells(3, i, 4, i); 
        	}
        	
            addCell(sheet, 0, 2, "Revenue");
            addCell(sheet, 0, 3, "Most Popular Products");
            addCell(sheet, 0, 4, "Most Sold Category");
            addCell(sheet, 0, 5, "Most Profitable Supplier");
            addCell(sheet, 0, 6, "Average Sales");
            addCell(sheet, 0, 7, "Peak Sales Periods");

            addCell(sheet, 3, 2, String.valueOf(calculateRevenue()));
            addCell(sheet, 3, 3, getMostSoldProduct());
            addCell(sheet, 3, 4, getMostSoldCategory());
            addCell(sheet, 3, 5, getMostProfitableSupplier());
            double averageSales = calculateAverageSales();
            String formattedAverageSales = String.format("%.3f", averageSales) + "DT";
            addCell(sheet, 3, 6, formattedAverageSales);
            
            List<LocalDate> peakSalesPeriods = identifyPeakSalesPeriods();
            String peakSalesString = String.join(", ", peakSalesPeriods.toString());
            addCell(sheet, 3, 7, peakSalesString);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
    
    
    private static Product product = new Product(0, "", "", 0, 0 , "");

    private void addInventorySection(WritableSheet sheet) throws WriteException {
        // Simulated Inventory Data
    	List<Product> products = product.readCsvFile();
        List<List<String>> inventoryData = new ArrayList<>();

        // Add header row
        List<String> header = Arrays.asList("ID", "Product Name" ,"Category", "Supplier", "Stock");
        inventoryData.add(header);

        // Populate inventory data
        for (Product product : products) {
            List<String> productDetails = Arrays.asList(
                String.valueOf(product.getId()),
                product.getCategory(),
                product.getName(),
                product.getSupplier(),
                String.valueOf(product.getStock())
            );
            inventoryData.add(productDetails);
        }

        // Write inventory data to the Excel sheet
        int row = 9; 
        for (List<String> rowData : inventoryData) {
            int col = 0;
            for (String cellData : rowData) {
                addCell(sheet, col++, row, cellData);
            }
            row++;
        }
    }

    private void addRecentTransactionsSection(WritableSheet sheet) throws WriteException {
    	SalesTransaction transactions = new SalesTransaction(product,0,"","");
    	List<SalesTransaction> transaction = transactions.readCsvFile();
        List<List<String>> transactionData = new ArrayList<>();
        List<String> header = Arrays.asList("ID", "Product ID", "Quantity", "Date","Transaction Type");
        transactionData.add(header);
        
     // Populate transactions data
        int c = 1 ;
        for (SalesTransaction t : transaction) {
            List<String> transactiondetails = Arrays.asList(
            	String.valueOf(c),
                String.valueOf(t.getProduct().getId()),
                String.valueOf(t.getQuantity()),
                t.getDate(),
                t.getTransactionType()
            );
            transactionData.add(transactiondetails);
            c++;
        }

        int row = 2; 
        for (List<String> rowData : transactionData) {
            int col = 5;
            for (String cellData : rowData) {
                addCell(sheet, col++, row, cellData);
            }
            row++;
        }
    }

    private void addCell(WritableSheet sheet, int column, int row, String content) throws WriteException {
        Label label = new Label(column, row, content);
        sheet.addCell(label);
    }
    private static void addCellH(WritableSheet sheet, int column, int row, String content, WritableCellFormat format) throws WriteException {
        
        WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat boldCellFormat = new WritableCellFormat(boldFont);
        boldCellFormat.setAlignment(Alignment.CENTRE);

        Label label = new Label(column, row, content, boldCellFormat);
        sheet.addCell(label);
    }
    private void BarChart(WritableSheet sheet, List<LocalDate> salesDates) throws WriteException, IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Collect sales count for each month
       
        Map<LocalDate, Integer> salesByDay = new TreeMap<>();
        
        for (SalesTransaction transaction : salesTransactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("sale")) {
                LocalDate date = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                int quantity = transaction.getQuantity();
                salesByDay.put(date, salesByDay.getOrDefault(date, 0) + quantity);
            }
        }

        for (Map.Entry<LocalDate, Integer> entry : salesByDay.entrySet()) {
            dataset.addValue(entry.getValue(), "Sales", entry.getKey().toString());
        }
        
        
        
        
        // Create the bar chart using JFreeChart
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "Date",
                "Number of Sales",
                dataset,
                PlotOrientation.VERTICAL,
                false, false, false);
        
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        
        // Customize the appearance of the bars
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.PINK.darker()); // Set bar color to dark blue

        // Set the Y-axis tick unit to 1.0 for quantity sold
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setTickUnit(new NumberTickUnit(1.0));

        // Save the chart as an image
        File chartImage = new File("SalesEvolutionChart.png");
        ChartUtilities.saveChartAsPNG(chartImage, barChart, 800, 600);

        // Add the chart image to the Excel sheet
        WritableImage image = new WritableImage(10, 1, 7,14, chartImage);
        sheet.addImage(image);
        
        
    }
    private void autoResizeCells(WritableSheet sheet) {
        for (int col = 0; col < sheet.getColumns(); col++) {
            int maxContentWidth = 0;
            for (int row = 0; row < sheet.getRows(); row++) {
                Cell cell = sheet.getCell(col, row);
                String content = cell.getContents();
                int contentLength = content.length();
                if (contentLength > maxContentWidth) {
                    maxContentWidth = contentLength;
                }
            }
            sheet.setColumnView(col, maxContentWidth + 5); // Add padding for better visibility
        }
    }

    private void addPieChart(WritableSheet sheet) throws WriteException {
        // Create a dataset for the pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Collect sales count for each product category
        Map<String, Integer> salesByCategory = getCategorySales();

        // Add data to the dataset
        for (Map.Entry<String, Integer> entry : salesByCategory.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Create the pie chart using JFreeChart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Sales by Product Category", // Chart title
                dataset,
                true, // Include legend
                true,
                false);

        // Get the pie plot to customize appearance
        PiePlot plot = (PiePlot) pieChart.getPlot();
        
        HashMap<String, Integer> sectionMapping = new HashMap<>();
        sectionMapping.put("SmartWatch", 0);
        sectionMapping.put("SmartPhone", 1);
        sectionMapping.put("Tablet", 2);

        
        // Set custom colors for pie sections
        plot.setSectionPaint(0, Color.BLUE.brighter()); // Change color for SmartWatch category
        plot.setSectionPaint(1, Color.green.brighter()); // Change color for SmartPhone category
        plot.setSectionPaint(2, Color.pink.darker()); // Change color for Tablet category

        // Save the chart as an image
        File pieChartImage = new File("SalesByCategoryPieChart.png");
        try {
			ChartUtilities.saveChartAsPNG(pieChartImage, pieChart, 800, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Add the chart image to the Excel sheet
        WritableImage image = new WritableImage(10, 15, 7, 14, pieChartImage);
        sheet.addImage(image);
    }

    private Map<String, Integer> getCategorySales() {
        Map<String, Integer> categorySales = new HashMap<>();
        for (SalesTransaction transaction : salesTransactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("sale")) {
                String category = transaction.getProduct().getCategory();
                categorySales.put(category, categorySales.getOrDefault(category, 0) + transaction.getQuantity());
            }
        }
        return categorySales;
    }
    
}
