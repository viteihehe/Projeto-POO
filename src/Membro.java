import java.util.List;

public class Membro extends Usuario {

    private String endereco;
    private String telefone;
    private String email;

    public void verMembro() {
    }

    public static Membro criarMembro(String nome, String cpf, String login, String senha,
                                     String endereco, String telefone, String email) {
        return null;
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
}