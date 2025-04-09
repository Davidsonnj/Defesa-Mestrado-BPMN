package br.edu.ifes.mestrado.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/bancoemails";
    private static final String USER = "davidson";
    private static final String PASSWORD = "davidson123";

    private DatabaseConnection() {
        // Construtor privado para evitar instâncias
    }

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão com o banco: " + e.getMessage());
        }
    }
}
