import java.util.Date;

public class Emprestimo {
    
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;
    private boolean devolvido;

    /**
     * @return Date return the dataEmprestimo
     */
    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    /**
     * @param dataEmprestimo the dataEmprestimo to set
     */
    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    /**
     * @return Date return the dataDevolucaoPrevista
     */
    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    /**
     * @param dataDevolucaoPrevista the dataDevolucaoPrevista to set
     */
    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    /**
     * @return Date return the dataDevolucaoReal
     */
    public Date getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    /**
     * @param dataDevolucaoReal the dataDevolucaoReal to set
     */
    public void setDataDevolucaoReal(Date dataDevolucaoReal) {
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
