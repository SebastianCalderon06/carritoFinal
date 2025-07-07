package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CarritoModificarView extends JInternalFrame {

    private JTextField txtCodigo;
    private JTable tblDetalleCarrito;
    private JButton btnModificar;
    private JTable tblModificar;
    private JPanel panelPrincipal;
    private JButton btnBuscar;
    private JLabel lblCodigo;

    private DefaultTableModel modeloTablaCarritos; // Para tblModificar
    private DefaultTableModel modeloTablaDetalle;  // Para tblDetalleCarrito

    private List<Carrito> ultimoListado;

    public CarritoModificarView() {
        super("", true, true, true, true); // Título internacionalizado

        panelPrincipal = new JPanel(new BorderLayout());

        // Panel superior para búsqueda
        JPanel panelSuperior = new JPanel();
        lblCodigo = new JLabel();
        panelSuperior.add(lblCodigo);
        txtCodigo = new JTextField(10);
        panelSuperior.add(txtCodigo);
        btnBuscar = new JButton();
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/icons/search-plus-svgrepo-com.png")));
        panelSuperior.add(btnBuscar);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // Modelo para la tabla de carritos
        modeloTablaCarritos = new DefaultTableModel(
                new Object[]{"", "", "", "", ""}, 0 // Columnas internacionalizadas abajo
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna de cantidad total es editable (col 3)
                return column == 3;
            }
        };
        tblModificar = new JTable(modeloTablaCarritos);

        // Modelo para la tabla de detalles
        modeloTablaDetalle = new DefaultTableModel(
                new Object[]{"", "", "", ""}, 0 // Columnas internacionalizadas abajo
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblDetalleCarrito = new JTable(modeloTablaDetalle);

        // Panel central con las dos tablas
        JPanel panelTablas = new JPanel(new GridLayout(2, 1));
        panelTablas.add(new JScrollPane(tblModificar));
        panelTablas.add(new JScrollPane(tblDetalleCarrito));
        panelPrincipal.add(panelTablas, BorderLayout.CENTER);

        // Panel inferior con el botón modificar
        JPanel panelInferior = new JPanel();
        btnModificar = new JButton();
        btnModificar.setIcon(new ImageIcon(getClass().getResource("/icons/edit-3-svgrepo-com.png")));
        panelInferior.add(btnModificar);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        setSize(800, 550);

        // Listener: al seleccionar un carrito, muestra detalles
        tblModificar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblModificar.getSelectedRow() != -1 && ultimoListado != null) {
                int fila = tblModificar.getSelectedRow();
                if (fila >= 0 && fila < ultimoListado.size()) {
                    Carrito carrito = ultimoListado.get(fila);
                    cargarDetalleCarrito(carrito);
                }
            }
        });

        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("carrito.modificar.titulo"));
        lblCodigo.setText(Idioma.get("carrito.modificar.lbl.codigo"));
        btnBuscar.setText(Idioma.get("carrito.modificar.btn.buscar"));
        btnModificar.setText(Idioma.get("carrito.modificar.btn.modificar"));

        // Columnas tabla carritos
        modeloTablaCarritos.setColumnIdentifiers(new Object[]{
                Idioma.get("carrito.modificar.tbl.codigo"),
                Idioma.get("carrito.modificar.tbl.fecha"),
                Idioma.get("carrito.modificar.tbl.numitems"),
                Idioma.get("carrito.modificar.tbl.canttotal"),
                Idioma.get("carrito.modificar.tbl.usuario")
        });

        // Columnas tabla detalles
        modeloTablaDetalle.setColumnIdentifiers(new Object[]{
                Idioma.get("carrito.modificar.tbl.codigoProd"),
                Idioma.get("carrito.modificar.tbl.nombreProd"),
                Idioma.get("carrito.modificar.tbl.cantidad"),
                Idioma.get("carrito.modificar.tbl.subtotal")
        });

        tblModificar.setToolTipText(Idioma.get("carrito.modificar.tbl.tooltip"));
        tblDetalleCarrito.setToolTipText(Idioma.get("carrito.modificar.tbldetalle.tooltip"));
    }

    // Cargar todos los carritos
    public void cargarCarritosUsuario(List<Carrito> carritos) {
        modeloTablaCarritos.setRowCount(0);
        ultimoListado = carritos;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (carritos != null) {
            for (Carrito carrito : carritos) {
                int cantidadItems = carrito.obtenerItems().size();
                int cantidadTotal = carrito.obtenerItems().stream().mapToInt(ItemCarrito::getCantidad).sum();
                String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
                modeloTablaCarritos.addRow(new Object[]{
                        carrito.getCodigo(),
                        sdf.format(carrito.getFechaCreacion().getTime()),
                        cantidadItems,
                        cantidadTotal,
                        nombreUsuario
                });
            }
        }
        modeloTablaDetalle.setRowCount(0);
    }

    // Cargar solo un carrito filtrado (por código)
    public void cargarCarrito(Carrito carrito) {
        modeloTablaCarritos.setRowCount(0);
        ultimoListado = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (carrito != null) {
            int cantidadItems = carrito.obtenerItems().size();
            int cantidadTotal = carrito.obtenerItems().stream().mapToInt(ItemCarrito::getCantidad).sum();
            String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
            modeloTablaCarritos.addRow(new Object[]{
                    carrito.getCodigo(),
                    sdf.format(carrito.getFechaCreacion().getTime()),
                    cantidadItems,
                    cantidadTotal,
                    nombreUsuario
            });
            cargarDetalleCarrito(carrito);
        } else {
            modeloTablaDetalle.setRowCount(0);
        }
    }

    // Muestra los detalles del carrito seleccionado
    public void cargarDetalleCarrito(Carrito carrito) {
        modeloTablaDetalle.setRowCount(0);
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaDetalle.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
            // Fila vacía
            modeloTablaDetalle.addRow(new Object[]{"", "", "", ""});
            modeloTablaDetalle.addRow(new Object[]{"", Idioma.get("carrito.modificar.lbl.subtotal"), "", String.format("%.2f", carrito.calcularSubtotal())});
            modeloTablaDetalle.addRow(new Object[]{"", Idioma.get("carrito.modificar.lbl.iva"), "", String.format("%.2f", carrito.calcularIva())});
            modeloTablaDetalle.addRow(new Object[]{"", Idioma.get("carrito.modificar.lbl.total"), "", String.format("%.2f", carrito.calcularTotal())});
        }
    }

    public void limpiarVista() {
        txtCodigo.setText("");
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
    }

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("carrito.modificar.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("carrito.modificar.msj.error"), JOptionPane.ERROR_MESSAGE);
    }

    // Getters para el controlador
    public JTextField getTxtCodigo() { return txtCodigo; }
    public JButton getBtnModificar() { return btnModificar; }
    public JTable getTblModificar() { return tblModificar; }
    public JTable getTblDetalleCarrito() { return tblDetalleCarrito; }
    public JButton getBtnBuscar() { return btnBuscar; }
}