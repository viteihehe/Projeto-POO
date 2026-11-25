package UI;

import java.util.Scanner;

import entities.Bibliotecario;
import entities.Membro;
import interfaces.Cargo;
import service.BuscarAPI;

public class TelaLogin extends TelaBase {

    private static final int MAX_TENTATIVAS = 4;
    private String login;

    public TelaLogin(Scanner sc) {
        super(sc);
    }

    @Override
    public void mostrar() {
        titulo("LOGIN");

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS; tentativa++) {
            this.login = lerTexto("Login");
            String senha = lerSenha("Senha");
            int tipo;

            Boolean logado = Membro.autenticar(login, senha);
            Boolean logado2 = Bibliotecario.autenticar(login, senha);

            if (logado || logado2) {
                if (logado) {
                    tipo = 2;
                } else {
                    tipo = 1;
                }
                System.out.println("\nBem-vindo !");
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
            Bibliotecario bibli = (Bibliotecario) (Object) BuscarAPI.buscar(this.login, TelaBase.getCliente(), "/bibliotecario/buscar", (Class) Bibliotecario.class);
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
