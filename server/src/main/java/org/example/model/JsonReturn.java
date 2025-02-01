package org.example.model;

import java.util.List;

public class JsonReturn {
 
    private int status;
    private String token;
    private String mensagem;
    private String operacao;
    private List<User> usuarios;
    private List<Category> categorias;
    private List<Warnings> avisos;

    public JsonReturn(){
        this.status = 0;
        this.token = "";
        this.mensagem = "";
        this.operacao = "";
        this.usuarios = null;
        this.categorias = null;
        this.avisos = null;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setOperation(String operacao) {
        this.operacao = operacao;
    }

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return mensagem;
    }

    public String getOperation() {
        return operacao;
    }

    public void setUser(List<User> user) {
        this.usuarios = user;
    }

    public List<User> getUser() {
        return usuarios;
    }

    public void setCategory(List<Category> category) {
        this.categorias = category;
    }

    public List<Category> getCategory() {
        return categorias;
    }

    public void setWarning(List<Warnings> warning) {
        this.avisos = warning;
    }

    public List<Warnings> getWarning() {
        return avisos;
    }
    
    @Override
    public String toString() {
        switch (this.operacao) {
            case "cadastrarUsuario":
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
            case "login":
                if (this.status == 200) {
                    return "{status=" + status +
                            ", token=" + '\'' + token + '\'' +
                            '}';
                } else {
                    return "{status=" + status +
                            ", operacao=" + '\'' + operacao + '\'' +
                            ", mensagem='" + '\'' + mensagem + '\'' +
                            '}';
                }
            case "logout":
                if (this.status == 200) {
                    return "{status=" + status + '}';
                }else{
                    return "{status=" + status +
                            ", operacao=" + '\'' + operacao + '\'' +
                            ", mensagem=" + '\'' + mensagem + '\'' +
                            '}';
                }
            case "listarCategorias":
                if (status != 200) {
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
                }
                String returnToString;
                returnToString = "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\''
                        + ", categorias=[";
                if (categorias == null) {
                    returnToString += "]}";
                    return returnToString;
                }
                if (categorias.isEmpty()) {
                    returnToString += "]}";
                    return returnToString;
                }
                for (Category category : this.categorias) {
                    if (category.equals(this.categorias.get(this.categorias.size() - 1))) {
                        returnToString += category.toString() + "]}";
                    } else {
                        returnToString += category.toString() + ", ";
                    }
                }
                return returnToString;
            case "localizarCategoria":
            if (status != 200) {
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
                }
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", categorias=" + categorias.toString() +
                        '}';
            case "listarAvisos":
                if (status != 200) {
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
                }
                String returnToStringWarnings;
                returnToStringWarnings = "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\''
                        + ", avisos=[";
                if (avisos == null) {
                    returnToStringWarnings += "]}";
                    return returnToStringWarnings;
                }
                if (avisos.isEmpty()) {
                    returnToStringWarnings += "]}";
                    return returnToStringWarnings;
                }
                for (Warnings warning : this.avisos) {
                    if (warning.equals(this.avisos.get(this.avisos.size() - 1))) {
                        returnToStringWarnings += warning.toString() + "]}";
                    } else {
                        returnToStringWarnings += warning.toString() + ", ";
                    }
                }
                return returnToStringWarnings;
            case "cadastrarUsuarioCategoria":
                if (status != 200) {
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
                }
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
            case "descadastrarUsuarioCategoria":
                if (status != 200) {
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
                }
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
            default:
                if (status != 200) {
                return "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
                }
                return  "{status=" + status + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
        }
        // case "listarAvisos": case "cadastrarUsuarioCategoria": case "descadastrarUsuarioCategoria":
    }
}