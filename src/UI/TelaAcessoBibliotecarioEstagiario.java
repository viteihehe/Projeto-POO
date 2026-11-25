package UI;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import entities.Bibliotecario;
import entities.Emprestimo;
import entities.Livro;
import entities.Membro;
import service.BuscarAPI;
import service.DadosBuscaEmp;
import service.DadosEmp;
import service.DadosEmp.EmprestimoRequest;
import service.DadosLogin;
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
            System.out.println("4 - Desativar membro");
            System.out.println("5 - Ativar membro");
            System.out.println("6 - Cadastrar Livro");
            System.out.println("7 - Gerenciar livros");
            System.out.println("8 - Listar livro");
            System.out.println("9 - Cadastrar empréstimo");
            System.out.println("10 - Gerenciar empréstimo");
            System.out.println("11 - Listar Emprestimos");
            System.out.println("12 - Sair (logout)");

            switch (lerOpcao()) {
                case 1 ->
                    verMeusDados();
                case 2 ->
                    exibirMembros(false);
                case 3 ->
                    exibirMembros(true);
                case 4 ->
                    desativarMembro();
                case 5 ->
                    ativarMembro();
                case 6 ->
                    cadastrarLivro();
                case 7 ->
                    gerenciarLivro();
                case 8 ->
                    consultarLivros();
                case 9 ->
                    cadastrarEmprestimo();
                case 10 ->
                    gerenciarEmprestimo();
                case 11 ->
                    listarEmprestimos();
                case 12 -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }
    }

    protected void cadastrarLivro() {

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

        System.out.println("Ano de publicação:");
        int anoPublicacao = lerInteiro();
        if (anoPublicacao < 0) {
            throw new IllegalArgumentException("O ano de publicação é obrigatório e deve ser positivo");
        }
        System.out.println("Numero de cópias:");
        int numeroCopias = lerInteiro();
        if (numeroCopias < 0) {
            throw new IllegalArgumentException("O numero de copias é obrigatório e deve ser positivo");
        }

        Livro livros = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias);
        String retorno = InserirApi.adicionarAPI(livros, TelaSistemaBiblioteca.getCliente(), "/Livro");
        System.out.println(retorno);
    }

    protected void verMeusDados() {
        titulo("MEUS DADOS");
        bibliotecario.verBibliotecario();
    }

    protected void consultarLivros() {
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

    protected void exibirMembros(boolean somenteSuspensos) {
        titulo(somenteSuspensos ? "MEMBROS SUSPENSOS" : "MEMBROS CADASTRADOS");

        List<Membro> lista;
        if (somenteSuspensos) {
            lista = Membro.listarMembrosSuspensos();
        } else {
            lista = Membro.listarMembros();
        }

        if (lista == null) {
            System.out.println("Nenhum membro encontrado.");
            return;
        }

        for (Membro m : lista) {
            m.verMembro();
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
                    Livro.excluirLivro(livro);
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
        if (login.isBlank()) {
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
        DadosBuscaEmp dados = new DadosBuscaEmp(LocalDate.parse(dataEmp), membro, livro);
        Emprestimo emp = (Emprestimo) (Object) BuscarAPI.buscar(login, TelaBase.getCliente(), "/emprestimo/buscar", (Class) Emprestimo.class);

        int op;
        boolean loop = true;
        while (loop) {
            System.out.println("1 - Editar");
            System.out.println("2 - Deletar");
            System.out.println("3 - Sair");
            op = lerOpcao();

            switch (op) {
                case 1:
                    editarEmprestimo(emp, membro, livro);
                    break;
                case 2:
                    Emprestimo.excluirEmprestimo(emp);
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
    protected void desativarMembro() {
        titulo("DESATIVAR MEMBRO");
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void ativarMembro() {
        titulo("ATIVAR MEMBRO");
        String login = lerTexto("Login do membro");

        Membro membro = (Membro) (Object) BuscarAPI.buscar(login, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        if (membro.isAtivo()) {
            System.out.println("Este membro já está ativo.");
            return;
        }

        Membro.editarMembro(membro, membro.getNome(), membro.getCpf(), login, null, true, membro.getEndereco(), membro.getTelefone(), membro.getEmail());
        String ret = EditarApi.editarNaAPI(membro, TelaBase.getCliente(), "/membro/atualizar");
        System.out.println(ret);
        System.out.println("Membro Ativo: " + membro.getNome());
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
                        throw new IllegalArgumentException("O titulo é obrigatório");
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
                    System.out.println("Ano de publicação:");
                    int anoPublicacao = lerInteiro();
                    if (anoPublicacao < 0) {
                        throw new IllegalArgumentException("O ano de publicação é obrigatório e deve ser positivo");
                    }
                    livro.setAnoPublicacao(anoPublicacao);
                }
                case 4 -> {
                    System.out.println("Numero de cópias:");
                    int numeroCopias = lerInteiro();
                    if (numeroCopias < 0) {
                        throw new IllegalArgumentException("O numero de copias é obrigatório e deve ser positivo");
                    }
                    System.out.println("Selecione a ação:");
                    System.out.println("1 - Adicionar");
                    System.out.println("2 - Remover");
                    int temp = lerOpcao();
                    switch (temp) {
                        case 1:
                            livro.adicionarCopias(numeroCopias);
                            break;
                        case 2:
                            livro.removerCopias(numeroCopias);
                            break;
                        default:
                            throw new AssertionError();
                    }
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
        Livro.editarLivro(livro.getIsbn(), livro.getTitulo(), livro.getAutor(), livro.getAnoPublicacao(), livro.getNumeroCopias(), livro.getDisponiveis());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void cadastrarEmprestimo() {

        int tentativas = 4;

        while (tentativas >= 0) {
            String login = lerTexto("Login");
            if (login.isBlank()) {
                throw new IllegalArgumentException("O login é obrigatório");
            }
            String senha = lerTexto("Senha");
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
                if (emprestimo == null) {
                    return;
                }
                EmprestimoRequest empenv = new EmprestimoRequest(
                        emprestimo.isDevolvido(),
                        emprestimo.getDataEmprestimo(),
                        emprestimo.getDataDevolucaoReal(),
                        emprestimo.getDataDevolucaoPrevista()
                );
                DadosEmp dadosEmp = new DadosEmp(empenv, membro, livro);

                String retorno = InserirApi.adicionarAPI(dadosEmp, TelaBase.getCliente(), "/emprestimo");
                System.out.println(retorno);
                String ret = EditarApi.editarNaAPI(livro, TelaBase.getCliente(), "/Livro/atualizar");
                System.out.println(retorno);
                return;
            }
        }
    }

    protected void editarEmprestimo(Emprestimo emp, Membro membro, Livro livro) {
        int op;
        boolean loop = true;

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
                    if (emp.isDevolvido()) {
                        livro.devolverCopia();
                    } else {
                        livro.emprestarCopia();
                    }
                }
                case 5 -> {
                    System.out.println("Fim da edição.");
                    loop = false;
                }
                default ->
                    throw new AssertionError();
            }

        }
        EmprestimoRequest empenv = new EmprestimoRequest(
                emp.isDevolvido(),
                emp.getDataEmprestimo(),
                emp.getDataDevolucaoReal(),
                emp.getDataDevolucaoPrevista()
        );
        DadosEmp dadosEmp = new DadosEmp(empenv, membro, livro);
        String ret = EditarApi.editarNaAPI(dadosEmp, TelaBase.getCliente(), "/emprestimo/atualizar");
        Livro.editarLivro(livro.getIsbn(), livro.getTitulo(), livro.getAutor(), livro.getAnoPublicacao(), livro.getNumeroCopias(), livro.getDisponiveis());
        System.out.println(ret);
    }

    protected void listarEmprestimos() {
        titulo("EMPRÉSTIMOS");
        System.out.println("Escolha a opção para listar:");
        System.out.println("1 - Todos");
        System.out.println("2 - Ativos");
        System.out.println("3 - Atrasados");
        System.out.println("4 - Ativos por membro");
        System.out.println("5 - Atrasados por membro");
        int op = lerOpcao();
        List<Emprestimo> emprestimos;

        switch (op) {
            case 1:
                emprestimos = Emprestimo.listarEmprestimos();
                break;
            case 2:
                emprestimos = Emprestimo.listarEmprestimosAtivos();
                break;
            case 3:
                emprestimos = Emprestimo.listarEmprestimosAtrasados();
                break;
            case 4:
                String login = lerTexto("Login");
                if (login.isBlank()) {
                    throw new IllegalArgumentException("O login é obrigatório");
                }
                Membro membro = (Membro) (Object) BuscarAPI.buscar(login, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);
                if (membro == null) {
                    System.out.println("Membro não encontrado.");
                    return;
                }
                emprestimos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);
                break;
            case 5:
                String login1 = lerTexto("Login");
                if (login1.isBlank()) {
                    throw new IllegalArgumentException("O login é obrigatório");
                }
                Membro membro1 = (Membro) (Object) BuscarAPI.buscar(login1, TelaBase.getCliente(), "/membro/buscar", (Class) Membro.class);
                if (membro1 == null) {
                    System.out.println("Membro não encontrado.");
                    return;
                }
                emprestimos = Emprestimo.listarEmprestimosAtrasadosPorMembro(membro1);
                break;
            default:
                throw new AssertionError();
        }

        try {
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
