package org.example.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
 
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("nome")
    private String name;


    public Category() {
        id = 0;
        name = "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String returnString = "{id: " + this.id;
        if (this.name != null && !this.name.isEmpty()) {
            return returnString += ", name: " + this.name + "}";
        }
        return returnString += "}";
    }
    
}