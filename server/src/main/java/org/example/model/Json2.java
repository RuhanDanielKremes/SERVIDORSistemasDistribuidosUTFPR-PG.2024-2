package org.example.model;

public class Json2 {
    private String operacao;
    private String ra;
    private String senha;
    private String nome;

    // Getters e setters
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Json{" +
                "operacao='" + operacao + '\'' +
                ", ra='" + ra + '\'' +
                ", senha='" + senha + '\'' +
                ", nome='" + '\'' +
                '}';
    }
}