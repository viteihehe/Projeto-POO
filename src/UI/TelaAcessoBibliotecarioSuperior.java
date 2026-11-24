package UI;

import java.util.List;
import java.util.Scanner;

import entities.Bibliotecario;
import service.ListarAPI;

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
            System.out.println("4 - Desativar membro");
            System.out.println("4 - Ativar membro");
            System.out.println("6 - Cadastrar Livro");
            System.out.println("7 - Gerenciar livros");
            System.out.println("8 - Listar livro");
            System.out.println("9 - Cadastrar empréstimo");
            System.out.println("10 - Gerenciar empréstimo");
            System.out.println("11 - Listar Emprestimos");
            System.out.println("12 - Criar membro");
            System.out.println("13 - Editar membro");
            System.out.println("14 - Criar Bibliotecario");
            System.out.println("15 - Listar Bibliotecario");
            System.out.println("16 - Sair (logout)");

            switch (lerOpcao()) {
                case 1 ->
                    super.verMeusDados();
                case 2 ->
                    super.listarMembros(false);
                case 3 ->
                    super.listarMembros(true);
                case 4 ->
                    super.desativarMembro();
                case 5 ->
                    super.ativarMembro();
                case 6 ->
                    super.cadastrarLivro();
                case 7 ->
                    super.gerenciarLivro();
                case 8 ->
                    super.consultarLivros();
                case 9 ->
                    super.cadastrarEmprestimo();
                case 10 ->
                    super.gerenciarEmprestimo();
                case 11 ->
                    super.listarEmprestimos();
                case 12 ->
                    new TelaCadastroMembro(sc).mostrar();
                case 13 ->
                    editarMembro();
                case 14 ->
                    new TelaCadastroBibliotecario(sc).mostrar();
                case 15 ->
                    listarBibliotecario();
                case 16 -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void listarBibliotecario() {
        titulo("BIBLIOTECARIO CADASTRADOS");

        List<Bibliotecario> lista = ListarAPI.listar(TelaAcessoMembro.getCliente(), "/bibliotecario", Bibliotecario.class);

        if (lista.isEmpty()) {
            System.out.println("Nenhum bibliotecario encontrado.");
            return;
        }

        for (Bibliotecario b : lista) {
            b.verBibliotecario();
        }
    }
}
