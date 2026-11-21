package  entities;
import java.util.List;

public class Bibliotecario extends Usuario {
private String cargo;

    public Bibliotecario(String nome, String cpf, String login, String senha, String cargo) {
    super(nome, cpf, login, senha);
    this.cargo = cargo;
}

    public Bibliotecario criarBibliotecario(String nome, String cpf, String login, String senha, String cargo){
        return criarBibliotecario(nome, cpf, login, senha, cargo);
    }

    public void editarBibliotecario(Bibliotecario bibliotecario, String nome, String cpf, String login, String senha, String cargo){
    this.editarBibliotecario(bibliotecario, nome, cpf, login, senha, cargo);
    }

    public void verBibliotecario(){
    }

    public List<Bibliotecario> listarBibliotecarios(){
        return listarBibliotecarios();
    }


    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override 
    public String toString(){
        return super.toString() + " Cargo:" + cargo;
    }


}
