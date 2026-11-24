package UI;

import java.util.Scanner;

import entities.Bibliotecario;
import entities.Membro;
import interfaces.Cargo;
import service.BuscarAPI;
import service.DadosLogin;

public class TelaLogin extends TelaBase {

    private static final int MAX_TENTATIVAS = 4;

    private int tipo;
    private String login;

    public TelaLogin(Scanner sc, int tipo) {
        super(sc);
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    @Override
    public void mostrar() {
        titulo("LOGIN");

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS; tentativa++) {
            String login = lerTexto("Login");
            String senha = lerSenha("Senha");

            DadosLogin dados = new DadosLogin(login, senha);
            String endereco;
            if (getTipo() == 2) {
                endereco = "/membro/login";
            } else {
                endereco = "/bibliotecario/login";
            }
            Boolean logado = BuscarAPI.login(dados, TelaSistemaBiblioteca.getCliente(), endereco);

            if (logado) {
                System.out.println("\nBem-vindo !");
                this.login = login;
                abrirAreaDoUsuario(tipo);
                return;
            }

            System.out.println("Login ou senha incorretos");
        }
        System.out.println("Número máximo de tentativas atingido. Voltando a tela inicial");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void abrirAreaDoUsuario(int tipo) {
        if (tipo == 1) {
            Bibliotecario bibli = (Bibliotecario) (Object) BuscarAPI.buscar(this.login, TelaBase.getCliente(), "/bibliotecario/buscar", (Class) Membro.class);
            if (bibli != null) {
                Cargo cargo = Cargo.valueOf(bibli.getCargo());

                switch (cargo) {
                    case SUPERVISOR ->
                        new TelaAcessoBibliotecarioSuperior(sc, bibli).mostrar();
                    case ATENDENTE ->
                        new TelaAcessoBibliotecarioAtendente(sc, bibli).mostrar();
                    case ESTAGIARIO ->
                        new TelaAcessoBibliotecarioEstagiario(sc, bibli).mostrar();
                    default ->
                        throw new AssertionError();
                }
            } else {
                System.out.println("Houve um problema com o banco de dados!");
            }
        } else if (tipo == 2) {
            Membro membro = (Membro) (Object) BuscarAPI.buscar(this.login, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);
            if (membro != null) {
                new TelaAcessoMembro(sc, membro).mostrar();
            } else {
                System.out.println("Houve um problema com o banco de dados!");
            }
        }
    }
}
