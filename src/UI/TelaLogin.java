package UI;

import java.util.List;
import java.util.Scanner;
import entities.Bibliotecario;
import entities.Membro;
import entities.Usuario;

public class TelaLogin extends TelaBase {

    private static final int MAX_TENTATIVAS = 4;

    private final List<Usuario> usuarios;

    public TelaLogin(Scanner sc, List<Usuario> usuarios) {
        super(sc);
        this.usuarios = usuarios;
    }

    @Override
    public void mostrar() {
        titulo("LOGIN");

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS; tentativa++) {
            String login = lerTexto("Login");
            String senha = lerSenha("Senha");

            Usuario logado = autenticar(login, senha);
            if (logado != null) {
                System.out.println("\nBem-vindo, " + logado.getNome() + "!");
                abrirAreaDoUsuario(logado);
                return;
            }

            System.out.println("Login ou senha incorretos");
        }
        System.out.println("Número máximo de tentativas atingido. Voltando a tela inicial");
    }

    private Usuario autenticar(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.autenticar(login, senha)) {
                return u;
            }
        }
        return null;
    }

    private void abrirAreaDoUsuario(Usuario usuario) {
        if (usuario instanceof Bibliotecario bibliotecario) {
            new TelaAcessoBibliotecario(sc, usuarios, bibliotecario).mostrar();
        } else if (usuario instanceof Membro membro) {
            new TelaAcessoMembro(sc, membro).mostrar();
        }
    }
}
