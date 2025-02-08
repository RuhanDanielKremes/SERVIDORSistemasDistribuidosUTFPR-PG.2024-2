package org.example.model;

public class Json<G> {
    
    private int id;
    private String nome;
    private String ra;
    private String senha;
    private String operacao;
    private String token;
    private String mensagem;
    private User usuario;
    private Category categorias;
    private G categoria;
    private Warnings aviso;

    public Json(int id, String nome, String ra, String senha, String operacao, String token, String mensagem,
            User usuario, Category categorias, G categoria, Warnings aviso) {
        this.id = id;
        this.nome = nome;
        this.ra = ra;
        this.senha = senha;
        this.operacao = operacao;
        this.token = token;
        this.mensagem = mensagem;
        this.usuario = usuario;
        this.categorias = categorias;
        this.categoria = categoria;
        this.aviso = aviso;
    }

    public Json() {
        this.operacao = "";
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

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Category getCategorias() {
        return categorias;
    }

    public void setCategorias(Category categorias) {
        this.categorias = categorias;
    }

    public G getCategoria() {
        return categoria; 
    }

    public void setCategoria(G categoria) {
        this.categoria = categoria;
    }

    public Warnings getAvisos() {
        return aviso;
    }

    public void setAvisos(Warnings aviso) {
        this.aviso = aviso;
    }

    @Override
    public String toString() {
        return "Json{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", ra='" + ra + '\'' +
                ", senha='" + senha + '\'' +
                ", operacao='" + operacao + '\'' +
                ", token='" + token + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", usuarios=" + usuario +
                ", categorias=" + categorias +
                ", categoria=" + categoria +
                ", avisos=" + aviso +
                '}';
    }
}
