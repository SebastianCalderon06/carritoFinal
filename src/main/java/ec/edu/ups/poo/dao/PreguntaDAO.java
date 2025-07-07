package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.modelo.Pregunta;

import java.util.List;

public interface PreguntaDAO {
    void crear(Pregunta pregunta);
    Pregunta buscarPorId(int id);
    List<Pregunta> listarTodas();
    void eliminar(int id);
}