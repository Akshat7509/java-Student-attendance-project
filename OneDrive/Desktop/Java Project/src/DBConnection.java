import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student_db",
                "root",
                "password"
            );
            return con;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}