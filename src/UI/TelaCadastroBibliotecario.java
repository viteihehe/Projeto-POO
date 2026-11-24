package UI;

import java.util.Scanner;

import entities.Bibliotecario;
import interfaces.Cargo;
import service.InserirApi;

public class TelaCadastroBibliotecario extends TelaCadastroBase {

    public TelaCadastroBibliotecario(Scanner sc) {
        super(sc);
    }

    @Override
    protected String tituloTela() {
        return "CADASTRO DE BIBLIOTECÁRIO";
    }

    @Override
    protected String criarUsuario() {
        DadosBasicos dados = lerDadosBasicos();
        String cargo = lerCargo();
        Bibliotecario bibliotecario;
        try {
            bibliotecario = Bibliotecario.criarBibliotecario(dados.nome(), dados.cpf(), dados.login(), dados.senha(), cargo);
        } catch (IllegalArgumentException e) {
            return "Erro para cadastrar";
        }
        String retorno = InserirApi.adicionarAPI(bibliotecario, TelaSistemaBiblioteca.getCliente(), "/bibliotecario");
        return retorno;
    }

    private String lerCargo() {
        String entrada = lerTexto("Cargo(ATENDENTE, ESTAGIARIO)");
        if (entrada.isBlank()) {
            throw new IllegalArgumentException("O cargo é obrigatório.");
        }

        try {
            entrada = entrada.trim().toUpperCase();
            Cargo cargo = Cargo.valueOf(entrada);
            return cargo.name();
        } catch (IllegalArgumentException e) {
            return "Erro: Cargo inválido! Digite exatamente uma das opções.";
        }

    }
}
