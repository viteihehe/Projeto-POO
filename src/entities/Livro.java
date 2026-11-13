package entities;
public class Livro {
    
    private String isbn;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private int numeroCopias;
    private int disponiveis;

    /**
     * @return String return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return String return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return String return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * @return int return the anoPublicacao
     */
    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    /**
     * @param anoPublicacao the anoPublicacao to set
     */
    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    /**
     * @return int return the numeroCopias
     */
    public int getNumeroCopias() {
        return numeroCopias;
    }

    /**
     * @param numeroCopias the numeroCopias to set
     */
    public void setNumeroCopias(int numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    /**
     * @return int return the disponiveis
     */
    public int getDisponiveis() {
        return disponiveis;
    }

    /**
     * @param disponiveis the disponiveis to set
     */
    public void setDisponiveis(int disponiveis) {
        this.disponiveis = disponiveis;
    }

}
