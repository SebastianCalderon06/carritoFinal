package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Pregunta;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;
import ec.edu.ups.poo.view.*;
import ec.edu.ups.poo.modelo.Genero;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.*;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private PreguntaDAO preguntaDAO;
    private LoginView loginView;
    private RegistrarUsuario registrarUsuarioView;
    private UsuarioAdminView usuarioAdminView;
    private Usuario usuarioAutenticado;
    private UsuarioBuscarView usuarioBuscarView;
    private UsuarioCrearView usuarioCrearView;
    private UsuarioModificarDatosView usuarioModificarDatosView;
    private OlvideContrasenaView olvideContrasenaView;

    private Usuario usuarioRecuperacion;
    private Integer idPreguntaRecuperacion;

    public UsuarioController(UsuarioDAO usuarioDAO,PreguntaDAO preguntaDAO,LoginView loginView,
                             RegistrarUsuario registrarUsuarioView, UsuarioAdminView usuarioAdminView,
                             UsuarioBuscarView usuarioBuscarView, UsuarioCrearView usuarioCrearView,
                             UsuarioModificarDatosView usuarioModificarDatosView
    ) {
        this.usuarioDAO = usuarioDAO;
        this.preguntaDAO = preguntaDAO;
        this.loginView = loginView;
        this.registrarUsuarioView = registrarUsuarioView;
        this.usuarioAdminView = usuarioAdminView;
        this.usuarioBuscarView = usuarioBuscarView;
        this.usuarioCrearView = usuarioCrearView;
        this.usuarioModificarDatosView = usuarioModificarDatosView;
        this.olvideContrasenaView = new OlvideContrasenaView();
        configurarEventosIdioma();
        configurarEventos();
    }
    private void configurarEventosIdioma() {
        ActionListener cambiarIdiomaListener = e -> {
            String lang = "es", country = "EC";

            if (e.getSource() instanceof JMenuItem) {
                JMenuItem sourceItem = (JMenuItem) e.getSource();
                if (sourceItem.getText().equals(Idioma.get("menu.idiomas.ingles"))) {
                    lang = "en"; country = "US";
                } else if (sourceItem.getText().equals(Idioma.get("menu.idiomas.frances"))) {
                    lang = "fr"; country = "FR";
                }
            }
            Idioma.setIdioma(lang, country);

            loginView.actualizarTextos();
            registrarUsuarioView.actualizarTextos();
            olvideContrasenaView.actualizarTextos();
        };

        loginView.getMenuItemEspañol().addActionListener(cambiarIdiomaListener);
        loginView.getMenuItemIngles().addActionListener(cambiarIdiomaListener);
        loginView.getMenuItemFrances().addActionListener(cambiarIdiomaListener);

        registrarUsuarioView.getMenuItemEspañol().addActionListener(cambiarIdiomaListener);
        registrarUsuarioView.getMenuItemIngles().addActionListener(cambiarIdiomaListener);
        registrarUsuarioView.getMenuItemFrances().addActionListener(cambiarIdiomaListener);

        olvideContrasenaView.getMenuItemEspañol().addActionListener(cambiarIdiomaListener);
        olvideContrasenaView.getMenuItemIngles().addActionListener(cambiarIdiomaListener);
        olvideContrasenaView.getMenuItemFrances().addActionListener(cambiarIdiomaListener);
    }

    private void configurarEventos() {
        this.loginView.getBtnIniciarSesion().addActionListener(e -> login());
        this.loginView.getBtnOlvide().addActionListener(e ->  abrirOlvideContrasena());
        this.loginView.getBtnRegistrarse().addActionListener(e -> abrirVentanaRegistro());

        this.registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());

        olvideContrasenaView.getBtnValidar().addActionListener(e -> validarUsuarioYMostrarPregunta());
        olvideContrasenaView.getBtnCambiar().addActionListener(e -> cambiarContrasena());

        this.usuarioAdminView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        this.usuarioAdminView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
        this.usuarioAdminView.getBtnRefrescar().addActionListener(e -> listarUsuarios());

        if (usuarioBuscarView != null) {
            usuarioBuscarView.getBtnBuscar().addActionListener(e -> buscarUsuarioAction());
            usuarioBuscarView.getBtnListar().addActionListener(e -> listarTodosAction());
        }
        if (usuarioCrearView != null) {
            usuarioCrearView.getBtnCrear().addActionListener(e -> crearUsuarioDesdeCrearView());
            usuarioCrearView.getBtnBorrar().addActionListener(e -> usuarioCrearView.getTxtUsuario().setText(""));
            usuarioCrearView.getBtnBorrar().addActionListener(e -> usuarioCrearView.getTxtContrasena().setText(""));
        }
        if (usuarioModificarDatosView != null) {
            usuarioModificarDatosView.getBtnModificar().addActionListener(e -> modificarDatosUsuario());
            usuarioModificarDatosView.getBtnBorrar().addActionListener(e -> usuarioModificarDatosView.limpiarCampos());
        }
    }
    private void abrirOlvideContrasena() {
        olvideContrasenaView.getTxtUsername().setText("");
        olvideContrasenaView.getLblPregunta().setText("");
        olvideContrasenaView.getTxtRespuesta().setText("");
        olvideContrasenaView.getLblNuevaContrasena().setVisible(false);
        olvideContrasenaView.getTxtNuevaContrasena().setVisible(false);
        olvideContrasenaView.getBtnCambiar().setVisible(false);
        olvideContrasenaView.setVisible(true);
    }
    private void validarUsuarioYMostrarPregunta() {
        String username = olvideContrasenaView.getTxtUsername().getText().trim();
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.msj.noecontrado"));
            return;
        }
        usuarioRecuperacion = usuario;
        List<Integer> idsPreguntas = new ArrayList<>(usuario.getPreguntasSeguridad().keySet());
        if (idsPreguntas.isEmpty()) {
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.nopreguntas"));
            return;
        }
        idPreguntaRecuperacion = idsPreguntas.get(new Random().nextInt(idsPreguntas.size()));
        Pregunta pregunta = preguntaDAO.buscarPorId(idPreguntaRecuperacion);
        if (pregunta != null) {
            olvideContrasenaView.getLblPregunta().setText(pregunta.getPregunta());
        } else {
            olvideContrasenaView.getLblPregunta().setText("Pregunta no encontrada.");
        }
        olvideContrasenaView.getLblNuevaContrasena().setVisible(true);
        olvideContrasenaView.getTxtNuevaContrasena().setVisible(true);
        olvideContrasenaView.getBtnCambiar().setVisible(true);
    }
    private void cambiarContrasena() {
        String respuesta = olvideContrasenaView.getTxtRespuesta().getText().trim();
        String respuestaCorrecta = usuarioRecuperacion.getPreguntasSeguridad().get(idPreguntaRecuperacion);
        if (!respuesta.equals(respuestaCorrecta)) {
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.incorrecta"));
            return;
        }
        String nuevaContra = new String(olvideContrasenaView.getTxtNuevaContrasena().getPassword());
        if (nuevaContra.isEmpty()) {
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.nuevacontra"));
            return;
        }
        usuarioRecuperacion.setPassword(nuevaContra);
        usuarioDAO.actualizar(usuarioRecuperacion);
        JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.msj.cambiado"));
        olvideContrasenaView.dispose();
    }
    private void login() {
        String username = loginView.getTxtUsername().getText().trim();
        String password = new String(loginView.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginView.mostrar("usuario.controller.msj.porfavor");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            loginView.mostrar("usuario.controller.msj.noecontrado");
            return;
        }
        if (!usuario.getPassword().equals(password)) {
            loginView.mostrar("usuario.controller.msj.contrasena");
            return;
        }
        this.usuarioAutenticado = usuario;
        loginView.dispose();
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    private void abrirVentanaRegistro() {
        List<Pregunta> preguntas = preguntaDAO.listarTodas();
        registrarUsuarioView.setPreguntasDisponibles(preguntas);
        registrarUsuarioView.limpiarCampos();
        registrarUsuarioView.setVisible(true);
    }

    private void registrarUsuario() {
        String username = registrarUsuarioView.getTxtUsuarioRe().getText().trim();
        String password = new String(registrarUsuarioView.getTxtContraRe().getPassword());
        String nombre = registrarUsuarioView.getTxtNombre().getText().trim();
        String apellido = registrarUsuarioView.getTxtApellido().getText().trim();
        String telefono = registrarUsuarioView.getTxtTelefono().getText().trim();
        String edadStr = registrarUsuarioView.getTxtEdad().getText().trim();

        Genero genero = null;
        Object itemGenero = registrarUsuarioView.getCbxGenero().getSelectedItem();
        if (itemGenero instanceof Genero) {
            genero = (Genero) itemGenero;
        } else if (itemGenero != null) {
            try {
                genero = Genero.valueOf(itemGenero.toString().toUpperCase());
            } catch (Exception ignore) {
                genero = Genero.OTROS;
            }
        } else {
            genero = Genero.OTROS;
        }

        if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                telefono.isEmpty() || edadStr.isEmpty()) {
            registrarUsuarioView.mostrarMensaje("registro.msj.camposprincipales.vacios");
            return;
        }
        if (usuarioDAO.buscarPorUsername(username) != null) {
            registrarUsuarioView.mostrarMensaje("registro.msj.yaextite");
            return;
        }
        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            registrarUsuarioView.mostrarMensaje("registro.msj.edadinvalida");
            return;
        }

        Map<Pregunta, String> preguntasRespondidas = registrarUsuarioView.getPreguntasYRespuestas();
        if (preguntasRespondidas.size() < 3) {
            registrarUsuarioView.mostrarMensaje("registro.msj.minpreguntas");
            return;
        }

        Usuario nuevo = new Usuario(username, password, Rol.USUARIO, genero, nombre, apellido, telefono, edad);
        Map<Integer, String> preguntasParaGuardar = new HashMap<>();
        for (Map.Entry<Pregunta, String> entry : preguntasRespondidas.entrySet()) {
            preguntasParaGuardar.put(entry.getKey().getId(), entry.getValue());
        }
        nuevo.setPreguntasSeguridad(preguntasParaGuardar);

        usuarioDAO.crear(nuevo);
        registrarUsuarioView.mostrarMensaje("registro.msj.registrado");
        registrarUsuarioView.limpiarCampos();
        registrarUsuarioView.dispose();
    }
    public void listarUsuarios() {
        List<Usuario> todosLosUsuarios = usuarioDAO.listarTodos();
        usuarioAdminView.cargarUsuarios(todosLosUsuarios);
    }

    public void listarUsuariosPorRol(Rol rol) {
        List<Usuario> usuariosFiltrados = usuarioDAO.listarPorRol(rol);
        usuarioAdminView.cargarUsuarios(usuariosFiltrados);
    }

    private void actualizarUsuario() {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();

        if (fila < 0) {
            usuarioAdminView.mostrarError(Idioma.get("usuario.controller.msj.seletable"));
            return;
        }

        String nuevoUsername = (String) usuarioAdminView.getModeloTabla().getValueAt(fila, 0);
        Rol rolActual = (Rol) usuarioAdminView.getTblUsuarios().getValueAt(fila, 1);
        Rol nuevoRol = (Rol) usuarioAdminView.getCbxRol().getSelectedItem();

        if (rolActual == nuevoRol && nuevoUsername.equals(usuarioDAO.buscarPorUsername(nuevoUsername).getUsername())) {
            usuarioAdminView.mostrarMensaje("usuario.controller.msj.sincambios");
            return;
        }

        Usuario usuarioBuscado = usuarioDAO.buscarPorUsername(nuevoUsername);
        if (usuarioBuscado != null && !usuarioAdminView.getTblUsuarios().getValueAt(fila, 0).equals(usuarioBuscado.getUsername())) {
            usuarioAdminView.mostrarError("usuario.controller.msj.uso");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorUsername((String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0));
        if (usuario == null) {
            usuarioAdminView.mostrarError("usuario.controller.msj.encontrado");
            return;
        }

        if (!usuarioAdminView.confirmarAccion("usuario.controller.msj.seguro", nuevoUsername, nuevoRol)) {
            return;
        }

        usuario.setUsername(nuevoUsername);
        usuario.setRol(nuevoRol);
        usuarioDAO.actualizar(usuario);

        listarUsuarios();
        usuarioAdminView.mostrarMensaje("usuario.controller.msj.actualizado");
    }

    private void eliminarUsuario() {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();

        if (fila < 0) {
            usuarioAdminView.mostrarError("usuario.controller.msj.seleliminado");
            return;
        }

        String username = (String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0);

        if (!usuarioAdminView.confirmarAccion("usuario.controller.msj.seguroelimar", username)) {
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            usuarioAdminView.mostrarError("usuario.controller.msj.encontrado");
            return;
        }

        usuarioDAO.eliminar(username);

        listarUsuarios();
        usuarioAdminView.limpiarSeleccion();
        usuarioAdminView.mostrarMensaje("usuario.controller.msj.eliminado");
    }

    public void cerrarSesion() {
        this.usuarioAutenticado = null;
    }

    private void buscarUsuarioAction() {
        String username = usuarioBuscarView.getTxtUsername().getText().trim();
        if (username.isEmpty()) {
            usuarioBuscarView.mostrarMensaje(Idioma.get("usuario.controller.msj.ingreseusuario"));
            return;
        }
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        usuarioBuscarView.mostrarUsuario(usuario);
    }

    public void listarTodosAction() {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        usuarioBuscarView.cargarUsuarios(usuarios);
    }

    private void crearUsuarioDesdeCrearView() {
        String username = usuarioCrearView.getTxtUsuario().getText().trim();
        String password = new String(usuarioCrearView.getTxtContrasena().getPassword());

        if (username.isEmpty()) {
            usuarioCrearView.mostrarMensaje("usuario.controller.msj.nousuario");
            return;
        }
        if (password.isEmpty()) {
            usuarioCrearView.mostrarMensaje("usuario.controller.msj.nopass");
            return;
        }
        if (usuarioDAO.buscarPorUsername(username) != null) {
            usuarioCrearView.mostrarMensaje("usuario.controller.extiste");
            return;
        }
        Usuario nuevo = new Usuario(username, password, Rol.USUARIO, Genero.OTROS, "", "", "", 0);
        usuarioDAO.crear(nuevo);
        usuarioCrearView.mostrarMensaje("usuario.controller.msj.creado");
        usuarioCrearView.getTxtUsuario().setText("");
        usuarioCrearView.getTxtContrasena().setText("");
        if (usuarioAdminView != null) listarUsuarios();
        if (usuarioBuscarView != null) listarTodosAction();
    }

    private void modificarDatosUsuario() {
        String nuevoUsername = usuarioModificarDatosView.getTxtUsuario().getText().trim();
        String nuevaContra = new String(usuarioModificarDatosView.getTxtContra().getPassword());
        String nuevoNombre = usuarioModificarDatosView.getTxtNombre().getText().trim();
        String nuevoApellido = usuarioModificarDatosView.getTxtApellido().getText().trim();
        String nuevoTelefono = usuarioModificarDatosView.getTxtTelefono().getText().trim();
        String edadTexto = usuarioModificarDatosView.getTxtEdad().getText().trim();
        int nuevaEdad = 0;
        try {
            nuevaEdad = Integer.parseInt(edadTexto);
        } catch (Exception ex) {
            mostrarMensaje("La edad debe ser un número válido");
            return;
        }
        Genero nuevoGenero = (Genero) usuarioModificarDatosView.getCbxGenero().getSelectedItem();

        if (nuevoUsername.isEmpty() || nuevaContra.isEmpty()) {
            mostrarMensaje(Idioma.get("usuario.controller.msj.nousuariopass"));
            return;
        }
        if (!nuevoUsername.equals(usuarioAutenticado.getUsername()) && usuarioDAO.buscarPorUsername(nuevoUsername) != null) {
            mostrarMensaje(Idioma.get("usuario.controller.msj.usoyaenuso"));
            return;
        }

        usuarioAutenticado.setUsername(nuevoUsername);
        usuarioAutenticado.setPassword(nuevaContra);
        usuarioAutenticado.setNombre(nuevoNombre);
        usuarioAutenticado.setApellido(nuevoApellido);
        usuarioAutenticado.setTelefono(nuevoTelefono);
        usuarioAutenticado.setEdad(nuevaEdad);
        usuarioAutenticado.setGenero(nuevoGenero);

        usuarioDAO.actualizar(usuarioAutenticado);

        mostrarMensaje(Idioma.get("usuario.controller.msj.actualizadodatos"));
        usuarioModificarDatosView.mostrarDatosUsuario(usuarioAutenticado);

        if (usuarioAdminView != null) listarUsuarios();
        if (usuarioBuscarView != null) listarTodosAction();
    }

    private void mostrarMensaje(String mensaje) {
        if (usuarioModificarDatosView != null) {
            JOptionPane.showMessageDialog(usuarioModificarDatosView, mensaje);
        }
    }

}
