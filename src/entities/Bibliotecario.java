package entities;

import java.util.ArrayList;
import java.util.List;

import UI.TelaAcessoMembro;
import UI.TelaSistemaBiblioteca;
import service.BuscarAPI;
import service.DadosLogin;
import service.ListarAPI;

public class Bibliotecario extends Usuario {

    private String cargo;
    private static final List<Bibliotecario> bibliotecarios = new ArrayList<>();

    public Bibliotecario(String nome, String cpf, String login, String senha, String cargo) {
        super(nome, cpf, login, senha);
        this.cargo = cargo;
        bibliotecarios.add(this);
    }

    public Bibliotecario() {
        super();
    }

    public static Bibliotecario criarBibliotecario(String nome, String cpf, String login, String senha, String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("Cargo invalido");
        }
        Bibliotecario b = new Bibliotecario(nome, cpf, login, senha, cargo);
        return b;
    }

    public static boolean autenticar(String login, String senha) {
        DadosLogin dados = new DadosLogin(login, senha);
        return BuscarAPI.login(dados, TelaSistemaBiblioteca.getCliente(), "/bibliotecario/login");
    }

    public static void editarBibliotecario(Bibliotecario bibliotecario, String nome, String cpf, String login, String senha, String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("Cargo invalido, alteração não realizada");
        }
        Usuario.editarUsuario(bibliotecario, nome, cpf, login, senha, bibliotecario.isAtivo());
        bibliotecario.setCargo(cargo);
    }

    public void verBibliotecario() {
        System.out.println(this);
    }

    public static List<Bibliotecario> listarBibliotecarios() {
        return ListarAPI.listar(TelaAcessoMembro.getCliente(), "/bibliotecario", Bibliotecario.class);
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return super.toString() + "Cargo:" + cargo;
    }

}
