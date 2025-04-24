package br.edu.ifes.mestrado.camunda.dao.implementations;

import br.edu.ifes.mestrado.camunda.dao.interfaceDAO.IDissertacaoDAO;
import br.edu.ifes.mestrado.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DissertacaoDAO implements IDissertacaoDAO {
    @Override
    public int inserir(int idDefesa, String url) {
        String sql = "UPDATE Defesa SET dissertacao = ? WHERE iddefesa = ?";

        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, url);
            stmt.setInt(2, idDefesa);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Dissertação atualizada com sucesso para a defesa ID " + idDefesa);
            } else {
                System.out.println("Nenhuma linha foi atualizada. Verifique se o ID de defesa existe.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a dissertação", e);
        }
        return idDefesa;
    }

}
