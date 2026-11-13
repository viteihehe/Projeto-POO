package entities;
import java.time.LocalDate;

public class Emprestimo {
    
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private boolean devolvido;

    /**
     * @return Date return the dataEmprestimo
     */
    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    /**
     * @param dataEmprestimo the dataEmprestimo to set
     */
    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    /**
     * @return LocalDate return the dataDevolucaoPrevista
     */
    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    /**
     * @param dataDevolucaoPrevista the dataDevolucaoPrevista to set
     */
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    /**
     * @return LocalDate return the dataDevolucaoReal
     */
    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    /**
     * @param dataDevolucaoReal the dataDevolucaoReal to set
     */
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    /**
     * @return boolean return the devolvido
     */
    public boolean isDevolvido() {
        return devolvido;
    }

    /**
     * @param devolvido the devolvido to set
     */
    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }
}
