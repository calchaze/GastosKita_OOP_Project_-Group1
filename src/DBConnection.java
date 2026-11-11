import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Handles the MySQL connection. Change the defaults below or set env variables. */
public class DBConnection {
    private static final String HOST = env("GASTOS_DB_HOST", "localhost");
    private static final String PORT = env("GASTOS_DB_PORT", "3306");
    private static final String USER = env("GASTOS_DB_USER", "root");
    private static final String PASS = env("GASTOS_DB_PASS", "");   // <-- put your MySQL password here
    private static final String DB   = "gastoskita";
    private static final String OPTS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DB + OPTS, USER, PASS);
    }

    /** Creates the database and tables if they do not exist yet. */
    public static void initSchema() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + OPTS, USER, PASS);
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB);
            st.executeUpdate("USE " + DB);
            st.executeUpdate("CREATE TABLE IF NOT EXISTS budget (id INT PRIMARY KEY, "
                    + "total_budget DECIMAL(12,2) NOT NULL DEFAULT 0)");
            st.executeUpdate("INSERT IGNORE INTO budget (id, total_budget) VALUES (1, 0)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS expenses (id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "amount DECIMAL(12,2) NOT NULL, category VARCHAR(30) NOT NULL, "
                    + "description VARCHAR(255) NOT NULL, expense_date DATE NOT NULL, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
    }
}
