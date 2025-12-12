import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JPanel MainPanel;
    private JTextField txtUsuario;
    private JPasswordField txtContra;
    private JButton btnLogin;

    private String usuario = "cliente123";
    private String contraseña= "clave456";


    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public LoginForm(){
        setTitle("Inicio de sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(315,300);
        setContentPane(MainPanel);
        setVisible(true);
        setLocationRelativeTo(null);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtUsuario.getText().equals(usuario) && txtContra.getText().equals(contraseña)){
                    JOptionPane.showMessageDialog(null,"ACCESO PERMITIDO", "INGRESANDO", JOptionPane.INFORMATION_MESSAGE);
                    //la ventana anterior se cerrara automaticamente
                    dispose();
                    new BancoForm();

                    }
                else {
                    JOptionPane.showMessageDialog(null,"Credenciales invalidas", "ERROR", JOptionPane.ERROR_MESSAGE);
                    //limpia los espacios en caso de ser incorrectos
                    txtUsuario.setText("");
                    txtContra.setText("");
                }
            }
        });

    }
}
