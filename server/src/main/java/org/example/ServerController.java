package org.example;

import java.net.*;

import org.example.controller.LogController;
import org.example.controller.OperacaoController;
import org.example.model.Json;
import org.example.model.JsonReturn;

import com.google.gson.Gson;

import java.io.*; 

public class ServerController { 
    LogController logController = new LogController();
    protected static boolean serverContinue = true;
    protected Socket clientSocket;
    public static int port = 22222;
    

    public static void main(String[] args) throws IOException { 
        try (FileWriter writer = new FileWriter("log.txt")) {
            writer.write("");
        }  

        ServerSocket serverSocket = null; 

        try { 
             serverSocket = new ServerSocket(port); 
             System.out.println ("Connection Socket Created");
             try { 
                  while (serverContinue)
                     {
                      serverSocket.setSoTimeout(10000);
                      System.out.println ("Waiting for Connection");
                      try {
                           new ServerController (serverSocket.accept()); 
                          }
                      catch (SocketTimeoutException ste)
                          {
                           System.out.println ("Timeout Occurred");
                          }
                     }
                 } 
             catch (IOException e) 
                 { 
                  System.err.println("Accept failed."); 
                  System.exit(1); 
                 } 
            } 
        catch (IOException e) 
            { 
             System.err.println("Could not listen on port: " + port + e.getMessage()); 
             System.exit(1); 
            } 
        finally
            {
             try {
                  System.out.println ("Closing Server Connection Socket");
                  serverSocket.close(); 
                 }
             catch (IOException e)
                 { 
                  System.err.println("Could not close port: " + port); 
                  System.exit(1); 
                 } 
            }
       }
    
     private ServerController (Socket clientSoc){
        clientSocket = clientSoc;
        new Thread(this::run).start();
    }
    
    public void run(){
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            String jsonInput = reader.readLine();
        try {
            logController.writeSimpleLog("SYSTEM: Server", "New Communication Thread Started", true);
            System.out.println ("New Communication Thread Started");
            JsonReturn jsonReturn = new JsonReturn();
            logController.writeSimpleLog("SYSTEM: READ JSON", "Getting file, if Json proced or if on the protocol, else Abort", true);
            Gson gson = new Gson();
            try {
                logController.writeLogJson("SYSTEAM: READ JSON", "Input equal to a .json file, content underneath", gson.fromJson(jsonInput, Json.class).toString());
                logController.writeSimpleLog("SYSTEM: READ JSON", "Passing the json to a class", true);
                Json json = gson.fromJson(jsonInput, Json.class);
                System.out.println("input: " + json.toString());
                logController.writeSimpleLog("SYSTEM: READ JSON", "Sucessfull! Json passed to a class", true);
                OperacaoController operacaoController = new OperacaoController();
                Json json1 = new Json();
                json1.setOperacao(json.getOperacao());
                json1.setRa(json.getRa());
                json1.setSenha(json.getSenha());
                json1.setNome(json.getNome());
                jsonReturn = operacaoController.findOperation(json1);
                System.out.println(jsonReturn.toString());
                String jsonString = gson.toJson(jsonReturn);
                try (FileWriter writer2 = new FileWriter("json.json", false)) {
                    writer2.write(jsonString);
                } catch (Exception e) {
                    System.out.println("Error! Json do not pass to a class" + e.getMessage());
                }
                writer.println(jsonString);
            } catch (Exception e) {
                logController.writeSimpleLog("SYSTEM: READ JSON", "Error! Json do not pass to a class" + e.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation("Json");
                jsonReturn.setMessage("Não foi possível ler o json recebido");
                System.out.println("Error! Json do not pass to a class" + e.getMessage());
                try (FileWriter writer2 = new FileWriter("json.json", false)) {
                    writer2.write(jsonReturn.toString());
                } catch (Exception e2) {
                    System.out.println("Error! Json do not pass to a class" + e2.getMessage());
                }
                String jsonString = gson.toJson(jsonReturn);
                writer.println(jsonString);
            }
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }
            try { 

                String inputLine; 
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                while ((inputLine = in.readLine()) != null) { 
                    System.out.println ("Server: " + inputLine); 
                    switch (inputLine) {
                        case "?":
                            inputLine = new String ("\"Bye.\" ends Client, " + "\"End Server.\" ends Server");
                            break;
                        case "Bye.":
                            break;
                        case "End Server.":
                            serverContinue = false;
                            break;
                        default:
                            break;
                    }
                    if (inputLine.equals("?")) 
                        inputLine = new String ("\"Bye.\" ends Client, " + "\"End Server.\" ends Server");
                    out.println(inputLine);
                    
                    if (inputLine.equals("Bye.")) 
                        break; 
                    if (inputLine.equals("End Server.")) 
                        serverContinue = false; 
                }     
                out.close(); 
                in.close(); 
                clientSocket.close(); 
            }catch (IOException e){ 
                System.err.println("Problem with Communication Server");
                System.exit(1); 
            } 
        } catch (IOException e) {
            System.err.println("Problem with log");
            System.exit(1);
        }
    }
}