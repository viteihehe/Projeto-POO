package service;

import java.time.LocalDate;

import entities.Livro;
import entities.Membro;

public class DadosEmp {

    private EmprestimoRequest emp;
    private Membro membro;
    private Livro livro;

    public DadosEmp(EmprestimoRequest emp, Membro membro, Livro livro) {
        this.emp = emp;
        this.membro = membro;
        this.livro = livro;
    }

    public EmprestimoRequest getEmp() {
        return emp;
    }

    public Membro getMembro() {
        return membro;
    }

    public Livro getLivro() {
        return livro;
    }

    public record EmprestimoRequest(
            boolean devolvido,
            LocalDate dataEmprestimo,
            LocalDate dataDevolucaoReal,
            LocalDate dataDevolucaoPrevista
            ) {}

}
