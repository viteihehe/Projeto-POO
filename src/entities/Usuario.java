package entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {

    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;

    private static final List<Usuario> cadastrados = new ArrayList<>();

    public Usuario(String nome, String cpf, String login, String senha) {
        setNome(nome);
        setCpf(cpf);
        setLogin(login);
        setSenha(senha);
        this.ativo = true;
        cadastrados.add(this);
    }

    public Usuario() {
    }

    public void verUsuario() {
        System.out.println(this);
    }

    public static void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, boolean ativo) {
        if (!validaCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido, alteração não realizada");
        }
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setAtivo(ativo);
    }

    public static void desativarUsuario(Usuario usuario) {
        usuario.setAtivo(false);
    }

    public static boolean autenticar(String login, String senha) {
        return buscarPorLogin(login, senha) != null;
    }

    public static Usuario buscarPorLogin(String login, String senha) {
        for (Usuario u : cadastrados) {
            if (u.ativo && u.login.equals(login) && u.senha.equals(senha)) {
                return u;
            }
        }
        return null;
    }

    public static List<Usuario> listarUsuarios(List<Usuario> usuarios) {
        List<Usuario> lista = new ArrayList<>(usuarios);

        for (Usuario usuario : usuarios) {
            lista.add(usuario);
            System.out.println(usuario);
        }
        return lista;
    }

    @Override
    public String toString() {
        return "Nome:" + getNome() + "\nCPF:" + getCpf() + "\nLogin:" + getLogin() + "\nAtivo:" + isAtivo() + "\n";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome != null) {
            this.nome = nome;
        } else {
            return;
        }
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf != null) {
            if (!validaCpf(cpf)) {
                throw new IllegalArgumentException("CPF invalido");
            }
            this.cpf = converteCpf(cpf);
        } else {
            return;
        }
    }

    public static String converteCpf(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("\\D", "");
    }

    public static boolean validaCpf(String cpf) {
        if (cpf == null) {
            return false;
        }
        cpf = converteCpf(cpf);

        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        char dig10, dig11;
        int sm, r, num, peso;

        sm = 0;
        peso = 10;

        for (int i = 0; i < 9; i++) {
            num = cpf.charAt(i) - 48;
            sm += num * peso;
            peso--;
        }

        r = 11 - (sm % 11);

        if (r == 10 || r == 11) {
            dig10 = '0';
        } else {
            dig10 = (char) (r + 48);
        }

        sm = 0;
        peso = 11;

        for (int i = 0; i < 10; i++) {
            num = cpf.charAt(i) - 48;
            sm += num * peso;
            peso--;
        }

        r = 11 - (sm % 11);

        if (r == 10 || r == 11) {
            dig11 = '0';
        } else {
            dig11 = (char) (r + 48);
        }

        return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login != null) {
            this.login = login; 
        }else {
            return;
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha != null) {
            this.senha = senha;
        } else {
            return;
        }
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
