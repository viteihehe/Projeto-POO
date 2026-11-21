package entities;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;

    public Usuario(String nome, String cpf, String login, String senha){
        setNome(nome);
        setCpf(cpf);
        setLogin(login);
        setSenha(senha);
        this.ativo = true;
    }
    
    public void verUsuario(){
        System.out.println(this);
    }
    public void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, boolean ativo){}
    public void desativarUsuario(Usuario usuario){
        this.ativo = false;
    }
    public boolean autenticar(String login, String senha){
        return ativo && this.login.equals(login) && this.senha.equals(senha);
    }

    public List<Usuario>listarUsuarios(List<Usuario> usuarios){
        List<Usuario> lista = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            lista.add(usuario);
            System.out.println(usuario);
        }
        return lista;
    }

    @Override
    public String toString(){
        return "Nome:" + getNome() + "\nCPF:" + getCpf() + "\nLogin:" + getLogin() + "\nAtivo:" + isAtivo() + "\n";
    }


    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        if(!validaCpf(cpf)){
            throw new IllegalArgumentException("CPF invalido");
        }
        this.cpf = converteCpf(cpf);
    }

    public String converteCpf(String cpf){
        cpf = cpf.replaceAll("\\D", "");
        return cpf;
    }

    public boolean validaCpf(String cpf){
        if(cpf == null){
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
            dig10 = (char)(r + 48);
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
            dig11 = (char)(r + 48);
        }

    return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
    }


    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
