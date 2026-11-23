package UI;

import java.net.http.HttpClient;
import java.util.Scanner;

public class TelaSistemaBiblioteca extends TelaBase {

    public TelaSistemaBiblioteca(Scanner sc, HttpClient cliente) {
        super(sc, cliente);
    }

    @Override
    public void mostrar() {
        boolean executando = true;

        while (executando) {
            titulo("SISTEMA DE GERENCIAMENTO DE BIBLIOTECA");
            System.out.println("1 - Login");
            System.out.println("2 - Cadastro");
            System.out.println("3 - Sair");

            switch (lerOpcao()) {
                case 1:
                    System.out.println("1 - Bibliotecário");
                    System.out.println("2 - Membro");
                    int tipo = lerOpcao();
                    new TelaLogin(sc, tipo).mostrar();
                    break;
                case 2:
                    mostrarMenuCadastro();
                    break;
                case 3:
                    System.out.println("Adeus!");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    private void mostrarMenuCadastro() {
        titulo("CADASTRO");
        System.out.println("1 - Bibliotecário");
        System.out.println("2 - Membro");
        System.out.println("3 - Voltar");

        switch (lerOpcao()) {
            case 1 ->
                // new TelaCadastroBibliotecario(sc).mostrar();
                System.out.println("Temporariamente desabilitada!");
            case 2 ->
                new TelaCadastroMembro(sc).mostrar();
            case 3 -> {
                /* apenas volta ao menu inicial */ }
            default ->
                System.out.println("Opção inválida!");
        }
    }

}
