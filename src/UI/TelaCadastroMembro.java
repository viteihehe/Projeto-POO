package UI;

import java.util.Scanner;

import entities.Membro;
import service.InserirApi;

public class TelaCadastroMembro extends TelaCadastroBase {

    public TelaCadastroMembro(Scanner sc) {
        super(sc);
    }

    @Override
    protected String tituloTela() {
        return "CADASTRO DE MEMBRO";
    }

    @Override
    protected String criarUsuario() {
        DadosBasicos dados = lerDadosBasicos();

        String email = lerTexto("E-mail");
        if (!Membro.validarEmail(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        String endereco = lerTexto("Endereço");
        if (endereco.isBlank()) {
            throw new IllegalArgumentException("O endereço é obrigatório.");
        }

        String telefone = lerTexto("Telefone (com DDD)");

        if (!Membro.validarTelefone(telefone)) {
            throw new IllegalArgumentException("Telefone Invalido.");
        }

        Membro membro = new Membro(dados.nome(), dados.cpf(), dados.login(), dados.senha(),
                endereco, telefone, email);

        String retorno = InserirApi.adicionarAPI(membro, TelaSistemaBiblioteca.getCliente(), "/membro");

        return retorno;
    }
}
