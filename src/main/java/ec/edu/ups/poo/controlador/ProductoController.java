package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.Idioma;
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

    private void buscarProductoCarrito() {
        String codigo1 = carritoAnadirView.getTxtCodigo().getText();
        if (codigo1.equals("")) {
            carritoAnadirView.mostrarMensaje("carrito.info.noproducto");
            return;
        }
        int codigo = Integer.parseInt(codigo1);

        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto == null) {
            carritoAnadirView.mostrarMensaje("producto.controller.msj.encontrado");
            carritoAnadirView.getTxtNombre().setText("");
            carritoAnadirView.getTxtPrecio().setText("");
        } else {
            carritoAnadirView.getTxtNombre().setText(producto.getNombre());
            carritoAnadirView.getTxtPrecio().setText(String.valueOf(producto.getPrecio()));
        }
    }

    private void actualizarTablaCarrito() {
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
        double subtotal = carrito.calcularTotal();
        double iva = subtotal * 0.12;
        double total = subtotal + iva;

        carritoAnadirView.getTxtSubtotal().setText(String.format("%.2f", subtotal));
        carritoAnadirView.getTxtIva().setText(String.format("%.2f", iva));
        carritoAnadirView.getTxtTotal().setText(String.format("%.2f", total));
    }

    private void guardarProducto() {
        String codigo1 = productoAnadirView.getTxtCodigo().getText();
        if (codigo1.equals("")) {
            carritoAnadirView.mostrarMensaje("producto.info.noproducto");
            return;
        }
        int codigo = Integer.parseInt(codigo1);
        String nombre = productoAnadirView.getTxtNombre().getText();
        double precio = Double.parseDouble(productoAnadirView.getTxtPrecio().getText());

        productoDAO.crear(new Producto(codigo, nombre, precio));
        productoAnadirView.mostrarMensaje(Idioma.get("producto.controller.msj.guardado"));
        productoAnadirView.limpiarCampos();
        productoAnadirView.mostrarProductos(productoDAO.listarTodos());
    }

    private void buscarProducto() {
        String nombre = productoListaView.getTxtBuscar().getText();
        List<Producto> productosEncontrados = productoDAO.buscarPorNombre(nombre);
        productoListaView.cargarDatos(productosEncontrados);
    }

    private void listarProductos() {
        productoListaView.cargarDatos(productoDAO.listarTodos());
    }

    private void listarProductosParaEditar() {
        productoEditarView.cargarDatos(productoDAO.listarTodos());
    }

    private void editarProducto() {
        int filaSeleccionada = productoEditarView.getTblEditarProducto().getSelectedRow();
        if (filaSeleccionada >= 0) {
            int codigo = (int) productoEditarView.getTblEditarProducto().getValueAt(filaSeleccionada, 0);
            String nuevoNombre = productoEditarView.getTxtNombre().getText();

            if (!nuevoNombre.trim().isEmpty()) {
                Producto producto = productoDAO.buscarPorCodigo(codigo);
                if (producto != null) {
                    producto.setNombre(nuevoNombre);
                    productoDAO.actualizar(producto);
                    productoEditarView.mostrarMensaje("producto.controller.msj.actualizado");
                    productoEditarView.limpiarCampos();
                    listarProductosParaEditar();
                } else {
                    productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.encontrado"));
                }
            } else {
                productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.valido"));
            }
        } else {
            productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.selectable"));
        }
    }

    private void listarProductosParaEliminar() {
        productoEliminarView.cargarDatos(productoDAO.listarTodos());
    }

    private void eliminarProducto() {
        int filaSeleccionada = productoEliminarView.getTblEliminarProducto().getSelectedRow();

        if (filaSeleccionada < 0) {
            productoEliminarView.mostrarMensaje("producto.controller.msj.please");
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
            productoDAO.eliminar(codigo);
            productoEliminarView.mostrarMensaje(Idioma.get("producto.controller.msj.eliminado"));
            listarProductosParaEliminar();
        }
    }
}
