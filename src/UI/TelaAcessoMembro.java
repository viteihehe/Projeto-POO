package UI;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import entities.Emprestimo;
import entities.Livro;
import entities.Membro;
import service.ListarAPI;

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
        System.out.println("Nome    : " + membro.getNome());
        System.out.println("CPF     : " + membro.getCpf());
        System.out.println("Login   : " + membro.getLogin());
        System.out.println("E-mail  : " + membro.getEmail());
        System.out.println("Telefone: " + membro.getTelefone());
        System.out.println("Endereço: " + membro.getEndereco());
    }

    private void consultarLivros() {
        titulo("ACERVO");

        try {
            List<Livro> livros = ListarAPI.listar(TelaBase.getCliente(), "/livro", Livro.class);

            if (livros.isEmpty()) {
                System.out.println("Nenhum livro cadastrado.");
                return;
            }
            for (Livro livro : livros) {
                System.out.println(livro.getTitulo() + " - " + livro.getAutor()
                        + " (" + livro.getAnoPublicacao() + ")"
                        + " | ISBN: " + livro.getIsbn()
                        + " | Disponíveis: " + livro.getDisponiveis() + "/" + livro.getNumeroCopias());
            }
        } catch (Exception e) {
            System.out.println("Não foi possível consultar os livros: " + e.getMessage());
        }
    }

    private void verMeusEmprestimos() {
        titulo("MEUS EMPRÉSTIMOS");
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        try {
            List<Emprestimo> emprestimos = ListarAPI.listarPorMembro(TelaBase.getCliente(), "/emprestimo/membro", membro);
            boolean encontrou = false;

            for (Emprestimo e : emprestimos) {
                if (!e.getMembro().getCpf().equals(membro.getCpf())) {
                    continue;
                }
                encontrou = true;
                String situacao = e.isDevolvido()
                        ? "Devolvido em " + formato.format(e.getDataDevolucaoReal())
                        : "Devolução prevista: " + formato.format(e.getDataDevolucaoPrevista());
                System.out.println(e.getLivro().getTitulo()
                        + " | Emprestado em " + formato.format(e.getDataEmprestimo())
                        + " | " + situacao);
                if (e.calcularMulta() > 0) {
                    System.out.println("  Atraso: " + e.calcularMulta() + " dia(s) de suspensão");
                }
            }
            if (!encontrou) {
                System.out.println("Você não possui empréstimos.");
            }
        } catch (Exception e) {
            System.out.println("Não foi possível consultar os empréstimos: " + e.getMessage());
        }
    }
}
