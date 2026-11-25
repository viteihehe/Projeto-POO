package UI;

import java.util.Scanner;

public abstract class TelaCadastroBase extends TelaBase {

    private static final int TAMANHO_MIN_SENHA = 6;

    protected record DadosBasicos(String nome, String cpf, String login, String senha) {

    }

    protected TelaCadastroBase(Scanner sc) {
        super(sc);
    }

    protected abstract String tituloTela();

    protected abstract String criarUsuario();

    @Override
    public void mostrar() {
        titulo(tituloTela());
        try {
            String retorno = criarUsuario();
            if (retorno == null) {
                return;
            }
            if (retorno.equals("sucesso")) {
                System.out.println("\nCadastro realizado com sucesso!");
            } else {
                System.out.println(retorno);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro no cadastro: " + e.getMessage());
        }
    }

    protected DadosBasicos lerDadosBasicos() {
        String nome = lerTexto("Nome");
        if (nome.isBlank()) {
            System.out.println("O nome é obrigatório");
            return null;
        }

        String cpf = lerTexto("CPF");
        if (cpf.isBlank()) {
            System.out.println("O CPF é obrigatório");
            return null;
        }

        String login = lerTexto("Login");
        if (login.isBlank()) {
            System.out.println("O login é obrigatório");
            return null;
        }

        String senha = lerSenha("Senha (mínimo " + TAMANHO_MIN_SENHA + " caracteres)");
        if (senha.length() < TAMANHO_MIN_SENHA) {
            System.out.println("A senha deve ter pelo menos " + TAMANHO_MIN_SENHA + " caracteres.");
            return null;
        }
        if (!senha.equals(lerSenha("Confirme a senha"))) {
            System.out.println("As senhas não conferem.");
            return null;
        }

        return new DadosBasicos(nome, cpf, login, senha);
    }
}
