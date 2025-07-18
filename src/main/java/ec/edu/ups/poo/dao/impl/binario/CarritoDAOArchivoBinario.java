package ec.edu.ups.poo.dao.impl.binario;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CarritoDAOArchivoBinario implements CarritoDAO {
    private String rutaArchivo;
    private List<Carrito> carritos;
    private AtomicInteger nextCodigo; // Para la asignación automática de códigos
    // Los DAOs dependientes no se serializan, se pasan al constructor
    private transient ProductoDAO productoDAO;
    private transient UsuarioDAO usuarioDAO;

    // Constructor que acepta DAOs dependientes
    public CarritoDAOArchivoBinario(String rutaArchivo, ProductoDAO productoDAO, UsuarioDAO usuarioDAO) {
        this.rutaArchivo = rutaArchivo;
        this.productoDAO = productoDAO;
        this.usuarioDAO = usuarioDAO;
        this.carritos = new ArrayList<>();
        this.nextCodigo = new AtomicInteger(1);
        cargarCarritos();
    }

    private void cargarCarritos() {
        carritos.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo binario de carritos no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        int maxCodigo = 0;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            // Al deserializar, los objetos Producto y Usuario dentro de Carrito
            // deben reconstruirse usando los DAOs si solo se guardó una referencia (e.g., username/id).
            // Si los objetos completos se serializaron (implementando Serializable), Java los reconstruye.
            // Para este ejemplo, asumimos que Carrito y ItemCarrito contienen los objetos completos.
            carritos = (List<Carrito>) ois.readObject();
            // Recalcular nextCodigo si se cargan carritos
            for (Carrito c : carritos) {
                if (c.getCodigo() > maxCodigo) {
                    maxCodigo = c.getCodigo();
                }
            }
            nextCodigo.set(maxCodigo + 1);
        } catch (FileNotFoundException e) {
            // Ya manejado por el 'if (!archivo.exists())'
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar carritos desde el archivo binario: " + e.getMessage());
            carritos = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            System.err.println("Clase de carrito no encontrada al deserializar: " + e.getMessage());
            carritos = new ArrayList<>();
        }
    }

    private void guardarCarritos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(carritos);
        } catch (IOException e) {
            System.err.println("Error de E/S al guardar carritos en el archivo binario: " + e.getMessage());
        }
    }

    @Override
    public void crear(Carrito carrito) {
        if (carrito.getCodigo() == 0) { // Si el código no está asignado, usa el automático
            carrito.setCodigo(nextCodigo.getAndIncrement());
        } else if (buscar(carrito.getCodigo()) != null) {
            // Si el código ya está en uso, asigna uno nuevo para evitar colisiones
            carrito.setCodigo(nextCodigo.getAndIncrement());
            System.err.println("Código de carrito " + (carrito.getCodigo()-1) + " ya existe, asignando el nuevo código: " + carrito.getCodigo());
        }
        carritos.add(carrito);
        guardarCarritos();
    }

    @Override
    public void eliminar(int codigo) {
        Iterator<Carrito> iterator = carritos.iterator();
        while (iterator.hasNext()) {
            Carrito carrito = iterator.next();
            if (carrito.getCodigo() == codigo) {
                iterator.remove();
                guardarCarritos();
                return;
            }
        }
    }

    @Override
    public void actualizar(Carrito carrito) {
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                guardarCarritos();
                return;
            }
        }
    }

    @Override
    public Carrito buscar(int codigo) {
        for (Carrito carrito : carritos) {
            if (carrito.getCodigo() == codigo) {
                return carrito;
            }
        }
        return null;
    }

    @Override
    public List<Carrito> listarTodos() {
        return new ArrayList<>(carritos);
    }

    @Override
    public List<Carrito> listarPorUsuario(Usuario usuario) {
        List<Carrito> resultado = new ArrayList<>();
        for (Carrito c : carritos) {
            if (c.getUsuario() != null && c.getUsuario().getUsername().equals(usuario.getUsername())) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}