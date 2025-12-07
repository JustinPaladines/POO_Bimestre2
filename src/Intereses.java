import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Intereses {
    private JPanel mainPanel;
    private JCheckBox chkProgramacion;
    private JCheckBox chkRedes;
    private JCheckBox chkBase;
    private JCheckBox chkSeguridad;
    private JCheckBox chkDiseño;
    private JButton btnProcesar;

    public Intereses() {
        btnProcesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                StringBuilder seleccion = new StringBuilder("Intereses seleccionados:\n");

                if (chkProgramacion.isSelected()) {
                    seleccion.append("- Programación\n");
                }
                if (chkRedes.isSelected()) {
                    seleccion.append("- Redes\n");
                }
                if (chkBase.isSelected()) {
                    seleccion.append("- Base de Datos\n");
                }
                if (chkDiseño.isSelected()) {
                    seleccion.append("- Diseño\n");
                }
                if (chkSeguridad.isSelected()) {
                    seleccion.append("- Seguridad Informática\n");
                }

                // validar si no marcó nada
                if (seleccion.toString().equals("Intereses seleccionados:\n")) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "No seleccionaste ningún interés.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Mostrar selección
                JOptionPane.showMessageDialog(mainPanel,
                        seleccion.toString(),
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Intereses");
        frame.setContentPane(new Intereses().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
