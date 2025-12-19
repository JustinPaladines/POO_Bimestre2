package forms;

import db.DBConnection;

import javax.swing.*;
import java.sql.*;

/*
 * FORM: BancoForm
 * PROPÓSITO:
 * Este formulario representa el “banco” del usuario ya logueado.
 * Aquí ya no trabajo con saldo “en memoria”, sino que:
 * - Leo el saldo real desde la tabla usuarios.
 * - Guardo cada transacción en la tabla movimientos.
 * - Actualizo el saldo en usuarios.
 *
 * Con esto cumplo:
 * - CREATE: Inserto movimientos (y en Register inserto usuarios).
 * - READ: Leo saldo e historial.
 * - UPDATE: Actualizo saldo.
 * - DELETE: Elimino la cuenta (usuario) y sus movimientos.
 */
public class BancoForm extends JFrame {

    private JPanel mainPanel;
    private JButton transferenciaButton;
    private JButton depositoButton;
    private JButton retiroButton;
    private JButton salirButton;
    private JButton btnEliminarCuenta;
    private JTextField txtNombre;
    private JTextField txtSaldo;
    private JTextArea txtHistorial;

    // Username que viene del login
    private final String username;

    // Datos del usuario en BD
    private int userId;     // lo uso como FK en movimientos
    private double saldo;   // saldo actual leído desde la BD

