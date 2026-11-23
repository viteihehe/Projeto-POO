package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InserirApi implements API {

    public static <T> String adicionarAPI(T novoObjeto, HttpClient cliente, String tipo) {

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
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        //Envia a requisição
        HttpResponse<String> resposta;
        try {
            resposta = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "Erro ao envio da mensagem";
        }
        if (resposta.statusCode() == 201) {
            return "Sucesso!";
        } else {
            try {
                return mapper.readValue(resposta.body(), String.class);
            } catch (Exception e) {
                return "CPF, Login, ou email em uso!";
            }
        }
    }
}
