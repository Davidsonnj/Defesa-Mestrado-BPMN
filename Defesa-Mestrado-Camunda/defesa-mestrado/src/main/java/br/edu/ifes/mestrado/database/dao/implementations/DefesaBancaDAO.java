package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.database.dao.interfaceDAO.IDefesaBancaDAO;
import br.edu.ifes.mestrado.database.DatabaseConnection;

import java.sql.*;

public class DefesaBancaDAO implements IDefesaBancaDAO {

    @Override
    public int inserir(int idDefesa, int idBanca) throws SQLException {
        String sql = "INSERT INTO Defesa_Banca (iddefesa, idbanca) VALUES (?, ?)";
        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, idDefesa);
            stmt.setInt(2, idBanca);

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
