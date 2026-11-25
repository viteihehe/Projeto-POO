package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import UI.TelaBase;
import service.DadosBuscaEmp;
import service.DeletarAPI;
import service.ListarAPI;

public class Emprestimo {

    private static final int PRAZO_DIAS = 14;
    private static List<Emprestimo> emprestimos = new ArrayList<>();

    private Livro livro;
    private Membro membro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private boolean devolvido;

    public Emprestimo(Livro livro, Membro membro) {
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = this.dataEmprestimo.plusDays(PRAZO_DIAS);
        this.dataDevolucaoReal = null;
        this.devolvido = false;
    }

    public Emprestimo() {
    }

    public static List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public static void setEmprestimos(List<Emprestimo> lista) {
        emprestimos = lista;
    }

    public Livro getLivro() {
        return livro;
    }

    public Membro getMembro() {
        return membro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        if (dataEmprestimo == null) {
            throw new IllegalArgumentException("A data de empréstimo é obrigatória.");
        }
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        if (dataDevolucaoPrevista == null) {
            throw new IllegalArgumentException("A data de devolução prevista é obrigatória.");
        }
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
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
        this.dataDevolucaoReal = LocalDate.now();
        this.devolvido = true;
        livro.devolverCopia();
    }

    public static boolean membroSuspenso(Membro membro) {
        LocalDate agora = LocalDate.now();
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
            if (agora.isBefore(e.dataDevolucaoReal.plusDays(multa))) {
                return true;
            }
        }
        return false;
    }

    public int calcularMulta() {
        LocalDate referencia = devolvido && dataDevolucaoReal != null ? dataDevolucaoReal : LocalDate.now();
        if (!referencia.isAfter(dataDevolucaoPrevista)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dataDevolucaoPrevista, referencia);
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

    public static void editarEmprestimo(Emprestimo emprestimo, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista,
            LocalDate dataDevolucaoReal, boolean devolvido) {
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não informado.");
        }
        if (dataEmprestimo == null || dataDevolucaoPrevista == null) {
            throw new IllegalArgumentException("As datas de empréstimo e de devolução prevista são obrigatórias.");
        }
        if (dataDevolucaoPrevista.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A devolução prevista não pode ser anterior ao empréstimo.");
        }
        if (devolvido && dataDevolucaoReal == null) {
            throw new IllegalArgumentException("Informe a data de devolução real para um empréstimo devolvido.");
        }
        if (!devolvido && dataDevolucaoReal != null) {
            throw new IllegalArgumentException("Empréstimo não devolvido não pode ter data de devolução real.");
        }
        if (dataDevolucaoReal != null && dataDevolucaoReal.isBefore(dataEmprestimo)) {
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
        DadosBuscaEmp dadosEmp = new DadosBuscaEmp(emprestimo.dataEmprestimo, emprestimo.membro, emprestimo.livro);
        String ret = DeletarAPI.deletar(dadosEmp, TelaBase.getCliente(), "/emprestimo/deletar");
        System.out.println(ret);
    }

    public static List<Emprestimo> listarEmprestimos() {
        return ListarAPI.listar(TelaBase.getCliente(), "/emprestimo", Emprestimo.class);
    }

    public static List<Emprestimo> listarEmprestimosAtrasados() {
        List<Emprestimo> lista = listarEmprestimos();
        List<Emprestimo> atrasados = new ArrayList<>();
        if (lista != null) {
            for (Emprestimo e : lista) {
                if (e.estaAtrasado()) {
                    atrasados.add(e);
                }
            }
        }
        return atrasados;
    }

    public static List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) {
        List<Emprestimo> lista = ListarAPI.listarPorMembro(TelaBase.getCliente(), "/emprestimo/membro", membro);
        List<Emprestimo> atrasados = new ArrayList<>();
        if (lista != null) {
            for (Emprestimo e : lista) {
                if (e.estaAtrasado()) {
                    atrasados.add(e);
                }
            }
        }
        return atrasados;
    }

    public static List<Emprestimo> listarEmprestimosAtivos() {
        List<Emprestimo> lista = listarEmprestimos();
        List<Emprestimo> ativos = new ArrayList<>();
        if (lista != null) {
            for (Emprestimo e : lista) {
                if (!e.estaAtrasado() && !e.isDevolvido()) {
                    ativos.add(e);
                }
            }
        }
        return ativos;
    }

    public static List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        List<Emprestimo> lista = ListarAPI.listarPorMembro(TelaBase.getCliente(), "/emprestimo/membro", membro);
        List<Emprestimo> ativo = new ArrayList<>();
        if (lista != null) {
            for (Emprestimo e : lista) {
                if (!e.estaAtrasado() && !e.isDevolvido()) {
                    ativo.add(e);
                }
            }
        }
        return ativo;
    }

    private boolean estaAtrasado() {
        return !devolvido && LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    @Override
    public String toString() {
        return "Empréstimo: " + dataEmprestimo
                + "\nDevolução prevista: " + dataDevolucaoPrevista
                + "\nDevolução real: " + (dataDevolucaoReal == null ? "-" : dataDevolucaoReal)
                + "\nDevolvido: " + devolvido
                + "\nMulta (dias): " + calcularMulta() + "\n";
    }

    
    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    
    public void setMembro(Membro membro) {
        this.membro = membro;
    }

}
