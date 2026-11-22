package UI;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import entities.Membro;
import entities.Usuario;

public class TelaCadastroMembro extends TelaCadastroBase {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    public TelaCadastroMembro(Scanner sc, List<Usuario> usuarios) {
        super(sc, usuarios);
    }

    @Override
    protected String tituloTela() {
        return "CADASTRO DE MEMBRO";
    }

    @Override
    protected Usuario criarUsuario() {
        DadosBasicos dados = lerDadosBasicos();

        String email = lerTexto("E-mail");
        if (!EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        String endereco = lerTexto("Endereço");
        if (endereco.isBlank()) {
            throw new IllegalArgumentException("O endereço é obrigatório.");
        }

        String telefone = lerTexto("Telefone (com DDD)");

        Membro membro = new Membro(dados.nome(), dados.cpf(), dados.login(), dados.senha(),
                true, endereco, telefone, email);

        membro.validaTelefone(telefone);

        return membro;
    }
}
