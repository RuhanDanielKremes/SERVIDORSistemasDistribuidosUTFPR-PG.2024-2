package org.example.model;

import java.util.List;

public class JsonReturnCategory2{
 
        private int status;
        private String token;
        private String mensagem;
        private String operacao;
        private List<User> usuarios;
        private Category categoria;
        private List<Avisos> avisos;
        private User usuario;

        public JsonReturnCategory2(){
            this.status = 0;
            this.token = null;
            this.mensagem = null;
            this.operacao = null;
            this.usuarios = null;
            this.categoria = null;
            this.avisos = null;
            this.usuario = null;
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

        public void setUsers(List<User> user) {
            this.usuarios = user;
        }

        public List<User> getUsers() {
            return usuarios;
        }

        public void setCategory(Category  category) {
            this.categoria = category;
        }

        public Category getCategory() {
            return categoria;
        }

        public void setWarning(List<Avisos> warning) {
            this.avisos = warning;
        }

        public List<Avisos> getWarning() {
            return avisos;
        }

        public void setUser(User user) {
            this.usuario = user;
        }

        public User getUser() {
            return usuario;
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
                            + ", categorias=" + categoria.toString() + "}";
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
                            ", categoria=" + categoria.toString() +
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
                    for (Avisos warning : this.avisos) {
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
                case "listarUsuarios":
                    if (status != 201) {
                        return "(status=" + status +
                                ", operacao=" + '\'' + operacao + '\'' +
                                ", mensagem=" + '\'' + mensagem + '\'' +
                                '}';
                    }
                    return "{status=" + status +
                            ", operacao=" + '\'' + operacao + '\'' +
                            ", usuarios=" + usuarios.toString() +
                            '}';
                case "localizarUsuario":
                    if (status != 201) {
                        return "{status=" + status +
                                ", operacao=" + '\'' + operacao + '\'' +
                                ", mensagem=" + '\'' + mensagem + '\'' +
                                '}';
                    }
                    return "{status=" + status +
                            ", operacao=" + '\'' + operacao + '\'' +
                            ", usuario=" + usuario.toString() +
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
        }
}
