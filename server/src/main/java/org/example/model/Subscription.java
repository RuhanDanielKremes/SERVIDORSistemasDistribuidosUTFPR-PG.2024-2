package org.example.model;

public class Subscription {
    
    int id;
    int user;
    int category;

    public Subscription() {
        this.id = 0;
        this.user = 0;
        this.category = 0;
    }

    public int getId() {
        return id;
    }

    public int getUser() {
        return user;
    }

    public int getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void printSubscription() {
        System.out.println("ID: " + this.id + "User: " + this.user + ", Category: " + this.category);
    }

}
