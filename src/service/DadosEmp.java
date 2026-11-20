package service;

import entities.Emprestimo;
import entities.Livro;
import entities.Membro;

public class DadosEmp {

    private Emprestimo emp;
    private Membro membro;
    private Livro livro;

    public DadosEmp(Emprestimo emp, Membro membro, Livro livro) {
        this.emp = emp;
        this.membro = membro;
        this.livro = livro;
    }

    public Emprestimo getEmp() {
        return emp;
    }

    public Membro getMembro() {
        return membro;
    }

    public Livro getLivro() {
        return livro;
    }

}
