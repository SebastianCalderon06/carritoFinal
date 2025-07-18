package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Vista de inicio de sesi\u00f3n para la aplicaci\u00f3n de carrito de compras.
 * Permite al usuario introducir credenciales, registrarse, recuperar contrase\u00f1a
 * y seleccionar el tipo de almacenamiento de datos para la sesi\u00f3n.
 *
 * @author Tu Nombre del Estudiante
 * @version 1.0
 * @since 2023-01-01
 */
public class LoginView extends JFrame {
    private JPanel panelPrincipal;
    private JPanel panelSecundario;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JButton btnOlvide;

    private JMenuBar menuBar;
    private JMenu menuIdiomas;
    private JMenuItem menuItemEspañol;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemFrances;

    private JComboBox<String> cbxTipoAlmacenamiento;
    private JTextField txtRutaArchivos;
    private JLabel lblTipoAlmacenamiento;
    private JLabel lblRutaArchivos;
    private JButton btnSeleccionarRuta;
    private JPanel panelAlmacenamiento;

    private Usuario usuarioAutenticado;

    /**
     * Constructor de la vista de inicio de sesi\u00f3n.
     * Inicializa los componentes de la interfaz de usuario, configura eventos,
     * y prepara las opciones de selecci\u00f3n de almacenamiento.
     */
    public LoginView() {
        setTitle("Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        setContentPane(panelPrincipal);

        if (btnOlvide != null) btnOlvide.setIcon(new ImageIcon(getClass().getResource("/icons/question-svgrepo-com.png")));
        if (btnRegistrarse != null) btnRegistrarse.setIcon(new ImageIcon(getClass().getResource("/icons/user-svgrepo-com.png")));
        if (btnIniciarSesion != null) btnIniciarSesion.setIcon(new ImageIcon(getClass().getResource("/icons/enter-svgrepo-com.png")));
        if (btnSeleccionarRuta != null) btnSeleccionarRuta.setIcon(new ImageIcon(getClass().getResource("/icons/enter-svgrepo-com.png")));

        crearMenu();

        if (cbxTipoAlmacenamiento != null) {
            cbxTipoAlmacenamiento.removeAllItems();
            cbxTipoAlmacenamiento.addItem("Memoria");
            cbxTipoAlmacenamiento.addItem("Archivos de Texto");
            cbxTipoAlmacenamiento.addItem("Archivos Binarios");
        }

        if (cbxTipoAlmacenamiento != null) {
            cbxTipoAlmacenamiento.addActionListener(e -> {
                boolean isFileStorageSelected = !cbxTipoAlmacenamiento.getSelectedItem().equals("Memoria");
                if (lblRutaArchivos != null) lblRutaArchivos.setVisible(isFileStorageSelected);
                if (txtRutaArchivos != null) txtRutaArchivos.setVisible(isFileStorageSelected);
                if (btnSeleccionarRuta != null) btnSeleccionarRuta.setVisible(isFileStorageSelected);

                if (!isFileStorageSelected && txtRutaArchivos != null) {
                    txtRutaArchivos.setText("");
                }

                if (panelPrincipal != null) {
                    panelPrincipal.revalidate();
                    panelPrincipal.repaint();
                }
            });
        }

        if (btnSeleccionarRuta != null) {
            btnSeleccionarRuta.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle(Idioma.get("login.selector.titulo"));

                if (txtRutaArchivos != null && !txtRutaArchivos.getText().isEmpty()) {
                    File currentDir = new File(txtRutaArchivos.getText());
                    if (currentDir.exists() && currentDir.isDirectory()) {
                        fileChooser.setCurrentDirectory(currentDir);
                    }
                }

                int option = fileChooser.showOpenDialog(this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    txtRutaArchivos.setText(selectedDirectory.getAbsolutePath());
                }
            });
        }

