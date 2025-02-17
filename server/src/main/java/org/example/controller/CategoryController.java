package org.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.model.Category;

public class CategoryController {
    
    LogController logController = new LogController();

    public String createCategory() throws IOException {
        String sqlQuery = "INSERT INTO categories (name) VALUES (?);";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String updateCategory() throws IOException {
        String sqlQuery = "UPDATE categories SET name = ? WHERE idcategory = ?;";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }   

    public List<?> updateCategoryList(Category category) throws IOException {
        List<Object> categoryList = new ArrayList<>();
        categoryList.add(category.getName());
        categoryList.add(category.getId());
        return categoryList;
    }

    public String deleteCategory() throws IOException {
        String sqlQuery = "DELETE FROM categories WHERE categories.idcategory = ? AND NOT EXISTS (SELECT * FROM warnings WHERE warnings.category = categories.idcategory);";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }   

    public String listCategory() throws IOException {
        String sqlQuery = "SELECT * FROM categories";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }   

    public String findCategoryById(int id) throws IOException {
        String sqlQuery = "SELECT * FROM categories WHERE categories.idcategory = " + id + ";";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String findCategoryByName() throws IOException {
        String sqlQuery = "SELECT * FROM categories WHERE categories.name = ?";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String findUserSubscriptionsCategoriesDetailed(String ra) throws IOException {
        String sqlQuery = "SELECT subscription.idsubscription, users.name, categories.name FROM subscription JOIN users ON subscription.user = users.iduser JOIN categories ON subscription.category = categories.idcategory WHERE users.ra = " + ra + ";";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String findUserSubscriptionsCategories(String ra) throws IOException {
        String sqlQuery = "SELECT subscription.* FROM subscription JOIN users ON users.iduser = subscription.user where users.ra = " + ra + ";";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String findAllUsersSubscriptionsCategoriesDetailed() throws IOException {
        String sqlQuery = "SELECT subscription.idsubscription, users.name, categories.name FROM subscription JOIN users ON subscription.user = users.iduser JOIN categories ON subscription.category = categories.idcategory;";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String findAllUsersSubscriptionsCategories() throws IOException {
        String sqlQuery = "SELECT * FROM subscription;";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

}
