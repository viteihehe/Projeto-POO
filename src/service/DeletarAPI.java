package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DeletarAPI implements API {

    public static <T> String deletar(T novoObjeto, HttpClient cliente, String tipo) {

        //Convertendo o objeto em um arquivo compativel com a API
        String json;
        try {
            json = mapper.writeValueAsString(novoObjeto);
        } catch (Exception e) {
            return "Erro ao converter o Objeto!";
        }

        //Prepara a requisição
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + tipo))
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        //Envia a requisição
        HttpResponse<String> resposta;
        try {
            resposta = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "Erro ao envio da mensagem";
        }
        if (resposta.statusCode() == 200) {
            return "Deletado";
        }
        return "Falha ao Deletar";
    }
}
