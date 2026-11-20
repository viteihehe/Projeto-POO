package entities;
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

    public String converteTelefone(String telefone){
        telefone = telefone.replaceAll("\\D", "");
        return telefone;
    }

    public boolean validaTelefone(String telefone){
        telefone = converteTelefone(telefone);

        if (telefone == null) {
            throw new NullPointerException("Telefone nulo");
        }

        telefone = converteTelefone(telefone);

        if (!telefone.matches("^[1-9]{2}9\\d{8}|[2-5]\\d{7})$")) {
            throw new IllegalArgumentException("Telefone inválido");
        }

        return true;

    }

    public boolean validaEmail(String email){
        
        email = getEmail();

        if(!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")){
            System.out.println("Endereço de Email invalido");
        } 

        return true;
        
    }
    
}