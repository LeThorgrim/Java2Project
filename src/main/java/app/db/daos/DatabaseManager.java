package app.db.daos;

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
            //sql file loader
            String sqlScript = readSqlFile(SQL_FILE_PATH);
            stmt.executeUpdate(sqlScript);
            System.out.println("DB initialized");
            //clear db
            stmt.executeUpdate("DELETE FROM person"); // clear table
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='person'"); // AUTO_INCREMENT renit
            //we add 3 persons
            stmt.executeUpdate("INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES " +
                    "('Doe', 'John', 'Johnny', '123456789', '123 Street', 'john.doe@email.com', '1990-01-01'), " +
                    "('Smith', 'Alice', 'Ali', '987654321', '456 Avenue', 'alice.smith@email.com', '1992-05-10'), " +
                    "('Brown', 'Charlie', 'Chuck', '111222333', '789 Boulevard', 'charlie.brown@email.com', '1985-09-23');");


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
