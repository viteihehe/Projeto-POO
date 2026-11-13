package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import entities.Bibliotecario;
import entities.Emprestimo;
import entities.Livro;
import entities.Membro;
import interfaces.DataBase;

public class DataBaseImplementation implements DataBase {
    final Properties props = new Properties();

    public DataBaseImplementation() {
        props.setProperty("user", "raiz");
        props.setProperty("password", "...");        
    }
    
    @Override
    public void adicionarMembro(Membro novoMembro) {
        String comando = "INSERT INTO membro (nome, cpf, login, senha, ativo, endereco, telefone, email) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ? , ?) ON CONFLICT (codmembro) DO NOTHING";
        try (Connection conn = DriverManager.getConnection(url, props);
            PreparedStatement st = conn.prepareStatement(comando)){
                st.setString(1, novoMembro.getNome());
                st.setString(2, novoMembro.getCpf());
                st.setString(3, novoMembro.getLogin());
                st.setString(4, novoMembro.getSenha());
                st.setBoolean(5, novoMembro.isAtivo());
                st.setString(6, novoMembro.getEndereco());
                st.setString(7, novoMembro.getTelefone());
                st.setString(8, novoMembro.getEmail());
                st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro na inserção do membro:"+e.getMessage());
        }
    }
    
    @Override
    public void adicionarBibliotecario(Bibliotecario novoBibliotecario) {
        String comando = "INSERT INTO bibliotecario (nome, cpf, login, senha, ativo, cargo)" +
                         "VALUES (?,?,?,?,?,?) ON CONFLICT (codbibli) DO NOTHING";
        try(Connection conn = DriverManager.getConnection(url, props);
            PreparedStatement st = conn.prepareStatement(comando)) {
            st.setString(1, novoBibliotecario.getNome());
            st.setString(2, novoBibliotecario.getCpf());
            st.setString(3, novoBibliotecario.getLogin());
            st.setString(4, novoBibliotecario.getSenha());
            st.setBoolean(5, novoBibliotecario.isAtivo());
            st.setString(6, novoBibliotecario.getCargo());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro na inserção do bibliotecario:"+e.getMessage());
        }
    }

    @Override 
    public void adicionarEmprestimo(Emprestimo novoEmprestimo, Membro membro, Livro livro) {
        String buscaIdMembro = "SELECT codmembro FROM membro WHERE login = ?";
        String buscaIdLivro = "SELECT codlivro FROM livro WHERE isbn = ?";
        String comando = "INSERT INTO emprestimo (dataemprestimo, datadevolprevista, datadevolreal, devolvido, codlivro, codmembro) VALUES (?,?,?,?,?,?) ON CONFLICT DO NOTHING";

        try (Connection conn = DriverManager.getConnection(url, props);){
            int idMembro = -1;
            int idLivro = -1;
            try(PreparedStatement busca = conn.prepareStatement(buscaIdMembro)) {
                busca.setString(1, membro.getLogin());
                try (ResultSet rs = busca.executeQuery()) {
                    if(rs.next()) {
                        idMembro = rs.getInt("codmembro");
                    }
                    else {
                        System.out.println("Membro não encontrado!");
                        return;
                    }
                } 
            }catch(SQLException e) {
                System.out.println("Erro no comando para pesquisa no banco (Membro)!");
                return;
            }

            try (PreparedStatement busca = conn.prepareStatement(buscaIdLivro)){
                busca.setString(1, livro.getIsbn());
                try (ResultSet rs = busca.executeQuery()){
                    if(rs.next()) {
                        idLivro = rs.getInt("codlivro");
                    }
                    else {
                        System.out.println("Livro não foi encontrado!");
                        return;
                    }
                } 
            } catch (SQLException e) {
                System.out.println("Erro no comando de pesquisa do banco (Livro)!");
                return;
            }

        try (PreparedStatement st = conn.prepareStatement(comando)) {
            st.setObject(1, novoEmprestimo.getDataEmprestimo());
            st.setObject(2, novoEmprestimo.getDataDevolucaoPrevista());
            st.setObject(3, novoEmprestimo.getDataDevolucaoReal());
            st.setBoolean(4, novoEmprestimo.isDevolvido());
            st.setInt(5, idLivro);
            st.setInt(6, idMembro);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro na inserção do emprestimo");
        }

        } catch (SQLException e) {
            System.out.println("Erro de conexão com o servidor!");
        }
    }

    @Override
    public void adicionarLivro(Livro novoLivro) {
        String comando = "INSERT INTO livro (isbn, titulo, anopublicacao, numerocopias, disponiveis)" +
                         "VALUES (?,?,?,?,?) ON CONFLICT (codlivro) DO NOTHING";

        try (Connection conn = DriverManager.getConnection(url, props);
            PreparedStatement st = conn.prepareStatement(comando)){
            st.setString(1, novoLivro.getIsbn());
            st.setString(2, novoLivro.getTitulo());
            st.setInt(3, novoLivro.getAnoPublicacao());
            st.setInt(4, novoLivro.getNumeroCopias());
            st.setInt(5, novoLivro.getDisponiveis());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro na inserção do livro!");
        }
    }

}