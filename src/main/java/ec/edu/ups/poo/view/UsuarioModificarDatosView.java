package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;

public class UsuarioModificarDatosView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JButton btnModificar;
    private JButton btnBorrar;
    private JTextField txtUsuario;
    private JPasswordField txtContra;
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtEdad;
    private JComboBox<Genero> cbxGenero;
    private JLabel lblNombre;
    private JLabel lblApellido;
    private JLabel lblTelefono;
    private JLabel lblEdad;
    private JLabel lblGenero;

    public UsuarioModificarDatosView() {
        super("", true, true, true, true);
        setContentPane(panelPrincipal);
        this.setSize(700, 500);
        btnModificar.setIcon(new ImageIcon(getClass().getResource("/icons/edit-3-svgrepo-com.png")));
        btnBorrar.setIcon(new ImageIcon(getClass().getResource("/icons/erase-svgrepo-com.png")));
        if (cbxGenero != null) {
            cbxGenero.removeAllItems();
            cbxGenero.addItem(Genero.MASCULINO);
            cbxGenero.addItem(Genero.FEMININO);
            cbxGenero.addItem(Genero.OTROS);
        }
        actualizarTextos();
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }

    public JLabel getLblApellido() {
        return lblApellido;
    }

    public void setLblApellido(JLabel lblApellido) {
        this.lblApellido = lblApellido;
    }

    public JLabel getLblTelefono() {
        return lblTelefono;
    }

    public void setLblTelefono(JLabel lblTelefono) {
        this.lblTelefono = lblTelefono;
    }

    public JLabel getLblEdad() {
        return lblEdad;
    }

    public void setLblEdad(JLabel lblEdad) {
        this.lblEdad = lblEdad;
    }

    public JLabel getLblGenero() {
        return lblGenero;
    }

    public void setLblGenero(JLabel lblGenero) {
        this.lblGenero = lblGenero;
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("usuario.modificar.titulo"));
        lblUsuario.setText(Idioma.get("usuario.modificar.lbl.usuario"));
        lblContrasena.setText(Idioma.get("usuario.modificar.lbl.contrasena"));
        lblNombre.setText(Idioma.get("usuario.modificar.lbl.nombre"));
        lblApellido.setText(Idioma.get("usuario.modificar.lbl.apellido"));
        lblTelefono.setText(Idioma.get("usuario.modificar.lbl.telefono"));
        lblEdad.setText(Idioma.get("usuario.modificar.lbl.edad"));
        lblGenero.setText(Idioma.get("usuario.modificar.lbl.genero"));
        btnModificar.setText(Idioma.get("usuario.modificar.btn.modificar"));
        btnBorrar.setText(Idioma.get("usuario.modificar.btn.borrar"));
    }

    public void mostrarDatosUsuario(Usuario usuario) {
        if (usuario != null) {
            txtUsuario.setText(usuario.getUsername());
            txtContra.setText(usuario.getPassword());
            txtNombre.setText(usuario.getNombre());
            txtApellido.setText(usuario.getApellido());
            txtTelefono.setText(usuario.getTelefono());
            txtEdad.setText(String.valueOf(usuario.getEdad()));
            if (cbxGenero != null && usuario.getGenero() != null) {
                cbxGenero.setSelectedItem(usuario.getGenero());
            }
        }
    }

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtContra.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEdad.setText("");
        if (cbxGenero != null && cbxGenero.getItemCount() > 0) cbxGenero.setSelectedIndex(0);
    }


    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(JButton btnModificar) {
        this.btnModificar = btnModificar;
    }

    public JButton getBtnBorrar() {
        return btnBorrar;
    }

    public void setBtnBorrar(JButton btnBorrar) {
        this.btnBorrar = btnBorrar;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(JTextField txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public JPasswordField getTxtContra() {
        return txtContra;
    }

    public void setTxtContra(JPasswordField txtContra) {
        this.txtContra = txtContra;
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    public JLabel getLblContrasena() {
        return lblContrasena;
    }

    public void setLblContrasena(JLabel lblContrasena) {
        this.lblContrasena = lblContrasena;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JTextField getTxtApellido() {
        return txtApellido;
    }

    public void setTxtApellido(JTextField txtApellido) {
        this.txtApellido = txtApellido;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(JTextField txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    public JTextField getTxtEdad() {
        return txtEdad;
    }

    public void setTxtEdad(JTextField txtEdad) {
        this.txtEdad = txtEdad;
    }

    public JComboBox<Genero> getCbxGenero() {
        return cbxGenero;
    }

    public void setCbxGenero(JComboBox<Genero> cbxGenero) {
        this.cbxGenero = cbxGenero;
    }

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("usuario.modificar.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }


}