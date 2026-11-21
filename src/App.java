import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import entities.*;


public class App {
    private static final List<Usuario> usuarios = new ArrayList<>();

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
                    menuCadastro(sc);
                    break;
                case 2:
                    login(sc);
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
        System.out.println("\n===============");
        System.out.println("1 - Cadastro");
        System.out.println("2 - Login");
        System.out.println("3 - Sair");
        System.out.println("===============");
    }

    public static void menuCadastro(Scanner sc){
        System.out.println("Bibliotecario - 1");
        System.out.println("Membro - 2");
        int tipo = pegarInt(sc);

            try {
            switch (tipo) {
                case 1:
                    usuarios.add(cadastraBibliotecario(sc));
                    System.out.println("Bibliotecario cadastrado!");
                    break;
                case 2:
                    usuarios.add(cadastraMembro(sc));
                    System.out.println("Membro cadastrado!");
                    break;
                default:
                    System.out.println("Valor invalido!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
        }
    }

    public static Bibliotecario cadastraBibliotecario(Scanner sc) {
        System.out.println("Nome:");
        String nome = sc.nextLine();
        System.out.println("CPF:");
        String cpf = sc.nextLine();
        System.out.println("Login:");
        String login = sc.nextLine();
        System.out.println("Senha:");
        String senha = sc.nextLine();
        System.out.println("Cargo:");
        String cargo = sc.nextLine();
        return new Bibliotecario(nome, cpf, login, senha, cargo);
    }

    public static Membro cadastraMembro(Scanner sc){
        System.out.println("Nome:");
        String nome = sc.nextLine();
        System.out.println("CPF:");
        String cpf = sc.nextLine();
        System.out.println("Login:");
        String login = sc.nextLine();
        System.out.println("Senha:");
        String senha = sc.nextLine();
        System.out.println("Email:");
        String email = sc.nextLine();
        System.out.println("Endereço");
        String endereco = sc.nextLine();
        System.out.println("Telefone:");
        String telefone = sc.nextLine();
        return new Membro(nome, cpf, login, senha, false, endereco, telefone, email);
    }

    static void login(Scanner sc) {
        System.out.println("Login:");
        String login = sc.nextLine();
        System.out.println("Senha:");
        String senha = sc.nextLine();

        for (Usuario u : usuarios) {
            if (u.autenticar(login, senha)) {
                System.out.println("Bem-vindo, " + u.getNome() + "!");
                if (u instanceof Bibliotecario) {
                    menuBibliotecario(sc, (Bibliotecario) u);
                } else {
                    menuMembro(sc, (Membro) u);
                }
                return;
            }
        }
        System.out.println("Login ou senha incorretos.");
    }

    static void menuBibliotecario(Scanner sc, Bibliotecario bibliotecario) {
        System.out.println("Menu do bibliotecário: " + bibliotecario.getNome());
    }

    static void menuMembro(Scanner sc, Membro membro){
        System.out.println("Menu do membro:" + membro.getNome());
    }

    public static int pegarInt(Scanner sc) {
        int op = 0;
        boolean loop;
        do { 
            loop = false;
            try {
                System.out.print("Escolha a opção desejada:");
                op = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Tente novamente!");
                sc.next();
                loop = true;
            }
        } while (loop);
        return op;
    }
}