package UI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import entities.Bibliotecario;
import entities.Emprestimo;
import entities.Livro;
import entities.Membro;
import service.BuscarAPI;
import service.DadosBuscaEmp;
import service.DadosEmp;
import service.DadosLogin;
import service.DeletarAPI;
import service.EditarApi;
import service.InserirApi;
import service.ListarAPI;

public class TelaAcessoBibliotecarioEstagiario extends TelaBase {

    private final Bibliotecario bibliotecario;

    public TelaAcessoBibliotecarioEstagiario(Scanner sc, Bibliotecario bibliotecario) {
        super(sc);
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
            System.out.println("11 - Sair (logout)");

            switch (lerOpcao()) {
                case 1 ->
                    verMeusDados();
                case 2 ->
                    listarMembros(false);
                case 3 ->
                    listarMembros(true);
                case 4 ->
                    suspenderMembro();
                case 5 ->
                    cadastrarLivro();
                case 6 ->
                    gerenciarLivro();
                case 7 ->
                    consultarLivros();
                case 8 ->
                    cadastrarEmprestimo();
                case 9 ->
                    gerenciarEmprestimo();
                case 10 ->
                    listarEmprestimos();
                case 11 -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }
    }

    protected String cadastrarLivro() {

        String titulo = lerTexto("Titulo");
        if (titulo.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório");
        }

        String autor = lerTexto("autor");
        if (autor.isBlank()) {
            throw new IllegalArgumentException("O CPF é obrigatório");
        }

        String isbn = lerTexto("ISBN");
        if (!Livro.validarIsbn(isbn)) {
            throw new IllegalArgumentException("O ibsn é obrigatório, ou sua entrada é invalida");
        }

        int anoPublicacao = lerInteiro();
        if (anoPublicacao < 0) {
            throw new IllegalArgumentException("O ano de publicação é obrigatório e deve ser positivo");
        }

        int numeroCopias = lerInteiro();
        if (numeroCopias < 0) {
            throw new IllegalArgumentException("O numero de copias é obrigatório e deve ser positivo");
        }

        Livro livros = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias);
        String retorno = InserirApi.adicionarAPI(livros, TelaSistemaBiblioteca.getCliente(), "/Livro");
        return retorno;
    }

    protected void verMeusDados() {
        titulo("MEUS DADOS");
        System.out.println("Nome : " + bibliotecario.getNome());
        System.out.println("CPF  : " + bibliotecario.getCpf());
        System.out.println("Login: " + bibliotecario.getLogin());
        System.out.println("Cargo: " + bibliotecario.getCargo());
    }

    protected void consultarLivros() {
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

    protected void listarMembros(boolean somenteSuspensos) {
        titulo(somenteSuspensos ? "MEMBROS SUSPENSOS" : "MEMBROS CADASTRADOS");

        List<Membro> lista = ListarAPI.listar(TelaAcessoMembro.getCliente(), "/membro", Membro.class);

        if (lista.isEmpty()) {
            System.out.println("Nenhum membro encontrado.");
            return;
        }
        for (Membro m : lista) {
            System.out.println(m.getNome() + " | login: " + m.getLogin()
                    + " | " + (m.isAtivo() ? "Ativo" : "Suspenso"));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void gerenciarLivro() {
        titulo("GERENCIAR LIVRO");
        String isbn = lerTexto("ISBN dos livros");

        Livro livro = (Livro) (Object) BuscarAPI.buscar(isbn, TelaBase.getCliente(), "/Livro/buscar", (Class) Livro.class);
        if (livro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        int op;
        boolean loop = true;
        while (loop) {
            System.out.println("1 - Editar");
            System.out.println("2 - Deletar");
            System.out.println("3 - Sair");
            op = lerOpcao();

            switch (op) {
                case 1:
                    editarLivro(livro);
                    break;
                case 2:
                    deletarLivro(isbn);
                    break;
                case 3:
                    System.out.println("Saindo do gerênciamento.");
                    loop = false;
                    break;
                default:
                    throw new AssertionError();
            }
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void gerenciarEmprestimo() {
        titulo("EMPRESTIMO LIVRO");
        String dataEmp = lerData();
        String isbn = lerTexto("ISBN");
        if (isbn.isBlank()) {
            throw new IllegalArgumentException("O ISBN é obrigatório");
        }
        String login = lerTexto("Login");
        if (isbn.isBlank()) {
            throw new IllegalArgumentException("O login é obrigatório");
        }

        Livro livro = (Livro) (Object) BuscarAPI.buscar(isbn, TelaBase.getCliente(), "/Livro/buscar", (Class) Livro.class);
        if (livro == null) {
            System.out.println("Livro não encontrado.");
            return;
        }

        Membro membro = (Membro) (Object) BuscarAPI.buscar(login, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        int op;
        boolean loop = true;
        while (loop) {
            System.out.println("1 - Editar");
            System.out.println("2 - Deletar");
            System.out.println("3 - Sair");
            op = lerOpcao();

            switch (op) {
                case 1:
                    editarEmprestimo(LocalDate.parse(dataEmp), membro, livro);
                    break;
                case 2:
                    deletarEmprestimo(LocalDate.parse(dataEmp), membro, livro);
                    break;
                case 3:
                    System.out.println("Saindo do gerênciamento.");
                    loop = false;
                    break;
                default:
                    throw new AssertionError();
            }
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void suspenderMembro() {
        titulo("SUSPENDER MEMBRO");
        String login = lerTexto("Login do membro");

        Membro membro = (Membro) (Object) BuscarAPI.buscar(login, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        if (!membro.isAtivo()) {
            System.out.println("Este membro já está suspenso.");
            return;
        }
        Membro.desativarUsuario(membro);
        String ret = EditarApi.editarNaAPI(membro, TelaBase.getCliente(), "/membro/atualizar");
        System.out.println(ret);
        System.out.println("Membro suspenso: " + membro.getNome());
    }

    protected void editarLivro(Livro livro) {
        int op;
        boolean loop = true;

        while (loop) {
            System.out.println("Escolha a opção para alterar:");
            System.out.println("1 - Titulo");
            System.out.println("2 - Autor");
            System.out.println("3 - Ano de publicação");
            System.out.println("4 - Numero de copias");
            System.out.println("5 - Disponiveis");
            System.out.println("6 - Sair");
            op = lerOpcao();
            switch (op) {
                case 1 -> {
                    String titulo = lerTexto("Titulo");
                    if (titulo.isBlank()) {
                        throw new IllegalArgumentException("O nome é obrigatório");
                    }
                    livro.setTitulo(titulo);
                }
                case 2 -> {
                    String autor = lerTexto("autor");
                    if (autor.isBlank()) {
                        throw new IllegalArgumentException("O CPF é obrigatório");
                    }
                    livro.setAutor(autor);
                }
                case 3 -> {
                    int anoPublicacao = lerInteiro();
                    if (anoPublicacao < 0) {
                        throw new IllegalArgumentException("O ano de publicação é obrigatório e deve ser positivo");
                    }
                    livro.setAnoPublicacao(anoPublicacao);
                }
                case 4 -> {
                    int numeroCopias = lerInteiro();
                    if (numeroCopias < 0) {
                        throw new IllegalArgumentException("O numero de copias é obrigatório e deve ser positivo");
                    }
                    livro.setNumeroCopias(numeroCopias);
                }
                case 5 -> {
                    int disponiveis = lerInteiro();
                    if (disponiveis < 0) {
                        throw new IllegalArgumentException("A quantidade de disponiveis é obrigatório e deve ser positivo");
                    }
                    livro.setDisponiveis(disponiveis);
                }
                case 6 -> {
                    System.out.println("Fim da edição.");
                    loop = false;
                }
                default ->
                    throw new AssertionError();
            }

        }

        String ret = EditarApi.editarNaAPI(livro, TelaBase.getCliente(), "/Livro/atualizar");
        System.out.println(ret);
    }

    protected void deletarLivro(String isbn) {
        String ret = DeletarAPI.deletar(isbn, TelaBase.getCliente(), "/Livro/deletar");
        System.out.println(ret);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void cadastrarEmprestimo() {

        int tentativas = 4;

        while (tentativas >= 0) {
            String login = lerTexto("Login");
            if (login.isBlank()) {
                throw new IllegalArgumentException("O login é obrigatório");
            }
            String senha = lerTexto("Titulo");
            if (senha.isBlank()) {
                throw new IllegalArgumentException("A senha é obrigatório");
            }
            DadosLogin dados = new DadosLogin(login, senha);
            boolean valido = BuscarAPI.login(dados, TelaBase.getCliente(), "/membro/login");
            if (!valido) {
                System.out.println("Login invalido tente novamente.");
            } else {
                String isbn = lerTexto("ISBN");
                if (login.isBlank()) {
                    throw new IllegalArgumentException("A ISBN é obrigatória");
                }
                Membro membro = (Membro) (Object) BuscarAPI.buscar(login, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);
                Livro livro = (Livro) (Object) BuscarAPI.buscar(isbn, TelaBase.getCliente(), "/Livro/buscar", (Class) Livro.class);
                Emprestimo.setEmprestimos(ListarAPI.listar(TelaBase.getCliente(), "/emprestimo", Emprestimo.class));
                Emprestimo emprestimo = Emprestimo.criarEmprestimo(livro, membro);
                DadosEmp dadosEmp = new DadosEmp(emprestimo, membro, livro);

                String retorno = InserirApi.adicionarAPI(dadosEmp, TelaBase.getCliente(), "/emprestimo");
                System.out.println(retorno);
                String ret = EditarApi.editarNaAPI(livro, TelaBase.getCliente(), "/Livro/atualizar");
                System.out.println(retorno);

                return;
            }
        }
    }

    protected void editarEmprestimo(LocalDate dataEmp, Membro membro, Livro livro) {
        int op;
        boolean loop = true;

        DadosBuscaEmp dados = new DadosBuscaEmp(dataEmp, membro, livro);
        Emprestimo emp = (Emprestimo) (Object) BuscarAPI.buscar(dados, TelaBase.getCliente(), "/emspretimos/buscar", (Class) Emprestimo.class);

        while (loop) {
            System.out.println("Escolha a opção para alterar:");
            System.out.println("1 - Data emprestimo");
            System.out.println("2 - Data de devolução prevista");
            System.out.println("3 - Data devolução real");
            System.out.println("4 - Devolvido(Inverte o valor no emprestimo)");
            System.out.println("5 - Sair");
            op = lerOpcao();
            switch (op) {
                case 1 -> {
                    String data = lerData();
                    emp.setDataEmprestimo(LocalDate.parse(data));
                }
                case 2 -> {
                    String data = lerData();
                    LocalDate date = LocalDate.parse(data);
                    if (!date.isBefore(emp.getDataEmprestimo())) {
                        emp.setDataDevolucaoPrevista(date);
                    } else {
                        System.out.println("A data deve ser posterior a do emprestimo");
                    }
                }
                case 3 -> {
                    String data = lerData();
                    LocalDate date = LocalDate.parse(data);
                    if (!date.isBefore(emp.getDataEmprestimo())) {
                        emp.setDataDevolucaoReal(date);
                    } else {
                        System.out.println("A data deve ser posterior a do emprestimo");
                    }
                }
                case 4 -> {
                    emp.setDevolvido(!emp.isDevolvido());
                }
                case 5 -> {
                    System.out.println("Fim da edição.");
                    loop = false;
                }
                default ->
                    throw new AssertionError();
            }

        }

        DadosEmp dadosEmp = new DadosEmp(emp, membro, livro);
        String ret = EditarApi.editarNaAPI(dadosEmp, TelaBase.getCliente(), "/emprestimo/atualizar");
        System.out.println(ret);
    }

    protected void deletarEmprestimo(LocalDate dataEmp, Membro membro, Livro livro) {
        DadosBuscaEmp dadosEmp = new DadosBuscaEmp(dataEmp, membro, livro);
        String ret = DeletarAPI.deletar(dadosEmp, TelaBase.getCliente(), "/emprestimo/deletar");
        System.out.println(ret);
    }

    protected void listarEmprestimos() {
        titulo("EMPRÉSTIMOS");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            List<Emprestimo> emprestimos = ListarAPI.listar(TelaBase.getCliente(), "/emprestimo", Emprestimo.class);

            if (emprestimos != null) {
                for (Emprestimo emp : emprestimos) {
                    emp.verEmprestimo();
                }
            } else {
                System.out.println("Você não possui empréstimos.");
            }
        } catch (Exception e) {
            System.out.println("Não foi possível consultar os empréstimos: " + e.getMessage());
        }
    }
}
