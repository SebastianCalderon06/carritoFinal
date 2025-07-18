package ec.edu.ups.poo.dao.impl.archivo;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PreguntaDAOArchivoTexto implements PreguntaDAO {
    private String rutaArchivo;
    private List<Pregunta> preguntas;

    public PreguntaDAOArchivoTexto(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.preguntas = new ArrayList<>();
        cargarPreguntas();
    }

    private void cargarPreguntas() {
        preguntas.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo de preguntas no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Pregunta pregunta = deserializarPregunta(linea);
                if (pregunta != null) {
                    preguntas.add(pregunta);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer preguntas desde el archivo de texto: " + e.getMessage());
        }
    }

    private void guardarPreguntas() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Pregunta pregunta : preguntas) {
                writer.write(serializarPregunta(pregunta));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar preguntas en el archivo de texto: " + e.getMessage());
        }
    }

    private String serializarPregunta(Pregunta p) {
        return p.getId() + "|" + p.getPregunta();
    }

    private Pregunta deserializarPregunta(String linea) {
        String[] partes = linea.split("\\|");
        if (partes.length < 2) {
            System.err.println("Línea de pregunta incompleta: " + linea);
            return null;
        }
        try {
            int id = Integer.parseInt(partes[0]);
            String textoPregunta = partes[1];
            return new Pregunta(id, textoPregunta);
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico al deserializar pregunta: " + e.getMessage() + " -> " + linea);
            return null;
        }
    }

    @Override
    public void crear(Pregunta pregunta) {
        if (buscarPorId(pregunta.getId()) == null) {
            preguntas.add(pregunta);
            guardarPreguntas();
        }
    }

    @Override
    public Pregunta buscarPorId(int id) {
        for (Pregunta p : preguntas) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Pregunta> listarTodas() {
        return new ArrayList<>(preguntas);
    }

    @Override
    public void eliminar(int id) {
        Iterator<Pregunta> iterator = preguntas.iterator();
        while (iterator.hasNext()) {
            Pregunta p = iterator.next();
            if (p.getId() == id) {
                iterator.remove();
                guardarPreguntas();
                return;
            }
        }
    }
}