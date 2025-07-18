package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.*;
import ec.edu.ups.poo.util.Idioma;
import ec.edu.ups.poo.util.Validador;
import ec.edu.ups.poo.view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class CarritoController {
    private final CarritoDAO carritoDAO;
    private final ProductoDAO productoDAO;
    private final Usuario usuarioAutenticado;

    private final CarritoAnadirView carritoAnadirView;
    private final CarritoListarView carritoListarView;
    private final CarritoBuscarView carritoBuscarView;
    private final CarritoModificarView carritoModificarView;
    private final CarritoEliminarView carritoEliminarView;

    private Carrito carritoActual;

    private List<Carrito> carritosUltimosMostradosBuscar;
    private List<Carrito> carritosUltimosMostradosModificar;

    public CarritoController(CarritoDAO carritoDAO, ProductoDAO productoDAO,
                             CarritoAnadirView carritoAnadirView, Usuario usuarioAutenticado,
                             CarritoListarView carritoListarView, CarritoBuscarView carritoBuscarView,
                             CarritoModificarView carritoModificarView, CarritoEliminarView carritoEliminarView) {
        this.carritoDAO = carritoDAO;
        this.productoDAO = productoDAO;
        this.usuarioAutenticado = usuarioAutenticado;

        this.carritoAnadirView = carritoAnadirView;
        this.carritoListarView = carritoListarView;
        this.carritoBuscarView = carritoBuscarView;
        this.carritoModificarView = carritoModificarView;
        this.carritoEliminarView = carritoEliminarView;

        this.carritosUltimosMostradosBuscar = null;
        this.carritosUltimosMostradosModificar = null;

        iniciarNuevoCarrito();

        configurarEventosListar();
        configurarEventosAnadir();
        configurarEventosBuscar();
        configurarEventosModificar();
        configurarEventosEliminar();
    }


    private boolean tienePermiso(Carrito carrito) {
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            return true;
        }

        return carrito.getUsuario() != null && carrito.getUsuario().equals(usuarioAutenticado);
    }

    private void iniciarNuevoCarrito() {
        this.carritoActual = new Carrito();
        this.carritoActual.setUsuario(this.usuarioAutenticado);
    }

    private void configurarEventosAnadir() {
        carritoAnadirView.getBtnAnadir().addActionListener(e -> anadirProducto());
        carritoAnadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());
        carritoAnadirView.getBtnLimpiar().addActionListener(e -> limpiarCarritoActual());
    }



    /**
     * A\u00f1ade un producto al carrito actual.
     * Valida el c\u00f3digo del producto y la cantidad.
     */
    private void anadirProducto() {
        String codigoStr = carritoAnadirView.getTxtCodigo().getText();

        Object cantidadObj = carritoAnadirView.getCbxCanridad().getSelectedItem();
        String cantidadObjStr = (cantidadObj != null) ? cantidadObj.toString() : "";

        try {

            int codigo = Validador.validarEntero(codigoStr, "carrito.lbl.codigo");
            int cantidad = Validador.validarEntero(cantidadObjStr, "carrito.lbl.cantidad");


            if (cantidad <= 0) {
                carritoAnadirView.mostrarMensaje(Idioma.get("carrito.controller.msj.cantpositiva"));
                return;
            }

            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto == null) {
                carritoAnadirView.mostrarMensaje(Idioma.get("carrito.controller.msj.pnoe"));
                return;
            }

            carritoActual.agregarProducto(producto, cantidad);
            actualizarVistaAnadir();
            limpiarCamposProductoAnadir();
        } catch (IllegalArgumentException ex) {

            carritoAnadirView.mostrarMensaje(ex.getMessage());
        } catch (Exception ex) {

            carritoAnadirView.mostrarError("Error inesperado al a\u00f1adir producto al carrito: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void guardarCarrito() {
        if (carritoActual.estaVacio()) {
            carritoAnadirView.mostrarMensaje("carrito.controller.msj.carvac"); // Cart is empty
            return;
        }
        try {
            carritoDAO.crear(carritoActual);
            carritoAnadirView.mostrarMensajeConParametros("carrito.anadir.msj.guardado", carritoActual.getCodigo());
            limpiarCarritoActual();
        } catch (Exception ex) {
            carritoAnadirView.mostrarError("Error inesperado al guardar carrito: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCarritoActual() {
        iniciarNuevoCarrito();
        actualizarVistaAnadir();
        limpiarCamposProductoAnadir();
    }

    private void actualizarVistaAnadir() {
        DefaultTableModel modelo = (DefaultTableModel) carritoAnadirView.getTblMostrar().getModel();
        modelo.setRowCount(0);
        for (ItemCarrito item : carritoActual.obtenerItems()) {
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(), item.getProducto().getNombre(),
                    String.format("%.2f", item.getProducto().getPrecio()), item.getCantidad(), String.format("%.2f", item.getSubtotal())
            });
        }
        carritoAnadirView.getTxtSubtotal().setText(String.format("%.2f", carritoActual.calcularSubtotal()));
        carritoAnadirView.getTxtIva().setText(String.format("%.2f", carritoActual.calcularIva()));
        carritoAnadirView.getTxtTotal().setText(String.format("%.2f", carritoActual.calcularTotal()));
    }

    private void limpiarCamposProductoAnadir() {
        carritoAnadirView.getTxtCodigo().setText("");
        carritoAnadirView.getTxtNombre().setText("");
        carritoAnadirView.getTxtPrecio().setText("");
        carritoAnadirView.getCbxCanridad().setSelectedIndex(0);
    }

    private void configurarEventosListar() {
        carritoListarView.getBtnListar().addActionListener(e -> listarCarritosDelUsuario());
    }

    public void listarCarritosDelUsuario() {
        List<Carrito> carritos;
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            carritos = carritoDAO.listarTodos();
        } else {
            carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        }
        carritoListarView.cargarCarritos(carritos);
        carritoListarView.mostrarDetallesCarrito(null);
    }

    private void configurarEventosBuscar() {
        carritoBuscarView.getBtnBuscarCarrito().addActionListener(e -> buscarCarritoParaVer());
        carritoBuscarView.getBtnListar().addActionListener(e -> listarCarritosBuscar());
    }

    public void mostrarCarritosUsuarioParaBuscar() {
        List<Carrito> carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        carritoBuscarView.cargarCarritosUsuario(carritos);
    }

    /**
     * Busca un carrito espec\u00edfico para su visualizaci\u00f3n.
     * Valida que el c\u00f3digo sea num\u00e9rico y verifica permisos.
     */
    private void buscarCarritoParaVer() {
        String codigoStr = carritoBuscarView.getTxtBuscarCarrito().getText().trim();
        try {

            int codigo = Validador.validarEntero(codigoStr, "carrito.buscar.lbl.codigo");

            Carrito carrito = carritoDAO.buscar(codigo);
            if (carrito == null || !tienePermiso(carrito)) {
                carritoBuscarView.mostrarMensaje(Idioma.get("carrito.controller.msj.noper"));
                carritoBuscarView.mostrarCarrito(null);
                return;
            }
            carritoBuscarView.mostrarCarrito(carrito);
        } catch (IllegalArgumentException ex) {
            carritoBuscarView.mostrarMensaje(ex.getMessage());
            carritoBuscarView.mostrarCarrito(null);
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(carritoBuscarView,
                    Idioma.get("main.error") + ": " + Idioma.get("carrito.controller.msj.errorinesperado") + ex.getMessage(),
                    Idioma.get("main.error"),
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void listarCarritosBuscar() {

        List<Carrito> carritos;
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            carritos = carritoDAO.listarTodos();
        } else {
            carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        }
        carritoBuscarView.cargarCarritosUsuario(carritos);

    }

    private void configurarEventosEliminar() {
        carritoEliminarView.getBtnEliminar().addActionListener(e -> eliminarCarritoConfirmado());
        carritoEliminarView.getBtnListar().addActionListener(e -> mostrarCarritosUsuarioParaEliminar());
    }

    public void mostrarCarritosUsuarioParaEliminar() {

        List<Carrito> carritos;
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            carritos = carritoDAO.listarTodos();
        } else {
            carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        }
        carritoEliminarView.cargarCarritosUsuario(carritos);
    }

    private void eliminarCarritoConfirmado() {
        Carrito carrito = carritoEliminarView.getCarritoSeleccionado();
        if (carrito == null) {
            carritoEliminarView.mostrarMensaje(Idioma.get("carrito.controller.msj.selecar"));
            return;
        }

        boolean confirmar = carritoEliminarView.confirmarAccion("carrito.controller.msj.estaseguro");
        if (!confirmar) return;

        try {
            carritoDAO.eliminar(carrito.getCodigo());
            carritoEliminarView.mostrarMensaje("carrito.controller.msj.eliminado");
            mostrarCarritosUsuarioParaEliminar();
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(carritoEliminarView,
                    Idioma.get("main.error") + ": " + Idioma.get("carrito.controller.msj.errorinesperado") + ex.getMessage(),
                    Idioma.get("main.error"),
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void configurarEventosModificar() {
        carritoModificarView.getBtnBuscar().addActionListener(e -> buscarCarritoParaModificar());
        carritoModificarView.getBtnModificar().addActionListener(e -> guardarCambiosCarrito());
    }

    /**
     * Busca un carrito espec\u00edfico para su modificaci\u00f3n.
     * Valida que el c\u00f3digo sea num\u00e9rico y verifica permisos.
     */
    private void buscarCarritoParaModificar() {
        String codigoStr = carritoModificarView.getTxtCodigo().getText().trim();
        try {
            // Validate the cart code
            int codigo = Validador.validarEntero(codigoStr, "carrito.modificar.lbl.codigo");

            Carrito carrito = carritoDAO.buscar(codigo);
            if (carrito == null || !tienePermiso(carrito)) {
                carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.noper"));
                carritoModificarView.cargarCarrito(null);
                return;
            }
            carritoModificarView.cargarCarrito(carrito);
        } catch (IllegalArgumentException ex) {
            carritoModificarView.mostrarError(ex.getMessage());
            carritoModificarView.cargarCarrito(null);
        } catch (Exception ex) {
            carritoModificarView.mostrarError("Error inesperado al buscar carrito para modificar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Guarda los cambios en un carrito, permitiendo modificar la cantidad total de productos.
     * Valida que la nueva cantidad sea num\u00e9rica y no negativa.
     */
    private void guardarCambiosCarrito() {
        int fila = carritoModificarView.getTblModificar().getSelectedRow();
        if (fila == -1) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.modificarno")); // No cart selected
            return;
        }

        int codigoCarrito = (int) carritoModificarView.getTblModificar().getValueAt(fila, 0);
        Carrito carrito = carritoDAO.buscar(codigoCarrito);
        if (carrito == null) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.mjs.noencontrado"));
            return;
        }


        String nuevaCantidadTotalStr = carritoModificarView.getTblModificar().getValueAt(fila, 3).toString();
        int nuevaCantidadTotal;

        try {
            nuevaCantidadTotal = Validador.validarEntero(nuevaCantidadTotalStr, "carrito.modificar.tbl.canttotal");
            if (nuevaCantidadTotal < 0) {
                throw new IllegalArgumentException(Idioma.get("carrito.controller.msj.cantidadnegativa"));
            }
        } catch (IllegalArgumentException e) {
            carritoModificarView.mostrarError(e.getMessage());
            return;
        }


        int confirm = JOptionPane.showConfirmDialog(
                carritoModificarView,
                Idioma.get("carrito.controller.msj.seguromod"),
                Idioma.get("carrito.controller.msj.confirmar"),
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        List<ItemCarrito> items = carrito.obtenerItems();
        int cantidadOriginalTotal = items.stream().mapToInt(ItemCarrito::getCantidad).sum();

        if (cantidadOriginalTotal == 0 && nuevaCantidadTotal > 0) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.noproductos")); //
            return;
        }

        if (nuevaCantidadTotal == 0) {
            carrito.setItems(new ArrayList<>());
        } else if (cantidadOriginalTotal > 0) {
            int cantidadAcumulada = 0;
            for (int i = 0; i < items.size(); i++) {
                ItemCarrito item = items.get(i);
                int cantidadAntigua = item.getCantidad();
                int nuevaCantidad;


                if (i < items.size() - 1) {
                    nuevaCantidad = (int) Math.round((cantidadAntigua * 1.0 / cantidadOriginalTotal) * nuevaCantidadTotal);
                    if (nuevaCantidad < 0) nuevaCantidad = 0;
                    cantidadAcumulada += nuevaCantidad;
                } else {
                    nuevaCantidad = nuevaCantidadTotal - cantidadAcumulada;
                    if (nuevaCantidad < 0) nuevaCantidad = 0;
                }
                item.setCantidad(nuevaCantidad);
            }

            carrito.getItems().removeIf(item -> item.getCantidad() <= 0);
        }

        try {
            carritoDAO.actualizar(carrito);
            carritoModificarView.mostrarMensaje("carrito.controller.msj.exitoso");
            mostrarCarritosUsuarioParaModificar();
        } catch (Exception ex) {
            carritoModificarView.mostrarError("Error inesperado al guardar cambios en el carrito: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void mostrarCarritosUsuarioParaModificar() {
        List<Carrito> carritos;
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            carritos = carritoDAO.listarTodos();
        } else {
            carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        }
        carritoModificarView.cargarCarritosUsuario(carritos);
    }
}