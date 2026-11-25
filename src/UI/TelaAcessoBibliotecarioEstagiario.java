package UI;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import entities.Bibliotecario;
import entities.Emprestimo;
import entities.Livro;
import entities.Membro;
import service.BuscarAPI;
import service.DadosLogin;
import service.EditarApi;
import service.EmprestimoRequest;
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
            System.out.println("11 - Listar Empréstimos");
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

        String titulo = lerTexto("Título");
        if (titulo.isBlank()) {
            System.out.println("O título é obrigatório.");
            return;
        }

        String autor = lerTexto("Autor");
        if (autor.isBlank()) {
            System.out.println("O autor é obrigatório.");
            return;
        }

        String isbn = lerTexto("ISBN");
        if (!Livro.validarIsbn(isbn)) {
            System.out.println("ISBN inválido.");
            return;
        }

        System.out.println("Ano de publicação:");
        int anoPublicacao = lerInteiro();

        if (anoPublicacao <= 0) {
            System.out.println("O ano de publicação deve ser positivo.");
            return;
        }

        System.out.println("Número de cópias:");
        int numeroCopias = lerInteiro();

        if (numeroCopias <= 0) {
            System.out.println("O número de cópias deve ser positivo.");
            return;
        }

        Livro livro = Livro.criarLivro(
                isbn, titulo, autor, anoPublicacao, numeroCopias
        );

        if (livro == null) {
            System.out.println("Não foi possível criar o livro.");
            return;
        }

        String retorno = InserirApi.adicionarAPI(
                livro, TelaSistemaBiblioteca.getCliente(), "/Livro"
        );

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

            if (livros == null || livros.isEmpty()) {
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

        if (lista == null || lista.isEmpty()) {
            System.out.println("Nenhum membro encontrado.");
            return;
        }

        for (Membro membro : lista) {
            membro.verMembro();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void gerenciarLivro() {
        titulo("GERENCIAR LIVRO");

        String isbn = lerTexto("ISBN do livro");

        if (isbn.isBlank()) {
            System.out.println("O ISBN é obrigatório.");
            return;
        }

        Livro livro = (Livro) (Object) BuscarAPI.buscar(
                isbn,
                TelaBase.getCliente(),
                "/Livro/buscar",
                (Class) Livro.class
        );

        if (livro == null) {
            System.out.println("Livro não encontrado.");
            return;
        }

        boolean loop = true;

        while (loop) {
            System.out.println("1 - Editar");
            System.out.println("2 - Deletar");
            System.out.println("3 - Sair");

            int op = lerOpcao();

            switch (op) {
                case 1 ->
                    editarLivro(livro);

                case 2 -> {
                    Livro.excluirLivro(livro);
                    loop = false;
                }

                case 3 -> {
                    System.out.println("Saindo do gerenciamento.");
                    loop = false;
                }

                default ->
                    System.out.println("Opção inválida.");
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void gerenciarEmprestimo() {
        titulo("GERENCIAR EMPRÉSTIMO");

        String dataEmp = lerData();

        LocalDate dataEmprestimo;

        try {
            dataEmprestimo = LocalDate.parse(dataEmp);
        } catch (Exception e) {
            System.out.println("Data de empréstimo inválida.");
            return;
        }

        String isbn = lerTexto("ISBN");

        if (isbn.isBlank()) {
            System.out.println("O ISBN é obrigatório.");
            return;
        }

        String login = lerTexto("Login");

        if (login.isBlank()) {
            System.out.println("O login é obrigatório.");
            return;
        }

        Livro livro = (Livro) (Object) BuscarAPI.buscar(
                isbn,
                TelaBase.getCliente(),
                "/Livro/buscar",
                (Class) Livro.class
        );

        if (livro == null) {
            System.out.println("Livro não encontrado.");
            return;
        }

        Membro membro = (Membro) (Object) BuscarAPI.buscar(
                login,
                TelaBase.getCliente(),
                "/membro/buscar",
                (Class) Membro.class
        );

        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        EmprestimoRequest dados = new EmprestimoRequest(
                dataEmprestimo,
                membro.getLogin(),
                livro.getIsbn()
        );

        Emprestimo emp = (Emprestimo) (Object) BuscarAPI.buscar(
                dados,
                TelaBase.getCliente(),
                "/emprestimo/buscar",
                (Class) Emprestimo.class
        );

        if (emp == null) {
            System.out.println("Empréstimo não encontrado.");
            return;
        }

        emp.setMembro(membro);
        emp.setLivro(livro);

        boolean loop = true;

        while (loop) {
            System.out.println("1 - Editar");
            System.out.println("2 - Deletar");
            System.out.println("3 - Sair");

            int op = lerOpcao();

            switch (op) {
                case 1 ->
                    editarEmprestimo(emp, livro);

                case 2 -> {
                    Emprestimo.excluirEmprestimo(emp);
                    loop = false;
                }

                case 3 -> {
                    System.out.println("Saindo do gerenciamento.");
                    loop = false;
                }

                default ->
                    System.out.println("Opção inválida.");
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void desativarMembro() {
        titulo("DESATIVAR MEMBRO");

        String login = lerTexto("Login do membro");

        if (login.isBlank()) {
            System.out.println("O login é obrigatório.");
            return;
        }

        Membro membro = (Membro) (Object) BuscarAPI.buscar(
                login,
                TelaBase.getCliente(),
                "/membro/buscar",
                (Class) Membro.class
        );

        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        if (!membro.isAtivo()) {
            System.out.println("Este membro já está suspenso.");
            return;
        }

        Membro.desativarUsuario(membro);

        String ret = EditarApi.editarNaAPI(
                membro,
                TelaBase.getCliente(),
                "/membro/atualizar"
        );

        System.out.println(ret);
        System.out.println("Membro suspenso: " + membro.getNome());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void ativarMembro() {
        titulo("ATIVAR MEMBRO");

        String login = lerTexto("Login do membro");

        if (login.isBlank()) {
            System.out.println("O login é obrigatório.");
            return;
        }

        Membro membro = (Membro) (Object) BuscarAPI.buscar(
                login,
                TelaBase.getCliente(),
                "/membro/buscar",
                (Class) Membro.class
        );

        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        if (membro.isAtivo()) {
            System.out.println("Este membro já está ativo.");
            return;
        }

        Membro.editarMembro(
                membro,
                membro.getNome(),
                membro.getCpf(),
                login,
                null,
                true,
                membro.getEndereco(),
                membro.getTelefone(),
                membro.getEmail()
        );

        String ret = EditarApi.editarNaAPI(
                membro,
                TelaBase.getCliente(),
                "/membro/atualizar"
        );

        System.out.println(ret);
        System.out.println("Membro ativo: " + membro.getNome());
    }

    protected void editarLivro(Livro livro) {
        boolean loop = true;

        while (loop) {
            System.out.println("Escolha a opção para alterar:");
            System.out.println("1 - Título");
            System.out.println("2 - Autor");
            System.out.println("3 - Ano de publicação");
            System.out.println("4 - Número de cópias");
            System.out.println("5 - Disponíveis");
            System.out.println("6 - Sair");

            int op = lerOpcao();

            switch (op) {
                case 1 -> {
                    String titulo = lerTexto("Título");

                    if (titulo.isBlank()) {
                        System.out.println("O título é obrigatório.");
                        break;
                    }

                    livro.setTitulo(titulo);
                }

                case 2 -> {
                    String autor = lerTexto("Autor");

                    if (autor.isBlank()) {
                        System.out.println("O autor é obrigatório.");
                        break;
                    }

                    livro.setAutor(autor);
                }

                case 3 -> {
                    System.out.println("Ano de publicação:");
                    int anoPublicacao = lerInteiro();

                    if (anoPublicacao <= 0) {
                        System.out.println("O ano de publicação deve ser positivo.");
                        break;
                    }

                    livro.setAnoPublicacao(anoPublicacao);
                }

                case 4 -> {
                    System.out.println("Quantidade de cópias:");
                    int numeroCopias = lerInteiro();

                    if (numeroCopias <= 0) {
                        System.out.println("A quantidade deve ser positiva.");
                        break;
                    }

                    System.out.println("Selecione a ação:");
                    System.out.println("1 - Adicionar");
                    System.out.println("2 - Remover");

                    int acao = lerOpcao();

                    switch (acao) {
                        case 1 ->
                            livro.adicionarCopias(numeroCopias);
                        case 2 ->
                            livro.removerCopias(numeroCopias);
                        default ->
                            System.out.println("Opção inválida.");
                    }
                }

                case 5 -> {
                    System.out.println("Quantidade disponível:");
                    int disponiveis = lerInteiro();

                    if (disponiveis < 0) {
                        System.out.println("A quantidade disponível não pode ser negativa.");
                        break;
                    }

                    if (disponiveis > livro.getNumeroCopias()) {
                        System.out.println("A quantidade disponível não pode superar o total de cópias.");
                        break;
                    }

                    livro.setDisponiveis(disponiveis);
                }

                case 6 -> {
                    System.out.println("Fim da edição.");
                    loop = false;
                }

                default ->
                    System.out.println("Opção inválida.");
            }
        }

        Livro.editarLivro(
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAnoPublicacao(),
                livro.getNumeroCopias(),
                livro.getDisponiveis()
        );
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void cadastrarEmprestimo() {

        int tentativas = 4;

        while (tentativas >= 0) {
            String login = lerTexto("Login");

            if (login.isBlank()) {
                System.out.println("O login é obrigatório.");
                return;
            }

            String senha = lerTexto("Senha");

            if (senha.isBlank()) {
                System.out.println("A senha é obrigatória.");
                return;
            }

            DadosLogin dados = new DadosLogin(login, senha);

            boolean valido = BuscarAPI.login(
                    dados,
                    TelaBase.getCliente(),
                    "/membro/login"
            );

            if (!valido) {
                tentativas--;

                if (tentativas >= 0) {
                    System.out.println("Login inválido. Tentativas restantes: " + (tentativas + 1));
                }

                continue;
            }

            String isbn = lerTexto("ISBN");

            if (isbn.isBlank()) {
                System.out.println("O ISBN é obrigatório.");
                return;
            }

            Membro membro = (Membro) (Object) BuscarAPI.buscar(
                    login,
                    TelaBase.getCliente(),
                    "/membro/buscar",
                    (Class) Membro.class
            );

            if (membro == null) {
                System.out.println("Membro não encontrado.");
                return;
            }

            Livro livro = (Livro) (Object) BuscarAPI.buscar(
                    isbn,
                    TelaBase.getCliente(),
                    "/Livro/buscar",
                    (Class) Livro.class
            );

            if (livro == null) {
                System.out.println("Livro não encontrado.");
                return;
            }

            List<Emprestimo> emprestimos = ListarAPI.listar(
                    TelaBase.getCliente(),
                    "/emprestimo",
                    Emprestimo.class
            );

            Emprestimo.setEmprestimos(
                    emprestimos != null ? emprestimos : List.of()
            );

            Emprestimo emprestimo = Emprestimo.criarEmprestimo(livro, membro);

            if (emprestimo == null) {
                System.out.println("Não foi possível criar o empréstimo.");
                return;
            }

            String retorno = InserirApi.adicionarAPI(
                    emprestimo,
                    TelaBase.getCliente(),
                    "/emprestimo"
            );

            System.out.println(retorno);
            return;
        }

        System.out.println("Número máximo de tentativas excedido.");
    }

    protected void editarEmprestimo(Emprestimo emp, Livro livro) {
        boolean loop = true;

        while (loop) {
            System.out.println("Escolha a opção para alterar:");
            System.out.println("1 - Data de devolução prevista");
            System.out.println("2 - Data de devolução real");
            System.out.println("3 - Devolvido (inverte o valor no empréstimo)");
            System.out.println("4 - Sair");

            int op = lerOpcao();

            switch (op) {
                case 1 -> {
                    String data = lerData();

                    try {
                        LocalDate date = LocalDate.parse(data);

                        if (!date.isBefore(emp.getDataEmprestimo())) {
                            emp.setDataDevolucaoPrevista(date);
                        } else {
                            System.out.println("A data deve ser posterior à data do empréstimo.");
                        }

                    } catch (Exception e) {
                        System.out.println("Data inválida.");
                    }
                }

                case 2 -> {
                    String data = lerData();

                    try {
                        LocalDate date = LocalDate.parse(data);

                        if (!date.isBefore(emp.getDataEmprestimo())) {
                            emp.setDataDevolucaoReal(date);
                        } else {
                            System.out.println("A data deve ser posterior à data do empréstimo.");
                        }

                    } catch (Exception e) {
                        System.out.println("Data inválida.");
                    }
                }

                case 3 -> {
                    emp.setDevolvido(!emp.isDevolvido());

                    if (emp.isDevolvido()) {
                        livro.devolverCopia();
                    } else {
                        livro.emprestarCopia();
                    }
                }

                case 4 -> {
                    System.out.println("Fim da edição.");
                    loop = false;
                }

                default ->
                    System.out.println("Opção inválida.");
            }
        }

        Emprestimo.editarEmprestimo(
                emp,
                emp.getDataEmprestimo(),
                emp.getDataDevolucaoPrevista(),
                emp.getDataDevolucaoReal(),
                emp.isDevolvido()
        );

        Livro.editarLivro(
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAnoPublicacao(),
                livro.getNumeroCopias(),
                livro.getDisponiveis()
        );
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
            case 1 ->
                emprestimos = Emprestimo.listarEmprestimos();

            case 2 ->
                emprestimos = Emprestimo.listarEmprestimosAtivos();

            case 3 ->
                emprestimos = Emprestimo.listarEmprestimosAtrasados();

            case 4 -> {
                String login = lerTexto("Login");

                if (login.isBlank()) {
                    System.out.println("O login é obrigatório.");
                    return;
                }

                Membro membro = (Membro) (Object) BuscarAPI.buscar(
                        login,
                        TelaBase.getCliente(),
                        "/membro/buscar",
                        (Class) Membro.class
                );

                if (membro == null) {
                    System.out.println("Membro não encontrado.");
                    return;
                }

                emprestimos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);
            }

            case 5 -> {
                String login = lerTexto("Login");

                if (login.isBlank()) {
                    System.out.println("O login é obrigatório.");
                    return;
                }

                Membro membro = (Membro) (Object) BuscarAPI.buscar(
                        login,
                        TelaBase.getCliente(),
                        "/membro/buscar",
                        (Class) Membro.class
                );

                if (membro == null) {
                    System.out.println("Membro não encontrado.");
                    return;
                }

                emprestimos = Emprestimo.listarEmprestimosAtrasadosPorMembro(membro);
            }

            default -> {
                System.out.println("Opção inválida.");
                return;
            }
        }

        try {
            if (emprestimos == null || emprestimos.isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado.");
                return;
            }

            for (Emprestimo emp : emprestimos) {
                emp.verEmprestimo();
            }

        } catch (Exception e) {
            System.out.println("Não foi possível consultar os empréstimos: " + e.getMessage());
        }
    }
}
