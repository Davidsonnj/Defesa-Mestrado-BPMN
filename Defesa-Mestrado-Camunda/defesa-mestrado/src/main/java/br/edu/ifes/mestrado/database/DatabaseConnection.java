package br.edu.ifes.mestrado.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static final Dotenv dotenv = Dotenv.load();
    
    private static Connection connection;

    private static final String URL = dotenv.get("URL_BD");
    private static final String USER = dotenv.get("USER_BD");
    private static final String PASSWORD = dotenv.get("PASSWORD_BD");

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
