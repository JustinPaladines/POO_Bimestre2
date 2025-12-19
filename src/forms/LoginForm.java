package forms;

import db.DBConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JPanel MainPanel;
    private JTextField txtUsuario;
    private JPasswordField txtContra;
    private JButton btnLogin;
    private JButton btnRegister;

    private int intentos = 0;
    private final int MAX_INTENTOS = 3;

    public LoginForm() {
        setTitle("Inicio de sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(315, 300);
        setContentPane(MainPanel);
        setLocationRelativeTo(null);
        setVisible(true);


        btnLogin.addActionListener(e -> login());


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();      // opcional, para cerrar login
                new RegisterForm();
            }
        });
    }

    private void login() {
        String u = txtUsuario.getText().trim();
        String p = new String(txtContra.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Completa usuario y contraseña.",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String sql = "SELECT 1 FROM usuarios WHERE username=? AND password=? AND activo=TRUE";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, u);
            ps.setString(2, p);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "ACCESO PERMITIDO",
                            "INGRESANDO",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                    new BancoForm(u);
                    return;
                }
            }

            // Si llegó aquí, no encontró usuario válido
            intentos++;
            int restantes = MAX_INTENTOS - intentos;

            if (restantes > 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Credenciales inválidas. Te quedan " + restantes + " intento(s).",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
                txtUsuario.setText("");
                txtContra.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Acceso bloqueado: alcanzaste el máximo de intentos.",
                        "BLOQUEADO",
                        JOptionPane.ERROR_MESSAGE
                );
                btnLogin.setEnabled(false);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error conectando a la BD: " + ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
