package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.database.dao.interfaceDAO.IDefesaDAO;
import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.camunda.model.Defesa;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        String sql = "UPDATE Defesa SET FK_aluno = ?, FK_orientador = ?, FK_coorientador = ?, dataDefesa = ?, localDefesa = ?, tituloTrabalho = ? WHERE idDefesa = ?";

        // Usando try-with-resources para garantir que tudo é fechado automaticamente
        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, defesa.getIdAluno());
            stmt.setInt(2, defesa.getIdOrientador());
            stmt.setObject(3, defesa.getIdCoorientador(), java.sql.Types.INTEGER);

            String dataDefesaString = defesa.getDataDefesa();
            if (dataDefesaString != null && !dataDefesaString.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yyyy][yyyy/MM/dd][yyyy-MM-dd]");

                    LocalDate localDate = LocalDate.parse(dataDefesaString, formatter);

                    stmt.setDate(4, java.sql.Date.valueOf(localDate));

                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Formato de data inválido recebido: " + dataDefesaString, e);
                }
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, defesa.getLocalDefesa());
            stmt.setString(6, defesa.getTituloTrabalho());
            stmt.setInt(7, idDefesa);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizarStatus(int idDefesa, String status) {
        String sql = "UPDATE Defesa SET Status = ? WHERE idDefesa = ?";

        try {
            Connection connection = DatabaseConnection.getInstance();
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, status);
            stmt.setInt(2, idDefesa);

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
