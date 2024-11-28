package org.example.model;

public class JsonReturn {
 
    private int status;
    private String token;
    private String message;
    private String operation;

    public JsonReturn(){
        this.status = 0;
        this.token = "";
        this.message = "";
        this.operation = "";
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public String getOperation() {
        return operation;
    }
    
    @Override
    public String toString() {
        switch (this.operation) {
            case "cadastrarUsuario":
                return "{status=" + '\'' + status + '\'' + 
                        ", operacao=" + '\'' + operation + '\'' +
                        ", mensagem=" + '\'' + message + '\'' +
                        '}';
            case "login":
                if (this.status == 200) {
                    return "{status=" + '\'' + status +
                            ", token=" + '\'' + token + '\'' +
                            '}';
                } else {
                    return "{status=" + '\'' + status + '\'' +
                            ", operacao=" + '\'' + operation + '\'' +
                            ", mensagem='" + '\'' + message + '\'' +
                            '}';
                }
            case "logout":
                if (this.status == 200) {
                    return "{status=" + '\'' + status + '\'' + '}';
                }else{
                    return "{status=" + '\'' + status + '\'' +
                            ", operacao=" + '\'' + operation + '\'' +
                            ", mensagem=" + '\'' + message + '\'' +
                            '}';
                }
            default:
                return  "{status=" + '\'' + status + '\'' +
                        ", operacao=" + '\'' + operation + '\'' +
                        ", mensagem=" + '\'' + message + '\'' +
                        '}';
        }
    }
}