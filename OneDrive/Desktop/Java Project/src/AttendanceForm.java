import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;

public class AttendanceForm extends JFrame {

    JTextField idField, nameField, subjectField;
    JComboBox<String> statusBox;
    JDateChooser dateChooser;

    JButton btnAdd, btnUpdate, btnDelete, btnClear, btnView;

    Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student_db",
                "root",
                "Akshat@7509"
        );
    }

    public AttendanceForm() {

        setTitle("Attendance System");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(90, 100, 200));
        JLabel title = new JLabel("ATTENDANCE SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        idField = new JTextField(15);
        form.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Subject:"), gbc);

        gbc.gridx = 1;
        subjectField = new JTextField(15);
        form.add(subjectField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        form.add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        statusBox = new JComboBox<>(new String[]{"P", "A"});
        form.add(statusBox, gbc);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        btnAdd = new JButton("Mark");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnView = new JButton("View All");

        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);
        buttons.add(btnView);

        add(buttons, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> insertData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearFields());
        btnView.addActionListener(e -> viewData());

        setVisible(true);
    }

    void insertData() {
        try {
            Connection con = getConnection();

            String query = "INSERT INTO attendance (id, name, subject, date, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, Integer.parseInt(idField.getText()));
            pst.setString(2, nameField.getText());
            pst.setString(3, subjectField.getText());

            String date = new SimpleDateFormat("dd/MM/yyyy").format(dateChooser.getDate());
            pst.setString(4, date);

            pst.setString(5, statusBox.getSelectedItem().toString());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Record Added");
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void updateData() {
        try {
            Connection con = getConnection();

            String query = "UPDATE attendance SET name=?, subject=?, date=?, status=? WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, nameField.getText());
            pst.setString(2, subjectField.getText());

            String date = new SimpleDateFormat("dd/MM/yyyy").format(dateChooser.getDate());
            pst.setString(3, date);

            pst.setString(4, statusBox.getSelectedItem().toString());
            pst.setInt(5, Integer.parseInt(idField.getText()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Record Updated");
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void deleteData() {
        try {
            Connection con = getConnection();

            String query = "DELETE FROM attendance WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, Integer.parseInt(idField.getText()));
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Record Deleted");
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void viewData() {
        try {
            Connection con = getConnection();

            String query = "SELECT * FROM attendance";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            String[] cols = {"ID", "Name", "Subject", "Date", "Status"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("date"),
                        rs.getString("status")
                });
            }

            JTable table = new JTable(model);
            JScrollPane pane = new JScrollPane(table);

            JFrame frame = new JFrame("Records");
            frame.add(pane);
            frame.setSize(500, 300);
            frame.setVisible(true);

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void clearFields() {
        idField.setText("");
        nameField.setText("");
        subjectField.setText("");
        dateChooser.setDate(null);
        statusBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new AttendanceForm();
    }
}