package org.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.model.Json;
import org.example.model.JsonReturn;
import org.example.model.User;
import org.example.model.Warnings;
import org.example.model.Category;

public class OperacaoController{

    public LogController logController = new LogController();

    public JsonReturn findOperation(Json json) throws IOException{
        logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Shearching for a protocol Operation", true);
        switch (json.getOperacao()) {
            case "cadastrarUsuario":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (cadastro(json));
            case "login":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (login(json));
            case "logout":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (logout(json));
            case "listarCategorias":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (listCategory(json));
            case "localizarCategoria":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (findCategory(json));
            case "listarUsuarioCategorias":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (findUserSubscriptionsCategories(json));
            case "listarAvisos":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (listWarinigs(json));
            case "cadastrarUsuarioCategoria":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (subscribeUserCategory(json));
            case "descadastrarUsuarioCategoria":
                logController.writeSimpleLog("SYSTEM: Find protocol Opreation", "Protocol found!", true);
                return (unsubscribeUserCategory(json));
            default:
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol not found!", true);
                JsonReturn jsonReturn = new JsonReturn();
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Operação não encontrada");
                return jsonReturn;
        }        
    }

    public JsonReturn cadastro(Json json) throws IOException{
        logController.writeSimpleLog("cadastrarUsuario", "Inicializado procedimento de cadastro", false);
        Connection conn = null;
        User user = new User();
        user.setName(json.getNome());
        user.setRa(json.getRa());
        user.setPassword(json.getSenha());
        JsonReturn jsonReturn = new JsonReturn();

        if (user.getRa().length() != 7) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "RA invalido", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("RA invalido");
            return jsonReturn;
        }
        if (user.getPassword().length() < 8) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Senha muito Curta", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Senha muito curta");
            return jsonReturn;
        }
        if (user.getPassword().length() > 20) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Senha muito Longa", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Senha muito longa");
            return jsonReturn;
        }
        if (user.getName().length() > 50) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Nome muito Longo", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Nome muito longo");
            return jsonReturn;
        }
        if (!(user.getName().matches("[A-Z\\s]+"))) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Nome invalido: Uso de caracteres fora de padrão", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Nome invalido, use apenas letras maiusculas");
            return jsonReturn;
        }

        try{
            logController.writeSimpleLog("SYSTEM: DB Connetion", "Starting conection", true);
            conn = DbController.conectDb();
            UserController userController = new UserController();
            try{
                logController.writeSimpleLog("cadastrarUsuario", "shearching for ra on DB before create", true);
                ResultSet rs = DbController.executeQuery(conn, userController.getUser(user.getRa()));
                if (!rs.next()) {
                    logController.writeSimpleLog("SYSTEM: Data Validation", "Data not found, attempting to registrate new user", false);
                    try {
                        DbController.executeStatment(conn, userController.createUser(user), userController.createUserList(user));
                        logController.writeSimpleLog("cadastrarUsuario -> 200", "Usuario cadastrado com sucesso!", true);
                        jsonReturn.setStatus(201);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("Usuario cadastrado com sucesso!");
                        return jsonReturn;
                    } catch (Exception e) {
                        logController.writeSimpleLog("cadastrarUsuario -> 401", "Erro ao cadastrar usuario", true);
                        jsonReturn.setStatus(401);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                        return jsonReturn;
                    }
                }else{
                    logController.writeSimpleLog("cadastrarUsuario -> 401", "Usuario ja cadastrado", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Não foi cadastrar pois o usuario informado ja existe");
                    return jsonReturn;                    
                }
            }catch (Exception e){
                logController.writeSimpleLog("cadastrarUsuario -> 401", "Erro ao buscar usuario no banco de dados", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                return jsonReturn;
            }
        }catch (Exception e){
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Erro ao conectar com o banco de dados", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn login(Json json) throws IOException{
        logController.writeSimpleLog("login", "Inicializado procedimento de login", true);
        JsonReturn jsonReturn = new JsonReturn();
        Connection conn = null;
        User user = new User();
        user.setRa(json.getRa());
        user.setPassword(json.getSenha());
        jsonReturn.setOperation(json.getOperacao());

        if (user.getRa().length() != 7) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "RA invalido", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Credenciais incorretas.");
            return jsonReturn;
        }
        if (user.getPassword().length() < 8) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Senha muito Curta", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Credenciais incorretas.");
            return jsonReturn;
        }
        if (user.getPassword().length() > 20) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Senha muito Longa", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Credenciais incorretas.");
            return jsonReturn;
        }

        try {
            conn = DbController.conectDb();
            UserController userController = new UserController();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(user.getRa()));
            if (!rs.next()) {
                logController.writeSimpleLog("login -> 401", "Usuario ou senha incorretos", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Credenciais incorretas.");
                return jsonReturn;
            }else{
                if (rs.getString("password").equals(user.getPassword())) {
                    logController.writeSimpleLog("login -> 200", "Usuario logado com sucesso", true);
                    jsonReturn.setStatus(200);
                    jsonReturn.setToken(user.getRa());
                    user.setName(rs.getString("name"));
                    user.setRole(rs.getString("role"));
                    user.setId(rs.getInt("idUser"));
                    return jsonReturn;
                }else{
                logController.writeSimpleLog("login -> 401", "Usuario ou senha incorretos", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Credenciais incorretas.");
                return jsonReturn;
                }
            }   
        } catch (Exception e) {
            logController.writeSimpleLog("login -> 401", "Erro na conexão com o banco de dados" + e.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn logout(Json json) throws IOException{
        logController.writeSimpleLog("logout", "Inicializado procedimento de logout", true);
        JsonReturn jsonReturn = new JsonReturn();
        jsonReturn.setStatus(200);
        jsonReturn.setOperation(json.getOperacao());
        jsonReturn.setMessage("Logout realizado com sucesso.");
        return jsonReturn;
    }

    public JsonReturn validateToken(Json json) throws IOException{
        logController.writeSimpleLog("validateToken", "Inicializado procedimento de validação de token", true);
        Connection conn = null;
        JsonReturn jsonReturn = new JsonReturn();
        if (json.getToken().length() != 7) {
            logController.writeSimpleLog("validateToken -> 401", "Token invalido", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Token invalido");
            return jsonReturn;
        }
        try {
            conn = DbController.conectDb();
            UserController userController = new UserController();
            logController.writeSimpleLog("DBUG", userController.getUser(json.getToken()), true);
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
            if (rs.next()) {
                logController.writeSimpleLog("validateToken -> 200", "Token valido", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Token valido");
                return jsonReturn;
            }else{
                logController.writeSimpleLog("validateToken -> 401", "Token invalido", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Token invalido");
                return jsonReturn;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("validateToken -> 401", "Erro na conexão com o banco de dados", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    // public JsonReturn changePassword(Json json) throws IOException{
    //     logController.writeSimpleLog("changePassword", "Inicializado procedimento de mudança de senha", true);
    //     JsonReturn jsonReturn = new JsonReturn();
    //     Connection conn = null;
    //     User user = new User();
    //     user.setRa(json.getRa());
    //     user.setPassword(json.getSenha());
    //     if (user.getPassword().length() < 8) {
    //         logController.writeSimpleLog("changePassword -> 401", "Senha muito Curta", true);
    //         jsonReturn.setStatus(401);
    //         jsonReturn.setOperation(json.getOperacao());
    //         jsonReturn.setMessage("Senha muito curta");
    //         return jsonReturn;
    //     }
    //     if (user.getPassword().length() > 20) {
    //         logController.writeSimpleLog("changePassword -> 401", "Senha muito Longa", true);
    //         jsonReturn.setStatus(401);
    //         jsonReturn.setOperation(json.getOperacao());
    //         jsonReturn.setMessage("Senha muito longa");
    //         return jsonReturn;
    //     }
    //     try {
    //         conn = DbController.conectDb();
    //         UserController userController = new UserController();
    //         ResultSet rs = DbController.executeQuery(conn, userController.getUser(user.getRa()));
    //         if (rs == null) {
    //             logController.writeSimpleLog("changePassword -> 401", "Usuario nao encontrado", true);
    //             jsonReturn.setStatus(401);
    //             jsonReturn.setOperation(json.getOperacao());
    //             jsonReturn.setMessage("Usuario nao encontrado");
    //             return jsonReturn;
    //         }else{
    //             DbController.executeStatment(conn, userController.updateUser(user), userController.updateUserList(user));
    //             logController.writeSimpleLog("changePassword -> 200", "Senha alterada com sucesso", true);
    //             jsonReturn.setStatus(200);
    //             jsonReturn.setOperation(json.getOperacao());
    //             jsonReturn.setMessage("Senha alterada com sucesso");
    //             return jsonReturn;
    //         }
    //     } catch (Exception e) {
    //         logController.writeSimpleLog("changePassword -> 401", "Erro na conexão com o banco de dados", true);
    //         jsonReturn.setStatus(401);
    //         jsonReturn.setOperation(json.getOperacao());
    //         jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
    //         return jsonReturn;
    //     }
    // }

    // public JsonReturn Echo(Json json) throws IOException{
    //     logController.writeSimpleLog("Echo", "Inicializado procedimento de echo", true);
    //     JsonReturn jsonReturn = validateToken(json);
    //     if (jsonReturn.getStatus() == 401) {
    //         jsonReturn.setStatus(200);
    //         jsonReturn.setOperation(json.getOperacao());
    //         return jsonReturn;
    //     }
    //     jsonReturn.setMessage(json.getMensagem());
    //     return jsonReturn;
    // }

    public JsonReturn listCategory(Json json) throws IOException{
        logController.writeSimpleLog("listCategory", "Inicializado procedimento de listagem de categorias", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        jsonReturn.setMessage(null);
        List<Category> categoryList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbController.conectDb();
            CategoryController categoryController = new CategoryController();
            ResultSet rs = DbController.executeQuery(conn, categoryController.listCategory());
            try {
                logController.writeSimpleLog("listCategory", "Buscando categorias", true);
                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("idCategory"));
                    category.setName(rs.getString("name"));
                    categoryList.add(category);
                }
                if (categoryList.isEmpty()) {
                    logController.writeSimpleLog("listCategory -> 401", "Nenhuma categoria encontrada", true);
                }
                jsonReturn.setStatus(201);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setCategory(categoryList);
                return jsonReturn;
            }catch (Exception e){
                logController.writeSimpleLog("listCategory -> 401", "Erro ao buscar categorias:" + e, true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                return jsonReturn;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("listCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn findCategory(Json json) throws IOException{
        logController.writeSimpleLog("findCategory", "Inicializado procedimento de busca de categoria", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        jsonReturn.setMessage(null);
        Connection conn = null;
        Category category = new Category();
        System.out.println(json.getId());
        category.setId(json.getId());
        try {
            conn = DbController.conectDb();
            CategoryController categoryController = new CategoryController();
            ResultSet rs = DbController.executeQuery(conn, categoryController.findCategoryById(category.getId()));
            try {
                logController.writeSimpleLog("findCategory", "Buscando categoria", true);
                if (rs.next()) {
                    category.setName(rs.getString("name"));
                    jsonReturn.setStatus(201);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setCategory(Arrays.asList(category));
                    return jsonReturn;
                }else{
                    logController.writeSimpleLog("findCategory -> 401", "Categoria nao encontrada", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Categoria nao encontrada");
                    return jsonReturn;
                }
            }catch (Exception e){
                logController.writeSimpleLog("findCategory -> 401", "Erro ao buscar categoria:" + e, true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                return jsonReturn;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("findCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn findUserSubscriptionsCategories(Json json) throws IOException{
        logController.writeSimpleLog("findUserSubscriptionsCategory", "Inicializado procedimento de busca de categorias inscritas por usuario", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        jsonReturn.setMessage(null);
        Connection conn = null;
        User user = new User();
        user.setRa(json.getToken());
        try {
            conn = DbController.conectDb();
            CategoryController categoryController = new CategoryController();
            ResultSet rs = DbController.executeQuery(conn, categoryController.findUserSubscriptionsCategories(user.getRa()));
            try {
                logController.writeSimpleLog("findUserSubscriptionsCategory", "Buscando categorias", true);
                List<Category> categoryList = new ArrayList<>();
                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("category"));
                    categoryList.add(category);
                }
                if (categoryList.isEmpty()) {
                    logController.writeSimpleLog("findUserSubscriptionsCategory -> 401", "Nenhuma categoria encontrada", true);
                }
                jsonReturn.setStatus(201);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setCategory(categoryList);
                return jsonReturn;
            }catch (Exception e){
                logController.writeSimpleLog("findUserSubscriptionsCategory -> 401", "Erro ao buscar categorias:" + e, true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                return jsonReturn;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("findUserSubscriptionsCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn listWarinigs(Json json) throws IOException{
        logController.writeSimpleLog("listWarnings", "Inicializado procedimento de listagem de avisos", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        jsonReturn.setMessage(null);
        List<Warnings> warningsList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbController.conectDb();
            WarningController warningController = new WarningController();
            ResultSet rs = DbController.executeQuery(conn, warningController.listWarning());
            try {
                logController.writeSimpleLog("listWarnings", "Buscando avisos", true);
                while (rs.next()) {
                    Warnings warning = new Warnings();
                    warning.setId(rs.getInt("idwarning"));
                    warning.setTitle(rs.getString("title"));
                    warning.setDescription(rs.getString("description"));
                    warning.setCategory(rs.getInt("category"));
                    warningsList.add(warning);
                }
                if (warningsList.isEmpty()) {
                    logController.writeSimpleLog("listWarnings -> 401", "Nenhum aviso encontrado", true);
                }
                jsonReturn.setStatus(201);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setWarning(warningsList);
                return jsonReturn;
            }catch (Exception e){
                logController.writeSimpleLog("listWarnings -> 401", "Erro ao buscar avisos:" + e, true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                return jsonReturn;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("listWarnings -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn subscribeUserCategory(Json json) throws IOException{
        logController.writeSimpleLog("subscribeUserCategory", "Inicializado procedimento de inscrição de categoria", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        jsonReturn.setMessage(null);
        Connection conn = null;
        User user = new User();
        user.setRa(json.getToken());
        try {
            conn = DbController.conectDb();
            UserController userController = new UserController();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(user.getRa()));
            if (rs.next()) {
                user.setId(rs.getInt("idUser"));
            }else{
                logController.writeSimpleLog("subscribeUserCategory -> 401", "Usuario nao encontrado", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Usuario nao encontrado");
                return jsonReturn;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("subscribeUserCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
        try {
            logController.writeSimpleLog("subscribeUserCategory", "Conectando com o banco de dados", true);
            conn = DbController.conectDb();
            SubscriptionController subscriptionController = new SubscriptionController();
            boolean result = false;
            result = DbController.executeStatment(conn, subscriptionController.subscribe(), subscriptionController.subscribeList(user.getId(), json.getId()));
            if (result) {
                logController.writeSimpleLog("subscribeUserCategory -> 200", "Inscrição realizada com sucesso", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Inscrição realizada com sucesso");
                return jsonReturn;
            }
            logController.writeSimpleLog("subscribeUserCategory -> 401", "Erro ao realizar inscrição", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn; 
        } catch (Exception e) {
            logController.writeSimpleLog("subscribeUserCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn unsubscribeUserCategory(Json json) throws IOException{
        logController.writeSimpleLog("unsubscribeUserCategory", "Inicializado procedimento de desinscrição de categoria", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        jsonReturn.setMessage(null);
        Connection conn = null;
        try {
            conn = DbController.conectDb();
            SubscriptionController subscriptionController = new SubscriptionController();
            boolean result = false;
            result = DbController.executeStatment(conn, subscriptionController.unsubscribe(), subscriptionController.unsubscribeList(json.getId()));
            if (result) {
                logController.writeSimpleLog("unsubscribeUserCategory -> 200", "Desinscrição realizada com sucesso", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Desinscrição realizada com sucesso");
                return jsonReturn;
            }
            logController.writeSimpleLog("unsubscribeUserCategory -> 401", "Erro ao realizar desinscrição", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn; 
        } catch (Exception e) {
            logController.writeSimpleLog("unsubscribeUserCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }
    
}