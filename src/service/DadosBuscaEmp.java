package service;

import java.time.LocalDate;

import entities.Livro;
import entities.Membro;

public class DadosBuscaEmp {

    private LocalDate dataEmp;
    private Membro membro;
    private Livro livro;

    public DadosBuscaEmp(LocalDate dataEmp, Membro membro, Livro livro) {
        this.dataEmp = dataEmp;
        this.membro = membro;
        this.livro = livro;
    }

    public LocalDate getEmp() {
        return dataEmp;
    }

    public Membro getMembro() {
        return membro;
    }

    public Livro getLivro() {
        return livro;
    }
}
