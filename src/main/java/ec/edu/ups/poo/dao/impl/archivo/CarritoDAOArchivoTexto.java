package ec.edu.ups.poo.dao.impl.archivo;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto; // Necesario para deserializar ItemCarrito
import ec.edu.ups.poo.modelo.Usuario; // Necesario para deserializar Carrito
import ec.edu.ups.poo.dao.ProductoDAO; // Necesitas un DAO de Producto para cargar productos por ID
import ec.edu.ups.poo.dao.UsuarioDAO; // Necesitas un DAO de Usuario para cargar usuarios por ID

import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger; // Para generar códigos

public class CarritoDAOArchivoTexto implements CarritoDAO {
    private String rutaArchivo;
    private List<Carrito> carritos;
    private AtomicInteger nextCodigo; // Para la asignación automática de códigos
    private transient ProductoDAO productoDAO; // Transitorio porque no lo serializamos directamente
    private transient UsuarioDAO usuarioDAO;   // Transitorio

    // Constructor que acepta DAOs dependientes
    public CarritoDAOArchivoTexto(String rutaArchivo, ProductoDAO productoDAO, UsuarioDAO usuarioDAO) {
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
            System.out.println("Archivo de carritos no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        int maxCodigo = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Carrito carrito = deserializarCarrito(linea);
                if (carrito != null) {
                    carritos.add(carrito);
                    if (carrito.getCodigo() > maxCodigo) {
                        maxCodigo = carrito.getCodigo();
                    }
                }
            }
            nextCodigo.set(maxCodigo + 1); // Actualizar el siguiente código disponible
        } catch (IOException e) {
            System.err.println("Error al leer carritos desde el archivo de texto: " + e.getMessage());
        }
    }

    private void guardarCarritos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Carrito carrito : carritos) {
                writer.write(serializarCarrito(carrito));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar carritos en el archivo de texto: " + e.getMessage());
        }
    }

    private String serializarCarrito(Carrito c) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.getCodigo()).append("|")
                .append(c.getFechaCreacion().getTimeInMillis()).append("|"); // Guardar fecha como long

        // Guardar solo el username del usuario para simplificar (requiere que UsuarioDAO lo encuentre)
        sb.append(c.getUsuario() != null ? c.getUsuario().getUsername() : "N/A").append("|");

        // Serializar ItemsCarrito (codigoProducto:cantidad;codigoProducto:cantidad)
        List<String> itemsList = new ArrayList<>();
        for (ItemCarrito item : c.obtenerItems()) {
            itemsList.add(item.getProducto().getCodigo() + ":" + item.getCantidad());
        }
        sb.append(String.join(";", itemsList));
        return sb.toString();
    }

    private Carrito deserializarCarrito(String linea) {
        String[] partes = linea.split("\\|");
        if (partes.length < 4) { // Código|Fecha|Username|Items
            System.err.println("Línea de carrito incompleta: " + linea);
            return null;
        }

        try {
            int codigo = Integer.parseInt(partes[0]);
            long fechaMillis = Long.parseLong(partes[1]);
            String username = partes[2];

            Carrito c = new Carrito();
            c.setCodigo(codigo);
            GregorianCalendar fecha = new GregorianCalendar();
            fecha.setTimeInMillis(fechaMillis);
            c.setFechaCreacion(fecha);

            // Reconstruir el usuario (usando UsuarioDAO)
            if (!"N/A".equals(username) && usuarioDAO != null) {
                Usuario usuario = usuarioDAO.buscarPorUsername(username);
                c.setUsuario(usuario);
            }

            // Reconstruir ItemsCarrito
            if (partes.length > 3 && productoDAO != null) {
                String itemsStr = partes[3];
                if (!itemsStr.isEmpty()) {
                    String[] itemsArr = itemsStr.split(";");
                    for (String itemPair : itemsArr) {
                        String[] kv = itemPair.split(":");
                        if (kv.length == 2) {
                            int prodCodigo = Integer.parseInt(kv[0]);
                            int cantidad = Integer.parseInt(kv[1]);
                            Producto producto = productoDAO.buscarPorCodigo(prodCodigo);
                            if (producto != null) {
                                c.agregarProducto(producto, cantidad); // Reutiliza lógica de Carrito
                            } else {
                                System.err.println("Producto con código " + prodCodigo + " no encontrado para carrito " + codigo);
                            }
                        }
                    }
                }
            }
            return c;
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico al deserializar carrito: " + e.getMessage() + " -> " + linea);
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al deserializar carrito: " + e.getMessage() + " -> " + linea);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void crear(Carrito carrito) {
        if (carrito.getCodigo() == 0) { // Si el código no está asignado, usa el automático
            carrito.setCodigo(nextCodigo.getAndIncrement());
        } else if (buscar(carrito.getCodigo()) != null) {
            // Si el código ya está en uso, no lo crees o asigna uno nuevo
            carrito.setCodigo(nextCodigo.getAndIncrement()); // Asigna uno nuevo si hay colisión
            System.err.println("Código de carrito " + carrito.getCodigo() + " ya existe, asignando uno nuevo.");
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
            // Asegúrate de que el usuario del carrito no sea nulo y compara por username o equals
            if (c.getUsuario() != null && c.getUsuario().getUsername().equals(usuario.getUsername())) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}
