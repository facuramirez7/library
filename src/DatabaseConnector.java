import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    //Path de la base de datos
    private static final String url = "jdbc:sqlite:src/database/library.db";

    //Conexión con la base de datos
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            createTables(conn);  // Llamar a la función para crear tablas
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    private static void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            // Crear la tabla solo si no existe
            String createTableSQL = "CREATE TABLE IF NOT EXISTS books ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT,"
                    + "author TEXT,"
                    + "genre TEXT,"
                    + "status TEXT"
                    + ")";
            statement.executeUpdate(createTableSQL);

            System.out.println("Table 'books' created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
