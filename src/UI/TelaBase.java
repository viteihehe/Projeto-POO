package UI;

import java.net.http.HttpClient;
import java.util.Scanner;

public abstract class TelaBase {

    private static HttpClient cliente;

    protected final Scanner sc;

    protected TelaBase(Scanner sc, HttpClient cliente) {
        this.sc = sc;
        TelaBase.cliente = cliente;
    }

    protected TelaBase(Scanner sc) {
        this.sc = sc;
    }

    public abstract void mostrar();

    /**
     * Imprime um título destacado.
     */
    protected void titulo(String texto) {
        String linha = "=".repeat(texto.length() + 4);
        System.out.println("\n" + linha);
        System.out.println("  " + texto);
        System.out.println(linha);
    }

    protected String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        return sc.nextLine().trim();
    }

    protected String lerSenha(String rotulo) {
        System.out.print(rotulo + ": ");
        return sc.nextLine();
    }

    protected int lerOpcao() {
        while (true) {
            System.out.print("Escolha a opção desejada: ");
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas números.");
            }
        }
    }

    protected int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas números.");
            }
        }
    }

    protected String lerData() {
        int ano, dia, mes;
        while (true) {
            System.out.println("Insira o dia:");
            dia = lerInteiro();
            if (!(dia < 1 || dia > 31)) {
                break;
            }
        }
        while (true) {
            System.out.println("Insira o mês:");
            mes = lerInteiro();
            if (!(mes < 1 || mes > 12)) {
                break;
            }
        }
        while (true) {
            System.out.println("Insira o dia:");
            ano = lerInteiro();
            if (!(dia < 1 || dia > 31)) {
                break;
            }
        }
        return String.format("%d-%d-%d", ano, mes, ano);
    }

    protected void emDesenvolvimento(String funcao) {
        System.out.println("[" + funcao + "] ainda não implementado.");
    }

    public static HttpClient getCliente() {
        return cliente;
    }
}
