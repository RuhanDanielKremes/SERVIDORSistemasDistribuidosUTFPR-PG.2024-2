package org.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.model.Json;
import org.example.model.JsonReturn;
import org.example.model.JsonReturnCategory;
import org.example.model.JsonReturnCategory2;
import org.example.model.JsonReturnWarning;
import org.example.model.User;
import org.example.model.Avisos;
import org.example.model.Category;

public class OperacaoController{

    public LogController logController = new LogController();

    public JsonReturn findOperation(Json<?> json) throws IOException{
        logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Shearching for a protocol Operation", true);
        switch (json.getOperacao()) {
            case "cadastrarUsuario":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: cadastrarUsuario", true);
                return cadastro(json);
            case "login":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: login", true);
                return login(json);
            case "logout":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: logout", true);
                return logout(json);
            case "listarCategorias":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: listarCategorias", true);
                return listCategory(json);
            // case "localizarCategoria":
            //     logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: localizarCategoria", true);
            //     return findCategory(json);
            case "listarUsuarioCategorias":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: listarUsuarioCategorias", true);
                return findUserSubscriptionsCategories(json);
            case "cadastrarUsuarioCategoria":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: cadastrarUsuarioCategoria", true);
                return subscribeUserCategory(json);
            case "listarUsuarioAvisos":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: listarUsuarioAvisos",true);
                return findUserWarnings(json);
            case "descadastrarUsuarioCategoria":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: descadastrarUsuarioCategoria", true);
                return unsubscribeUserCategory(json);
            case "listarAvisos":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: listarAvisos", true);
                return listWarnings(json);
            case "listarUsuarios":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: listarUsuarios", true);
                return listUsers(json);
            case "localizarUsuario":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: localizarUsuario", true);
                return findUser(json);
            case "excluirUsuario":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: excluirUsuario", true);
                return deleteUser(json);
            case "editarUsuario":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: editarUsuario", true);
                return editUser(json);
            case "salvarCategoria":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: salvarCategoria", true);
                return saveCategory(json);
            case "excluirCategoria":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: excluirCategoria", true);
                return deleteCategory(json);
            case "salvarAviso":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: salvarAviso", true);
                return saveWarning(json);
            // case "localizarAviso":
                // logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: localizarAviso", true);
                // return findWarning(json);
            case "excluirAviso":
                logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol found: excluirAviso", true);
                return deleteWarning(json);
            default:
            logController.writeSimpleLog("SYSTEM: Find protocol Operation", "Protocol not found!", true);
            JsonReturn jsonReturn = new JsonReturn();
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Operação não encontrada");
            return jsonReturn;
        }
    }

