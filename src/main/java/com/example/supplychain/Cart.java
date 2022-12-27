package com.example.supplychain;

import java.sql.ResultSet;

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
    public static void removeCart(int customer_id) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = String.format("delete from cart where customer_id = %s", customer_id);
        try{
            int rs = databaseConnection.executeUpdateQuery(query);
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
}
