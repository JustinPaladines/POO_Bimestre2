import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BancoForm extends JFrame {
    private JPanel mainPanel;
    private JButton transferenciaButton;
    private JButton depositoButton;
    private JButton retiroButton;
    private JButton salirButton;
    private JTextField txtNombre;
    private JTextField txtSaldo;
    private JTextArea txtHistorial;
    private Double saldo = 1000.00;
    private String user = "cliente123";



    public BancoForm() {
        setTitle("Inicio de sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setContentPane(mainPanel);
        setVisible(true);
        setLocationRelativeTo(null);

        txtSaldo.setText(String.valueOf(saldo));
        txtNombre.setText(user);
        txtHistorial.setEditable(false);


        depositoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deposito = JOptionPane.showInputDialog("Monto a depositar:");

                if (deposito == null) return; // Canceló

                try {
                    double monto = Double.parseDouble(deposito);

                    if (monto <= 0) {
                        JOptionPane.showMessageDialog(null, "Ingrese un valor mayor a 0.");
                        return;
                    }

                    saldo += monto;                     // sumamos
                    txtSaldo.setText(String.valueOf(saldo));  // actualizamos en pantalla
                    txtHistorial.append("Depósito: +$" + monto + "\n");

                    JOptionPane.showMessageDialog(null, "Depósito realizado.");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese un número válido.","ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        retiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String retiro = JOptionPane.showInputDialog("Ingresa el valor a retirar: ");
                if (retiro==null){
                    return;
                }
                try {
                    double montoretirar = Double.parseDouble(retiro);

                    if (montoretirar<=0){
                        JOptionPane.showMessageDialog(null, "Ingrese un valor mayor a 0.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (saldo<montoretirar){
                        JOptionPane.showMessageDialog(null, "No existen suficientes Fondos.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    saldo-=montoretirar;
                    txtSaldo.setText(String.valueOf(saldo));
                    txtHistorial.append("Retiro: -$" + montoretirar + "\n");
                    JOptionPane.showMessageDialog(null, "Monto retirado Exitosamente.");
                }
                catch (NumberFormatException exception){
                    JOptionPane.showMessageDialog(null, "Ingrese un numero valido","ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        transferenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destinatario=JOptionPane.showInputDialog("Ingrese el nombre del destinatario: ");
                String transferir= JOptionPane.showInputDialog("Ingresa el valor a transferir: ");

                if (destinatario == null && transferir == null){
                    JOptionPane.showMessageDialog(null, "No pueden quedar campos vacios.","ERROR", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    double montoretirar = Double.parseDouble(transferir);
                    if (montoretirar <= 0){
                        JOptionPane.showMessageDialog(null, "Ingrese un numero mayor a 0.","ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (saldo < montoretirar){
                        JOptionPane.showMessageDialog(null, "No existen Suficientes Fondos.","ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    saldo -= montoretirar;
                    txtSaldo.setText(String.valueOf(saldo));
                    txtHistorial.append("Transferencia a " + destinatario + ": -$" + montoretirar + "\n");
                    JOptionPane.showMessageDialog(null, "La transferencia de " + montoretirar + " a "+ destinatario + " fue realizada existosamente.");

                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Ingrese un numero valido.","ERROR", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JOptionPane.showMessageDialog(null, "Saliendo...");
            }
        });
    }

}
