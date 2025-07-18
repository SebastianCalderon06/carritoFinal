package ec.edu.ups.poo.dao.impl.binario;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsuarioDAOArchivoBinario implements UsuarioDAO {
    private String rutaArchivo;
    private List<Usuario> usuarios;

    public UsuarioDAOArchivoBinario(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.usuarios = new ArrayList<>();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        usuarios.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo binario de usuarios no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            usuarios = (List<Usuario>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Esto ya lo maneja el 'if (!archivo.exists())'
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar usuarios desde el archivo binario: " + e.getMessage());
            usuarios = new ArrayList<>(); // Si hay un error, inicializar lista vacía
        } catch (ClassNotFoundException e) {
            System.err.println("Clase de usuario no encontrada al deserializar: " + e.getMessage());
            usuarios = new ArrayList<>();
        }
    }

    private void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Error de E/S al guardar usuarios en el archivo binario: " + e.getMessage());
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
            if (usuarios.get(i).equals(usuarioActualizado)) {
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
        return new ArrayList<>(usuarios);
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