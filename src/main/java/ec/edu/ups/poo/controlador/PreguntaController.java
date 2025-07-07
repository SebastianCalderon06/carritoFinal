package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.util.List;

public class PreguntaController {
    private PreguntaDAO preguntaDAO;

    public PreguntaController(PreguntaDAO preguntaDAO) {
        this.preguntaDAO = preguntaDAO;
    }

    public List<Pregunta> obtenerTodasLasPreguntas() {
        return preguntaDAO.listarTodas();
    }

    public Pregunta buscarPorId(int id) {
        return preguntaDAO.buscarPorId(id);
    }

    public void agregarPregunta(Pregunta pregunta) {
        preguntaDAO.crear(pregunta);
    }
}
