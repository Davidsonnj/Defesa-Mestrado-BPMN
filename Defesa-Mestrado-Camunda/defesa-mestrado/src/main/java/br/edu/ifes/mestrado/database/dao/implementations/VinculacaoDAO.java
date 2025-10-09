package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.camunda.model.VinculacaoAluno;
import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.database.dao.interfaceDAO.IVinculacaoDAO;

import java.sql.*;

public class VinculacaoDAO implements IVinculacaoDAO {

    /**
     * Insere um novo registro de vinculação de aluno no banco de dados.
     *
     * @param vinculacao O objeto VinculacaoAluno contendo todos os dados a serem inseridos.
     */
    @Override
    public void insert(VinculacaoAluno vinculacao) {
        // SQL com placeholders (?) para evitar SQL Injection.
        // Os nomes das colunas são os que definimos no script SQL (snake_case).
        String sql = "INSERT INTO vinculacao_aluno " +
                "(email_id, aluno_nome, matricula, email_aluno, tema, periodo_inicio) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Atribui os valores do objeto aos placeholders do SQL.
            // A ordem deve ser a mesma da declaração VALUES.
            stmt.setLong(1, vinculacao.getEmailId());
            stmt.setString(2, vinculacao.getAlunoNome());
            stmt.setString(3, vinculacao.getMatricula());
            stmt.setString(4, vinculacao.getEmailAluno());
            stmt.setString(5, vinculacao.getTema());
            stmt.setString(6, vinculacao.getPeriodoInicio());

            // Executa a inserção.
            int affectedRows = stmt.executeUpdate();

            // Verifica se a inserção foi bem-sucedida.
            if (affectedRows == 0) {
                throw new SQLException("A inserção da vinculação falhou, nenhuma linha foi afetada.");
            }

            // Recupera e retorna o ID gerado pelo banco de dados.
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long idGerado = rs.getLong(1); // Usamos getLong pois a coluna é BIGINT.
                    System.out.println("Vinculação salva com sucesso! ID gerado: " + idGerado);
                } else {
                    System.err.println("Vinculação salva, mas não foi possível recuperar o ID gerado.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro de SQL ao tentar inserir a vinculação: " + e.getMessage());
            // Lança uma exceção de tempo de execução para sinalizar um erro grave.
            throw new RuntimeException(e);
        }
    }
}