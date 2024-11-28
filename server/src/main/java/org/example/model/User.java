package org.example.model;

public class User {

    private int id;
    private String name;
    private String ra;
    private String password;
    private String role;

    public User() {
        this.id = 0;
        this.name = "";
        this.ra = "";
        this.password = "";
        this.role = "user";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRa() {
        return ra;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void printUser() {
        System.out.println("ID: " + this.id + "\n\tName: " + this.name + ", RA: " + this.ra + ", Password: " + this.password + ", Role: " + this.role + "\n");
    }

}
