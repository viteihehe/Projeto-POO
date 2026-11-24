package UI;

import java.util.Scanner;

import entities.Bibliotecario;

public class TelaAcessoBibliotecarioSuperior extends TelaAcessoBibliotecarioAtendente {

    private final Bibliotecario bibliotecario;

    public TelaAcessoBibliotecarioSuperior(Scanner sc, Bibliotecario bibliotecario) {
        super(sc, bibliotecario);
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
            System.out.println("5 - Cadastrar Livro");
            System.out.println("6 - Gerenciar livros");
            System.out.println("7 - Listar livro");
            System.out.println("8 - Cadastrar empréstimo");
            System.out.println("9 - Gerenciar empréstimo");
            System.out.println("10 - Listar Emprestimos");
            System.out.println("11 - Criar membro");
            System.out.println("12 - Editar membro");
            System.out.println("13 - Criar Bibliotecario");
            System.out.println("14 - Sair (logout)");

            switch (lerOpcao()) {
                case 1 ->
                    super.verMeusDados();
                case 2 ->
                    super.listarMembros(false);
                case 3 ->
                    super.listarMembros(true);
                case 4 ->
                    super.suspenderMembro();
                case 5 ->
                    super.cadastrarLivro();
                case 6 ->
                    super.gerenciarLivro();
                case 7 ->
                    super.consultarLivros();
                case 8 ->
                    super.cadastrarEmprestimo();
                case 9 ->
                    super.gerenciarEmprestimo();
                case 10 ->
                    super.listarEmprestimos();
                case 11 ->
                    new TelaCadastroMembro(sc).mostrar();
                case 12 ->
                    editarMembro();
                case 13 ->
                    new TelaCadastroBibliotecario(sc).mostrar();
                case 14 -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }
    }
}
