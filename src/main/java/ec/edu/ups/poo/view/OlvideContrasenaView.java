package ec.edu.ups.poo.view;

import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;

public class OlvideContrasenaView extends JDialog {
    private JPanel panelPrincipal;
    private JLabel lblUsuario;
    private JTextField txtUsername;
    private JLabel lblPregunta;
    private JTextField txtRespuesta;
    private JButton btnValidar;
    private JLabel lblNuevaContrasena;
    private JPasswordField txtNuevaContrasena;
    private JButton btnCambiar;

    private JMenuBar menuBar;
    private JMenu menuIdiomas;
    private JMenuItem menuItemEspañol;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemFrances;

    public OlvideContrasenaView() {
        setTitle("Recuperar Contraseña");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setContentPane(panelPrincipal);
        btnValidar.setIcon(new ImageIcon(getClass().getResource("/icons/validate-svgrepo-com.png")));
        btnCambiar.setIcon(new ImageIcon(getClass().getResource("/icons/change-management-backup-svgrepo-com.png")));
        crearMenu();
        actualizarTextos();
    }
    private void crearMenu() {
        menuBar = new JMenuBar();
        menuIdiomas = new JMenu();
        menuItemEspañol = new JMenuItem();
        menuItemIngles = new JMenuItem();
        menuItemFrances = new JMenuItem();

        menuIdiomas.add(menuItemEspañol);
        menuIdiomas.add(menuItemIngles);
        menuIdiomas.add(menuItemFrances);
        menuBar.add(menuIdiomas);
        setJMenuBar(menuBar);
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("olvide.titulo"));
        lblUsuario.setText(Idioma.get("olvide.lbl.usuario"));
        // lblPregunta se actualiza dinámicamente, pero podemos poner un placeholder
        // lblPregunta.setText(Idioma.get("olvide.lbl.pregunta"));
        lblNuevaContrasena.setText(Idioma.get("olvide.lbl.nuevacontra"));
        btnValidar.setText(Idioma.get("olvide.btn.validar"));
        btnCambiar.setText(Idioma.get("olvide.btn.cambiar"));

        // Actualizar menú
        menuIdiomas.setText(Idioma.get("menu.idiomas"));
        menuItemEspañol.setText(Idioma.get("menu.idiomas.español"));
        menuItemIngles.setText(Idioma.get("menu.idiomas.ingles"));
        menuItemFrances.setText(Idioma.get("menu.idiomas.frances"));
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JLabel getLblPregunta() {
        return lblPregunta;
    }

    public void setLblPregunta(JLabel lblPregunta) {
        this.lblPregunta = lblPregunta;
    }

    public JTextField getTxtRespuesta() {
        return txtRespuesta;
    }

    public void setTxtRespuesta(JTextField txtRespuesta) {
        this.txtRespuesta = txtRespuesta;
    }

    public JButton getBtnValidar() {
        return btnValidar;
    }

    public void setBtnValidar(JButton btnValidar) {
        this.btnValidar = btnValidar;
    }

    public JLabel getLblNuevaContrasena() {
        return lblNuevaContrasena;
    }

    public void setLblNuevaContrasena(JLabel lblNuevaContrasena) {
        this.lblNuevaContrasena = lblNuevaContrasena;
    }

    public JPasswordField getTxtNuevaContrasena() {
        return txtNuevaContrasena;
    }

    public void setTxtNuevaContrasena(JPasswordField txtNuevaContrasena) {
        this.txtNuevaContrasena = txtNuevaContrasena;
    }

    public JMenuItem getMenuItemEspañol() {
        return menuItemEspañol;
    }

    public void setMenuItemEspañol(JMenuItem menuItemEspañol) {
        this.menuItemEspañol = menuItemEspañol;
    }

    public JMenuItem getMenuItemIngles() {
        return menuItemIngles;
    }

    public void setMenuItemIngles(JMenuItem menuItemIngles) {
        this.menuItemIngles = menuItemIngles;
    }

    public JMenuItem getMenuItemFrances() {
        return menuItemFrances;
    }

    public void setMenuItemFrances(JMenuItem menuItemFrances) {
        this.menuItemFrances = menuItemFrances;
    }

    public JButton getBtnCambiar() {
        return btnCambiar;
    }

    public void setBtnCambiar(JButton btnCambiar) {
        this.btnCambiar = btnCambiar;
    }
}