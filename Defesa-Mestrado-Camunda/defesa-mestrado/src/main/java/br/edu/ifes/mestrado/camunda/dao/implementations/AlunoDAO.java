package br.edu.ifes.mestrado.camunda.dao.implementations;

import br.edu.ifes.mestrado.camunda.dao.interfaceDAO.IAlunoDAO;
import br.edu.ifes.mestrado.camunda.model.Aluno;
import br.edu.ifes.mestrado.database.DatabaseConnection;

import java.sql.*;

public class AlunoDAO implements IAlunoDAO {
    @Override
    public int inserir(Aluno aluno) {
        String sql = "INSERT INTO Aluno (nome, email) VALUES (?, ?)";

        try{
            Connection connection = DatabaseConnection.getInstance();
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
            System.out.println("Aluno salvo com sucesso! Id nao retornou.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
