import java.util.List;
import java.util.ArrayList;

public abstract class Usuario {
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;
    
    public void verUsuario(){}
    public void editarUsuario(Usuario user, String nome, String cpf, String login, String senha, boolean ativo){}
    public void desativarUsuario(Usuario user){}
    public boolean autenticar(String login, String senha){
        return ativo;
    }
    public List<Usuario>listarUsuarios(){
        List<Usuario> lista = new ArrayList<>();
        return lista;
    }

}
