package br.edu.ifes.mestrado.database.dao.implementations;

import br.edu.ifes.mestrado.database.DatabaseConnection;
import br.edu.ifes.mestrado.database.dao.interfaceDAO.IEmailDAO;
import br.edu.ifes.mestrado.emailAPI.model.Email;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailDAO implements IEmailDAO {
    @Override
    public void insert(Email email){
        String sql = "INSERT INTO emails (uid, subject, sender, date, body, attachment_paths, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            Timestamp date = new Timestamp(email.getDate().getTime());

            stmt.setLong(1, email.getUid());
            stmt.setString(2, email.getSubject());
            stmt.setString(3, email.getSender());
            stmt.setTimestamp(4, date);
            stmt.setString(5, email.getBody());
            stmt.setArray(6, connection.createArrayOf("text", email.getAttachmentPaths().toArray()));
            stmt.setString(7, email.getStatus());
            stmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("Erro ao inserir dados do email com UID: " + email.getUid());
        }
    }
    @Override
    public List<Email> findAll() {
        String sql = "SELECT uid, subject, sender, date, body, attachment_paths, status FROM emails";
        List<Email> emails = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Email email = new Email();
                email.setUid(rs.getLong("uid"));
                email.setSubject(rs.getString("subject"));
                email.setSender(rs.getString("sender"));
                email.setDate(rs.getTimestamp("date"));
                email.setBody(rs.getString("body"));
                email.setStatus(rs.getString("status"));


                Array sqlArray = rs.getArray("attachment_paths");
                if (sqlArray != null) {
                    String[] paths = (String[]) sqlArray.getArray();
                    email.setAttachmentPaths(Arrays.asList(paths));
                }

                emails.add(email);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar emails.");
        }

        return emails;
    }
    @Override
    public void update(Email email) {
        String sql = "UPDATE emails SET subject = ?, sender = ?, date = ?, body = ?, attachment_paths = ?, status = ? WHERE uid = ?";

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            Timestamp date = new Timestamp(email.getDate().getTime());

            stmt.setString(1, email.getSubject());
            stmt.setString(2, email.getSender());
            stmt.setTimestamp(3, date);
            stmt.setString(4, email.getBody());

            List<String> attachments = email.getAttachmentPaths();
            if (attachments != null) {
                Array sqlArray = connection.createArrayOf("VARCHAR", attachments.toArray(new String[0]));
                stmt.setArray(5, sqlArray);
            } else {
                stmt.setNull(5, java.sql.Types.ARRAY);
            }

            stmt.setString(6, email.getStatus());
            stmt.setLong(7, email.getUid());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Email atualizado com sucesso!");
            } else {
                System.out.println("Nenhum email encontrado com uid = " + email.getUid());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
