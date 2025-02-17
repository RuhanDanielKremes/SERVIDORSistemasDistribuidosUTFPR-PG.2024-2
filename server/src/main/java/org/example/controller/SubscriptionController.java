package org.example.controller;

import java.io.IOException;
import java.util.List;

public class SubscriptionController {
 
    LogController logController = new LogController();

    public String ListUserSubscription(String ra) throws IOException {
        String sqlQuery;
        sqlQuery = "SELECT subscription.* FROM subscription JOIN users ON users.iduser = subscription.user where users.ra = " + ra + ";";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String ListAllUsersSubscription() throws IOException {
        String sqlQuery;
        sqlQuery = "SELECT subscription.idsubscription, users.name, categories.name FROM subscription JOIN users ON subscription.user = users.iduser JOIN categories ON subscription.category = categories.idcategory;";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String subscribe() throws IOException{
        String sqlQuery = "INSERT INTO subscription (user, category) VALUES (?, ?)";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    } 

    public List<?> subscribeList(int user, int category) throws IOException{
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "Subscription added to list", true);
        return List.of(user, category);
    }

    public String unsubscribe() throws IOException{
        String sqlQuery = "DELETE FROM subscription WHERE user IN (SELECT iduser FROM users WHERE ra = ?) AND category = ?;";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public List<?> unsubscribeList(String userRa, int categoryId) throws IOException{
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "Subscription added to list", true);
        System.out.println("build list: " + userRa + " " + categoryId);
        return List.of(userRa, categoryId);
    }

}