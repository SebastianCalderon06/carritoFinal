package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioAdminView extends JInternalFrame {
    private JTable tblUsuarios;
    private DefaultTableModel modeloTabla;
    private JComboBox<Rol> cbxRol;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JPanel panelPrincipal;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton actualizarButton;
    private JButton listarButton;
    private JButton eliminarUsuarioButton;
    private JLabel lblRol;
    private JLabel lblUsuarioSeleccionado;

    public UsuarioAdminView() {
        super("", true, true, true, true);
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        actualizarTextos();

    }

    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout());

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // SOLO el username es editable (columna 0)
                return column == 0;
            }
        };
        modeloTabla.addColumn(""); // username
        modeloTabla.addColumn(""); // rol

        tblUsuarios = new JTable(modeloTabla);
        tblUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsuarios.getTableHeader().setReorderingAllowed(false);

        cbxRol = new JComboBox<>();
        cbxRol.addItem(Rol.ADMINISTRADOR);
        cbxRol.addItem(Rol.USUARIO);

        btnActualizar = new JButton();
        btnEliminar = new JButton();
        btnRefrescar = new JButton();
        btnRefrescar.setIcon(new ImageIcon(getClass().getResource("/icons/update-svgrepo-com.png")));
        btnActualizar.setIcon(new ImageIcon(getClass().getResource("/icons/update-svgrepo-com.png")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/icons/erase-svgrepo-com.png")));



        lblRol = new JLabel();
        lblUsuarioSeleccionado = new JLabel();
        lblUsuarioSeleccionado.setFont(new Font("Arial", Font.ITALIC, 12));
        lblUsuarioSeleccionado.setForeground(Color.GRAY);
    }

    private void configurarLayout() {
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.add(new JLabel(Idioma.get("usuario.admin.titulo")));
        panelInfo.setBorder(BorderFactory.createEtchedBorder());

        JScrollPane scrollPane = new JScrollPane(tblUsuarios);
        scrollPane.setBorder(BorderFactory.createTitledBorder(Idioma.get("usuario.admin.tbl.titulo")));

        JPanel panelControles = new JPanel(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSeleccion.add(lblUsuarioSeleccionado);

        JPanel panelRol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRol.add(lblRol);
        panelRol.add(cbxRol);
        panelRol.add(btnActualizar);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);

        panelControles.add(panelSeleccion, BorderLayout.NORTH);
        panelControles.add(panelRol, BorderLayout.CENTER);
        panelControles.add(panelBotones, BorderLayout.SOUTH);
        panelControles.setBorder(BorderFactory.createTitledBorder(Idioma.get("usuario.admin.acciones")));

        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelControles, BorderLayout.SOUTH);

        this.setContentPane(panelPrincipal);
    }

    private void configurarEventos() {
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblUsuarios.getSelectedRow() != -1) {
                int selectedRow = tblUsuarios.getSelectedRow();
                String username = (String) modeloTabla.getValueAt(selectedRow, 0);
                Rol currentRol = (Rol) modeloTabla.getValueAt(selectedRow, 1);

                lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccionado") + ": " + username);
                cbxRol.setSelectedItem(currentRol);

                btnActualizar.setEnabled(true);
                btnEliminar.setEnabled(true);
            } else {
                lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccione"));
                btnActualizar.setEnabled(false);
                btnEliminar.setEnabled(false);
            }
        });

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("usuario.admin.titulo.ventana"));
        modeloTabla.setColumnIdentifiers(new Object[]{
                Idioma.get("usuario.admin.tbl.col.username"),
                Idioma.get("usuario.admin.tbl.col.rol")
        });
        btnActualizar.setText(Idioma.get("usuario.admin.btn.actualizar"));
        btnEliminar.setText(Idioma.get("usuario.admin.btn.eliminar"));
        btnRefrescar.setText(Idioma.get("usuario.admin.btn.refrescar"));
        lblRol.setText(Idioma.get("usuario.admin.lbl.rol"));
        lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccione"));
        tblUsuarios.setToolTipText(Idioma.get("usuario.admin.tbl.tooltip"));
    }

    public void cargarUsuarios(List<Usuario> usuarios) {
        modeloTabla.setRowCount(0);

        for (Usuario usuario : usuarios) {
            Object[] fila = {
                    usuario.getUsername(),
                    usuario.getRol()
            };
            modeloTabla.addRow(fila);
        }

        tblUsuarios.clearSelection();
        lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.cargados") + ": " + usuarios.size());
    }

    public void limpiarSeleccion() {
        tblUsuarios.clearSelection();
        lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccione"));
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public JTable getTblUsuarios() {
        return tblUsuarios;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JComboBox<Rol> getCbxRol() {
        return cbxRol;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnRefrescar() {
        return btnRefrescar;
    }

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("usuario.admin.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("usuario.admin.msj.error"), JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmarAccion(String mensajeKey, Object... params) {
        String mensaje = java.text.MessageFormat.format(Idioma.get(mensajeKey), params);
        int opcion = JOptionPane.showConfirmDialog(
                this,
                mensaje,
                Idioma.get("usuario.admin.msj.confirmar"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return opcion == JOptionPane.YES_OPTION;
    }
}