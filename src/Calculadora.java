import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculadora {
    private JPanel mainPanel;
    private JTextField txtValor1;
    private JTextField txtValor2;
    private JTextField txtResultado;
    private JButton btnSumar;
    private JButton btnDividir;
    private JButton btnRestar;
    private JButton btnMultiplicar;


    public Calculadora() {
        btnSumar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    double n1 = Double.parseDouble(txtValor1.getText().trim());
                    double n2 = Double.parseDouble(txtValor2.getText().trim());
                    double resultado = n1 + n2;
                    txtResultado.setText(String.valueOf(resultado));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Solo se permiten números",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        btnRestar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double n1 = Double.parseDouble(txtValor1.getText().trim());
                    double n2 = Double.parseDouble(txtValor2.getText().trim());
                    double resultado = n1 - n2;
                    txtResultado.setText(String.valueOf(resultado));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Solo se permiten números",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnMultiplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double n1 = Double.parseDouble(txtValor1.getText().trim());
                    double n2 = Double.parseDouble(txtValor2.getText().trim());
                    double resultado = n1 * n2;
                    txtResultado.setText(String.valueOf(resultado));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Solo se permiten números",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnDividir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double n1 = Double.parseDouble(txtValor1.getText().trim());
                    double n2 = Double.parseDouble(txtValor2.getText().trim());
                    if (n2 == 0) {
                        JOptionPane.showMessageDialog(null,
                                "No se puede dividir para cero",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return; // se detiene la ejecucion
                    }
                    double resultado = n1 / n2;
                    txtResultado.setText(String.valueOf(resultado));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Solo se permiten números",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculadora");
        frame.setContentPane(new Calculadora().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //codigo para que no salga en esquinas, interfaz en la pantalla demanera centrada
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
