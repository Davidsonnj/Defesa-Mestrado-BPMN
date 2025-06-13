package br.edu.ifes.mestrado.camunda.dao.implementations;

import br.edu.ifes.mestrado.camunda.dao.interfaceDAO.IBancaDAO;
import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.database.DatabaseConnection;

import java.sql.*;

public class BancaDAO implements IBancaDAO {
    @Override
    public int inserir(Banca banca) {
        String verificaSql = "SELECT idBanca FROM Banca WHERE email = ?";
        String insertSql = "INSERT INTO Banca (minicurriculo, instituicao, nome, email) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE Banca SET minicurriculo = ?, instituicao = ?, nome = ? WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement verificaStmt = connection.prepareStatement(verificaSql)) {

            verificaStmt.setString(1, banca.getEmail());
            ResultSet rsVerifica = verificaStmt.executeQuery();

            if (rsVerifica.next()) {
                int idExistente = rsVerifica.getInt("idBanca");

                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, banca.getMinicurriculo());
                    updateStmt.setString(2, banca.getInstituicao());
                    updateStmt.setString(3, banca.getNome());
                    updateStmt.setString(4, banca.getEmail());

                    updateStmt.executeUpdate();
                    System.out.println("Banca com email '" + banca.getEmail() + "' já existe e os dados foram atualizado com sucesso! ID: " + idExistente);
                    return idExistente;
                }

            } else {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, banca.getMinicurriculo());
                    insertStmt.setString(2, banca.getInstituicao());
                    insertStmt.setString(3, banca.getNome());
                    insertStmt.setString(4, banca.getEmail());

                    insertStmt.executeUpdate();
                    ResultSet rs = insertStmt.getGeneratedKeys();
                    if (rs.next()) {
                        int idGerado = rs.getInt(1);
                        System.out.println("Banca salva com sucesso! ID: " + idGerado);
                        return idGerado;
                    }

                    System.out.println("Banca salva, mas ID não retornou.");
                    return -1;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
