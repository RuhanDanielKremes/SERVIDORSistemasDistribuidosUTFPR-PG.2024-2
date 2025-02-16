package org.example.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.example.model.User;

public class UserController {

    LogController logController = new LogController();

    public String listUsers() throws IOException {
        String sqlQuery;
        sqlQuery = "SELECT * FROM users";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String getUser(String ra) throws IOException {
        String sqlQuery;
        sqlQuery = "SELECT * FROM users WHERE ra = '" + ra + "'";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String createUser() throws IOException {
        String sqlQuery;
        sqlQuery = "INSERT INTO users (name, ra, password) VALUES (?, ?, ?)";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public List<?> createUserList(User user) throws IOException {
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "User added to list", true);
        return Arrays.asList(user.getName(), user.getRa(), user.getPassword());
    }
    
    public String updateUser() throws IOException {
        String sqlQuery;
        sqlQuery = "UPDATE users SET name = ?, ra = ?, password = ? WHERE ra  = ?";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public List<?> updateUserList(User user) throws IOException {
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "User added to list", true);
        return Arrays.asList(user.getName(), user.getRa(), user.getPassword(), user.getRa());
    }

    public String deleteUser() throws IOException {
        String sqlQuery;
        sqlQuery = "DELETE FROM users WHERE ra = ?";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public List<?> deleteUserList(String ra) throws IOException {
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "User add to list", true);
        return Arrays.asList(ra);
    }
}