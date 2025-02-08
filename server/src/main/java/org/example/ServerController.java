package org.example;

import java.net.*;

import org.example.controller.LogController;
import org.example.controller.OperacaoController;
import org.example.model.Json;
import org.example.model.JsonDeserializerCustom;
import org.example.model.JsonReturn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
                System.out.println ("Waiting for Connection");
                while (serverContinue)
                     {
                      serverSocket.setSoTimeout(10000);
                    //   System.out.println ("Waiting for Connection");
                      try {
                           new ServerController (serverSocket.accept()); 
                          }
                      catch (SocketTimeoutException ste)
                          {
                        //    System.out.println ("Timeout Occurred");
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
        try {
            new Thread(this::run).start();
        } catch (Exception e) {
            System.err.println("Problem with Communication Server" + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    public void run(){
        try{
            while (true) {
                BufferedReader reader = null;
                PrintWriter writer = null;
                PushbackInputStream in = new PushbackInputStream(clientSocket.getInputStream());
                try {
                    if (in.available() > 0) {
                        int data = in.read();
                        if (data == -1) {
                            clientSocket.close();
                        } else {
                            in.unread(data);           
                            reader = new BufferedReader(new InputStreamReader(in));
                            writer = new PrintWriter(clientSocket.getOutputStream(), true);
                            String jsonInput;
                            JsonReturn jsonReturn = new JsonReturn();
                            try {
                                if ((jsonInput = reader.readLine()) != null) {
                                    logController.writeSimpleLog("SYSTEM: Server", "New Communication Thread Started", true);
                                    System.out.println ("New Communication Thread Started");
                                    System.out.println("input: " + jsonInput);
                                    logController.writeSimpleLog("SYSTEM: READ JSON", "Getting file, if Json proced or if on the protocol, else Abort", true);
                                    Gson gson = new Gson();
                                    try {
                                        logController.writeLogJson("SYSTEAM: READ JSON", "Input equal to a .json file, content underneath", gson.fromJson(jsonInput, Json.class).toString());
                                        logController.writeSimpleLog("SYSTEM: READ JSON", "Passing the json to a class", true);
                                        Gson gsonbuilder  = new GsonBuilder().registerTypeAdapter(Json.class, new JsonDeserializerCustom()).create();
                                        Json<?> json = gsonbuilder.fromJson(jsonInput, Json.class);
                                        // System.out.println("input: " + json.toString());
                                        logController.writeSimpleLog("SYSTEM: READ JSON", "Sucessfull! Json passed to a class", true);
                                        OperacaoController operacaoController = new OperacaoController();
                                        jsonReturn = operacaoController.findOperation(json);
                                        String jsonString = gson.toJson(jsonReturn);
                                        System.out.println("output:" + jsonString);
                                        writer.println(jsonString);
                                    } catch (Exception e) {
                                        logController.writeSimpleLog("SYSTEM: READ JSON", "Error! Json do not pass to a class" + e.getMessage(), true);
                                        jsonReturn.setStatus(401);
                                        jsonReturn.setOperation("Json");
                                        jsonReturn.setMessage("Nao foi possivel ler o json recebido");
                                        System.out.println("Error! Json do not pass to a class" + e.getMessage());
                                        String jsonString = gson.toJson(jsonReturn);
                                        writer.println(jsonString);
                                    }
                                }else {
                                    logController.writeSimpleLog("SYSTEM: READ JSON", "Error! Json do not pass to a class", true);
                                    jsonReturn.setStatus(401);
                                    jsonReturn.setOperation("Json");
                                    jsonReturn.setMessage("Nao foi possivel ler o json recebido");
                                }
                            } catch (NullPointerException npe) {
                                logController.writeSimpleLog("SYSTEM: READ JSON", "Error! Json do not pass to a class" + npe.getMessage(), true);
                                System.err.println("Problem with Communication Server");
                                clientSocket.close();
                            } catch (Exception e) {
                                logController.writeSimpleLog("SYSTEM: READ JSON", "Error! Json do not pass to a class" + e.getMessage(), true);
                                System.err.println("Problem with Communication Server");
                                clientSocket.close();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Conexão fechada pelo cliente:" + e.getMessage());
                    clientSocket.close();
                }     
            }
        }catch (NullPointerException npe) {
            try {
                logController.writeSimpleLog("SYSTEM: READ JSON", "Error! Json do not pass to a class" + npe.getMessage(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println("Problem with Communication Server" + npe.getMessage());
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            try {
                logController.writeSimpleLog("SYSTEM: READ JSON", "Error! Json do not pass to a class" + e.getMessage(), true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.err.println("Problem with Communication Server" + e.getMessage());
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                clientSocket.close();
            } catch (IOException ioe) {
                System.err.println("Problem with Communication Server" + ioe.getMessage());
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}