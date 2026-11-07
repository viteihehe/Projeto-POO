import UI.TelaSistemaBiblioteca;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TelaSistemaBiblioteca tela = new TelaSistemaBiblioteca();
                tela.mostrar();
            }
        });
    }
}