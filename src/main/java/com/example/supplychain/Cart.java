package com.example.supplychain;

public class Cart {
    public static boolean cartProduct(String customer, Product product) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = String.format("INSERT INTO cart (customer_id, product_id, name, price) VALUES ((SELECT customer_id from customer where first_name = '%s'),%s, (SELECT name from product where product_id = %s), (SELECT price from product where product_id = %s))", customer, product.getId(),product.getId(),product.getId());
        int rowCount = 0;
        try{
            rowCount = databaseConnection.executeUpdateQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rowCount != 0;
    }
}
