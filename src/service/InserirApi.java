package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InserirApi implements API {

    public static <T> String adicionarAPI(T novoObjeto, HttpClient cliente, String tipo) {

        
        String json;
        try {
            json = mapper.writeValueAsString(novoObjeto);
        } catch (Exception e) {
            return "Erro ao converter o Objeto!";
        }

        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + tipo))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        
        HttpResponse<String> resposta;
        try {
            resposta = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "Erro ao envio da mensagem";
        }
        if (resposta.statusCode() == 201) {
            return "Sucesso!";
        } else {
            System.out.println("Status HTTP: " + resposta.statusCode());
            System.out.println("Corpo da resposta: " + resposta.body());
            try {
                return mapper.readValue(resposta.body(), String.class);
            } catch (Exception e) {
                return "Erro ao interpretar a resposta da API";
            }
        }
    }
}
