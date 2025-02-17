package org.example.model;

public class Warnings {
 
    private int id;
    private String titulo;
    private String descricao;
    private int categoria;

    public Warnings() {
        id = 0;
        titulo = "";
        descricao = "";
        categoria = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.titulo = title;
    }

    public String getTitle() {
        return titulo;
    }

    public void setDescription(String description) {
        this.descricao = description;
    }

    public String getDescription() {
        return descricao;
    }

    public void setCategory(int category) {
        this.categoria = category;
    }

    public int getCategory() {
        return categoria;
    }

    @Override
    public String toString() {
        String returnString = "{id: " + this.id;
        if (this.titulo != null && !this.titulo.isEmpty()) {
            returnString += ", titulo: " + this.titulo;
        }
        if (this.descricao != null && !this.descricao.isEmpty()) {
            returnString += ", descricao: " + this.descricao;
        }
        return returnString += ", categoria: " + this.categoria + "}";
    }

}