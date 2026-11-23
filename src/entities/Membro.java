package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Membro extends Usuario {

    private static final Set<String> DDDS_VALIDOS = new HashSet<>(Arrays.asList(
            "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "21", "22", "24", "27", "28",
            "31", "32", "33", "34", "35", "37", "38",
            "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "51", "53", "54", "55",
            "61", "62", "63", "64", "65", "66", "67", "68", "69",
            "71", "73", "74", "75", "77", "79",
            "81", "82", "83", "84", "85", "86", "87", "88", "89",
            "91", "92", "93", "94", "95", "96", "97", "98", "99"));

    private static final List<Membro> membros = new ArrayList<>();

    private String endereco;
    private String telefone;
    private String email;

    private Membro(String nome, String cpf, String login, String senha, String endereco, String telefone, String email) {
        super(nome, cpf, login, senha);
        setEndereco(endereco);
        setTelefone(telefone);
        setEmail(email);
    }

    public void verMembro() {
        System.out.println(this);
    }

    public static Membro criarMembro(String nome, String cpf, String login, String senha, String endereco, String telefone, String email) {
        Membro membro = new Membro(nome, cpf, login, senha, endereco, telefone, email);
        membros.add(membro);
        return membro;
    }

    public static void editarMembro(Membro membro, String nome, String cpf, String login, String senha, boolean ativo, String endereco, String telefone, String email) {
        if (membro == null) {
            throw new IllegalArgumentException("Membro não informado.");
        }
        if (!validaCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido, alteração não realizada");
        }
        if (!validarTelefone(telefone)) {
            throw new IllegalArgumentException("Telefone inválido: " + telefone);
        }
        if (!validarEmail(email)) {
            throw new IllegalArgumentException("E-mail inválido: " + email);
        }
        membro.setNome(nome);
        membro.setCpf(cpf);
        membro.setLogin(login);
        membro.setSenha(senha);
        membro.setAtivo(ativo);
        membro.setEndereco(endereco);
        membro.setTelefone(telefone);
        membro.setEmail(email);
    }

    public static List<Membro> listarMembros() {
        return new ArrayList<>(membros);
    }

    public static List<Membro> listarMembrosSuspensos() {
        List<Membro> lista = new ArrayList<>();
        for (Membro membro : membros) {
            if (Emprestimo.membroSuspenso(membro)) {
                lista.add(membro);
            }
        }
        return lista;
    }

    @Override
    public String toString() {
        return super.toString() + "Endereço:" + endereco + "\nTelefone:" + telefone
                + "\nEmail:" + email + "\n";
    }

    public static String normalizarTelefone(String telefone) {
        if (telefone == null) {
            return null;
        }
        String digitos = telefone.replaceAll("\\D", "");
        if ((digitos.length() == 12 || digitos.length() == 13) && digitos.startsWith("55")) {
            digitos = digitos.substring(2);
        }
        return digitos;
    }

    public static boolean validarTelefone(String telefone) {
        String t = normalizarTelefone(telefone);
        if (t == null || (t.length() != 10 && t.length() != 11)) {
            return false;
        }
        if (!validarDDD(t.substring(0, 2))) {
            return false;
        }
        return validarNumeroTelefone(t.substring(2));
    }

    public static boolean validarDDD(String ddd) {
        return ddd != null && DDDS_VALIDOS.contains(ddd);
    }

    public static boolean validarNumeroTelefone(String numero) {
        if (numero == null) {
            return false;
        }
        if (numero.length() == 9) {
            return numero.matches("9\\d{8}");
        }
        if (numero.length() == 8) {
            return numero.matches("[2-5]\\d{7}");
        }
        return false;
    }

    public static boolean validarEmail(String email) {
        if (email == null) {
            return false;
        }
        email = email.trim();
        if (email.length() > 254) {
            return false;
        }

        int arroba = email.indexOf('@');
        if (arroba <= 0 || arroba != email.lastIndexOf('@')) {
            return false;
        }

        String local = email.substring(0, arroba);
        String dominio = email.substring(arroba + 1);

        return validarParteLocalEmail(local) && validarDominioEmail(dominio);
    }

    private static boolean validarParteLocalEmail(String local) {
        if (local.length() > 64) {
            return false;
        }
        if (local.startsWith(".") || local.endsWith(".") || local.contains("..")) {
            return false;
        }
        return local.matches("[A-Za-z0-9._%+\\-]+");
    }

    public static boolean validarDominioEmail(String dominio) {
        if (dominio == null || dominio.isEmpty() || dominio.length() > 253) {
            return false;
        }
        if (!dominio.contains(".")) {
            return false;
        }

        String[] rotulos = dominio.split("\\.", -1);
        for (String rotulo : rotulos) {
            if (rotulo.isEmpty() || rotulo.length() > 63) {
                return false;
            }
            if (rotulo.startsWith("-") || rotulo.endsWith("-")) {
                return false;
            }
            if (!rotulo.matches("[A-Za-z0-9\\-]+")) {
                return false;
            }
        }
        return rotulos[rotulos.length - 1].matches("[A-Za-z]{2,}");
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (!validarTelefone(telefone)) {
            throw new IllegalArgumentException("Telefone inválido: " + telefone);
        }
        this.telefone = normalizarTelefone(telefone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!validarEmail(email)) {
            throw new IllegalArgumentException("E-mail inválido: " + email);
        }
        this.email = email.trim();
    }
}
