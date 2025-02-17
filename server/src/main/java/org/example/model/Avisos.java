package org.example.model;

public class Avisos {
 
    private int id;
    private String titulo;
    private String descricao;
    private Category categoria;

    public Avisos() {
        id = 0;
        titulo = "";
        descricao = "";
        categoria = null;
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

    public void setCategory(Category category) {
        this.categoria = category;
    }

    public Category getCategory() {
        return categoria;
    }

    @Override
    public String toString() {
        String returnString = "{id: " + this.id + "titulo:" + this.titulo + "descricao:" + this.descricao + "categoria:[" + this.categoria + "]}";
        return returnString;
    }

}