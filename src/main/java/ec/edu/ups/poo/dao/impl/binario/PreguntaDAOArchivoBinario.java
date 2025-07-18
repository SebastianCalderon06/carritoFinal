package ec.edu.ups.poo.dao.impl.binario;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PreguntaDAOArchivoBinario implements PreguntaDAO {
    private String rutaArchivo;
    private List<Pregunta> preguntas;

    public PreguntaDAOArchivoBinario(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.preguntas = new ArrayList<>();
        cargarPreguntas();
    }

    private void cargarPreguntas() {
        preguntas.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo binario de preguntas no encontrado: " + rutaArchivo + ". Se creará al guardar.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            preguntas = (List<Pregunta>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Esto ya lo maneja el 'if (!archivo.exists())'
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar preguntas desde el archivo binario: " + e.getMessage());
            preguntas = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            System.err.println("Clase de pregunta no encontrada al deserializar: " + e.getMessage());
            preguntas = new ArrayList<>();
        }
    }

    private void guardarPreguntas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(preguntas);
        } catch (IOException e) {
            System.err.println("Error de E/S al guardar preguntas en el archivo binario: " + e.getMessage());
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