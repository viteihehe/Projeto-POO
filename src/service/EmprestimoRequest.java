package service;

import java.time.LocalDate;

public class EmprestimoRequest {

    private LocalDate dataEmprestimo;
    private String login;
    private String isbn;

    public EmprestimoRequest(LocalDate dataEmprestimo, String login, String isbn) {
        this.dataEmprestimo = dataEmprestimo;
        this.login = login;
        this.isbn = isbn;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getLogin() {
        return login;
    }

    public String getIsbn() {
        return isbn;
    }

}
