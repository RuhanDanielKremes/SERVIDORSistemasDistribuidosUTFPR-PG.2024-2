package org.example.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.example.model.Warnings;


public class WarningController {
    
    LogController logController = new LogController();

    public String createWarning() throws IOException{
        String sqlQuery = "INSERT INTO warnings (title, description, category) VALUES (?, ?, ?)";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public List<?> createWarningList(Warnings warning) throws IOException{
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "Warning added to list", true);
        return Arrays.asList(warning.getTitle(), warning.getDescription(), warning.getCategory());
    }

    public String updateWarning() throws IOException{
        String sqlQuery = "UPDATE warnings SET title = ?, description = ?, category = ? WHERE idwarning = ?";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public List<?> updateWarningList(Warnings warning) throws IOException{
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "Warning added to list", true);
        return Arrays.asList(warning.getTitle(), warning.getDescription(), warning.getCategory(), warning.getId());
    }

    public String deleteWarning() throws IOException{
        String sqlQuery = "DELETE FROM warnings WHERE idwarning =?";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String listWarning() throws IOException{
        String sqlQuery = "SELECT * FROM warnings";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String listWarningByCategory(int category) throws IOException{
        String sqlQuery = "SELECT * FROM warnings WHERE warnings.category = " + category;
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String listWarningByTitle() throws IOException{
        String sqlQuery = "SELECT * FROM warnings WHERE warnings.title = ?";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

    public String findWarning(int id) throws IOException{
        String sqlQuery = "SELECT w.*, c.name FROM warnings w JOIN categories c ON w.category = c.idcategory WHERE w.idwarning = " + id + ";";
        logController.writeSimpleLog("SYSTEM: QueryConstruction", "SQL Query created: " + sqlQuery, true);
        return sqlQuery;
    }

}
