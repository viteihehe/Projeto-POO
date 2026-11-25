package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BuscarAPI implements API {

    public static <T> T buscar(
            T objetoAlvo,
            HttpClient cliente,
            String tipo,
            Class<T> classe
    ) {
        String json;

        try {
            json = mapper.writeValueAsString(objetoAlvo);
        } catch (Exception e) {
            System.err.println("Erro ao converter objeto para JSON:");
            e.printStackTrace();
            return null;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + tipo))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resposta;

        try {
            resposta = cliente.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (Exception e) {
            System.err.println("Erro ao enviar requisição:");
            e.printStackTrace();
            return null;
        }

        System.out.println("Status HTTP: " + resposta.statusCode());
        System.out.println("Resposta da API: " + resposta.body());

        if (resposta.statusCode() == 200) {
            try {
                return mapper.readValue(resposta.body(), classe);
            } catch (Exception e) {
                System.err.println("Erro ao converter JSON para objeto:");
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public static <T> boolean login(T objetoAlvo, HttpClient cliente, String tipo) {

        String json;
        try {
            json = mapper.writeValueAsString(objetoAlvo);
        } catch (Exception e) {
            return false;
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
            return false;
        }
        if (resposta.statusCode() == 200) {
            try {
                return mapper.readValue(resposta.body(), boolean.class);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}
