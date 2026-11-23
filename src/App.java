
import java.net.http.HttpClient;
import java.util.Scanner;

import UI.TelaSistemaBiblioteca;

public class App {

    public static void main(String[] args) {
        HttpClient cliente = HttpClient.newHttpClient();

        try (Scanner sc = new Scanner(System.in)) {
            new TelaSistemaBiblioteca(sc, cliente).mostrar();
        }
    }
}
