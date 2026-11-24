package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;

import entities.Emprestimo;
import entities.Membro;

public class ListarAPI implements API {

    public static <T> List<T> listar(HttpClient cliente, String tipo, Class<T> classe) {
        List<T> retorno = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + tipo))
                .header("Accept", "application/json")
                .GET()
                .build();

        //Envia a requisição
        HttpResponse<String> resposta;
        try {
            resposta = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }
        if (resposta.statusCode() == 200) {
            JavaType tipoLista = mapper.getTypeFactory().constructParametricType(List.class, classe);
            try {
                return mapper.readValue(resposta.body(), tipoLista);
            } catch (Exception e) {
                return retorno;
            }
        }
        return null;
    }

    public static List<Emprestimo> listarPorMembro(HttpClient cliente, String tipo, Membro membro) {
        List<Emprestimo> retorno = new ArrayList<>();

        String json;
        try {
            json = mapper.writeValueAsString(membro);
        } catch (Exception e) {
            return null;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + tipo + "/membro"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resposta;
        try {
            resposta = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }
        if (resposta.statusCode() == 200) {
            JavaType tipoLista = mapper.getTypeFactory().constructParametricType(List.class, Emprestimo.class);
            try {
                return mapper.readValue(resposta.body(), tipoLista);
            } catch (Exception e) {
                return retorno;
            }
        }
        return null;
    }
}
