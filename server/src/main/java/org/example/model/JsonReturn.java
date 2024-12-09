package org.example.model;

public class JsonReturn {
 
    private int status;
    private String token;
    private String mensagem;
    private String operacao;

    public JsonReturn(){
        this.status = 0;
        this.token = "";
        this.mensagem = "";
        this.operacao = "";
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
    
    @Override
    public String toString() {
        switch (this.operacao) {
            case "cadastrarUsuario":
                return "{status=" + '\'' + status + '\'' + 
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
            case "login":
                if (this.status == 200) {
                    return "{status=" + '\'' + status +
                            ", token=" + '\'' + token + '\'' +
                            '}';
                } else {
                    return "{status=" + '\'' + status + '\'' +
                            ", operacao=" + '\'' + operacao + '\'' +
                            ", mensagem='" + '\'' + mensagem + '\'' +
                            '}';
                }
            case "logout":
                if (this.status == 200) {
                    return "{status=" + '\'' + status + '\'' + '}';
                }else{
                    return "{status=" + '\'' + status + '\'' +
                            ", operacao=" + '\'' + operacao + '\'' +
                            ", mensagem=" + '\'' + mensagem + '\'' +
                            '}';
                }
            default:
                return  "{status=" + '\'' + status + '\'' +
                        ", operacao=" + '\'' + operacao + '\'' +
                        ", mensagem=" + '\'' + mensagem + '\'' +
                        '}';
        }
    }
}