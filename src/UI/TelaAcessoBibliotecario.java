package UI;

import java.util.List;
import java.util.Scanner;

import entities.Bibliotecario;
import entities.Membro;
import service.BuscarAPI;
import service.EditarApi;
import service.ListarAPI;

public class TelaAcessoBibliotecario extends TelaBase {

    private final Bibliotecario bibliotecario;

    public TelaAcessoBibliotecario(Scanner sc, Bibliotecario bibliotecario) {
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
            System.out.println("5 - Gerenciar livros");
            System.out.println("6 - Gerenciar empréstimos");
            System.out.println("7 - Sair (logout)");

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
                    emDesenvolvimento("Gerenciar livros");
                case 6 ->
                    emDesenvolvimento("Gerenciar empréstimos");
                case 7 -> {
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
        System.out.println("Nome : " + bibliotecario.getNome());
        System.out.println("CPF  : " + bibliotecario.getCpf());
        System.out.println("Login: " + bibliotecario.getLogin());
        System.out.println("Cargo: " + bibliotecario.getCargo());
    }

    private void listarMembros(boolean somenteSuspensos) {
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
    private void suspenderMembro() {
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

}
