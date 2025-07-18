package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.Idioma;
import ec.edu.ups.poo.util.Validador;
import ec.edu.ups.poo.view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoController {

    private ProductoAnadirView productoAnadirView;
    private ProductoListaView productoListaView;
    private ProductoEditar productoEditarView;
    private ProductoEliminar productoEliminarView;
    private CarritoAnadirView carritoAnadirView;
    private ProductoDAO productoDAO;
    private Carrito carrito = new Carrito();

    public ProductoController(ProductoDAO productoDAO, ProductoAnadirView productoAnadirView, ProductoListaView productoListaView, ProductoEditar productoEditarView, ProductoEliminar productoEliminarView, CarritoAnadirView carritoAnadirView) {
        this.productoDAO = productoDAO;
        this.productoAnadirView = productoAnadirView;
        this.productoListaView = productoListaView;
        this.productoEditarView = productoEditarView;
        this.productoEliminarView = productoEliminarView;
        this.carritoAnadirView = carritoAnadirView;

        configurarLitsaEventos();
        configurarAnadirEventos();
        configurarEditarEventos();
        configurarEliminarEventos();
        configurarCarritoEventos();
    }

    private void configurarAnadirEventos() {
        productoAnadirView.getBtnGuardar().addActionListener(e -> guardarProducto());
        productoAnadirView.getBtnEliminar().addActionListener(e -> productoAnadirView.limpiarCampos());
    }

    private void configurarLitsaEventos() {
        productoListaView.getBtnBuscar().addActionListener(e -> buscarProducto());
        productoListaView.getBtnListar().addActionListener(e -> listarProductos());
    }

    private void configurarEditarEventos() {
        productoEditarView.getBtnListar1().addActionListener(e -> listarProductosParaEditar());
        productoEditarView.getBtnEditar().addActionListener(e -> editarProducto());
    }

    private void configurarEliminarEventos() {
        productoEliminarView.getBtnListar().addActionListener(e -> listarProductosParaEliminar());
        productoEliminarView.getBtnEliminar().addActionListener(e -> eliminarProducto());
    }

    private void configurarCarritoEventos() {
        carritoAnadirView.getBtnBuscar().addActionListener(e -> buscarProductoCarrito());
    }

    /**
     * Busca un producto por su código para añadirlo al carrito.
     * Valida que el código sea un número entero.
     */
    private void buscarProductoCarrito() {
        String codigoStr = carritoAnadirView.getTxtCodigo().getText();
        try {
            int codigo = Validador.validarEntero(codigoStr, "carrito.lbl.codigo");

            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto == null) {
                carritoAnadirView.mostrarMensaje(Idioma.get("producto.controller.msj.encontrado"));
                carritoAnadirView.getTxtNombre().setText("");
                carritoAnadirView.getTxtPrecio().setText("");
            } else {
                carritoAnadirView.getTxtNombre().setText(producto.getNombre());
                carritoAnadirView.getTxtPrecio().setText(String.format("%.2f", producto.getPrecio()));
            }
        } catch (IllegalArgumentException ex) {
            carritoAnadirView.mostrarMensaje(ex.getMessage());
            carritoAnadirView.getTxtNombre().setText("");
            carritoAnadirView.getTxtPrecio().setText("");
        } catch (Exception ex) {
            carritoAnadirView.mostrarError("Error inesperado al buscar producto para carrito: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Guarda un nuevo producto en el sistema.
     * Valida el código, nombre y precio.
     */
    private void guardarProducto() {
        String codigoStr = productoAnadirView.getTxtCodigo().getText();
        String nombre = productoAnadirView.getTxtNombre().getText();
        String precioStr = productoAnadirView.getTxtPrecio().getText();

        try {
            int codigo = Validador.validarEntero(codigoStr, "producto.anadir.lbl.codigo");
            Validador.validarNoVacio(nombre, "producto.anadir.lbl.nombre");
            double precio = Validador.validarDouble(precioStr, "producto.anadir.lbl.precio");

            if (productoDAO.buscarPorCodigo(codigo) != null) {
                productoAnadirView.mostrarMensaje(Idioma.get("producto.controller.msj.codigoExistente"));
                return;
            }

            productoDAO.crear(new Producto(codigo, nombre, precio));
            productoAnadirView.mostrarMensaje(Idioma.get("producto.controller.msj.guardado"));
            productoAnadirView.limpiarCampos();
        } catch (IllegalArgumentException ex) {
            productoAnadirView.mostrarMensaje(ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(productoAnadirView,
                    Idioma.get("main.error") + ": " + Idioma.get("producto.controller.msj.errorinesperado") + ex.getMessage(),
                    Idioma.get("main.error"),
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Edita un producto existente.
     * Valida que un producto esté seleccionado y que el nuevo nombre no esté vacío.
     */
    private void editarProducto() {
        int filaSeleccionada = productoEditarView.getTblEditarProducto().getSelectedRow();
        if (filaSeleccionada < 0) {
            productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.selectable"));
            return;
        }

        int codigo = (int) productoEditarView.getTblEditarProducto().getValueAt(filaSeleccionada, 0);
        String nuevoNombre = productoEditarView.getTxtNombre().getText();

        try {
            Validador.validarNoVacio(nuevoNombre, "producto.editar.lbl.nombre");

            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto != null) {
                producto.setNombre(nuevoNombre);
                productoDAO.actualizar(producto);
                productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.actualizado"));
                productoEditarView.limpiarCampos();
                listarProductosParaEditar();
            } else {
                productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.encontrado"));
            }
        } catch (IllegalArgumentException ex) {
            productoEditarView.mostrarMensaje(ex.getMessage());
        } catch (Exception ex) {
            productoEditarView.mostrarMensaje("Error inesperado al editar producto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Elimina un producto seleccionado.
     * Valida que un producto esté seleccionado y pide confirmación.
     */
    private void eliminarProducto() {
        int filaSeleccionada = productoEliminarView.getTblEliminarProducto().getSelectedRow();

        if (filaSeleccionada < 0) {
            productoEliminarView.mostrarMensaje(Idioma.get("producto.controller.msj.please"));
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                productoEliminarView,
                Idioma.get("producto.controller.msj.seguro"),
                Idioma.get("producto.controller.msj.confirmar"),
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            int codigo = (int) productoEliminarView.getTblEliminarProducto().getValueAt(filaSeleccionada, 0);
            try {
                productoDAO.eliminar(codigo);
                productoEliminarView.mostrarMensaje(Idioma.get("producto.controller.msj.eliminado"));
                listarProductosParaEliminar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(productoEliminarView,
                        Idioma.get("main.error") + ": " + Idioma.get("producto.controller.msj.errorinesperado") + ex.getMessage(),
                        Idioma.get("main.error"),
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void actualizarTablaCarrito() {
        if (carrito == null) return;
        String[] columnas = {
                Idioma.get("carrito.tbl.codigo"),
                Idioma.get("carrito.tbl.nombre"),
                Idioma.get("carrito.tbl.precio"),
                Idioma.get("carrito.tbl.cantidad"),
                Idioma.get("carrito.tbl.subtotal")
        };
        List<ItemCarrito> items = carrito.getItems();
        Object[][] datos = new Object[items.size()][5];

        int i = 0;
        for (ItemCarrito item : items) {
            datos[i][0] = item.getProducto().getCodigo();
            datos[i][1] = item.getProducto().getNombre();
            datos[i][2] = item.getProducto().getPrecio();
            datos[i][3] = item.getCantidad();
            datos[i][4] = item.getProducto().getPrecio() * item.getCantidad();
            i++;
        }

        DefaultTableModel model = new DefaultTableModel(datos, columnas);
        carritoAnadirView.getTblMostrar().setModel(model);
    }

    private void actualizarTotales() {
        if (carrito == null) return;
        double subtotal = carrito.calcularTotal();
        double iva = subtotal * 0.12;
        double total = subtotal + iva;

        carritoAnadirView.getTxtSubtotal().setText(String.format("%.2f", subtotal));
        carritoAnadirView.getTxtIva().setText(String.format("%.2f", iva));
        carritoAnadirView.getTxtTotal().setText(String.format("%.2f", total));
    }

    private void buscarProducto() {
        String nombre = productoListaView.getTxtBuscar().getText();
        List<Producto> productosEncontrados = productoDAO.buscarPorNombre(nombre);
        productoListaView.cargarDatos(productosEncontrados);
    }

    public void listarProductos() {
        productoListaView.cargarDatos(productoDAO.listarTodos());
    }

    public void listarProductosParaEditar() {
        productoEditarView.cargarDatos(productoDAO.listarTodos());
    }

    public void listarProductosParaEliminar() {
        productoEliminarView.cargarDatos(productoDAO.listarTodos());
    }
}