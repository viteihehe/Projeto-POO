import java.util.List;

public class Membro extends Usuario {

    private String endereco;
    private String telefone;
    private String email;

    public void verMembro() {
    }

    public Membro(String nome, String cpf, String login, String senha, boolean ativo, String endereco, String telefone, String email) {
    }

    public static void editarMembro(Membro membro, String nome, String cpf, String login,
                                    String senha, boolean ativo, String endereco,
                                    String telefone, String email) {
    }

    public static List<Membro> listarMembros() {
        return null;
    }

    public static List<Membro> listarMembrosSuspensos() {
        return null;
    }

    /**
     * @return String return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return String return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

}