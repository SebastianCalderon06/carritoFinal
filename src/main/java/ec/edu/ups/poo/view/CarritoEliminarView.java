package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;

public class CarritoEliminarView extends JInternalFrame {
    // Variables de instancia que coinciden con el diseño
    private JPanel panelPrincipal;
    private JButton btnEliminar;
    private JButton btnListar;
    private JTable tblEliminarCarrito;
    private JTable tblMostrarDetalleCarrito;

    private DefaultTableModel modeloTablaCarritos;
    private DefaultTableModel modeloTablaDetalle;

    private Carrito carritoSeleccionado;
    private List<Carrito> ultimoListado;

    private DateFormat dateFormat;
    private NumberFormat currencyFormat;// Mantener referencia para selección

    public CarritoEliminarView() {
        super("", true, true, true, true); // Título internacionalizado
        setContentPane(panelPrincipal);
        this.setSize(700, 500);

        // Modelo para la tabla de carritos
        modeloTablaCarritos = new DefaultTableModel(
                new Object[]{"", "", "", ""}, 0 // Columnas internacionalizadas abajo
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEliminarCarrito.setModel(modeloTablaCarritos);

        // Modelo para la tabla de detalles
        modeloTablaDetalle = new DefaultTableModel(
                new Object[]{"", "", "", ""}, 0 // Columnas internacionalizadas abajo
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMostrarDetalleCarrito.setModel(modeloTablaDetalle);
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/icons/erase-svgrepo-com.png")));
        btnListar.setIcon(new ImageIcon(getClass().getResource("/icons/list-svgrepo-com.png")));
        btnEliminar.setEnabled(false);

        // Listener: al seleccionar un carrito, muestra detalles y habilita Eliminar
        tblEliminarCarrito.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEliminarCarrito.getSelectedRow() != -1 && ultimoListado != null) {
                int fila = tblEliminarCarrito.getSelectedRow();
                if (fila >= 0 && fila < ultimoListado.size()) {
                    mostrarCarrito(ultimoListado.get(fila));
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
        setTitle(Idioma.get("carrito.eliminar.titulo"));
        btnEliminar.setText(Idioma.get("carrito.eliminar.btn.eliminar"));
        btnListar.setText(Idioma.get("carrito.eliminar.btn.listar"));

        // Columnas tabla carritos
        modeloTablaCarritos.setColumnIdentifiers(new Object[] {
                Idioma.get("carrito.eliminar.tbl.codigo"),
                Idioma.get("carrito.eliminar.tbl.usuario"),
                Idioma.get("carrito.eliminar.tbl.fecha"),
                Idioma.get("carrito.eliminar.tbl.total")
        });

        // Columnas tabla detalle
        modeloTablaDetalle.setColumnIdentifiers(new Object[] {
                Idioma.get("carrito.eliminar.tbl.codigoProd"),
                Idioma.get("carrito.eliminar.tbl.nombreProd"),
                Idioma.get("carrito.eliminar.tbl.cantidadProd"),
                Idioma.get("carrito.eliminar.tbl.subtotalProd")
        });

        tblEliminarCarrito.setToolTipText(Idioma.get("carrito.eliminar.tbl.tooltip"));
        tblMostrarDetalleCarrito.setToolTipText(Idioma.get("carrito.eliminar.tblDetalle.tooltip"));
        if (ultimoListado != null) {
            cargarCarritosUsuario(ultimoListado);
        }
    }

    // Mostrar TODOS los carritos en la tabla principal
    public void cargarCarritosUsuario(List<Carrito> carritos) {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        ultimoListado = carritos;
        for (Carrito carrito : carritos) {
            String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
            modeloTablaCarritos.addRow(new Object[]{
                    carrito.getCodigo(),
                    nombreUsuario,
                    dateFormat.format(carrito.getFechaCreacion().getTime()),
                    String.format("%.2f", carrito.calcularTotal())
            });
        }
        btnEliminar.setEnabled(false);
        carritoSeleccionado = null;
    }

    // Mostrar detalle de carrito seleccionado
    public void mostrarCarrito(Carrito carrito) {
        modeloTablaDetalle.setRowCount(0);
        carritoSeleccionado = null;
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaDetalle.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
            btnEliminar.setEnabled(true);
            carritoSeleccionado = carrito;
        } else {
            btnEliminar.setEnabled(false);
        }
    }

    public void limpiarVista() {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        btnEliminar.setEnabled(false);
        carritoSeleccionado = null;
    }

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("carrito.eliminar.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean confirmarAccion(String mensajeKey) {
        int respuesta = JOptionPane.showConfirmDialog(this,
                Idioma.get(mensajeKey),
                Idioma.get("carrito.eliminar.msj.confirmar"),
                JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    // Getters
    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public Carrito getCarritoSeleccionado() {
        return carritoSeleccionado;
    }
}