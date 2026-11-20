import java.net.http.HttpClient;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        //Estabelecendo a rota do servidor.
        boolean loop = true;
        int op;
        Scanner sc = new Scanner(System.in);
        HttpClient cliente = HttpClient.newHttpClient();
        // SwingUtilities.invokeLater(new Runnable() {
        //     @Override
        //     public void run() {
        //         TelaSistemaBiblioteca tela = new TelaSistemaBiblioteca();
        //         tela.mostrar();
        //     }
        // });
        while(loop) {
            menuTemp();
            op = pegarInt(sc);
            
            switch (op) {
                case 1:
                   
                    
                    break;
                    
                case 2:
                    break;
                case 3:
                    System.out.println("Adeus!");
                    loop = false;
                    break;
                default:
                    System.out.println("Valor invalido!");
                    break;
            }
            
        }
        
    }

    public static void menuTemp() {
        System.out.println("===============");
        System.out.println("1 - Cadastro");
        System.out.println("2 - Login");
        System.out.println("3 - Sair");
        System.out.println("===============");
    }
    public static int pegarInt(Scanner sc) {
        int op = 0;
        boolean loop;
        do { 
            loop = false;
            try {
                System.out.println("Escolha a opção desejada:");
                op = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Tente novamente!");
                sc.next();
                loop = true;
            }
        } while (loop);
        return op;
    }
}