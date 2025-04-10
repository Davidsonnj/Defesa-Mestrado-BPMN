package br.edu.ifes.mestrado.camunda.dao.implementations;

import br.edu.ifes.mestrado.camunda.dao.interfaceDAO.IBancaDAO;
import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.database.DatabaseConnection;

import java.sql.*;

public class BancaDAO implements IBancaDAO {
    @Override
    public int inserir(Banca banca) {
        String sql = "INSERT INTO Banca (minicurriculo, instituicao, nome, email) VALUES (?, ?, ?, ?)";

        try{
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, banca.getMinicurriculo());
            stmt.setString(2, banca.getInstituicao());
            stmt.setString(3, banca.getNome());
            stmt.setString(4, banca.getEmail());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Banca salvo com sucesso! ID: " + idGerado);
                return idGerado;
            }
            System.out.println("Banca salvo com sucesso! Id nao retornou.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
