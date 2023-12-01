import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLServerExample {
    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:sqlserver://yourServer:1433;databaseName=yourDatabase";
        String user = "yourUsername";
        String password = "yourPassword";

        // JDBC variables for opening, closing and managing connection
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute a sample query
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT * FROM yourTable";
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    // Process the result set
                    while (resultSet.next()) {
                        // Access your data here, e.g., resultSet.getString("columnName")
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
