package ec.edu.ups.poo.view;

import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;

public class LoginView extends JFrame{
    private JPanel panelPrincipal;
    private JPanel panelSecundario;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;// NUEVO
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JButton btnOlvide;

    private JMenuBar menuBar;
    private JMenu menuIdiomas;
    private JMenuItem menuItemEspañol;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemFrances;

    public LoginView(){
        setContentPane(panelPrincipal);
        setTitle("Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);
        btnOlvide.setIcon(new ImageIcon(getClass().getResource("/icons/question-svgrepo-com.png")));
        btnRegistrarse.setIcon(new ImageIcon(getClass().getResource("/icons/user-svgrepo-com.png")));
        btnIniciarSesion.setIcon(new ImageIcon(getClass().getResource("/icons/enter-svgrepo-com.png")));
        crearMenu();
        actualizarTextos();

    }
    private void crearMenu() {
        menuBar = new JMenuBar();
        menuIdiomas = new JMenu(); // El texto se establece en actualizarTextos
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
        setTitle(Idioma.get("login.titulo"));
        lblUsuario.setText(Idioma.get("login.lbl.usuario"));
        lblContrasena.setText(Idioma.get("login.lbl.contrasena"));
        btnIniciarSesion.setText(Idioma.get("login.btn.iniciar"));
        btnRegistrarse.setText(Idioma.get("login.btn.registrar"));
        btnOlvide.setText(Idioma.get("login.btn.olvide"));

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

    public JPanel getPanelSecundario() {
        return panelSecundario;
    }

    public void setPanelSecundario(JPanel panelSecundario) {
        this.panelSecundario = panelSecundario;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(JPasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    public void setBtnIniciarSesion(JButton btnIniciarSesion) {
        this.btnIniciarSesion = btnIniciarSesion;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public void setBtnRegistrarse(JButton btnRegistrarse) {
        this.btnRegistrarse = btnRegistrarse;
    }

    public JButton getBtnOlvide() {
        return btnOlvide;
    }

    public void setBtnOlvide(JButton btnOlvide) {
        this.btnOlvide = btnOlvide;
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

    public void mostrar (String mensaje){
        JOptionPane.showMessageDialog(this, Idioma.get(mensaje));
    }
}