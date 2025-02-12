package org.example.model;

public class User {

    private int id;
    private String nome;
    private String ra;
    private String senha;
    private String role;

    public User() {
        this.id = 0;
        this.nome = "";
        this.ra = "";
        this.senha = "";
        this.role = "user";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return nome;
    }

    public String getRa() {
        return ra;
    }

    public String getPassword() {
        return senha;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public void setPassword(String password) {
        this.senha = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void printUser() {
        System.out.println("{\"ID\": \"" + this.id + "\", \"Name\": \"" + this.nome + "\", \"RA\": \"" + this.ra
                + "\", \"Password\": \"" + this.senha + "\", \"Role\": \"" + this.role + "\"}");
    }
    
    @Override
    public String toString() {
        String returnString;
        returnString = "{";
        returnString = id == 0 ? "" : "ID: " + id;
        returnString = nome == "" ? returnString
                : returnString.length() > 1 ? returnString + ", \"Name\": \"" + nome + "\""
                        : "\"Name\": \"" + nome + "\"";
        returnString = ra == "" ? returnString
                : returnString.length() > 1 ? returnString + ", \"RA\": \"" + ra + "\""
                        : "\"RA\": \"" + ra + "\"";
        returnString = senha == "" ? returnString
                : returnString.length() > 1 ? returnString + ", \"Password\": \"" + senha + "\""
                        : "\"Password\": \"" + senha + "\"";
        returnString = role == "" ? returnString
                : returnString.length() > 1 ? returnString + ", \"Role\": \"" + role + "\""
                        : "\"Role\": \"" + role + "\"";
        returnString += "}";
        return returnString;
    }

}
