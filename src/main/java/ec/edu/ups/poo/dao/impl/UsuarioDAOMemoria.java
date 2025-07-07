package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsuarioDAOMemoria implements UsuarioDAO {
    private List<Usuario> listaUsuarios;

    private void mostrarEstadisticas() {
        System.out.println("=== Estadísticas de usuarios ===");
        System.out.println("Total de usuarios: " + listaUsuarios.size());
        System.out.println("Administradores: " + listarPorRol(Rol.ADMINISTRADOR).size());
        System.out.println("Usuarios normales: " + listarPorRol(Rol.USUARIO).size());
        System.out.println("===============================");
    }

    private void imprimirUsuarios() {
        System.out.println("=== Lista de usuarios ===");
        for (int index = 0; index < listaUsuarios.size(); index++) {
            Usuario u = listaUsuarios.get(index);
            System.out.println((index + 1) + ". " + u.toString());
        }
        System.out.println("========================");
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        if (rol == null) {
            System.out.println("No se puede listar usuarios con rol nulo");
            return new ArrayList<>();
        }
        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario u : listaUsuarios) {
            if (u.getRol() == rol) {
                filtrados.add(u);
            }
        }
        System.out.println("Usuarios encontrados con rol " + rol + ": " + filtrados.size());
        return filtrados;
    }

    @Override
    public void eliminar(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("No se puede eliminar un usuario sin nombre de usuario");
            return;
        }
        Iterator<Usuario> it = listaUsuarios.iterator();
        while (it.hasNext()) {
            Usuario u = it.next();
            if (u.getUsername().equals(username)) {
                it.remove();
                System.out.println("Usuario eliminado exitosamente: " + username);
                System.out.println("Total de usuarios restantes: " + listaUsuarios.size());
                return;
            }
        }
        System.out.println("Usuario no encontrado para eliminar: " + username);
    }

    @Override
    public void actualizar(Usuario usuarioActualizado) {
        if (usuarioActualizado == null || usuarioActualizado.getUsername() == null) {
            System.out.println("No se puede actualizar un usuario nulo o sin nombre de usuario");
            return;
        }
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario existente = listaUsuarios.get(i);
            if (existente.getUsername().equals(usuarioActualizado.getUsername())) {
                listaUsuarios.set(i, usuarioActualizado);
                System.out.println("Usuario actualizado exitosamente: " + usuarioActualizado.getUsername());
                System.out.println("Nueva información: " + usuarioActualizado);
                return;
            }
        }
        System.out.println("Usuario no encontrado para actualizar: " + usuarioActualizado.getUsername());
    }

    @Override
    public void crear(Usuario usuario) {
        if (usuario == null) {
            System.out.println("No se puede crear un usuario nulo");
            return;
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            System.out.println("No se puede crear un usuario sin nombre de usuario");
            return;
        }
        if (buscarPorUsername(usuario.getUsername()) != null) {
            System.out.println("Ya existe un usuario con el nombre: " + usuario.getUsername());
            return;
        }
        listaUsuarios.add(usuario);
        System.out.println("Usuario creado exitosamente: " + usuario.getUsername());
        System.out.println("Total de usuarios: " + listaUsuarios.size());
    }

    @Override
    public Usuario autenticar(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                System.out.println("Autenticación exitosa para: " + username);
                return u;
            }
        }
        System.out.println("Autenticación fallida para: " + username);
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        System.out.println("Listando todos los usuarios. Total: " + listaUsuarios.size());
        return new ArrayList<>(listaUsuarios);
    }

    public UsuarioDAOMemoria() {
        listaUsuarios = new ArrayList<>();
        inicializarUsuariosPorDefecto();
    }

    private void inicializarUsuariosPorDefecto() {
        Usuario admin = new Usuario("admin", "12345", Rol.ADMINISTRADOR, Genero.MASCULINO, "Admin", "Principal", "000000000", 30);
        Usuario usuarioNormal = new Usuario("usuario", "12345", Rol.USUARIO, Genero.FEMININO, "Usuario", "Principal", "099324321", 20);
        Usuario usuario2 = new Usuario("juan", "1234", Rol.USUARIO, Genero.MASCULINO, "Juan", "Perez", "03232432434", 18);
        Usuario usuario3 = new Usuario("maria", "1234", Rol.USUARIO, Genero.FEMININO, "Maria", "Perez", "03232432434", 16);
        Usuario admin2 = new Usuario("supervisor", "admin", Rol.ADMINISTRADOR, Genero.MASCULINO, "Supervisor", "Principal", "03232432434", 20);

        listaUsuarios.add(admin);
        listaUsuarios.add(usuarioNormal);
        listaUsuarios.add(usuario2);
        listaUsuarios.add(usuario3);
        listaUsuarios.add(admin2);

        System.out.println("Usuarios inicializados: " + listaUsuarios.size());
        imprimirUsuarios();
    }
}
