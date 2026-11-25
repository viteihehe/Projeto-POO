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
        if (dados == null) {
            return null;
        }

        String email = lerTexto("E-mail");
        if (!Membro.validarEmail(email)) {
            System.out.println("E-mail inválido.");
            return null;
        }

        String endereco = lerTexto("Endereço");
        if (endereco.isBlank()) {
            System.out.println("O endereço é obrigatório.");
            return null;
        }

        String telefone = lerTexto("Telefone (com DDD)");

        if (!Membro.validarTelefone(telefone)) {
            System.out.println("Telefone Invalido.");
            return null;
        }

        Membro membro = Membro.criarMembro(dados.nome(), dados.cpf(), dados.login(), dados.senha(), endereco, telefone, email);

        String retorno = InserirApi.adicionarAPI(membro, TelaSistemaBiblioteca.getCliente(), "/membro");

        return retorno;
    }
}
