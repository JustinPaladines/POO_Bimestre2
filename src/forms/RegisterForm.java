package forms;


import javax.swing.*;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RegisterForm extends JFrame {
    private JPanel mainPanel;
    private JTextField txtUsuario;
    private JPasswordField txtContra;
    private JPasswordField txtConfirmar;
    private JButton btnRegistrar;
    private JButton btnVolver;

    public RegisterForm() {
        setTitle("Registro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 320);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);

        btnRegistrar.addActionListener(e -> registrar());

        if (btnVolver != null) {
            btnVolver.addActionListener(e -> {
                dispose();
                new LoginForm();
            });
        }
    }

    private void registrar() {
        String u = txtUsuario.getText().trim();
        String p1 = new String(txtContra.getPassword());
        String p2 = new String(txtConfirmar.getPassword());

        if (u.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO usuarios(username, password, saldo, activo) VALUES(?, ?, 1000.00, TRUE)";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, u);
            ps.setString(2, p1);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registrado correctamente. Inicia sesión.", "OK", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginForm();

        } catch (SQLException ex) {
            // Si el username ya existe (por UNIQUE), MySQL lanza un error de integridad.
            if ("23000".equals(ex.getSQLState())) {
                JOptionPane.showMessageDialog(this, "Ese usuario ya existe. Elige otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
