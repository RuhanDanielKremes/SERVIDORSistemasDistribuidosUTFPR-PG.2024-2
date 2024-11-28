package org.example.model;

import com.google.gson.annotations.SerializedName;

public class Json {
    
     @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String nome;

    @SerializedName("ra")
    private String ra;

    @SerializedName("password")
    private String senha;

    @SerializedName("role")
    private String operacao;

    private String token;
    private String mensagem;

    public Json() {
        this.id = 0;
        this.nome = "";
        this.ra = "";
        this.senha = "";
        this.operacao = "";
        this.token = "";
        this.mensagem = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
