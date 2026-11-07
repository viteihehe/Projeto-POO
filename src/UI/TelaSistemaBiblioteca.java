package UI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class TelaSistemaBiblioteca {
    private JFrame janela;
    
    public TelaSistemaBiblioteca(){
        janela = new JFrame("Sistema de Gerenciamento de Biblioteca"); 
        janela.setSize(400, 300);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(new FlowLayout());
        addComponentes();
    }
    
    private void addComponentes(){
        JLabel lblBoasVindas = new JLabel("Selecione uma opção"); 
        JButton btnLogin = new JButton("Login"); 
        JButton btnCadastro = new JButton("Cadastro"); 

        janela.add(lblBoasVindas);
        janela.add(btnLogin);
        janela.add(btnCadastro);
    }

    public void mostrar(){
        janela.setVisible(true);
    }
}