    public JsonReturn cadastro(Json<?> json) throws IOException{
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
            jsonReturn.setMessage("Os campos recebidos não são válidos");
            return jsonReturn;
        }
        if (user.getPassword().length() < 8) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Senha muito Curta", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Os campos recebidos não são válidos");
            return jsonReturn;
        }
        if (user.getPassword().length() > 20) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Senha muito Longa", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Os campos recebidos não são válidos");
            return jsonReturn;
        }
        if (user.getName().length() > 50) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Nome muito Longo", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Os campos recebidos não são válidos");
            return jsonReturn;
        }
        if (!(user.getName().matches("[A-Z\\s]+"))) {
            logController.writeSimpleLog("cadastrarUsuario -> 401", "Nome invalido: Uso de caracteres fora de padrão", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Os campos recebidos não são válidos");
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
                        DbController.executeStatment(conn, userController.createUser(), userController.createUserList(user));
                        logController.writeSimpleLog("cadastrarUsuario -> 201", "Usuario cadastrado com sucesso!", true);
                        jsonReturn.setStatus(201);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("Cadastro realizado com sucesso");
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
                    jsonReturn.setMessage("Usuário já está cadastrado no sistema");
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

    public JsonReturn login(Json<?> json) throws IOException{
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
                    jsonReturn.setOperation(null);
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

    public JsonReturn logout(Json<?> json) throws IOException{
        logController.writeSimpleLog("logout", "Inicializado procedimento de logout", true);
        JsonReturn jsonReturn = new JsonReturn();
        jsonReturn.setStatus(200);
        jsonReturn.setOperation(json.getOperacao());
        jsonReturn.setMessage("Logout realizado com sucesso.");
        return jsonReturn;
    }

    public JsonReturn validateToken(Json<?> json) throws IOException{
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

    public JsonReturn listCategory(Json<?> json) throws IOException{
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

    public JsonReturnCategory2 findCategory(Json<?> json) throws IOException{
        logController.writeSimpleLog("findCategory", "Inicializado procedimento de busca de categoria", true);
        JsonReturnCategory2 jsonReturn = new JsonReturnCategory2();
        JsonReturn jsonReturn1 = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage(jsonReturn1.getMessage());
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
                    jsonReturn.setCategory(category);;
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

    public JsonReturn findUserSubscriptionsCategories(Json<?> json) throws IOException {
        logController.writeSimpleLog("findUserSubscriptionsCategory",
                "Inicializado procedimento de busca de categorias inscritas por usuario", true);
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
            ResultSet rs = DbController.executeQuery(conn,
                    categoryController.findUserSubscriptionsCategories(user.getRa()));
            try {
                logController.writeSimpleLog("findUserSubscriptionsCategory", "Buscando categorias", true);
                List<Category> categoryList = new ArrayList<>();
                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("category"));
                    categoryList.add(category);
                }
                if (categoryList.isEmpty()) {
                    logController.writeSimpleLog("findUserSubscriptionsCategory -> 401", "Nenhuma categoria encontrada",
                            true);
                }
                jsonReturn.setStatus(201);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setCategory(categoryList);
                return jsonReturn;
            } catch (Exception e) {
                logController.writeSimpleLog("findUserSubscriptionsCategory -> 401", "Erro ao buscar categorias:" + e,
                        true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                return jsonReturn;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("findUserSubscriptionsCategory -> 401",
                    "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturnCategory findUserSubscriptionsCategoriesJRC(Json<?> json) throws IOException{
        logController.writeSimpleLog("findUserSubscriptionsCategory", "Inicializado procedimento de busca de categorias inscritas por usuario", true);
        JsonReturn jsonReturn = validateToken(json);
        JsonReturnCategory jsonReturnCategory = new JsonReturnCategory();
        jsonReturnCategory.setOperation(jsonReturnCategory.getOperation());
        jsonReturnCategory.setStatus(jsonReturn.getStatus());
        jsonReturnCategory.setMessage(jsonReturn.getMessage());
        if (jsonReturnCategory.getStatus() == 401) {
            return jsonReturnCategory;
        }
        jsonReturnCategory.setMessage(null);
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
                jsonReturnCategory.setStatus(201);
                jsonReturnCategory.setOperation(json.getOperacao());
                List<Integer> categoryIds = new ArrayList<>();
                for (Category category : categoryList) {
                    categoryIds.add(category.getId());
                }
                jsonReturnCategory.setCategory(categoryIds);
                return jsonReturnCategory;
            }catch (Exception e){
                logController.writeSimpleLog("findUserSubscriptionsCategory -> 401", "Erro ao buscar categorias:" + e, true);
                jsonReturnCategory.setStatus(401);
                jsonReturnCategory.setOperation(json.getOperacao());
                jsonReturnCategory.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                return jsonReturnCategory;
            }
        } catch (Exception e) {
            logController.writeSimpleLog("findUserSubscriptionsCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturnCategory.setStatus(401);
            jsonReturnCategory.setOperation(json.getOperacao());
            jsonReturnCategory.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturnCategory;
        }
    }

    public JsonReturn subscribeUserCategory(Json<?> json) throws IOException{
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
            result = DbController.executeStatment(conn, subscriptionController.subscribe(), subscriptionController.subscribeList(user.getId(), (Integer) json.getCategoria()));
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

    public JsonReturn unsubscribeUserCategory(Json<?> json) throws IOException{
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
            result = DbController.executeStatment(conn, subscriptionController.unsubscribe(), subscriptionController.unsubscribeList(json.getRa(),(Integer) json.getCategoria()));
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

    public JsonReturn listWarnings(Json<?> json) throws IOException{
        logController.writeSimpleLog("listWarnigs", "Inicializando procedimento de desinscrição de categoria", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        Connection conn;
        try {
            logController.writeSimpleLog("listWarnigs", "Listing category warnings", true);
            conn = DbController.conectDb();
            CategoryController categoryController = new CategoryController();
            ResultSet rs;
            if ((Integer) json.getCategoria() == 0) {
                rs = DbController.executeQuery(conn, categoryController.listCategory());
            }else{
                rs = DbController.executeQuery(conn, categoryController.findCategoryById((Integer) json.getCategoria()));
            }
            List<Avisos> warningsList = new ArrayList<>();
            while (rs.next()) {
                Category category = new Category();
                category.setName(rs.getString("name"));
                category.setId(rs.getInt("idcategory"));
                logController.writeSimpleLog("listWarnigs", "Foud user category: " + category.getName() , true);
                WarningController warningController = new WarningController();
                logController.writeSimpleLog("listWarnigs", "Listing warnings from category", true);
                try {
                    ResultSet rs2 = DbController.executeQuery(conn, warningController.listWarningByCategory(category.getId()));
                    while (rs2.next()) {
                        Avisos warning = new Avisos();
                        warning.setId(rs2.getInt("idwarning"));
                        warning.setTitle(rs2.getString("title"));
                        logController.writeSimpleLog("listWarnigs", "Foud warning: " + warning.getTitle() , true);
                        warning.setDescription(rs2.getString("description"));
                        warning.setCategory(category);   
                        warningsList.add(warning);
                    }
                    jsonReturn.setWarning(warningsList);
                } catch (Exception e) {
                    logController.writeSimpleLog("listWarnigs -> 401", "Erro ao buscar avisos:" + e, true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                    return jsonReturn;
                }
            }
            
        } catch (Exception e) {
            logController.writeSimpleLog("listWarnigs -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
        jsonReturn.setStatus(201);
        jsonReturn.setOperation(json.getOperacao());
        return jsonReturn;
    }

    public JsonReturn listUsers(Json<?> json) throws IOException{
        logController.writeSimpleLog("listUsers", "Inicializando procedimento de listagem de usuarios", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        try {
            UserController userController = new UserController();
            Connection conn = DbController.conectDb();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
            if (rs.next()) {
                User user = new User();
                user.setRa(json.getRa());
                user.setRole(rs.getString("role"));
                if (user.getRole().equals("admin")) {
                    logController.writeSimpleLog("SERVER: ListUsers", "Admin access granted", true);
                    jsonReturn.setMessage(null);
                    try {
                        logController.writeSimpleLog("listUsers", "Listing users", true);
                        conn = DbController.conectDb();
                        rs = DbController.executeQuery(conn, userController.listUsers());
                        List<User> userList = new ArrayList<>();
                        while (rs.next()) {
                            User user1 = new User();
                            // user1.setId(rs.getInt("idUser"));
                            user1.setName(rs.getString("name"));
                            user1.setRa(rs.getString("ra"));
                            // user1.setRole(rs.getString("role"));
                            user1.setPassword(rs.getString("password"));
                            userList.add(user1);
                        }
                        jsonReturn.setUsers(userList);
                    } catch (Exception e) {
                        logController.writeSimpleLog("listUsers -> 401", "Erro na conexão com o banco de dados" + e, true);
                        jsonReturn.setStatus(401);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
                        return jsonReturn;
                    }
                    jsonReturn.setStatus(201);
                    jsonReturn.setOperation(json.getOperacao());
                    return jsonReturn;
                }else{
                    logController.writeSimpleLog("SERVER: ListUsers", "Access denied. Client tried to acess other User information", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Acesso negado");
                    return jsonReturn;
                }
            } else {
                logController.writeSimpleLog("SERVER: ListUsers", "User not found", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Access Negado");
                return jsonReturn;
            }
        } catch (SQLException sqle) {
            logController.writeSimpleLog("SERVER: ListUsers", "Error on database connection" + sqle.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("SERVER: ListUsers", "Error on database connection" + e.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        }
    }

    public JsonReturn findUser(Json<?> json) throws IOException{
        logController.writeSimpleLog("findUser", "Inicializando procedimento de busca de usuario", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        if(!json.getToken().matches(json.getRa())){
            try {
                UserController userController = new UserController();
                Connection conn = DbController.conectDb();
                ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getRa()));
                if (rs.next()) {
                    User user = new User();
                    user.setRa(json.getRa());
                    user.setRole(rs.getString("role"));
                    if (user.getRole().equals("admin")) {
                        logController.writeSimpleLog("SERVER: FindUser", "Admin access granted", true);
                        jsonReturn.setMessage(null);
                    }else{
                        logController.writeSimpleLog("SERVER: FindUser", "Access denied. Client tried to acess other User information", true);
                        jsonReturn.setStatus(401);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("Acesso negado");
                        return jsonReturn;
                    }
                } else {
                    logController.writeSimpleLog("SERVER: FindUser", "User not found", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Usuario nao encontrado");
                    return jsonReturn;
                }
            } catch (SQLException sqle) {
                logController.writeSimpleLog("SERVER: FindUser", "Error on database connection" + sqle.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            } catch (Exception e) {
                logController.writeSimpleLog("SERVER: FindUser", "Error on database connection" + e.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            }
        }
        logController.writeSimpleLog("findUser", "User validated. Access granted", true);
        Connection conn;
        try {
            logController.writeSimpleLog("findUser", "Listing user", true);
            conn = DbController.conectDb();
            UserController userController = new UserController();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getRa()));
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                // user.setId(rs.getInt("idUser"));
                user.setName(rs.getString("name"));
                user.setRa(rs.getString("ra"));
                // user.setRole(rs.getString("role"));
                user.setPassword(rs.getString("password"));
                userList.add(user);
            }
            jsonReturn.setUsers(userList);
        } catch (Exception e) {
            logController.writeSimpleLog("findUser -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
        jsonReturn.setStatus(201);
        jsonReturn.setOperation(json.getOperacao());
        return jsonReturn;
    }

    public JsonReturn deleteUser(Json<?> json) throws IOException{
        logController.writeSimpleLog("deleteUser", "Inicializando procedimento de exclusão de usuario", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        if(!json.getToken().matches(json.getRa())){
            try {
                UserController userController = new UserController();
                Connection conn = DbController.conectDb();
                ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getRa()));
                if (rs.next()) {
                    User user = new User();
                    user.setRa(rs.getString("ra"));
                    user.setRole(rs.getString("role"));
                    if (user.getRole().equals("admin")) {
                        logController.writeSimpleLog("SERVER: DeleteUser", "Admin access granted", true);
                        jsonReturn.setMessage(null);
                    }else{
                        logController.writeSimpleLog("SERVER: DeleteUser", "Access denied. Client tried to acess other User information", true);
                        jsonReturn.setStatus(401);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("Acesso negado");
                        return jsonReturn;
                    }
                } else {
                    logController.writeSimpleLog("SERVER: DeleteUser", "User not found", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Usuario nao encontrado");
                    return jsonReturn;
                }
            } catch (SQLException sqle) {
                logController.writeSimpleLog("SERVER: DeleteUser", "Error on database connection" + sqle.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            } catch (Exception e) {
                logController.writeSimpleLog("SERVER: DeleteUser", "Error on database connection" + e.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            }
        }
        logController.writeSimpleLog("deleteUser", "User validated. Access granted", true);
        Connection conn;
        try {
            logController.writeSimpleLog("deleteUser", "Deleting user", true);
            conn = DbController.conectDb();
            UserController userController = new UserController();
            boolean result = false;
            result = DbController.executeStatment(conn, userController.deleteUser(), userController.deleteUserList(json.getRa()));
            if (result) {
                logController.writeSimpleLog("deleteUser -> 200", "Usuario deletado com sucesso", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Usuario deletado com sucesso");
                return jsonReturn;
            }
            logController.writeSimpleLog("deleteUser -> 401", "Erro ao deletar usuario", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("deleteUser -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn editUser(Json<?> json) throws IOException{
        logController.writeSimpleLog("editUser", "Inicializando procedimento de edição de usuario", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        if(!json.getToken().matches(json.getUsuario().getRa())){
            try {
                UserController userController = new UserController();
                Connection conn = DbController.conectDb();
                ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
                if (rs.next()) {
                    User user = new User();
                    user.setRa(rs.getString("ra"));
                    user.setRole(rs.getString("role"));
                    if (user.getRole().equals("admin")) {
                        logController.writeSimpleLog("SERVER: EditUser", "Admin access granted", true);
                        jsonReturn.setMessage(null);
                    }else{
                        logController.writeSimpleLog("SERVER: EditUser", "Access denied. Client tried to acess other User information", true);
                        jsonReturn.setStatus(401);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("Acesso negado");
                        return jsonReturn;
                    }
                } else {
                    logController.writeSimpleLog("SERVER: EditUser", "User not found", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Usuario nao encontrado");
                    return jsonReturn;
                }
            } catch (SQLException sqle) {
                logController.writeSimpleLog("SERVER: EditUser", "Error on database connection" + sqle.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            } catch (Exception e) {
                logController.writeSimpleLog("SERVER: EditUser", "Error on database connection" + e.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            }
        }
        logController.writeSimpleLog("SERVER: editUser", "User validated. Access granted", true);
        logController.writeSimpleLog("SERVER: editUser", "Validating user information", true);
        if (json.getUsuario().getName().length() > 50
                || !json.getUsuario().getName().matches(("^[A-Z\\s]+$"))) {
                    System.out.println(json.getUsuario().toString());
            if (json.getUsuario().getName().length() < 3) {
                logController.writeSimpleLog("editUser -> 401", "Nome muito Curto", true);
            } else if (json.getUsuario().getName().length() > 50) {
                logController.writeSimpleLog("editUser -> 401", "Nome muito Longo", true);
            } else if (!json.getUsuario().getName().matches(("^[A-Z\\s]+$"))){
                logController.writeSimpleLog("editUser -> 401", "Nome não contem apenas maiusculas", true);
            }
            // logController.writeSimpleLog("editUser -> 401", "Nome muito Curto", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Os campos recebidos não são válidos.");
            return jsonReturn;
        }
        if (json.getUsuario().getRa().length() != 7) {
            logController.writeSimpleLog("editUser -> 401", "RA invalido", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Os campos recebidos não são válidos.");
            return jsonReturn;
        }
        if (json.getUsuario().getPassword().length() < 8 || json.getUsuario().getPassword().length() > 20) {
            logController.writeSimpleLog("editUser -> 401", "Senha muito Curta", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Os campos recebidos não são válidos.");
            return jsonReturn;
        }
        Connection conn;
        try {
            logController.writeSimpleLog("SERVER: editUser", "Editing user", true);
            conn = DbController.conectDb();
            UserController userController = new UserController();
            boolean result = false;
            result = DbController.executeStatment(conn, userController.updateUser(), userController.updateUserList(json.getUsuario()));
            if (result) {
                logController.writeSimpleLog("editUser -> 201", "Edição realizada com sucesso.", true);
            jsonReturn.setStatus(201);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Edição realizada com sucesso.");
                return jsonReturn;
            }
            logController.writeSimpleLog("editUser -> 401", "Erro ao editar usuario", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("editUser -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn saveCategory(Json<?> json) throws IOException{
        logController.writeSimpleLog("editCategory", "Inicializando procedimento de edição de categoria", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        try {
            UserController userController = new UserController();
            Connection conn = DbController.conectDb();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
            if (rs.next()) {
                User user = new User();
                user.setRa(rs.getString("ra"));
                user.setRole(rs.getString("role"));
                if (user.getRole().equals("admin")) {
                    logController.writeSimpleLog("SERVER: EditCategory", "Admin access granted", true);
                    jsonReturn.setMessage(null);
                }else{
                    logController.writeSimpleLog("SERVER: EditCategory", "Access denied. Client tried to acess other User information", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Acesso negado");
                    return jsonReturn;
                }
            } else {
                logController.writeSimpleLog("SERVER: EditCategory", "User not found", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Usuario nao encontrado");
                return jsonReturn;
            }
        } catch (SQLException sqle) {
            logController.writeSimpleLog("SERVER: EditCategory", "Error on database connection" + sqle.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("SERVER: EditCategory", "Error on database connection" + e.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        }
        Connection conn;
        try {
            conn = DbController.conectDb();
            CategoryController categoryController = new CategoryController();
            boolean result = false;
            if (json.getCategoria() == null) {
                logController.writeSimpleLog("editCategory -> 401", "Categoria nao encontrada", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Categoria nao encontrada");
                return jsonReturn;
            }
            if (((Category) json.getCategoria()).getId() == 0) {
                System.out.println(json.getCategoria().toString());
                result = DbController.executeStatment(conn, categoryController.createCategory(), new ArrayList<>(Arrays.asList(((Category) json.getCategoria()).getName())));
            } else {
                result = DbController.executeStatment(conn, categoryController.updateCategory(), categoryController.updateCategoryList((Category) json.getCategoria()));
            }
            if (result) {
                logController.writeSimpleLog("editCategory -> 200", "Categoria editada com sucesso", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Categoria editada com sucesso");
                return jsonReturn;
            }
            logController.writeSimpleLog("editCategory -> 401", "Erro ao editar categoria", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("editCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn deleteCategory(Json<?> json) throws IOException{
        logController.writeSimpleLog("deleteCategory", "Inicializando procedimento de exclusão de categoria", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        try {
            UserController userController = new UserController();
            Connection conn = DbController.conectDb();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
            if (rs.next()) {
                User user = new User();
                user.setRa(rs.getString("ra"));
                user.setRole(rs.getString("role"));
                if (user.getRole().equals("admin")) {
                    logController.writeSimpleLog("SERVER: DeleteCategory", "Admin access granted", true);
                    jsonReturn.setMessage(null);
                }else{
                    logController.writeSimpleLog("SERVER: DeleteCategory", "Access denied. Client tried to acess other User information", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Acesso negado");
                    return jsonReturn;
                }
            } else {
                logController.writeSimpleLog("SERVER: DeleteCategory", "User not found", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Usuario nao encontrado");
                return jsonReturn;
            }
        } catch (SQLException sqle) {
            logController.writeSimpleLog("SERVER: DeleteCategory", "Error on database connection" + sqle.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("SERVER: DeleteCategory", "Error on database connection" + e.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        }
        Connection conn;
        try{
            logController.writeSimpleLog("SERVER: DeleteCategory", "Verifying if category does not have any warnings", false);
            conn = DbController.conectDb();
            WarningController warningController = new WarningController();
            ResultSet rs = DbController.executeQuery(conn, warningController.listWarningByCategory((Integer) json.getId()));
            if (rs.next()) {
                logController.writeSimpleLog("SERVER: DeleteCategory", "Category has warnings", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("A categoria nao pode ser deletada pois possui avisos cadastrados.");
                return jsonReturn;
            }
        } catch (SQLException sqle) {
            logController.writeSimpleLog("SERVER: DeleteCategory", "Error on database connection" + sqle.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        }
        catch (Exception e) {
            logController.writeSimpleLog("SERVER: DeleteCategory", "Error on database connection" + e.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        }
        try {
            logController.writeSimpleLog("deleteCategory", "Deleting category", true);
            conn = DbController.conectDb();
            CategoryController categoryController = new CategoryController();
            boolean result = false;
            result = DbController.executeStatment(conn, categoryController.deleteCategory(), (Arrays.asList(json.getId())));
            if (result) {
                logController.writeSimpleLog("deleteCategory -> 200", "Categoria deletada com sucesso", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Categoria deletada com sucesso");
                return jsonReturn;
            }
            logController.writeSimpleLog("deleteCategory -> 401", "Erro ao deletar categoria", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("deleteCategory -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturn saveWarning(Json<?> json) throws IOException {
        logController.writeSimpleLog("saveWarning", "Inicializando procedimento de salvar aviso", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        try {
            UserController userController = new UserController();
            Connection conn = DbController.conectDb();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
            if (rs.next()) {
                User user = new User();
                user.setRa(rs.getString("ra"));
                user.setRole(rs.getString("role"));
                if (user.getRole().equals("admin")) {
                    logController.writeSimpleLog("SERVER: EditWarning", "Admin access granted", true);
                    jsonReturn.setMessage(null);
                } else {
                    logController.writeSimpleLog("SERVER: EditWarning",
                            "Access denied. Client tried to acess other User information", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Acesso negado");
                    return jsonReturn;
                }
            } else {
                logController.writeSimpleLog("SERVER: EditWarning", "User not found", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Usuario nao encontrado");
                return jsonReturn;
            }
        } catch (SQLException sqle) {
            logController.writeSimpleLog("SERVER: EditWarning", "Error on database connection" + sqle.getMessage(),
                    true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("SERVER: EditWarning", "Error on database connection" + e.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        }
        Connection conn;
        try {
            logController.writeSimpleLog("SERVER: saveWarning", "Editing warning", true);
            conn = DbController.conectDb();
            WarningController warningController = new WarningController();
            boolean result = false;

            if (json.getAvisos().getCategory() == 0) {
                result = DbController.executeStatment(conn, warningController.createWarning(),
                        warningController.createWarningList(json.getAvisos()));
            } else {
                result = DbController.executeStatment(conn, warningController.updateWarning(),
                        warningController.updateWarningList(json.getAvisos()));
            }
            if (result) {
                logController.writeSimpleLog("editWarning -> 200", "Aviso editado com sucesso", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Aviso editado com sucesso");
                return jsonReturn;
            }
            logController.writeSimpleLog("editWarning -> 401", "Erro ao editar aviso", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("editWarning -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }

    public JsonReturnWarning findWarning(Json<?> json) throws IOException {
        logController.writeSimpleLog("findWarning", "Inicializando procedimento de busca de aviso", true);
        JsonReturnWarning jsonReturn = new JsonReturnWarning();
        JsonReturn jsonReturn1 = validateToken(json);
        if (jsonReturn1.getStatus() == 401) {
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Acesso negado");
            return jsonReturn;
        }
        Connection conn;
        try {
            logController.writeSimpleLog("findWarning", "Listing warning", true);
            conn = DbController.conectDb();
            WarningController warningController = new WarningController();
            ResultSet rs = DbController.executeQuery(conn, warningController.findWarning(json.getId()));
            Avisos warning = new Avisos();
            if (rs.next()) {
                warning.setId(rs.getInt("idwarning"));
                warning.setTitle(rs.getString("title"));
                warning.setDescription(rs.getString("description"));
                Category category = new Category();
                category.setId(rs.getInt("category"));
                category.setName(rs.getString("name"));
                warning.setCategory(category);
                jsonReturn.setWarning(warning);
            }
        } catch (Exception e) {
            logController.writeSimpleLog("findWarning -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
        jsonReturn.setStatus(201);
        jsonReturn.setOperation(json.getOperacao());
        return jsonReturn;
    }

    public JsonReturn findUserWarnings(Json<?> json) throws IOException {
        logController.writeSimpleLog("findUserWarnings", "Inicializando procedimento de busca de avisos", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        if (!json.getToken().equals(json.getRa())) {
            try {
                UserController userController = new UserController();
                Connection conn = DbController.conectDb();
                ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
                if (rs.next()) {
                    User user = new User();
                    user.setRa(json.getToken());
                    user.setRole(rs.getString("role"));
                    if (user.getRole().equals("admin")) {
                        logController.writeSimpleLog("SERVER: FindUserWarnings", "Admin access granted", true);
                        jsonReturn.setMessage(null);
                    } else {
                        logController.writeSimpleLog("SERVER: FindUserWarnings","Access denied. Client tried to acess other User information", true);
                        jsonReturn.setStatus(401);
                        jsonReturn.setOperation(json.getOperacao());
                        jsonReturn.setMessage("Acesso negado");
                        return jsonReturn;
                    }
                } else {
                    logController.writeSimpleLog("SERVER: FindUserWarnings", "User not found", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Usuario nao encontrado");
                    return jsonReturn;
                }
            } catch (SQLException sqle) {
                logController.writeSimpleLog("SERVER: FindUserWarnings",
                        "Error on database connection" + sqle.getMessage(),
                        true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            } catch (Exception e) {
                logController.writeSimpleLog("SERVER: FindUserWarnings",
                        "Error on database connection" + e.getMessage(), true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
                return jsonReturn;
            }
        }
        Connection conn;
        try {
            logController.writeSimpleLog("findUserWarnings", "Listing user warnings", true);
            conn = DbController.conectDb();
            SubscriptionController subscriptionController = new SubscriptionController();
            ResultSet rs = DbController.executeQuery(conn, subscriptionController.ListUserSubscription(json.getRa()));
            List<Avisos> warningsList = new ArrayList<>();
            while (rs.next()) {
                WarningController warningController = new WarningController();
                ResultSet rs2 = DbController.executeQuery(conn, warningController.listWarningByCategory(rs.getInt("category")));
                while (rs2.next()) {
                    Avisos warning = new Avisos();
                    warning.setId(rs2.getInt("idwarning"));
                    warning.setTitle(rs2.getString("title"));
                    warning.setDescription(rs2.getString("description"));
                    Category category = new Category();
                    category.setId(rs.getInt("category"));
                    warning.setCategory(category);
                    warningsList.add(warning);
                }   
            }
        } catch (Exception e) {
            logController.writeSimpleLog("findUserWarnings -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
        jsonReturn.setStatus(201);
        jsonReturn.setOperation(json.getOperacao());
        return jsonReturn;
    }
    
    public JsonReturn deleteWarning(Json<?> json) throws IOException{
        logController.writeSimpleLog("deleteWarning", "Inicializando procedimento de exclusão de aviso", true);
        JsonReturn jsonReturn = validateToken(json);
        if (jsonReturn.getStatus() == 401) {
            return jsonReturn;
        }
        try {
            UserController userController = new UserController();
            Connection conn = DbController.conectDb();
            ResultSet rs = DbController.executeQuery(conn, userController.getUser(json.getToken()));
            if (rs.next()) {
                User user = new User();
                user.setRa(rs.getString("ra"));
                user.setRole(rs.getString("role"));
                if (user.getRole().equals("admin")) {
                    logController.writeSimpleLog("SERVER: DeleteWarning", "Admin access granted", true);
                    jsonReturn.setMessage(null);
                }else{
                    logController.writeSimpleLog("SERVER: DeleteWarning", "Access denied. Client tried to acess other User information", true);
                    jsonReturn.setStatus(401);
                    jsonReturn.setOperation(json.getOperacao());
                    jsonReturn.setMessage("Acesso negado");
                    return jsonReturn;
                }
            } else {
                logController.writeSimpleLog("SERVER: DeleteWarning", "User not found", true);
                jsonReturn.setStatus(401);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Usuario nao encontrado");
                return jsonReturn;
            }
        } catch (SQLException sqle) {
            logController.writeSimpleLog("SERVER: DeleteWarning", "Error on database connection" + sqle.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("SERVER: DeleteWarning", "Error on database connection" + e.getMessage(), true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("Erro ao buscar usuario no banco de dados");
            return jsonReturn;
        }
        Connection conn;
        try {
            logController.writeSimpleLog("deleteWarning", "Deleting warning", true);
            conn = DbController.conectDb();
            WarningController warningController = new WarningController();
            boolean result = false;
            result = DbController.executeStatment(conn, warningController.deleteWarning(), (Arrays.asList(json.getId())));
            if (result) {
                logController.writeSimpleLog("deleteWarning -> 200", "Aviso deletado com sucesso", true);
                jsonReturn.setStatus(200);
                jsonReturn.setOperation(json.getOperacao());
                jsonReturn.setMessage("Aviso deletado com sucesso");
                return jsonReturn;
            }
            logController.writeSimpleLog("deleteWarning -> 401", "Erro ao deletar aviso", true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        } catch (Exception e) {
            logController.writeSimpleLog("deleteWarning -> 401", "Erro na conexão com o banco de dados" + e, true);
            jsonReturn.setStatus(401);
            jsonReturn.setOperation(json.getOperacao());
            jsonReturn.setMessage("O servidor nao conseguiu conectar com o banco de dados.");
            return jsonReturn;
        }
    }  
    
}