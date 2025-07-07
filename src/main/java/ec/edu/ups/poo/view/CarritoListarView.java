package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;

public class CarritoListarView extends JInternalFrame {
    private JButton btnListar;
    private JTable tblCarritos;
    private JTable tblDetallesCarrito;
    private JPanel panelPrincipal;

    private DefaultTableModel modeloTablaCarritos;
    private DefaultTableModel modeloDetallesCarrito;

    private List<Carrito> ultimoListado;

    private DateFormat dateFormat;
    private NumberFormat currencyFormat;

    public CarritoListarView() {
        super("", true, true, true, true); // Título internacionalizado

        panelPrincipal = new JPanel(new BorderLayout());

        // Modelo de la tabla de carritos
        modeloTablaCarritos = new DefaultTableModel(
                new Object[]{"", "", "", "", ""}, 0 // Columnas internacionalizadas abajo
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCarritos = new JTable(modeloTablaCarritos);

        // Modelo de la tabla de detalles
        modeloDetallesCarrito = new DefaultTableModel(
                new Object[]{"", "", "", ""}, 0 // Columnas internacionalizadas abajo
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetallesCarrito = new JTable(modeloDetallesCarrito);

        btnListar = new JButton();
        btnListar.setIcon(new ImageIcon(getClass().getResource("/icons/list-svgrepo-com.png")));


        JPanel panelTablas = new JPanel(new GridLayout(2, 1));
        panelTablas.add(new JScrollPane(tblCarritos));
        panelTablas.add(new JScrollPane(tblDetallesCarrito));

        panelPrincipal.add(panelTablas, BorderLayout.CENTER);
        panelPrincipal.add(btnListar, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        this.setSize(800, 500);

        // Listener de selección
        tblCarritos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblCarritos.getSelectedRow() != -1 && ultimoListado != null) {
                    int fila = tblCarritos.getSelectedRow();
                    if (fila >= 0 && fila < ultimoListado.size()) {
                        Carrito carrito = ultimoListado.get(fila);
                        mostrarDetallesCarrito(carrito);
                    }
                }
            }
        });
        actualizarFormatos();
        actualizarTextos();
    }
    private void actualizarFormatos() {
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Idioma.getLocale());
        currencyFormat = NumberFormat.getCurrencyInstance(Idioma.getLocale());
    }

    public void actualizarTextos() {
        actualizarFormatos();
        setTitle(Idioma.get("carrito.listar.titulo"));
        btnListar.setText(Idioma.get("carrito.listar.btn.listar"));

        // Columnas tabla carritos
        modeloTablaCarritos.setColumnIdentifiers(new Object[]{
                Idioma.get("carrito.listar.tbl.codigo"),
                Idioma.get("carrito.listar.tbl.fecha"),
                Idioma.get("carrito.listar.tbl.numitems"),
                Idioma.get("carrito.listar.tbl.canttotal"),
                Idioma.get("carrito.listar.tbl.usuario")
        });

        // Columnas tabla detalles
        modeloDetallesCarrito.setColumnIdentifiers(new Object[]{
                Idioma.get("carrito.listar.tbl.codigoProd"),
                Idioma.get("carrito.listar.tbl.nombreProd"),
                Idioma.get("carrito.listar.tbl.cantidad"),
                Idioma.get("carrito.listar.tbl.subtotal")
        });

        tblCarritos.setToolTipText(Idioma.get("carrito.listar.tbl.tooltip"));
        tblDetallesCarrito.setToolTipText(Idioma.get("carrito.listar.tbldetalle.tooltip"));
        if (ultimoListado != null) {
            cargarCarritos(ultimoListado);
        }
    }

    /**
     * Carga la lista de carritos en la tabla principal.
     * @param carritos Carritos a mostrar (solo del usuario autenticado)
     */
    public void cargarCarritos(List<Carrito> carritos) {
        modeloTablaCarritos.setRowCount(0);
        ultimoListado = carritos;
        if (carritos != null) {
            for (Carrito carrito : carritos) {
                int cantidadItems = carrito.getItems().size();
                int cantidadTotal = carrito.getItems().stream().mapToInt(ItemCarrito::getCantidad).sum();
                String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
                modeloTablaCarritos.addRow(new Object[]{
                        carrito.getCodigo(),
                        dateFormat.format(carrito.getFechaCreacion().getTime()),
                        cantidadItems,
                        cantidadTotal,
                        nombreUsuario
                });
            }
        }
        modeloDetallesCarrito.setRowCount(0); // limpia detalles
    }

    /**
     * Muestra los detalles del carrito seleccionado en la tabla inferior,
     * incluyendo productos y al final, subtotal, iva y total.
     */
    public void mostrarDetallesCarrito(Carrito carrito) {
        modeloDetallesCarrito.setRowCount(0);
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloDetallesCarrito.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
            // Fila vacía
            modeloDetallesCarrito.addRow(new Object[]{"", "", "", ""});
            // Subtotal, IVA y Total (internacionalizados)
            modeloDetallesCarrito.addRow(new Object[]{"", Idioma.get("carrito.listar.lbl.subtotal"), "", String.format("%.2f", carrito.calcularSubtotal())});
            modeloDetallesCarrito.addRow(new Object[]{"", Idioma.get("carrito.listar.lbl.iva"), "", String.format("%.2f", carrito.calcularIva())});
            modeloDetallesCarrito.addRow(new Object[]{"", Idioma.get("carrito.listar.lbl.total"), "", String.format("%.2f", carrito.calcularTotal())});
        }
    }

    // Getters
    public JButton getBtnListar() { return btnListar; }
    public JTable getTblCarritos() { return tblCarritos; }
    public JTable getTblDetallesCarrito() { return tblDetallesCarrito; }
}