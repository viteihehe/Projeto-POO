package UI;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import entities.Bibliotecario;
import entities.Membro;
import entities.Usuario;

public class TelaAcessoBibliotecario extends TelaBase {

    private final List<Usuario> usuarios;
    private final Bibliotecario bibliotecario;

    public TelaAcessoBibliotecario(Scanner sc, List<Usuario> usuarios, Bibliotecario bibliotecario) {
        super(sc);
        this.usuarios = usuarios;
        this.bibliotecario = bibliotecario;
    }

    @Override
    public void mostrar() {
        boolean logado = true;

        while (logado) {
            titulo("ÁREA DO BIBLIOTECÁRIO - " + bibliotecario.getNome());
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Listar membros");
            System.out.println("3 - Listar membros suspensos");
            System.out.println("4 - Suspender membro");
            System.out.println("5 - Gerenciar livros");
            System.out.println("6 - Gerenciar empréstimos");
            System.out.println("7 - Sair (logout)");

            switch (lerOpcao()) {
                case 1 -> verMeusDados();
                case 2 -> listarMembros(false);
                case 3 -> listarMembros(true);
                case 4 -> suspenderMembro();
                case 5 -> emDesenvolvimento("Gerenciar livros");
                case 6 -> emDesenvolvimento("Gerenciar empréstimos");
                case 7 -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void verMeusDados() {
        titulo("MEUS DADOS");
        System.out.println("Nome : " + bibliotecario.getNome());
        System.out.println("CPF  : " + bibliotecario.getCpf());
        System.out.println("Login: " + bibliotecario.getLogin());
        System.out.println("Cargo: " + bibliotecario.getCargo());
    }

    private void listarMembros(boolean somenteSuspensos) {
        titulo(somenteSuspensos ? "MEMBROS SUSPENSOS" : "MEMBROS CADASTRADOS");

        List<Membro> lista = buscarMembros().stream()
                .filter(m -> !somenteSuspensos || !m.isAtivo())
                .toList();

        if (lista.isEmpty()) {
            System.out.println("Nenhum membro encontrado.");
            return;
        }
        for (Membro m : lista) {
            System.out.println(m.getNome() + " | login: " + m.getLogin()
                    + " | " + (m.isAtivo() ? "Ativo" : "Suspenso"));
        }
    }

    private void suspenderMembro() {
        titulo("SUSPENDER MEMBRO");
        String login = lerTexto("Login do membro");

        Optional<Membro> encontrado = buscarMembros().stream()
                .filter(m -> m.getLogin().equals(login))
                .findFirst();

        if (encontrado.isEmpty()) {
            System.out.println("Membro não encontrado.");
            return;
        }

        Membro membro = encontrado.get();
        if (!membro.isAtivo()) {
            System.out.println("Este membro já está suspenso.");
            return;
        }
        Membro.desativarUsuario(membro);
        System.out.println("Membro suspenso: " + membro.getNome());
    }

    private List<Membro> buscarMembros() {
        return usuarios.stream()
                .filter(Membro.class::isInstance)
                .map(Membro.class::cast)
                .toList();
    }
}