import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    public static Connection connection;

    public static void initializeDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/school.db");
        } catch (Exception e) {
            System.out.println("No suitable driver found for sqlite3");
        }
    }
    public static void close(){
        try {
            connection.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
