package entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Emprestimo {

    private static final int PRAZO_DIAS = 14;
    private static final List<Emprestimo> emprestimos = new ArrayList<>();

    private Livro livro;
    private Membro membro;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;
    private boolean devolvido;

    public Emprestimo(Livro livro, Membro membro) {
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = new Date();
        this.dataDevolucaoPrevista = somarDias(this.dataEmprestimo, PRAZO_DIAS);
        this.dataDevolucaoReal = null;
        this.devolvido = false;
    }

    public Livro getLivro() {
        return livro;
    }

    public Membro getMembro() {
        return membro;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public Date getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        if (dataEmprestimo == null) {
            throw new IllegalArgumentException("A data de empréstimo é obrigatória.");
        }
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
        if (dataDevolucaoPrevista == null) {
            throw new IllegalArgumentException("A data de devolução prevista é obrigatória.");
        }
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public void setDataDevolucaoReal(Date dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    public void verEmprestimo() {
        System.out.println(this);
    }

    public void registrarDevolucao() {
        if (devolvido) {
            throw new IllegalStateException("Este empréstimo já foi devolvido.");
        }
        this.dataDevolucaoReal = new Date();
        this.devolvido = true;
        livro.devolverCopia();
    }

    public static boolean membroSuspenso(Membro membro) {
        Date agora = new Date();
        for (Emprestimo e : emprestimos) {
            if (e.membro != membro) {
                continue;
            }
            int multa = e.calcularMulta();
            if (multa <= 0) {
                continue;
            }
            if (!e.devolvido) {
                return true;
            }
            if (agora.before(somarDias(e.dataDevolucaoReal, multa))) {
                return true;
            }
        }
        return false;
    }

    public int calcularMulta() {
        Date referencia = devolvido && dataDevolucaoReal != null ? dataDevolucaoReal : new Date();
        if (!referencia.after(dataDevolucaoPrevista)) {
            return 0;
        }
        long diferenca = referencia.getTime() - dataDevolucaoPrevista.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(diferenca);
    }

    public static Emprestimo criarEmprestimo(Livro livro, Membro membro) {
        if (livro == null || membro == null) {
            throw new IllegalArgumentException("Livro e membro são obrigatórios.");
        }
        if (!membro.isAtivo()) {
            throw new IllegalStateException("O membro está desativado.");
        }
        if (membroSuspenso(membro)) {
            throw new IllegalStateException("O membro está suspenso e não pode realizar empréstimos.");
        }
        livro.emprestarCopia();
        Emprestimo emprestimo = new Emprestimo(livro, membro);
        emprestimos.add(emprestimo);
        return emprestimo;
    }

    public static void editarEmprestimo(Emprestimo emprestimo, Date dataEmprestimo, Date dataDevolucaoPrevista,
                                       Date dataDevolucaoReal, boolean devolvido) {
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não informado.");
        }
        if (dataEmprestimo == null || dataDevolucaoPrevista == null) {
            throw new IllegalArgumentException("As datas de empréstimo e de devolução prevista são obrigatórias.");
        }
        if (dataDevolucaoPrevista.before(dataEmprestimo)) {
            throw new IllegalArgumentException("A devolução prevista não pode ser anterior ao empréstimo.");
        }
        if (devolvido && dataDevolucaoReal == null) {
            throw new IllegalArgumentException("Informe a data de devolução real para um empréstimo devolvido.");
        }
        if (!devolvido && dataDevolucaoReal != null) {
            throw new IllegalArgumentException("Empréstimo não devolvido não pode ter data de devolução real.");
        }
        if (dataDevolucaoReal != null && dataDevolucaoReal.before(dataEmprestimo)) {
            throw new IllegalArgumentException("A devolução real não pode ser anterior ao empréstimo.");
        }
        if (devolvido && !emprestimo.devolvido) {
            emprestimo.livro.devolverCopia();
        } else if (!devolvido && emprestimo.devolvido) {
            emprestimo.livro.emprestarCopia();
        }
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataDevolucaoPrevista(dataDevolucaoPrevista);
        emprestimo.setDataDevolucaoReal(dataDevolucaoReal);
        emprestimo.setDevolvido(devolvido);
    }

    public static void excluirEmprestimo(Emprestimo emprestimo) {
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não informado.");
        }
        if (!emprestimo.devolvido) {
            emprestimo.livro.devolverCopia();
        }
        emprestimos.remove(emprestimo);
    }

    public static List<Emprestimo> listarEmprestimos() {
        return new ArrayList<>(emprestimos);
    }

    public static List<Emprestimo> listarEmprestimosAtrasados() {
        List<Emprestimo> lista = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.estaAtrasado()) {
                lista.add(e);
            }
        }
        return lista;
    }

    public static List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) {
        List<Emprestimo> lista = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.membro == membro && e.estaAtrasado()) {
                lista.add(e);
            }
        }
        return lista;
    }

    public static List<Emprestimo> listarEmprestimosAtivos() {
        List<Emprestimo> lista = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (!e.devolvido) {
                lista.add(e);
            }
        }
        return lista;
    }

    public static List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        List<Emprestimo> lista = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.membro == membro && !e.devolvido) {
                lista.add(e);
            }
        }
        return lista;
    }

    private boolean estaAtrasado() {
        return !devolvido && new Date().after(dataDevolucaoPrevista);
    }

    private static Date somarDias(Date data, int dias) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DAY_OF_MONTH, dias);
        return c.getTime();
    }

    @Override
    public String toString() {
        return "Livro: " + livro.getTitulo()
                + "\nMembro: " + membro.getNome()
                + "\nEmpréstimo: " + dataEmprestimo
                + "\nDevolução prevista: " + dataDevolucaoPrevista
                + "\nDevolução real: " + (dataDevolucaoReal == null ? "-" : dataDevolucaoReal)
                + "\nDevolvido: " + devolvido
                + "\nMulta (dias): " + calcularMulta() + "\n";
    }
}