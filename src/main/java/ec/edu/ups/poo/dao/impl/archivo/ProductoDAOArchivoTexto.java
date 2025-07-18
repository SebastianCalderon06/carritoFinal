package ec.edu.ups.poo.dao.impl.archivo;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductoDAOArchivoTexto implements ProductoDAO {
    private String rutaArchivo;
    private List<Producto> productos;

    public ProductoDAOArchivoTexto(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.productos = new ArrayList<>();
        cargarProductos();
    }

    private void cargarProductos() {
        productos.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo de productos no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Producto producto = deserializarProducto(linea);
                if (producto != null) {
                    productos.add(producto);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer productos desde el archivo de texto: " + e.getMessage());
        }
    }

    private void guardarProductos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Producto producto : productos) {
                writer.write(serializarProducto(producto));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar productos en el archivo de texto: " + e.getMessage());
        }
    }

    private String serializarProducto(Producto p) {
        return p.getCodigo() + "|" + p.getNombre() + "|" + p.getPrecio();
    }

    private Producto deserializarProducto(String linea) {
        String[] partes = linea.split("\\|");
        if (partes.length < 3) {
            System.err.println("Línea de producto incompleta: " + linea);
            return null;
        }
        try {
            int codigo = Integer.parseInt(partes[0]);
            String nombre = partes[1];
            double precio = Double.parseDouble(partes[2]);
            return new Producto(codigo, nombre, precio);
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico al deserializar producto: " + e.getMessage() + " -> " + linea);
            return null;
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
        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                return producto;
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
            Producto producto = iterator.next();
            if (producto.getCodigo() == codigo) {
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