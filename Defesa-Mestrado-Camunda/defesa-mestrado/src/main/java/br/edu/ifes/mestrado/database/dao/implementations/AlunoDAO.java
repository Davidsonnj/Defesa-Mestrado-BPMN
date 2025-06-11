package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.database.dao.interfaceDAO.IAlunoDAO;
import br.edu.ifes.mestrado.camunda.model.Aluno;
import br.edu.ifes.mestrado.database.DatabaseConnection;

import java.sql.*;

public class AlunoDAO implements IAlunoDAO {
    @Override
    public int inserir(Aluno aluno) {
        String verificaSql = "SELECT idAluno FROM Aluno WHERE email = ?";
        String updateSql = "UPDATE Aluno SET nome = ? WHERE email = ?";
        String sql = "INSERT INTO Aluno (nome, email) VALUES (?, ?)";


        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement verificaStmt = connection.prepareStatement(verificaSql)) {

            verificaStmt.setString(1, aluno.getEmail());
            ResultSet rsVerifica = verificaStmt.executeQuery();

            if (rsVerifica.next()) {
                try(PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    int idExistente = rsVerifica.getInt("idAluno");
                    updateStmt.setString(1, aluno.getNome());
                    updateStmt.setString(2, aluno.getEmail());

                    updateStmt.executeUpdate();

                    System.out.println("Aluno com email '" + aluno.getEmail() + "' já existe e os dados foram atualizado com sucesso! ID: " + idExistente);

                    return idExistente;
                }

            }

            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Aluno salvo com sucesso! ID: " + idGerado);
                return idGerado;
            }

            System.out.println("Aluno salvo com sucesso! Id não retornou.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
