package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EditarApi implements API {

    public static <T> String editarNaAPI(T novoObjeto, HttpClient cliente, String tipo) {

        
        String json;
        try {
            json = mapper.writeValueAsString(novoObjeto);
        } catch (Exception e) {
            return "Erro ao converter o Objeto!";
        }

        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + tipo))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        
        HttpResponse<String> resposta;
        try {
            resposta = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "Erro ao envio da mensagem";
        }
        if (resposta.statusCode() == 200) {
            return "Alterado";
        }
        return "Falha ao Editar!";
    }
}
