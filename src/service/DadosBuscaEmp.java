package service;

import java.time.LocalDate;

import entities.Livro;
import entities.Membro;

public class DadosBuscaEmp {

    private LocalDate dataEmp;
    private Membro membroUtil;
    private Livro livroUtil;

    public DadosBuscaEmp(LocalDate dataEmp, Membro membroUtil, Livro livroUtil) {
        this.dataEmp = dataEmp;
        this.membroUtil = membroUtil;
        this.livroUtil = livroUtil;
    }

    public LocalDate getDataEmp() {
        return dataEmp;
    }

    public Membro getMembroUtil() {
        return membroUtil;
    }

    public Livro getLivroUtil() {
        return livroUtil;
    }
}