        actualizarTextos();
    }

    /**
     * Crea la barra de men\u00fa y los \u00edtems de men\u00fa para la selecci\u00f3n de idiomas.
     * Asegura que los componentes del men\u00fa est\u00e9n inicializados.
     */
    private void crearMenu() {
        if (menuBar == null) menuBar = new JMenuBar();
        if (menuIdiomas == null) menuIdiomas = new JMenu();
        if (menuItemEspañol == null) menuItemEspañol = new JMenuItem();
        if (menuItemIngles == null) menuItemIngles = new JMenuItem();
        if (menuItemFrances == null) menuItemFrances = new JMenuItem();

        menuIdiomas.add(menuItemEspañol);
        menuIdiomas.add(menuItemIngles);
        menuIdiomas.add(menuItemFrances);
        menuBar.add(menuIdiomas);
        setJMenuBar(menuBar);
    }

    /**
     * Actualiza los textos de todos los componentes de la interfaz de usuario
     * seg\u00fan el idioma configurado actualmente.
     */
    public void actualizarTextos() {
        setTitle(Idioma.get("login.titulo"));

        if (lblUsuario != null) lblUsuario.setText(Idioma.get("login.lbl.usuario"));
        if (lblContrasena != null) lblContrasena.setText(Idioma.get("login.lbl.contrasena"));
        if (btnIniciarSesion != null) btnIniciarSesion.setText(Idioma.get("login.btn.iniciar"));
        if (btnRegistrarse != null) btnRegistrarse.setText(Idioma.get("login.btn.registrar"));
        if (btnOlvide != null) btnOlvide.setText(Idioma.get("login.btn.olvide"));

        if (menuIdiomas != null) menuIdiomas.setText(Idioma.get("menu.idiomas"));
        if (menuItemEspañol != null) menuItemEspañol.setText(Idioma.get("menu.idiomas.español"));
        if (menuItemIngles != null) menuItemIngles.setText(Idioma.get("menu.idiomas.ingles"));
        if (menuItemFrances != null) menuItemFrances.setText(Idioma.get("menu.idiomas.frances"));

        if (lblTipoAlmacenamiento != null) lblTipoAlmacenamiento.setText(Idioma.get("login.lbl.tipoAlmacenamiento"));
        if (lblRutaArchivos != null) lblRutaArchivos.setText(Idioma.get("login.lbl.rutaArchivos"));
        if (btnSeleccionarRuta != null) btnSeleccionarRuta.setText(Idioma.get("login.btn.seleccionarRuta"));

        if (panelAlmacenamiento != null && panelAlmacenamiento.getBorder() instanceof javax.swing.border.TitledBorder) {
            javax.swing.border.TitledBorder titledBorder = (javax.swing.border.TitledBorder) panelAlmacenamiento.getBorder();
            titledBorder.setTitle(Idioma.get("login.panel.almacenamiento"));
            panelAlmacenamiento.repaint();
        }
    }

    /**
     * Muestra un mensaje de informaci\u00f3n al usuario en un di\u00e1logo.
     * El mensaje se obtiene de los recursos de internacionalizaci\u00f3n.
     *
     * @param mensajeKey La clave del mensaje a mostrar.
     */
    public void mostrar(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("login.info"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de error al usuario en un di\u00e1logo.
     *
     * @param mensaje El mensaje de error a mostrar.
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Establece el objeto Usuario autenticado en esta vista.
     *
     * @param usuario El objeto Usuario autenticado.
     */
    public void setUsuarioAutenticado(Usuario usuario) {
        this.usuarioAutenticado = usuario;
    }

    /**
     * Obtiene el objeto Usuario autenticado almacenado en esta vista.
     *
     * @return El objeto Usuario autenticado.
     */
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    /**
     * Obtiene el panel principal de la vista.
     * @return El JPanel principal.
     */
    public JPanel getPanelPrincipal() { return panelPrincipal; }
    /**
     * Obtiene el panel secundario de la vista, que contiene los campos de login.
     * @return El JPanel secundario.
     */
    public JPanel getPanelSecundario() { return panelSecundario; }
    /**
     * Obtiene el campo de texto para el nombre de usuario.
     * @return El JTextField del nombre de usuario.
     */
    public JTextField getTxtUsername() { return txtUsername; }
    /**
     * Obtiene el campo de contrase\u00f1a.
     * @return El JPasswordField de la contrase\u00f1a.
     */
    public JPasswordField getTxtPassword() { return txtPassword; }
    /**
     * Obtiene el bot\u00f3n para iniciar sesi\u00f3n.
     * @return El JButton para iniciar sesi\u00f3n.
     */
    public JButton getBtnIniciarSesion() { return btnIniciarSesion; }
    /**
     * Obtiene el bot\u00f3n para registrarse.
     * @return El JButton para registrarse.
     */
    public JButton getBtnRegistrarse() { return btnRegistrarse; }
    /**
     * Obtiene el bot\u00f3n para la opci\u00f3n "Olvid\u00e9 mi contrase\u00f1a".
     * @return El JButton para olvidar contrase\u00f1a.
     */
    public JButton getBtnOlvide() { return btnOlvide; }
    /**
     * Obtiene el \u00edtem de men\u00fa para el idioma espa\u00f1ol.
     * @return El JMenuItem para el idioma espa\u00f1ol.
     */
    public JMenuItem getMenuItemEspañol() { return menuItemEspañol; }
    /**
     * Obtiene el \u00edtem de men\u00fa para el idioma ingl\u00e9s.
     * @return El JMenuItem para el idioma ingl\u00e9s.
     */
    public JMenuItem getMenuItemIngles() { return menuItemIngles; }
    /**
     * Obtiene el \u00edtem de men\u00fa para el idioma franc\u00e9s.
     * @return El JMenuItem para el idioma franc\u00e9s.
     */
    public JMenuItem getMenuItemFrances() { return menuItemFrances; }

    /**
     * Obtiene el JComboBox para seleccionar el tipo de almacenamiento de datos.
     * @return El JComboBox del tipo de almacenamiento.
     */
    public JComboBox<String> getCbxTipoAlmacenamiento() { return cbxTipoAlmacenamiento; }
    /**
     * Obtiene el campo de texto para mostrar la ruta de los archivos de almacenamiento.
     * @return El JTextField de la ruta de archivos.
     */
    public JTextField getTxtRutaArchivos() { return txtRutaArchivos; }
}