package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.database.dao.interfaceDAO.IDefesaDAO;
import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.camunda.model.Defesa;

import java.sql.*;

public class DefesaDAO implements IDefesaDAO {
    @Override
    public int inserir(Defesa defesa) {

        String sql = "INSERT INTO Defesa (FK_aluno, FK_orientador, FK_coorientador, dataDefesa, localDefesa, tituloTrabalho) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, defesa.getIdAluno());
            stmt.setInt(2, defesa.getIdOrientador());
            stmt.setInt(3, defesa.getIdCoorientador());
            stmt.setTimestamp(4, defesa.combinarDataHora());
            stmt.setString(5, defesa.getLocalDefesa());
            stmt.setString(6, defesa.getTituloTrabalho());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Defesa salvo com sucesso! ID: " + idGerado);
                return idGerado;
            }
            System.out.println("Defesa salvo com sucesso! Id nao retornou.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int criarInstanciaVazia(){

        String sql = "INSERT INTO Defesa DEFAULT VALUES";

        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Defesa salvo com sucesso! ID: " + idGerado);
                return idGerado;
            }
            System.out.println("Defesa salvo com sucesso! Id nao retornou.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void atualizar(int idDefesa, Defesa defesa) {

        String sql = "UPDATE Defesa SET FK_aluno = ?, FK_orientador = ?, FK_coorientador= ?, dataDefesa = ?, localDefesa = ?, tituloTrabalho = ?) WHERE = ";

        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
