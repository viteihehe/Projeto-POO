package UI;

import java.util.Scanner;

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
                    emDesenvolvimento("Consultar livros");
                case 3 ->
                    emDesenvolvimento("Meus empréstimos");
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

        System.out.println("Login   : " + membro.getLogin());
        System.out.println("E-mail  : " + membro.getEmail());
        System.out.println("Telefone: " + membro.getTelefone());
        System.out.println("Endereço: " + membro.getEndereco());
    }
}
