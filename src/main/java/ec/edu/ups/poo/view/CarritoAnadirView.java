package ec.edu.ups.poo.view;

import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.MessageFormat;

public class CarritoAnadirView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JButton btnBuscar;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAnadir;
    private JTable tblMostrar;
    private JTextField txtSubtotal;
    private JTextField txtIva;
    private JTextField txtTotal;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JComboBox<String> cbxCanridad;
    private JPanel lblDatosProducto;
    private JLabel lblCodigo;
    private JLabel lblPrecio;
    private JLabel lblNombre;
    private JLabel lblCantidad;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;

    public CarritoAnadirView() {
        super("", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setMinimumSize(new Dimension(550, 500));
        cargarDatos();
        configurarTabla();
        configurarComponentes();
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("carrito.titulo"));

        lblCodigo.setText(Idioma.get("carrito.lbl.codigo"));
        lblNombre.setText(Idioma.get("carrito.lbl.nombre"));
        lblPrecio.setText(Idioma.get("carrito.lbl.precio"));
        lblCantidad.setText(Idioma.get("carrito.lbl.cantidad"));
        lblSubtotal.setText(Idioma.get("carrito.lbl.subtotal"));
        lblIva.setText(Idioma.get("carrito.lbl.iva"));
        lblTotal.setText(Idioma.get("carrito.lbl.total"));

        btnBuscar.setText(Idioma.get("carrito.btn.buscar"));
        btnAnadir.setText(Idioma.get("carrito.btn.anadir"));
        btnGuardar.setText(Idioma.get("carrito.btn.guardar"));
        btnLimpiar.setText(Idioma.get("carrito.btn.limpiar"));

        btnGuardar.setToolTipText(Idioma.get("carrito.tooltip.guardar"));
        btnLimpiar.setToolTipText(Idioma.get("carrito.tooltip.limpiar"));
        btnAnadir.setToolTipText(Idioma.get("carrito.tooltip.anadir"));
        btnBuscar.setToolTipText(Idioma.get("carrito.tooltip.buscar"));

        DefaultTableModel model = (DefaultTableModel) tblMostrar.getModel();
        model.setColumnIdentifiers(new Object[]{
                Idioma.get("carrito.tbl.codigo"),
                Idioma.get("carrito.tbl.nombre"),
                Idioma.get("carrito.tbl.precio"),
                Idioma.get("carrito.tbl.cantidad"),
                Idioma.get("carrito.tbl.subtotal")
        });
        tblMostrar.setToolTipText(Idioma.get("carrito.tbl.tooltip"));
    }

    public void cambiarIdiomas(String lenguaje, String pais) {
        Idioma.setIdioma(lenguaje, pais);
        actualizarTextos();
    }

    private void cargarDatos() {
        cbxCanridad.removeAllItems();
        for (int i = 1; i <= 20; i++) {
            cbxCanridad.addItem(String.valueOf(i));
        }
    }

    private void configurarTabla() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Object[] columnas = new Object[]{
                Idioma.get("carrito.tbl.codigo"),
                Idioma.get("carrito.tbl.nombre"),
                Idioma.get("carrito.tbl.precio"),
                Idioma.get("carrito.tbl.cantidad"),
                Idioma.get("carrito.tbl.subtotal")
        };
        model.setColumnIdentifiers(columnas);
        tblMostrar.setModel(model);

        tblMostrar.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblMostrar.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblMostrar.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblMostrar.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblMostrar.getColumnModel().getColumn(4).setPreferredWidth(100);

        tblMostrar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMostrar.setRowSelectionAllowed(true);
        tblMostrar.setColumnSelectionAllowed(false);

        tblMostrar.setToolTipText(Idioma.get("carrito.tbl.tooltip"));
    }

    private void configurarComponentes() {
        txtNombre.setBackground(new Color(240, 240, 240));
        txtPrecio.setBackground(new Color(240, 240, 240));
        txtSubtotal.setBackground(new Color(240, 240, 240));
        txtIva.setBackground(new Color(240, 240, 240));
        txtTotal.setBackground(new Color(240, 240, 240));

        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/icons/save-floppy-svgrepo-com.png")));

        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setIcon(new ImageIcon(getClass().getResource("/icons/erase-svgrepo-com.png")));

        btnAnadir.setForeground(Color.WHITE);
        btnAnadir.setFocusPainted(false);
        btnAnadir.setIcon(new ImageIcon(getClass().getResource("/icons/add-square-svgrepo-com.png")));

        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/icons/search-plus-svgrepo-com.png")));

        txtSubtotal.setText("0.00");
        txtIva.setText("0.00");
        txtTotal.setText("0.00");
    }

    public JPanel getPanelPrincipal() { return panelPrincipal; }
    public void setPanelPrincipal(JPanel panelPrincipal) { this.panelPrincipal = panelPrincipal; }
    public JTextField getTxtCodigo() { return txtCodigo; }
    public void setTxtCodigo(JTextField txtCodigo) { this.txtCodigo = txtCodigo; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public void setBtnBuscar(JButton btnBuscar) { this.btnBuscar = btnBuscar; }
    public JTextField getTxtNombre() { return txtNombre; }
    public void setTxtNombre(JTextField txtNombre) { this.txtNombre = txtNombre; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public void setTxtPrecio(JTextField txtPrecio) { this.txtPrecio = txtPrecio; }
    public JButton getBtnAnadir() { return btnAnadir; }
    public void setBtnAnadir(JButton btnAnadir) { this.btnAnadir = btnAnadir; }
    public JTable getTblMostrar() { return tblMostrar; }
    public void setTblMostrar(JTable tblMostrar) { this.tblMostrar = tblMostrar; }
    public JTextField getTxtSubtotal() { return txtSubtotal; }
    public void setTxtSubtotal(JTextField txtSubtotal) { this.txtSubtotal = txtSubtotal; }
    public JTextField getTxtIva() { return txtIva; }
    public void setTxtIva(JTextField txtIva) { this.txtIva = txtIva; }
    public JTextField getTxtTotal() { return txtTotal; }
    public void setTxtTotal(JTextField txtTotal) { this.txtTotal = txtTotal; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public void setBtnGuardar(JButton btnGuardar) { this.btnGuardar = btnGuardar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public void setBtnLimpiar(JButton btnLimpiar) { this.btnLimpiar = btnLimpiar; }
    public JComboBox<String> getCbxCanridad() { return cbxCanridad; }
    public void setCbxCanridad(JComboBox<String> cbxCanridad) { this.cbxCanridad = cbxCanridad; }

    public void mostrarMensaje(String clave) {
        JOptionPane.showMessageDialog(this, Idioma.get(clave), Idioma.get("carrito.info"), JOptionPane.INFORMATION_MESSAGE);
    }
    public void mostrarMensajeConParametros(String clave, Object... parametros) {
        String mensaje = MessageFormat.format(Idioma.get(clave), parametros);
        JOptionPane.showMessageDialog(this, mensaje, Idioma.get("carrito.info"), JOptionPane.INFORMATION_MESSAGE);
    }
    public void mostrarError(String clave) {
        JOptionPane.showMessageDialog(this, Idioma.get(clave), Idioma.get("carrito.error"), JOptionPane.ERROR_MESSAGE);
    }
    public void mostrarAdvertencia(String clave) {
        JOptionPane.showMessageDialog(this, Idioma.get(clave), Idioma.get("carrito.advertencia"), JOptionPane.WARNING_MESSAGE);
    }
    public int mostrarConfirmacion(String clave) {
        return JOptionPane.showConfirmDialog(this, Idioma.get(clave), Idioma.get("carrito.confirmacion"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public void limpiarCamposProducto() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        cbxCanridad.setSelectedIndex(0);
    }

    public void establecerProducto(String codigo, String nombre, String precio) {
        txtCodigo.setText(codigo);
        txtNombre.setText(nombre);
        txtPrecio.setText(precio);
    }
}