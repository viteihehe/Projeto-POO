import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import UI.TelaSistemaBiblioteca;
import entities.*;

public class App {
    private static final List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        HttpClient cliente = HttpClient.newHttpClient();

        try (Scanner sc = new Scanner(System.in)) {
            new TelaSistemaBiblioteca(sc, usuarios).mostrar();
        }
    }
}