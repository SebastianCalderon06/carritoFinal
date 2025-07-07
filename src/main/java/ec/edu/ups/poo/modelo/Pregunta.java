package ec.edu.ups.poo.modelo;

public class Pregunta {
    private String pregunta;
    private int id;

    public Pregunta(int id, String pregunta) {
        this.id = id;
        this.pregunta = pregunta;
    }

    public int getId() {
        return id;
    }

    public String getPregunta() {
        return pregunta;
    }

    @Override
    public String toString() {
        return pregunta;
    }
}
