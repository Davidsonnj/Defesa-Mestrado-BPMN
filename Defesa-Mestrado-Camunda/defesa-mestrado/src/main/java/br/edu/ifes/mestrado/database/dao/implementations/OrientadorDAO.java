package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.camunda.model.Orientador;
import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.database.dao.interfaceDAO.IOrientadorDAO;

import java.sql.*;

public class OrientadorDAO implements IOrientadorDAO {
    @Override
    public int inserir(Orientador orientador) {
        String verificaSql = "SELECT idOrientador FROM Orientador WHERE email = ?";
        String updateSql = "UPDATE Orientador SET nome = ? WHERE email = ?";
        String sql = "INSERT INTO Orientador (nome, email) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement verificaStmt = connection.prepareStatement(verificaSql)) {

            verificaStmt.setString(1, orientador.getEmail());
            ResultSet rsVerifica = verificaStmt.executeQuery();

            // Se o orientador já existe, atualiza os dados
            if (rsVerifica.next()) {
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    int idExistente = rsVerifica.getInt("idOrientador");
                    updateStmt.setString(1, orientador.getNome());
                    updateStmt.setString(2, orientador.getEmail());

                    updateStmt.executeUpdate();

                    System.out.println("Orientador com email '" + orientador.getEmail() + "' já existe e os dados foram atualizados com sucesso! ID: " + idExistente);

                    return idExistente;
                }
            }

            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, orientador.getNome());
            stmt.setString(2, orientador.getEmail());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Orientador salvo com sucesso! ID: " + idGerado);
                return idGerado;
            }

            System.out.println("Orientador salvo com sucesso! ID não foi retornado.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}