    public BancoForm(String username) {
        this.username = username;

        setTitle("Banco");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 360);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);

        txtNombre.setText(this.username);
        txtHistorial.setEditable(false);

        // 1) Al abrir el banco: cargo usuario (id y saldo) y el historial desde la BD
        cargarUsuarioYSaldo();
        cargarHistorial();

        // 2) Listeners: cada acción llama a un método que hace la operación en BD
        depositoButton.addActionListener(e -> hacerDeposito());
        retiroButton.addActionListener(e -> hacerRetiro());
        transferenciaButton.addActionListener(e -> hacerTransferencia());

        // NUEVO: eliminar cuenta (DELETE)
        btnEliminarCuenta.addActionListener(e -> eliminarCuenta());

        salirButton.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(this, "Saliendo...");
            new LoginForm();
        });
    }

    /* CARGA INICIAL DESDE LA BD */

    private void cargarUsuarioYSaldo() {
        /*
         * Busco el id y saldo real del usuario.
         * Esto reemplaza el saldo fijo de 1000.00 que antes era solo una variable.
         */
        String sql = "SELECT id, saldo FROM usuarios WHERE username=? AND activo=TRUE";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                    saldo = rs.getDouble("saldo");
                    txtSaldo.setText(String.valueOf(saldo));
                } else {
                    // Si no existe o está inactivo, cierro el banco y vuelvo al login
                    JOptionPane.showMessageDialog(this, "Usuario no existe o está inactivo.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    new LoginForm();
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando usuario: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        /*
         * Cargo el historial desde movimientos.
         * Muestro los últimos 50 movimientos.
         */
        String sql = "SELECT tipo, monto, destinatario, fecha " +
                "FROM movimientos WHERE user_id=? ORDER BY fecha DESC LIMIT 50";

        txtHistorial.setText("");

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    double monto = rs.getDouble("monto");
                    String dest = rs.getString("destinatario");
                    Timestamp fecha = rs.getTimestamp("fecha");

                    String linea;
                    if ("TRANSFERENCIA".equals(tipo)) {
                        linea = fecha + " - TRANSFERENCIA a " + dest + ": -$" + monto;
                    } else if ("RETIRO".equals(tipo)) {
                        linea = fecha + " - RETIRO: -$" + monto;
                    } else {
                        linea = fecha + " - DEPOSITO: +$" + monto;
                    }
                    txtHistorial.append(linea + "\n");
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando historial: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
       ACCIONES DEL BANCO (CRUD)
       */

    private void hacerDeposito() {
        String input = JOptionPane.showInputDialog(this, "Monto a depositar:");
        if (input == null) return;

        double monto;
        try {
            monto = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (monto <= 0) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor mayor a 0.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Depósito = aumentar saldo + guardar movimiento
        ejecutarMovimiento("DEPOSITO", monto, null);
    }

    private void hacerRetiro() {
        String input = JOptionPane.showInputDialog(this, "Monto a retirar:");
        if (input == null) return;

        double monto;
        try {
            monto = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (monto <= 0) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor mayor a 0.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (saldo < monto) {
            JOptionPane.showMessageDialog(this, "No existen suficientes fondos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retiro = disminuir saldo + guardar movimiento
        ejecutarMovimiento("RETIRO", monto, null);
    }

    private void hacerTransferencia() {
        String destinatario = JOptionPane.showInputDialog(this, "Nombre del destinatario:");
        if (destinatario == null || destinatario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Destinatario obligatorio.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Monto a transferir:");
        if (input == null) return;

        double monto;
        try {
            monto = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (monto <= 0) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor mayor a 0.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (saldo < monto) {
            JOptionPane.showMessageDialog(this, "No existen suficientes fondos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Transferencia = disminuir saldo + guardar movimiento con destinatario
        ejecutarMovimiento("TRANSFERENCIA", monto, destinatario.trim());
    }

    /*
     * Método central para ejecutar cualquier operación del banco.
     * Hace 2 operaciones en la BD:
     * 1) UPDATE del saldo en usuarios
     * 2) INSERT del movimiento en movimientos
     *
     * Se usa transacción: si falla algo, hago rollback y no se guarda nada a medias.
     */
    private void ejecutarMovimiento(String tipo, double monto, String destinatario) {

        String sqlUpdate;
        if ("DEPOSITO".equals(tipo)) {
            sqlUpdate = "UPDATE usuarios SET saldo = saldo + ? WHERE id=?";
        } else {
            sqlUpdate = "UPDATE usuarios SET saldo = saldo - ? WHERE id=?";
        }

        String sqlInsert = "INSERT INTO movimientos(user_id, tipo, monto, destinatario) VALUES(?, ?, ?, ?)";

        Connection cn = null;
        try {
            cn = DBConnection.getConnection();
            cn.setAutoCommit(false); // inicio transacción

            // 1) Actualizar saldo
            try (PreparedStatement psUp = cn.prepareStatement(sqlUpdate)) {
                psUp.setDouble(1, monto);
                psUp.setInt(2, userId);

                int filas = psUp.executeUpdate();
                if (filas == 0) {
                    throw new SQLException("No se actualizó el saldo (usuario no encontrado).");
                }
            }

            // 2) Insertar movimiento
            try (PreparedStatement psIn = cn.prepareStatement(sqlInsert)) {
                psIn.setInt(1, userId);
                psIn.setString(2, tipo);
                psIn.setDouble(3, monto);
                psIn.setString(4, destinatario); // puede ser null
                psIn.executeUpdate();
            }

            cn.commit(); // confirmo cambios

            // Refresco pantalla
            cargarUsuarioYSaldo();
            cargarHistorial();

            JOptionPane.showMessageDialog(this, tipo + " realizado correctamente.");

        } catch (SQLException ex) {
            try { if (cn != null) cn.rollback(); } catch (SQLException ignored) {}
            JOptionPane.showMessageDialog(this, "Error en operación: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

        } finally {
            try { if (cn != null) cn.close(); } catch (SQLException ignored) {}
        }
    }

    /*
       DELETE: ELIMINAR CUENTA
      */

    /*
     * Eliminar cuenta del usuario logueado:
     * 1) Pido confirmación
     * 2) Pido contraseña (para evitar que alguien borre la cuenta si dejó la sesión abierta)
     * 3) Borro movimientos y luego borro usuario
     * 4) Regreso al Login
     */
    private void eliminarCuenta() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar tu cuenta?\nSe borrará tu usuario y su historial.",
                "Eliminar cuenta",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        String pass = JOptionPane.showInputDialog(this, "Confirma tu contraseña:");
        if (pass == null || pass.isBlank()) return;

        String sqlVerificar = "SELECT id FROM usuarios WHERE username=? AND password=? AND activo=TRUE";
        String sqlDelMov = "DELETE FROM movimientos WHERE user_id=?";
        String sqlDelUser = "DELETE FROM usuarios WHERE id=?";

        Connection cn = null;
        try {
            cn = DBConnection.getConnection();
            cn.setAutoCommit(false); // transacción

            int idEncontrado;

            // 1) Verifico que la contraseña sea correcta y obtengo el id
            try (PreparedStatement ps = cn.prepareStatement(sqlVerificar)) {
                ps.setString(1, username);
                ps.setString(2, pass);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        cn.rollback();
                        JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    idEncontrado = rs.getInt("id");
                }
            }

            // 2) Borro movimientos del usuario (para no romper la FK)
            try (PreparedStatement ps = cn.prepareStatement(sqlDelMov)) {
                ps.setInt(1, idEncontrado);
                ps.executeUpdate();
            }

            // 3) Borro el usuario
            int filas;
            try (PreparedStatement ps = cn.prepareStatement(sqlDelUser)) {
                ps.setInt(1, idEncontrado);
                filas = ps.executeUpdate();
            }

            if (filas == 0) {
                cn.rollback();
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la cuenta.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            cn.commit();

            JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente.");

            // 4) Vuelvo al login
            dispose();
            new LoginForm();

        } catch (SQLException ex) {
            try { if (cn != null) cn.rollback(); } catch (SQLException ignored) {}
            JOptionPane.showMessageDialog(this, "Error eliminando cuenta: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (cn != null) cn.close(); } catch (SQLException ignored) {}
        }
    }
}
