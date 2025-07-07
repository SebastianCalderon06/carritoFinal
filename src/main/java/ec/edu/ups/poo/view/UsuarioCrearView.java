package ec.edu.ups.poo.view;

import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;

public class UsuarioCrearView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnCrear;
    private JButton btnBorrar;
    private JLabel lblUsuario;
    private JLabel lblContrasena;

    public UsuarioCrearView() {
        super("", true, true, true, true);
        setContentPane(panelPrincipal);
        this.setSize(700, 500);
        btnCrear.setIcon(new ImageIcon(getClass().getResource("/icons/add-square-svgrepo-com.png")));
        btnBorrar.setIcon(new ImageIcon(getClass().getResource("/icons/erase-svgrepo-com.png")));
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("usuario.crear.titulo"));
        lblUsuario.setText(Idioma.get("usuario.crear.lbl.usuario"));
        lblContrasena.setText(Idioma.get("usuario.crear.lbl.contrasena"));
        btnCrear.setText(Idioma.get("usuario.crear.btn.crear"));
        btnBorrar.setText(Idioma.get("usuario.crear.btn.borrar"));
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(JTextField txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public JPasswordField getTxtContrasena() {
        return txtContrasena;
    }

    public void setTxtContrasena(JPasswordField txtContrasena) {
        this.txtContrasena = txtContrasena;
    }

    public JButton getBtnCrear() {
        return btnCrear;
    }

    public void setBtnCrear(JButton btnCrear) {
        this.btnCrear = btnCrear;
    }

    public JButton getBtnBorrar() {
        return btnBorrar;
    }

    public void setBtnBorrar(JButton btnBorrar) {
        this.btnBorrar = btnBorrar;
    }

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("usuario.crear.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }
}