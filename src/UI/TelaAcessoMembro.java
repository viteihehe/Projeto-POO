package UI;

import java.util.List;
import java.util.Scanner;

import entities.Emprestimo;
import entities.Livro;
import entities.Membro;

public class TelaAcessoMembro extends TelaBase {

    private final Membro membro;

    public TelaAcessoMembro(Scanner sc, Membro membro) {
        super(sc);
        this.membro = membro;
    }

    @Override
    public void mostrar() {
        boolean logado = true;

        while (logado) {
            titulo("ÁREA DO MEMBRO - " + membro.getNome());
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Consultar livros");
            System.out.println("3 - Meus empréstimos");
            System.out.println("4 - Sair (logout)");

            switch (lerOpcao()) {
                case 1 ->
                    verMeusDados();
                case 2 ->
                    consultarLivros();
                case 3 ->
                    verMeusEmprestimos();
                case 4 -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void verMeusDados() {
        titulo("MEUS DADOS");
        membro.verMembro();
    }

    private void consultarLivros() {
        titulo("ACERVO");

        try {
            List<Livro> livros = Livro.listarLivros();

            if (livros.isEmpty()) {
                System.out.println("Nenhum livro cadastrado.");
                return;
            }
            for (Livro livro : livros) {
                livro.verLivro();
            }
        } catch (Exception e) {
            System.out.println("Não foi possível consultar os livros: " + e.getMessage());
        }
    }

    private void verMeusEmprestimos() {
        titulo("MEUS EMPRÉSTIMOS");

        try {
            List<Emprestimo> emprestimos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);

            for (Emprestimo e : emprestimos) {
                e.verEmprestimo();
            }
            if (emprestimos.isEmpty()) {
                System.out.println("Você não possui empréstimos.");
            }
        } catch (Exception e) {
            System.out.println("Não foi possível consultar os empréstimos: " + e.getMessage());
        }
    }
}
