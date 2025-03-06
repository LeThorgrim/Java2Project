package db.daos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public class DatabaseManager {
    private static final String SQL_FILE_PATH = "/sql/database-creation.sql"; // Fichier SQL dans resources

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sqlScript = readSqlFile(SQL_FILE_PATH);
            stmt.executeUpdate(sqlScript);
            System.out.println("Base de données initialisée avec succès !");
        } catch (SQLException | IOException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        DataSource dataSource = DataSourceFactory.getDataSource();
        return dataSource.getConnection();
    }

    private static String readSqlFile(String resourcePath) throws IOException {
        InputStream inputStream = DatabaseManager.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Fichier SQL introuvable : " + resourcePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
