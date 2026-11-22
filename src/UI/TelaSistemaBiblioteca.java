package UI;

import java.util.List;
import java.util.Scanner;
import entities.Usuario;

public class TelaSistemaBiblioteca extends TelaBase {

    private final List<Usuario> usuarios;

    public TelaSistemaBiblioteca(Scanner sc, List<Usuario> usuarios) {
        super(sc);
        this.usuarios = usuarios;
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
                case 1 -> new TelaLogin(sc, usuarios).mostrar();
                case 2 -> mostrarMenuCadastro();
                case 3 -> {
                    System.out.println("Adeus!");
                    executando = false;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void mostrarMenuCadastro() {
        titulo("CADASTRO");
        System.out.println("1 - Bibliotecário");
        System.out.println("2 - Membro");
        System.out.println("3 - Voltar");

        switch (lerOpcao()) {
            case 1 -> new TelaCadastroBibliotecario(sc, usuarios).mostrar();
            case 2 -> new TelaCadastroMembro(sc, usuarios).mostrar();
            case 3 -> { /* apenas volta ao menu inicial */ }
            default -> System.out.println("Opção inválida!");
        }
    }
}
