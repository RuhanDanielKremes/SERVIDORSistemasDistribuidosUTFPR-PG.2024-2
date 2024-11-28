package org.example.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

public class LogController {
    
    private Gson gson = new Gson();
    
    public void writeSimpleLog(String operation, String msg) throws IOException{
        String returnMsg = msg;
        try {
            FileWriter writer = new FileWriter("log.txt", true);
            returnMsg = "Operation: " + operation + "\n\t" + msg + "\n"; 
            writer.write(returnMsg);  
            writer.close();
        } catch (Exception e) {
            FileWriter writer = new FileWriter("log.txt");
            writer.write("LOG ERROR: " + e.getMessage());
            writer.close();
        }finally{
            System.out.println(msg);
        }
    }	

    public boolean writeSimpleLog(String operation, String msg, boolean timeDate) throws IOException{
        try {
            FileWriter writer = new FileWriter("log.txt", true);
            LocalDateTime now = LocalDateTime.now();  
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
            String formattedNow = now.format(formatter);  

            writer.write("[" + formattedNow + "] Operation: " + operation + "\n\t" + msg + "\n");
            writer.close();
            return true;
        } catch (Exception e) {
            String eMsg = "writeSimpleLog ERROR: " + e.getMessage();
            writeSimpleLog(eMsg, operation);
            return false;
        }
    }

    public boolean writeLogJson(String operation, String msg, String json) throws IOException{
        try {
            FileWriter writer = new FileWriter("log.txt", true);
            LocalDateTime now = LocalDateTime.now();  
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
            String formattedNow = now.format(formatter);  

            writer.write("[" + formattedNow + "] Operation: " + operation + "\n\tJSON - " + gson.fromJson(json, String.class) + "\n" + msg.toUpperCase() + "\n");  
            
            writer.close();

            return true;
        } catch (Exception e) {
            if (writeSimpleLog(operation, msg, true) ) {
                return true;
            }
            return false;
        }
    }

}