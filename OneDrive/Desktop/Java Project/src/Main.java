import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student_db",
                "root",
                "Akshat@7509"
            );

            String query = "INSERT INTO students(name, age, course) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, "Test");
            pst.setInt(2, 20);
            pst.setString(3, "MCA");

            pst.executeUpdate();

            System.out.println("SUCCESS");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}   