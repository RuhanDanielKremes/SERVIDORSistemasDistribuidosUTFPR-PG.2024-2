package org.example.model;

import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.example.controller.LogController;

public class JsonDeserializerCustom implements JsonDeserializer<Json<?>> {
 
    @Override
    public Json<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LogController logController = new LogController();
        Json<?> jsonDesserialized = new Json<>();
        try {
            logController.writeSimpleLog("System; Desserializing Json", "Start Procedure", true);
            JsonObject jsonObject = json.getAsJsonObject();
            String operacao = jsonObject.has("operacao") && !jsonObject.get("operacao").getAsString().isEmpty() ? jsonObject.get("operacao").getAsString() : "";
            String token = jsonObject.has("token") && !jsonObject.get("token").getAsString().isEmpty() ? jsonObject.get("token").getAsString() : "";
            String mensagem = jsonObject.has("mensagem") && !jsonObject.get("mensagem").getAsString().isEmpty() ? jsonObject.get("mensagem").getAsString() : "";
            int id = jsonObject.has("id") ? jsonObject.get("id").getAsInt() : 0;
            String nome = jsonObject.has("nome") && !jsonObject.get("nome").getAsString().isEmpty() ? jsonObject.get("nome").getAsString() : "";
            String ra = jsonObject.has("ra") && !jsonObject.get("ra").getAsString().isEmpty() ? jsonObject.get("ra").getAsString() : "";
            String senha = jsonObject.has("senha") && !jsonObject.get("senha").getAsString().isEmpty() ? jsonObject.get("senha").getAsString() : "";
            User usuario = jsonObject.has("usuario") ? context.deserialize(jsonObject.get("usuario"), User.class) : null;
            Category categorias = jsonObject.has("categorias") ? context.deserialize(jsonObject.get("categorias"), Category.class) : null;
            Warnings aviso = jsonObject.has("aviso") ? context.deserialize(jsonObject.get("aviso"), Warnings.class) : null;
            
            if (!operacao.isEmpty()) {
                jsonDesserialized.setOperacao(operacao);
            }
            if (!token.isEmpty()) {
                jsonDesserialized.setToken(token);
            }
            if (!mensagem.isEmpty()) {
                jsonDesserialized.setMensagem(mensagem);
            }
            if (id != 0) {
                jsonDesserialized.setId(id);
            }
            if (!nome.isEmpty()) {
                jsonDesserialized.setNome(nome);
            }
            if (!ra.isEmpty()) {
                jsonDesserialized.setRa(ra);
            }
            if (!senha.isEmpty()) {
                jsonDesserialized.setSenha(senha);
            }
            if (usuario != null) {
                jsonDesserialized.setUsuario(usuario);
            }
            if (categorias != null) {
                jsonDesserialized.setCategorias(categorias);
            }
            if (aviso != null) {
                jsonDesserialized.setAvisos(aviso);
            }
            logController.writeSimpleLog("System: Desserializing Json", "all essential data recivied", true);
            
            if (jsonObject.has("categoria")) {
                JsonElement jsonElement = jsonObject.get("categoria");
                logController.writeSimpleLog("System: Desserializing Json", "trying to parse Categoria G Type Class", true);
                try {               
                    if (jsonElement.isJsonObject()) {
                        Category categoria = context.deserialize(jsonElement, Category.class);
                        logController.writeSimpleLog("System: Desserializing Json", "Categoria G Type Class parsed", true);
                        return new Json<>(id, nome, ra, senha, operacao, token, mensagem, usuario, categorias, categoria, aviso);
                    } else {
                        int categoria = context.deserialize(jsonElement, Integer.class);
                        logController.writeSimpleLog("System: Desserializing Json", "Categoria G Type Class parsed", true);
                        return new Json<>(id, nome, ra, senha, operacao, token, mensagem, usuario, categorias, categoria, aviso);
                    }
                } catch (JsonSyntaxException jse) {
                    logController.writeSimpleLog("System: Desserializing Json", "JsonSyntaxException " +jse.getMessage() , true);
                    return jsonDesserialized;
                } catch (JsonParseException jpe) {
                    logController.writeSimpleLog("System: Desserializing Json", "JsonParseException " +jpe.getMessage() , true);
                    return jsonDesserialized;
                } catch (Exception e) {
                    logController.writeSimpleLog("System: Desserializing Json", "Exception " +e.getMessage() , true);
                    return jsonDesserialized;
                }
            }else {
                logController.writeSimpleLog("System: Desserializing Json", "all data recivied", true);
                return jsonDesserialized;
            }
        } catch (IOException e) {
            System.out.println("Failed to write log");
            throw new JsonParseException("Failed to write log", e);
        }

    }

}