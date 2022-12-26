package com.example.supplychain;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;

public class Signup {

    private static byte[] getSHA(String input) {
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public static String getEncryptedPassword(String password) {
        String encrypedPassword = "";
        try{
            BigInteger number = new BigInteger(getSHA(password));
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while(hexString.length()<32) {
                hexString.insert(0,'0');
            }
            return hexString.toString();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        return "no";
    }

    public static boolean customerSignup(String email, String password,String first_name , String last_name, String address, String mobile) {
        if(email.equals("") || password.equals("") || first_name.equals("")|| address.equals("") || mobile.equals("")) {
            return false;
        }

        String query = "INSERT INTO customer (email,password,first_name,last_name,address,mobile) VALUES ('" + email + "' ,'" + password + "' ,'" + first_name + "' , '" + last_name + "' ,'" + address + "' ,'" + mobile + "')";

        try{
            DatabaseConnection dbCon = new DatabaseConnection();
            int i = dbCon.executeUpdateQuery(query);
            if(i>0){
                return true;
            }
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return false;
    }
}
