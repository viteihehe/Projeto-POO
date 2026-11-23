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
            throw new IllegalArgumentException("O nome é obrigatório");
        }

        String cpf = lerTexto("CPF");
        if (cpf.isBlank()) {
            throw new IllegalArgumentException("O CPF é obrigatório");
        }

        String login = lerTexto("Login");
        if (login.isBlank()) {
            throw new IllegalArgumentException("O login é obrigatório");
        }

        String senha = lerSenha("Senha (mínimo " + TAMANHO_MIN_SENHA + " caracteres)");
        if (senha.length() < TAMANHO_MIN_SENHA) {
            throw new IllegalArgumentException("A senha deve ter pelo menos " + TAMANHO_MIN_SENHA + " caracteres.");
        }
        if (!senha.equals(lerSenha("Confirme a senha"))) {
            throw new IllegalArgumentException("As senhas não conferem.");
        }

        return new DadosBasicos(nome, cpf, login, senha);
    }
}
