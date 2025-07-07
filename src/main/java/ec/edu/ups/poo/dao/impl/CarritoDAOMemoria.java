package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarritoDAOMemoria implements CarritoDAO {
    private List<Carrito> carritos;
    private int nextCodigo = 1; // Para asignar códigos automáticamente

    public CarritoDAOMemoria() {
        carritos = new ArrayList<>();
    }

    @Override
    public void crear(Carrito carrito) {
        if (carrito.getCodigo() == 0) {
            carrito.setCodigo(nextCodigo++);
        }
        carritos.add(carrito);
    }

    @Override
    public void eliminar(int codigo) {
        Iterator<Carrito> iterator = carritos.iterator();
        while (iterator.hasNext()) {
            Carrito carrito = iterator.next();
            if (carrito.getCodigo() == codigo) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void actualizar(Carrito carrito) {
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
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
            if (c.getUsuario() != null && c.getUsuario().equals(usuario)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}
