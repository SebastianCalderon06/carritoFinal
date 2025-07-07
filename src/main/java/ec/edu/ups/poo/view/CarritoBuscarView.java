package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CarritoBuscarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtBuscarCarrito;
    private JButton btnBuscarCarrito;
    private JButton btnListar;
    private JTable tblBuscarCarrito;
    private JTable tblDetalleCarrito;
    private JLabel lblCodigo;

    private DefaultTableModel modeloTablaCarritos;
    private DefaultTableModel modeloTablaDetalle;

    private List<Carrito> ultimoListadoCarritos; // Para referencia rápida en selección
    private DateFormat dateFormat;
    private NumberFormat currencyFormat;
    public CarritoBuscarView() {
        super("", true, true, true, true); // Título será internacionalizado

        // Inicialización de componentes
        panelPrincipal = new JPanel(new BorderLayout());

        JPanel panelBusqueda = new JPanel();
        lblCodigo = new JLabel(); // Internacionalizado después
        panelBusqueda.add(lblCodigo);
        txtBuscarCarrito = new JTextField(8);
        btnBuscarCarrito = new JButton();
        btnBuscarCarrito.setIcon(new ImageIcon(getClass().getResource("/icons/search-plus-svgrepo-com.png")));

        btnListar = new JButton();
        btnListar.setIcon(new ImageIcon(getClass().getResource("/icons/list-svgrepo-com.png")));
        panelBusqueda.add(txtBuscarCarrito);
        panelBusqueda.add(btnBuscarCarrito);
        panelBusqueda.add(btnListar);

        modeloTablaCarritos = new DefaultTableModel(
                new Object[]{"", "", "", ""}, 0 // Columnas internacionalizadas después
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBuscarCarrito = new JTable(modeloTablaCarritos);

        modeloTablaDetalle = new DefaultTableModel(
                new Object[]{"", "", "", ""}, 0 // Columnas internacionalizadas después
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetalleCarrito = new JTable(modeloTablaDetalle);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblBuscarCarrito), new JScrollPane(tblDetalleCarrito));
        split.setResizeWeight(0.5);

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(split, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setSize(700, 500);

        // Listener para seleccionar carrito y mostrar detalles
        tblBuscarCarrito.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblBuscarCarrito.getSelectedRow() != -1 && ultimoListadoCarritos != null) {
                int fila = tblBuscarCarrito.getSelectedRow();
                if (fila >= 0 && fila < ultimoListadoCarritos.size()) {
                    mostrarDetalleCarrito(ultimoListadoCarritos.get(fila));
                }
            }
        });
        actualizarFormatos();
        actualizarTextos();

    }
    private void actualizarFormatos() {
        Locale locale = Idioma.getLocale();
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        currencyFormat = NumberFormat.getCurrencyInstance(locale);
    }

    public void actualizarTextos() {
        actualizarFormatos();
        setTitle(Idioma.get("carrito.buscar.titulo"));

        lblCodigo.setText(Idioma.get("carrito.buscar.lbl.codigo"));
        btnBuscarCarrito.setText(Idioma.get("carrito.buscar.btn.buscar"));
        btnListar.setText(Idioma.get("carrito.buscar.btn.listar"));

        // Columnas tabla carritos
        modeloTablaCarritos.setColumnIdentifiers(new Object[]{
                Idioma.get("carrito.buscar.tbl.codigo"),
                Idioma.get("carrito.buscar.tbl.usuario"),
                Idioma.get("carrito.buscar.tbl.fecha"),
                Idioma.get("carrito.buscar.tbl.total")
        });

        // Columnas tabla detalle
        modeloTablaDetalle.setColumnIdentifiers(new Object[]{
                Idioma.get("carrito.buscar.tbl.codigoProd"),
                Idioma.get("carrito.buscar.tbl.nombreProd"),
                Idioma.get("carrito.buscar.tbl.cantidadProd"),
                Idioma.get("carrito.buscar.tbl.subtotalProd")
        });

        tblBuscarCarrito.setToolTipText(Idioma.get("carrito.buscar.tbl.tooltip"));
        tblDetalleCarrito.setToolTipText(Idioma.get("carrito.buscar.tblDetalle.tooltip"));
        if (ultimoListadoCarritos != null) {
            cargarCarritosUsuario(ultimoListadoCarritos);
        }

    }

    // Muestra todos los carritos en la tabla principal
    public void cargarCarritosUsuario(List<Carrito> carritos) {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        ultimoListadoCarritos = carritos;
        for (Carrito carrito : carritos) {
            String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
            modeloTablaCarritos.addRow(new Object[]{
                    carrito.getCodigo(),
                    nombreUsuario,
                    dateFormat.format(carrito.getFechaCreacion().getTime()),
                    currencyFormat.format(carrito.calcularTotal())
            });
        }
    }

    // Muestra solo un carrito (por código)
    public void mostrarCarrito(Carrito carrito) {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);

        if (carrito != null) {
            String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
            modeloTablaCarritos.addRow(new Object[]{
                    carrito.getCodigo(),
                    nombreUsuario,
                    dateFormat.format(carrito.getFechaCreacion().getTime()),
                    currencyFormat.format(carrito.calcularTotal())
            });
            mostrarDetalleCarrito(carrito);
            ultimoListadoCarritos = List.of(carrito); // Para permitir click en tabla única
        } else {
            mostrarMensaje(Idioma.get("carrito.buscar.msj.noencontrado"));
            ultimoListadoCarritos = null;
        }
    }

    // Muestra los ítems de un carrito en la tabla de detalles
    public void mostrarDetalleCarrito(Carrito carrito) {
        modeloTablaDetalle.setRowCount(0);
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaDetalle.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        currencyFormat.format(item.getSubtotal())
                });
            }
        }
    }

    public void limpiarVista() {
        txtBuscarCarrito.setText("");
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        ultimoListadoCarritos = null;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "carrito.buscar.msj.info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Getters para el controlador
    public JTextField getTxtBuscarCarrito() { return txtBuscarCarrito; }
    public JButton getBtnBuscarCarrito() { return btnBuscarCarrito; }
    public JButton getBtnListar() { return btnListar; }
    public JTable getTblBuscarCarrito() { return tblBuscarCarrito; }
    public JTable getTblDetalleCarrito() { return tblDetalleCarrito; }
}