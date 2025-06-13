package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.database.dao.interfaceDAO.IDefesaDAO;
import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.camunda.model.Defesa;

import java.sql.*;

public class DefesaDAO implements IDefesaDAO {
    @Override
    public int inserir(Defesa defesa) {
        String sql = "INSERT INTO Defesa (FK_aluno, dataDefesa, localDefesa, tituloTrabalho) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, defesa.getIdAluno());
            stmt.setTimestamp(2, defesa.combinarDataHora());
            stmt.setString(3, defesa.getLocalDefesa());
            stmt.setString(4, defesa.getTituloTrabalho());

            stmt.executeUpdate();

            // Aqui você pode inserir na tabela associativa defesa_banca
            // (FK_defesa, FK_banca) para cada membro da banca
            // Isso depende se o ID da defesa é gerado automaticamente ou você já tem

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
}
