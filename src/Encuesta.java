
import javax.swing.*;
import javax.swing.ButtonGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Encuesta {
    private JPanel mainPanel;
    private JRadioButton siRadioButton;
    private JRadioButton rbAveces;
    private JRadioButton noRadioButton;
    private JRadioButton siRadioButton1;
    private JRadioButton rbAveces1;
    private JRadioButton noRadioButton1;
    private JRadioButton siRadioButton2;
    private JRadioButton rbAveces2;
    private JRadioButton noRadioButton2;
    private JButton btnEnviarRespuesta;

    public Encuesta (){
        ButtonGroup grupo1 = new ButtonGroup();
        grupo1.add(siRadioButton);
        grupo1.add(rbAveces);
        grupo1.add(noRadioButton);

        ButtonGroup grupo2 = new ButtonGroup();
        grupo2.add(siRadioButton1);
        grupo2.add(rbAveces1);
        grupo2.add(noRadioButton1);

        ButtonGroup grupo3 = new ButtonGroup();
        grupo3.add(siRadioButton2);
        grupo3.add(rbAveces2);
        grupo3.add(noRadioButton2);


        btnEnviarRespuesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String respuesta1 = "";
                if (siRadioButton.isSelected()) respuesta1 = "Sí";
                else if (rbAveces.isSelected()) respuesta1 = "A veces";
                else if (noRadioButton.isSelected()) respuesta1 = "No";

                String respuesta2 = "";
                if (siRadioButton1.isSelected()) respuesta2 = "Sí";
                else if (rbAveces1.isSelected()) respuesta2 = "A veces";
                else if (noRadioButton1.isSelected()) respuesta2 = "No";

                String respuesta3 = "";
                if (siRadioButton2.isSelected()) respuesta3 = "Sí";
                else if (rbAveces2.isSelected()) respuesta3 = "A veces";
                else if (noRadioButton2.isSelected()) respuesta3 = "No";

                String resultado = "Resultado de la encuesta \n" +
                        "1. ¿Te gusta tu Carrera?: " + respuesta1 + "\n" +
                        "2. ¿Deseas seguir una maestria?: " + respuesta2 + "\n" +
                        "3. ¿Recomendarías tu carrera a otros?: " + respuesta3;

                JOptionPane.showMessageDialog(null, resultado);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Encuesta");
        frame.setContentPane(new Encuesta().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}