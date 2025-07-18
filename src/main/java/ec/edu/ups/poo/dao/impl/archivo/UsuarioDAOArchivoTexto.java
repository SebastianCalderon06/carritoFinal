package ec.edu.ups.poo.dao.impl.archivo;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UsuarioDAOArchivoTexto implements UsuarioDAO {
    private String rutaArchivo;
    private List<Usuario> usuarios;

    public UsuarioDAOArchivoTexto(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.usuarios = new ArrayList<>();
        cargarUsuarios(); // Cargar datos al inicializar
    }

    private void cargarUsuarios() {
        usuarios.clear(); // Limpiar la lista actual antes de cargar
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo de usuarios no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Usuario usuario = deserializarUsuario(linea);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer usuarios desde el archivo de texto: " + e.getMessage());
            // Considerar lanzar una RuntimeException o manejarla de otra forma si la carga inicial falla
        }
    }

    private void guardarUsuarios() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Usuario usuario : usuarios) {
                writer.write(serializarUsuario(usuario));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en el archivo de texto: " + e.getMessage());
        }
    }

    // Método para convertir un objeto Usuario a una cadena de texto
    private String serializarUsuario(Usuario u) {
        StringBuilder sb = new StringBuilder();
        sb.append(u.getUsername()).append("|") // Usamos '|' como delimitador principal
                .append(u.getPassword()).append("|")
                .append(u.getRol().name()).append("|") // Guardamos el nombre del enum
                .append(u.getGenero().name()).append("|")
                .append(u.getNombre()).append("|")
                .append(u.getApellido()).append("|")
                .append(u.getTelefono()).append("|")
                .append(u.getEdad());

        // Serializar preguntas de seguridad: id:respuesta;id:respuesta
        if (u.getPreguntasSeguridad() != null && !u.getPreguntasSeguridad().isEmpty()) {
            sb.append("|"); // Nuevo delimitador para las preguntas
            List<String> preguntasList = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : u.getPreguntasSeguridad().entrySet()) {
                preguntasList.add(entry.getKey() + ":" + entry.getValue().replace("|", "")); // Evitar '|' en respuestas
            }
            sb.append(String.join(";", preguntasList)); // ';' como delimitador entre preguntas
        }
        return sb.toString();
    }

    // Método para convertir una cadena de texto a un objeto Usuario
    private Usuario deserializarUsuario(String linea) {
        String[] partes = linea.split("\\|"); // Dividir por el delimitador principal '|'
        if (partes.length < 8) { // Mínimo de 8 partes para los campos básicos
            System.err.println("Línea de usuario incompleta: " + linea);
            return null;
        }

        try {
            String username = partes[0];
            String password = partes[1];
            Rol rol = Rol.valueOf(partes[2]);
            Genero genero = Genero.valueOf(partes[3]);
            String nombre = partes[4];
            String apellido = partes[5];
            String telefono = partes[6];
            int edad = Integer.parseInt(partes[7]);

            Usuario u = new Usuario(username, password, rol, genero, nombre, apellido, telefono, edad);

            // Deserializar preguntas de seguridad si existen
            if (partes.length > 8 && !partes[8].isEmpty()) {
                Map<Integer, String> preguntasSeguridad = new HashMap<>();
                String[] preguntasStr = partes[8].split(";"); // Dividir por ';' entre preguntas
                for (String p : preguntasStr) {
                    String[] kv = p.split(":"); // Dividir por ':' para id y respuesta
                    if (kv.length == 2) {
                        preguntasSeguridad.put(Integer.parseInt(kv[0]), kv[1]);
                    }
                }
                u.setPreguntasSeguridad(preguntasSeguridad);
            }
            return u;
        } catch (Exception e) {
            System.err.println("Error al deserializar línea de usuario: " + e.getMessage() + " -> " + linea);
            return null;
        }
    }

    @Override
    public void crear(Usuario usuario) {
        if (buscarPorUsername(usuario.getUsername()) == null) {
            usuarios.add(usuario);
            guardarUsuarios();
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Usuario usuarioActualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).equals(usuarioActualizado)) { // Usa el equals de Usuario (basado en username)
                usuarios.set(i, usuarioActualizado);
                guardarUsuarios();
                return;
            }
        }
    }

    @Override
    public void eliminar(String username) {
        Iterator<Usuario> it = usuarios.iterator();
        while (it.hasNext()) {
            Usuario u = it.next();
            if (u.getUsername().equals(username)) {
                it.remove();
                guardarUsuarios();
                return;
            }
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios); // Devolver una copia para evitar modificación externa
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.getRol() == rol) {
                filtrados.add(u);
            }
        }
        return filtrados;
    }

    @Override
    public Usuario autenticar(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}