package UI;

import java.util.Scanner;

import entities.Bibliotecario;
import entities.Membro;
import service.BuscarAPI;
import service.EditarApi;

public class TelaAcessoBibliotecarioAtendente extends TelaAcessoBibliotecarioEstagiario {

    private final Bibliotecario bibliotecario;

    public TelaAcessoBibliotecarioAtendente(Scanner sc, Bibliotecario bibliotecario) {
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
            System.out.println("5 - Ativar membro");
            System.out.println("6 - Cadastrar Livro");
            System.out.println("7 - Gerenciar livros");
            System.out.println("8 - Listar livro");
            System.out.println("9 - Cadastrar empréstimo");
            System.out.println("10 - Gerenciar empréstimo");
            System.out.println("11 - Listar Emprestimos");
            System.out.println("12 - Criar membro");
            System.out.println("13 - Editar membro");
            System.out.println("14 - Sair (logout)");

            switch (lerOpcao()) {
                case 1 ->
                    super.verMeusDados();
                case 2 ->
                    super.exibirMembros(false);
                case 3 ->
                    super.exibirMembros(true);
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
                case 14 -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }
    }

    protected void editarMembro() {
        String login = lerTexto("Login");
        if (login.isBlank()) {
            throw new IllegalArgumentException("O login é obrigatório");
        }

        Membro membro = (Membro) (Object) BuscarAPI.buscar(login, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);

        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }
        editarMembro(membro);
    }

    protected void editarMembro(Membro membro) {
        boolean loop = true;

        while (loop) {
            System.out.println("Escolha a opção para alterar:");
            System.out.println("1 - Nome");
            System.out.println("2 - CPF");
            System.out.println("3 - Email");
            System.out.println("4 - Telefone");
            System.out.println("5 - Senha");
            System.out.println("6 - Sair");

            int op = lerOpcao();
            switch (op) {
                case 1 -> {
                    String nome = lerTexto("Nome");
                    if (nome.isBlank()) {
                        throw new IllegalArgumentException("O nome é obrigatório");
                    }
                    membro.setNome(nome);
                }
                case 2 -> {
                    String cpf = lerTexto("CPF");
                    if (cpf.isBlank()) {
                        throw new IllegalArgumentException("O CPF é obrigatório");
                    }
                    membro.setCpf(cpf);
                }
                case 3 -> {
                    String email = lerTexto("Email");
                    if (email.isBlank()) {
                        throw new IllegalArgumentException("O email é obrigatório");
                    }
                    membro.setEmail(email);
                }
                case 4 -> {
                    String telefone = lerTexto("Telefone");
                    if (telefone.isBlank()) {
                        throw new IllegalArgumentException("O telefone é obrigatório");
                    }
                    membro.setTelefone(telefone);
                }
                case 5 -> {
                    String senha = lerTexto("Senha");
                    if (senha.isBlank()) {
                        throw new IllegalArgumentException("A senha é obrigatória");
                    }
                    membro.setSenha(senha);
                }
                case 6 -> {
                    System.out.println("Fim da edição.");
                    loop = false;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }

        Membro.editarMembro(membro, membro.getNome(), membro.getCpf(), membro.getLogin(), membro.getSenha(), membro.isAtivo(), membro.getEndereco(), membro.getTelefone(), membro.getEmail());
        String ret = EditarApi.editarNaAPI(membro, TelaBase.getCliente(), "/membro/atualizar");
        System.out.println(ret);
    }
}
