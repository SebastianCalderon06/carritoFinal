package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.Usuario;

import java.util.List;

public interface CarritoDAO {
    void crear(Carrito carrito);
    Carrito buscar(int codigo);
    void actualizar(Carrito carrito);
    void eliminar(int codigo);
    List<Carrito> listarTodos();
    List<Carrito> listarPorUsuario(Usuario usuario);
}