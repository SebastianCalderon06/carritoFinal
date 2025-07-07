package ec.edu.ups.poo.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Carrito {
    private Usuario usuario;
    private List<ItemCarrito> items;
    private GregorianCalendar fechaCreacion;
    private int codigo;

    public Carrito() {
        fechaCreacion = new GregorianCalendar();
        items = new ArrayList<>();
    }

    // --- GETTERS Y SETTERS ---
    public GregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(GregorianCalendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items);
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    // --- LÓGICA DE NEGOCIO ---

    public void agregarProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return;
        }

        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == producto.getCodigo()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }

        items.add(new ItemCarrito(producto, cantidad));
    }

    public void actualizarCantidad(int codigoProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(codigoProducto);
            return;
        }

        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                item.setCantidad(nuevaCantidad);
                return;
            }
        }
    }

    public void eliminarProducto(int codigoProducto) {
        items.removeIf(item -> item.getProducto().getCodigo() == codigoProducto);
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public double calcularSubtotal() {
        double totalSubtotal = 0;
        for (ItemCarrito item : items) {
            totalSubtotal += item.getSubtotal();
        }
        return totalSubtotal;
    }

    public double calcularIva() {
        return calcularSubtotal() * 0.12;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIva();
    }

    public List<ItemCarrito> obtenerItems() {
        return new ArrayList<>(items);
    }
}
