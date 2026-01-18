import java.sql.*;

public class DatabaseConnection {
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "college_events";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = System.getenv("MYSQL_PASSWORD") != null ? 
        System.getenv("MYSQL_PASSWORD") : "";
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + 
        DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
    
    public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
