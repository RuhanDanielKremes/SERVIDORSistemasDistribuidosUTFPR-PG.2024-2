package org.example.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.example.ServerController;

import java.sql.Statement;

public class DbController {

    public static LogController logController = new LogController();
        
    private static Properties loadProps() throws IOException {
        try {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Read database props", true);
            InputStream input = ServerController.class.getClassLoader().getResourceAsStream("props.properties");
            if (input == null) {
                logController.writeSimpleLog("SYSTEM:conectarBanco", "File not found", true);
                return null;
            }
            Properties props = new Properties();
            props.load(input);
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Database props read sucessfull", true);
            return props;
        } catch (FileNotFoundException fnfe) {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "File not found " + fnfe.getMessage(), true);
            return null;
        } catch (Exception e) {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Error reading file " + e.getMessage(), true);
            return null;
        }
    }
    
    public static Connection conectDb() throws IOException {
        try {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Configurating Connection", true);
            Properties props = loadProps();
            if (props == null) {
                return null;
            }
            String URL = props.getProperty("URL");
            String USUARIO = props.getProperty("USER");
            String SENHA = props.getProperty("PASSWORD");
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Try connection with db", true);
            DriverManager.getConnection(URL, USUARIO, SENHA);
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Connection with db sucessfull", true);
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Error connecting with db " + e.getMessage(), true);
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage() + "\nPesquise por mais informações no log.");
            return null;
        } catch (Exception e) {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Error connecting with db " + e.getMessage(), true);
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage() + "\nPesquise por mais informações no log.");
            return null;
        }
    }

    public static void closeConnection(Connection conn) throws IOException {
        try {
            conn.close();
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Connection closed", true);
        } catch (SQLException e) {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Error closing connection" + e.getMessage(), true);
            System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage() + "\nPesquise por mais informações no log.");
        } catch (Exception e) {
            logController.writeSimpleLog("SYSTEM:conectarBanco", "Error closing connection" + e.getMessage(), true);
            System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage() + "\nPesquise por mais informações no log.");
        }
    }

    public static ResultSet executeQuery(Connection conn, String query) throws IOException {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logController.writeSimpleLog("SYSTEM: QueryExecution", "Query executed ", true);
            return rs;
        } catch (SQLException e) {
            logController.writeSimpleLog("SYSTEM: QueryExecution", "Error executing query " + e.getMessage(), true);
            System.out.println("Erro ao executar a query: " + e.getMessage() + "\nPesquise por mais informações no log.");
            return null;
        } catch (Exception e) {
            logController.writeSimpleLog("SYSTEM: QueryExecution", "Error executing query " + e.getMessage(), true);
            System.out.println("Erro ao executar a query: " + e.getMessage() + "\nPesquise por mais informações no log.");
            return null;
        }
    }

    public static boolean executeStatment(Connection conn, String query, List<?> dataList) throws IOException{
        boolean result = false;
        try {
            logController.writeSimpleLog("SYSTEM: StatmentExecution", "Creating Steatment ", true);
            PreparedStatement pstmt = conn.prepareStatement(query);
            for(int i = 0; i < dataList.size(); i++){
                switch (dataList.get(i).getClass().getSimpleName()) {
                    case "Integer":
                        pstmt.setInt(i+1, Integer.parseInt(dataList.get(i).toString()));
                        break;
                    case "String":
                        pstmt.setString(i+1, dataList.get(i).toString());
                        break;
                    default:
                        logController.writeSimpleLog("SYSTEM: StatmentExecution", "Error creating statment, type not found", true);
                        System.out.println("Erro ao criar o statment, tipo não encontrado");
                        pstmt.close();
                        return false;
                }
            }
            logController.writeSimpleLog("SYSTEM: StatmentExecution", "Statment created ", true);
            logController.writeSimpleLog("SYSTEM: StatmentExecution", "Executing Statment ", true);
            // JUST FOR DEBUG: DO NOT UNCOMMENT
            System.out.println(pstmt);
            try {
                pstmt.execute();
                logController.writeSimpleLog("SYSTEM: StatmentExecution", "Sucess! Statment executed ", true);
                result = true;
            } catch (SQLException e) {
                logController.writeSimpleLog("SYSTEM: StatmentExecution", "Error executing statment " + e.getMessage(), true);
                System.out.println("Erro ao executar o statment: " + e.getMessage() + "\nPesquise por mais informações no log.");
            } catch (Exception e) {
                logController.writeSimpleLog("SYSTEM: StatmentExecution", "Error executing statment " + e.getMessage(), true);
                System.out.println("Erro ao executar o statment: " + e.getMessage() + "\nPesquise por mais informações no log.");
            } finally {
                pstmt.close();
            }
        } catch (Exception e) {
            logController.writeSimpleLog("SYSTEM: StatmentExecution", "Error creating statment " + e.getMessage(), true);
            System.out.println("Erro ao criar o statment: " + e.getMessage() + "\nPesquise por mais informações no log.");
        }
        return result;
    }
}
