package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.database.dao.interfaceDAO.IDefesaBancaDAO;
import br.edu.ifes.mestrado.database.dao.interfaceDAO.IDefesaEmailDAO;

import java.sql.*;

public class DefesaEmailsDAO implements IDefesaEmailDAO {

    @Override
    public int inserir(int idDefesa, long idEmail) throws SQLException {
        String sql = "INSERT INTO Defesa_Email (idDefesa, idEmail) VALUES (?, ?)";
        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, idDefesa);
            stmt.setLong(2, idEmail);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Email salvo com sucesso! ID: " + idGerado);
                return idGerado;
            }
            System.out.println("Email salvo com sucesso! Id nao retornou.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
