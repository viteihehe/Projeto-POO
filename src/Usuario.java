import java.util.List;
import java.util.ArrayList;

public abstract class Usuario {
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;
    
    public void verUsuario(){}
    public void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, boolean ativo){}
    public void desativarUsuario(Usuario usuario){}
    public boolean autenticar(String login, String senha){
        return ativo;
    }
    public List<Usuario>listarUsuarios(){
        List<Usuario> lista = new ArrayList<>();
        return lista;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


    
}
