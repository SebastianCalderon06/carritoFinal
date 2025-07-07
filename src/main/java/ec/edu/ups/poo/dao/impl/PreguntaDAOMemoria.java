package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class PreguntaDAOMemoria implements PreguntaDAO {
    private List<Pregunta> preguntas = new ArrayList<>();

    @Override
    public void crear(Pregunta pregunta) {
        preguntas.add(pregunta);
    }

    @Override
    public Pregunta buscarPorId(int id) {
        for (Pregunta p : preguntas) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    @Override
    public List<Pregunta> listarTodas() {
        return new ArrayList<>(preguntas);
    }

    @Override
    public void eliminar(int id) {
        preguntas.removeIf(p -> p.getId() == id);
    }
}
