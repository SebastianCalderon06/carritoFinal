package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioBuscarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtUsername;
    private JButton btnBuscar;
    private JButton btnListar;
    private JTable tblUsuarios;
    private JLabel lblUsuario;
    private DefaultTableModel modeloTablaUsuarios;

    private List<Usuario> ultimoListadoUsuarios;

    public UsuarioBuscarView()  {
        super("", true, true, true, true);

        // Inicialización manual de componentes
        panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel();

        lblUsuario = new JLabel();
        panelSuperior.add(lblUsuario);
        txtUsername = new JTextField(12);
        btnBuscar = new JButton();
        btnListar = new JButton();
        panelSuperior.add(txtUsername);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnListar);

        modeloTablaUsuarios = new DefaultTableModel(
                new Object[]{"", ""}, 0 // Internacionalizadas abajo
        ) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblUsuarios = new JTable(modeloTablaUsuarios);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setSize(700, 500);
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/icons/search-plus-svgrepo-com.png")));
        btnListar.setIcon(new ImageIcon(getClass().getResource("/icons/list-svgrepo-com.png")));
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("usuario.buscar.titulo"));
        lblUsuario.setText(Idioma.get("usuario.buscar.lbl.usuario"));
        btnBuscar.setText(Idioma.get("usuario.buscar.btn.buscar"));
        btnListar.setText(Idioma.get("usuario.buscar.btn.listar"));

        modeloTablaUsuarios.setColumnIdentifiers(new Object[]{
                Idioma.get("usuario.buscar.tbl.username"),
                Idioma.get("usuario.buscar.tbl.rol")
        });
        tblUsuarios.setToolTipText(Idioma.get("usuario.buscar.tbl.tooltip"));
    }

    // Mostrar todos los usuarios
    public void cargarUsuarios(List<Usuario> usuarios) {
        modeloTablaUsuarios.setRowCount(0);
        ultimoListadoUsuarios = usuarios;
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                modeloTablaUsuarios.addRow(new Object[]{u.getUsername(), u.getRol()});
            }
        }
    }

    // Mostrar solo un usuario encontrado
    public void mostrarUsuario(Usuario usuario) {
        modeloTablaUsuarios.setRowCount(0);
        if (usuario != null) {
            modeloTablaUsuarios.addRow(new Object[]{usuario.getUsername(), usuario.getRol()});
        } else {
            mostrarMensaje(Idioma.get("usuario.buscar.msj.noencontrado"));
        }
    }

    public void limpiarVista() {
        txtUsername.setText("");
        modeloTablaUsuarios.setRowCount(0);
        ultimoListadoUsuarios = null;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, Idioma.get("usuario.buscar.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }

    // Getters
    public JTextField getTxtUsername() { return txtUsername; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnListar() { return btnListar; }
    public JTable getTblUsuarios() { return tblUsuarios; }
}