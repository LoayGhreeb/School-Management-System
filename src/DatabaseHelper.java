import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHelper {
    public static Connection connection;

    public static void initializeDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/school.db");
        } catch (Exception e) {
            System.out.println("No suitable driver found for sqlite3");
        }
    }
}
