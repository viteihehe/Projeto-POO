package interfaces;

import entities.Bibliotecario;
import entities.Emprestimo;
import entities.Livro;
import entities.Membro;

public interface DataBase {
    final String url = "jdbc:postgresql://129.148.25.183:5432/biblioteca";
    public void adicionarMembro(Membro novoMembro);
    public void adicionarBibliotecario(Bibliotecario novoBibliotecario);
    public void adicionarEmprestimo(Emprestimo novoEmprestimo, Membro membro, Livro livro);
    public void adicionarLivro(Livro novoLivro);
}
