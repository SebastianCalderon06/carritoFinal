package ec.edu.ups.poo.dao.impl.binario;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductoDAOArchivoBinario implements ProductoDAO {
    private String rutaArchivo;
    private List<Producto> productos;

    public ProductoDAOArchivoBinario(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.productos = new ArrayList<>();
        cargarProductos();
    }

    private void cargarProductos() {
        productos.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo binario de productos no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            productos = (List<Producto>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Esto ya lo maneja el 'if (!archivo.exists())'
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar productos desde el archivo binario: " + e.getMessage());
            productos = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            System.err.println("Clase de producto no encontrada al deserializar: " + e.getMessage());
            productos = new ArrayList<>();
        }
    }

    private void guardarProductos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(productos);
        } catch (IOException e) {
            System.err.println("Error de E/S al guardar productos en el archivo binario: " + e.getMessage());
        }
    }

    @Override
    public void crear(Producto producto) {
        if (buscarPorCodigo(producto.getCodigo()) == null) {
            productos.add(producto);
            guardarProductos();
        }
    }

    @Override
    public Producto buscarPorCodigo(int codigo) {
        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ArrayList<>(productos);
        }
        String nombreBusqueda = nombre.trim().toLowerCase();
        List<Producto> productosEncontrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(nombreBusqueda)) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    @Override
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                guardarProductos();
                break;
            }
        }
    }

    @Override
    public void eliminar(int codigo) {
        Iterator<Producto> iterator = productos.iterator();
        while (iterator.hasNext()) {
            Producto p = iterator.next();
            if (p.getCodigo() == codigo) {
                iterator.remove();
                guardarProductos();
                break;
            }
        }
    }

    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }
}
