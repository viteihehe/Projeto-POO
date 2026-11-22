package UI;

import java.util.List;
import java.util.Scanner;
import entities.Usuario;

public abstract class TelaCadastroBase extends TelaBase {

    private static final int TAMANHO_MIN_SENHA = 6;

    protected final List<Usuario> usuarios;

    protected record DadosBasicos(String nome, String cpf, String login, String senha) { }

    protected TelaCadastroBase(Scanner sc, List<Usuario> usuarios) {
        super(sc);
        this.usuarios = usuarios;
    }

    protected abstract String tituloTela();

    protected abstract Usuario criarUsuario();

    @Override
    public void mostrar() {
        titulo(tituloTela());
        try {
            Usuario novo = criarUsuario();
            usuarios.add(novo);
            System.out.println("\nCadastro realizado com sucesso!");
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
        if (cpfJaCadastrado(cpf)) {
            throw new IllegalArgumentException("Já existe um usuário com este CPF");
        }

        String login = lerTexto("Login");
        if (login.isBlank()) {
            throw new IllegalArgumentException("O login é obrigatório");
        }
        if (loginJaCadastrado(login)) {
            throw new IllegalArgumentException("Este login já está em uso");
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

    private boolean cpfJaCadastrado(String cpf) {
        String digitos = cpf.replaceAll("\\D", "");
        return usuarios.stream().anyMatch(u -> u.getCpf().equals(digitos));
    }

    private boolean loginJaCadastrado(String login) {
        return usuarios.stream().anyMatch(u -> u.getLogin().equals(login));
    }
}