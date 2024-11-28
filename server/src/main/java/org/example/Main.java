package org.example;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
// import java.sql.Connection;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.List;
// import java.util.ArrayList;

// import org.example.controller.DbController;
// import org.example.controller.LogController;
// import org.example.controller.UserController;
import org.example.model.User;

public class Main {

    // public static void main(String[] args) {
    //     Gson gson = new Gson();
    //     User user = new User("filadamae","252525","soumemo");
    //     String json = gson.toJson(user);
    //     try (FileWriter writer = new FileWriter("user.json")) {
    //         writer.write(json);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    // public static void main(String[] args) throws IOException, SQLException {
    //     try (FileWriter writer = new FileWriter("log.txt")) {
    //         writer.write("");
    //     }
    //     LogController logController = new LogController();
    //     Connection conn = null;

    //     User user1 = new User();
    //     user1.setName("Iago");
    //     user1.setRa("2001009");
    //     user1.setPassword("123456");
    //     try{
    //         conn = DbController.conectDb();
    //         UserController userController = new UserController();
    //         DbController.executeStatment(conn, userController.createUser(user1), userController.createUserList(user1));
    //         ResultSet rs = DbController.executeQuery(conn, userController.getUser(user1.getRa()));
    //         if(rs == null){
    //             logController.writeSimpleLog("SYSTEM: Data Validation", "Data recivied as null", true);
    //         }else{
    //             logController.writeSimpleLog("SYSTEM: Data Validation", "Data recivied", true);
    //             try {
    //                 logController.writeSimpleLog("SYSTEM: Data -> Model", "Passing data to Model", true);
    //                 List<User> users = new ArrayList<>();
    //                 while(rs.next()){
    //                     User user = new User();
    //                     user.setId(rs.getInt("idUser"));
    //                     user.setName(rs.getString("name"));
    //                     user.setRa(rs.getString("ra"));
    //                     user.setPassword(rs.getString("password"));
    //                     user.setRole(rs.getString("role"));
    //                     if (rs.getString("ra").equals(user1.getRa())) {
    //                         user1.setId(user.getId());
    //                     }
    //                     users.add(user);
    //                 }
    //                 logController.writeSimpleLog("SYSTEM: Data -> Model", "Sucessfull! Data passed to Model", true);
    //                 for(User user : users){
    //                     user.printUser();
    //                 }
    //             } catch (Exception e) {
    //                 logController.writeSimpleLog("SYSTEM: Data -> Model", "Error! Data do not pass to the Model" + e.getMessage(), true);
    //                 System.out.println("Error! Data do not pass to the Model" + e.getMessage());
    //             }
    //         }
    //         user1.setName("notIago");
    //         user1.setRa("2001009");
    //         user1.setPassword("123456");
    //         DbController.executeStatment(conn, userController.updateUser(user1), userController.updateUserList(user1));

    //         DbController.executeStatment(conn, userController.deleteUser(user1.getRa()), userController.deleteUserList(user1.getRa()));
    //     }catch(IOException e){
    //         logController.writeSimpleLog("SYSTEM:conectarBanco", "Error connecting with db" + e.getMessage(), true);
    //         e.printStackTrace();
    //     }finally{
    //         DbController.closeConnection(conn);
    //     }
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        User user = new User();
        user.setName("JOHN");
        user.setPassword("ruminar");
        user.setRa("1234567");
        try (FileWriter writer = new FileWriter("user.json")) {
            String json = gson.toJson(user);
            writer.write(json);
        }
    }

}