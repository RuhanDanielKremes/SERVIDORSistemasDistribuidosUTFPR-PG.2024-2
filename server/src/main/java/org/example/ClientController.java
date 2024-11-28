package org.example;

import java.net.*;

import org.example.model.Json;
import org.example.model.Json2;
import org.example.model.JsonReturn;

import com.google.gson.Gson;

import java.io.*; 

public class ClientController extends Thread{
        public static void main(String[] args) throws IOException {
    
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        
        try {
            socket = new Socket("localhost", 22222);  // Conectando ao servidor na porta 12345
            writer = new PrintWriter(socket.getOutputStream(), true);  // Fluxo de saída (envio de dados)
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Fluxo de entrada (leitura de resposta)

            // Criando o objeto Json
            Json2 json2 = new Json2();
            json2.setOperacao("cadastrarUsuario");
            json2.setRa("1234567");
            json2.setSenha("essaehasenha");
            json2.setNome("JHON");

            // Usando Gson para converter o objeto em JSON
            Gson gson = new Gson();
            String jsonString = gson.toJson(json2);

            // Enviando o JSON para o servidor
            writer.println(jsonString);
            System.out.println("JSON enviado: " + jsonString);

            // Recebendo resposta do servidor
            String serverResponse = reader.readLine();
            JsonReturn jsonReturn = gson.fromJson(serverResponse, JsonReturn.class);
            System.out.println("JSON recebido: " + jsonReturn.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
                if (reader != null) reader.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}