package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.camunda.model.Coorientador;
import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.database.dao.interfaceDAO.ICoorientadorDAO;

import java.sql.*;

public class CoorientadorDAO implements ICoorientadorDAO {
    @Override
    public int inserir(Coorientador coorientador) {
        String verificaSql = "SELECT idCoorientador FROM Coorientador WHERE email = ?";
        String updateSql = "UPDATE idCoorientador SET nome = ? WHERE email = ?";
        String sql = "INSERT INTO idCoorientador(nome, email) VALUES (?, ?)";


        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement verificaStmt = connection.prepareStatement(verificaSql)) {

            verificaStmt.setString(1, coorientador.getEmail());
            ResultSet rsVerifica = verificaStmt.executeQuery();

            if (rsVerifica.next()) {
                try(PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    int idExistente = rsVerifica.getInt("idCoorientador");
                    updateStmt.setString(1, coorientador.getNome());
                    updateStmt.setString(2, coorientador.getEmail());

                    updateStmt.executeUpdate();

                    System.out.println("Coorientador com email '" + coorientador.getEmail() + "' já existe e os dados foram atualizado com sucesso! ID: " + idExistente);

                    return idExistente;
                }

            }

            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, coorientador.getNome());
            stmt.setString(2, coorientador.getEmail());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Coorientador salvo com sucesso! ID: " + idGerado);
                return idGerado;
            }

            System.out.println("Coorientador salvo com sucesso! Id não retornou.");
            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
