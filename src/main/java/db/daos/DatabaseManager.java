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


//tbf this whole class could have been put in the DataSourceFactory class but it was simpler to create this "wrapper" (kinda)
public class DatabaseManager {
    private static final String SQL_FILE_PATH = "/sql/database-creation.sql"; // for exemple to load our .sql script

    //function to initialize the database with the script quickly
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sqlScript = readSqlFile(SQL_FILE_PATH);
            stmt.executeUpdate(sqlScript);
            System.out.println("DB initialized");
        } catch (SQLException | IOException e) {
            System.err.println("Error when initialized: : " + e.getMessage());
        }
    }

    //function to get a connection to the database using the DataSourceFactory
    public static Connection getConnection() throws SQLException {
        DataSource dataSource = DataSourceFactory.getDataSource();
        return dataSource.getConnection();
    }

    //sql file loader
    private static String readSqlFile(String resourcePath) throws IOException {
        InputStream inputStream = DatabaseManager.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("SQL file not findable: " + resourcePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
