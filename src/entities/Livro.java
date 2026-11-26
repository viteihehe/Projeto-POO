package entities;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import UI.TelaBase;
import service.DeletarAPI;
import service.EditarApi;
import service.ListarAPI;

public class Livro {

    private static final List<Livro> livros = new ArrayList<>();

    private String isbn;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private int numeroCopias;
    private int disponiveis;

    public Livro(String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.numeroCopias = numeroCopias;
        this.disponiveis = numeroCopias;
    }

    public Livro() {
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public int getNumeroCopias() {
        return numeroCopias;
    }

    public int getDisponiveis() {
        return disponiveis;
    }

    public void setIsbn(String isbn) {
        if (!validarIsbn(isbn)) {
            throw new IllegalArgumentException("ISBN inválido.");
        }

        this.isbn = normalizarIsbn(isbn);
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título é obrigatório.");
        }
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("O autor é obrigatório.");
        }
        this.autor = autor;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        validarAno(anoPublicacao);
        this.anoPublicacao = anoPublicacao;
    }

    public void setNumeroCopias(int numeroCopias) {
        int emprestadas = this.numeroCopias - this.disponiveis;
        if (numeroCopias < emprestadas) {
            throw new IllegalArgumentException("O número de cópias não pode ser menor que o de cópias emprestadas.");
        }
        this.numeroCopias = numeroCopias;
        this.disponiveis = numeroCopias - emprestadas;
    }

    public void setDisponiveis(int disponiveis) {
        if (disponiveis < 0 || disponiveis > numeroCopias) {
            throw new IllegalArgumentException("Disponíveis deve estar entre 0 e o número de cópias.");
        }
        this.disponiveis = disponiveis;
    }

    public void verLivro() {
        System.out.println("ISBN: " + isbn);
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Ano de publicação: " + anoPublicacao);
        System.out.println("Cópias: " + numeroCopias);
        System.out.println("Disponíveis: " + disponiveis + "\n");
    }

    public void adicionarCopias(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        numeroCopias += quantidade;
        disponiveis += quantidade;
    }

    public void removerCopias(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (quantidade > disponiveis) {
            throw new IllegalArgumentException("Não há cópias disponíveis suficientes para remover.");
        }
        numeroCopias -= quantidade;
        disponiveis -= quantidade;
    }

    public void emprestarCopia() {
        if (disponiveis == 0) {
            throw new IllegalStateException("Nenhuma cópia disponível para empréstimo.");
        }
        disponiveis--;
    }

    public void devolverCopia() {
        if (disponiveis >= numeroCopias) {
            throw new IllegalStateException("Todas as cópias já estão na biblioteca.");
        }
        disponiveis++;
    }

    public static Livro criarLivro(String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias) {
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("Título e autor são obrigatórios.");
        }
        if (!validarIsbn(isbn)) {
            throw new IllegalArgumentException("ISBN inválido.");
        }
        isbn = normalizarIsbn(isbn);
        validarAno(anoPublicacao);
        if (numeroCopias < 0) {
            throw new IllegalArgumentException("O número de cópias não pode ser negativo.");
        }
        for (Livro l : livros) {
            if (l.isbn.equals(isbn)) {
                throw new IllegalArgumentException("Já existe um livro com esse ISBN.");
            }
        }
        Livro livro = new Livro(isbn, titulo, autor, anoPublicacao, numeroCopias);
        livros.add(livro);
        return livro;
    }

    public static void editarLivro(String isbn, String titulo, String autor, int anoPublicacao,
            int numeroCopias, int disponiveis) {
        if (!validarIsbn(isbn)) {
            throw new IllegalArgumentException("ISBN inválido.");
        }
        isbn = normalizarIsbn(isbn);
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("Título e autor são obrigatórios.");
        }
        validarAno(anoPublicacao);
        if (numeroCopias < 0 || disponiveis < 0 || disponiveis > numeroCopias) {
            throw new IllegalArgumentException("Quantidades inválidas: 0 <= disponíveis <= número de cópias.");
        }

        int emprestadasAtuais = numeroCopias - disponiveis;
        if (numeroCopias - disponiveis != emprestadasAtuais) {
            throw new IllegalArgumentException(
                    "Quantidades inconsistentes: há " + emprestadasAtuais + " cópia(s) emprestada(s).");
        }
        Livro livro = criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias);
        String ret = EditarApi.editarNaAPI(livro, TelaBase.getCliente(), "/Livro/atualizar");
        System.out.println(ret);
    }

    public static void excluirLivro(Livro livro) {
        String ret = DeletarAPI.deletar(livro.getIsbn(), TelaBase.getCliente(), "/Livro/deletar");
        System.out.println(ret);
    }

    private static void validarAno(int ano) {
        if (ano <= 0 || ano > Year.now().getValue()) {
            throw new IllegalArgumentException("Ano de publicação inválido.");
        }
    }

    public static String normalizarIsbn(String isbn) {
        if (isbn == null) {
            return "";
        }
        return isbn.replaceAll("[^0-9Xx]", "").toUpperCase();
    }

    public static boolean validarIsbn(String isbn) {
        String s = normalizarIsbn(isbn);
        if (s.length() == 10) {
            return validarIsbn10(s);
        }
        if (s.length() == 13) {
            return validarIsbn13(s);
        }
        return false;
    }

    private static boolean validarIsbn10(String s) {
        int soma = 0;
        for (int i = 0; i < 10; i++) {
            char c = s.charAt(i);
            int valor;
            if (c == 'X') {
                if (i != 9) {
                    return false;
                }
                valor = 10;
            } else {
                valor = c - '0';
            }
            soma += valor * (10 - i);
        }
        return soma % 11 == 0;
    }

    private static boolean validarIsbn13(String s) {
        int soma = 0;
        for (int i = 0; i < 13; i++) {
            char c = s.charAt(i);
            if (c == 'X') {
                return false;
            }
            int valor = c - '0';
            soma += (i % 2 == 0) ? valor : valor * 3;
        }
        return soma % 10 == 0;
    }

    public static List<Livro> listarLivros() {
        return ListarAPI.listar(TelaBase.getCliente(), "/Livro", Livro.class);
    }
}
