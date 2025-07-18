package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Pregunta;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;
import ec.edu.ups.poo.view.*;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.util.Validador;
import ec.edu.ups.poo.exeptions.CedulaExeption;
import ec.edu.ups.poo.exeptions.ContraseñaExeption;

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

    public UsuarioController(UsuarioDAO usuarioDAO, PreguntaDAO preguntaDAO, LoginView loginView,
                             RegistrarUsuario registrarUsuarioView, UsuarioAdminView usuarioAdminView,
                             UsuarioBuscarView usuarioBuscarView, UsuarioCrearView usuarioCrearView,
                             UsuarioModificarDatosView usuarioModificarDatosView) {
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

        this.olvideContrasenaView.getBtnValidar().addActionListener(e -> validarUsuarioYMostrarPregunta());
        this.olvideContrasenaView.getBtnCambiar().addActionListener(e -> cambiarContrasena());

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
    /**
     * Permite al usuario cambiar su contrase\u00f1a despu\u00e9s de validar la respuesta a una pregunta de seguridad.
     * Valida que la respuesta no est\u00e9 vac\u00eda y que la nueva contrase\u00f1a cumpla los requisitos de seguridad.
     */
    private void cambiarContrasena() {
        String respuesta = olvideContrasenaView.getTxtRespuesta().getText().trim();
        String respuestaCorrecta = (usuarioRecuperacion != null && idPreguntaRecuperacion != null) ?
                usuarioRecuperacion.getPreguntasSeguridad().get(idPreguntaRecuperacion) : null;
        String nuevaContra = new String(olvideContrasenaView.getTxtNuevaContrasena().getPassword());

        try {
            Validador.validarNoVacio(respuesta, "olvide.lbl.respuesta");

            if (usuarioRecuperacion == null || respuestaCorrecta == null || !respuesta.equals(respuestaCorrecta)) {
                JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.incorrecta"), Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            Validador.validarContrasenaSegura(nuevaContra);

            usuarioRecuperacion.setPassword(nuevaContra);
            usuarioDAO.actualizar(usuarioRecuperacion);
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.msj.cambiado"), Idioma.get("login.info"), JOptionPane.INFORMATION_MESSAGE);
            olvideContrasenaView.dispose();

        } catch (ContraseñaExeption | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(olvideContrasenaView, ex.getMessage(), Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(olvideContrasenaView, "Error inesperado al cambiar contrase\u00f1a: " + ex.getMessage(), Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    /**
     * Intenta iniciar sesi\u00f3n con el nombre de usuario y contrase\u00f1a proporcionados.
     * Valida que los campos no est\u00e9n vac\u00edos y verifica las credenciales con el DAO.
     */
    private void login() {
        String username = loginView.getTxtUsername().getText().trim();
        String password = new String(loginView.getTxtPassword().getPassword());

        try {
            Validador.validarNoVacio(username, "login.lbl.usuario");
            Validador.validarNoVacio(password, "login.lbl.contrasena");

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
            loginView.setUsuarioAutenticado(usuario);
            loginView.dispose();
        } catch (IllegalArgumentException ex) {
            loginView.mostrar(ex.getMessage());
        } catch (Exception ex) {
            loginView.mostrar("Error inesperado durante el login: " + ex.getMessage());
            ex.printStackTrace();
        }
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
    /**
     * Registra un nuevo usuario en el sistema.
     * Realiza validaciones de obligatoriedad de campos, formato de c\u00e9dula (username),
     * requisitos de seguridad de contrase\u00f1a, edad y cantidad de preguntas de seguridad.
     * Muestra mensajes de error claros al usuario en caso de fallos de validaci\u00f3n.
     */
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
            } catch (IllegalArgumentException ignore) {
                genero = Genero.OTROS;
            }
        } else {
            genero = Genero.OTROS;
        }

        try {
            Validador.validarNoVacio(username, "registro.lbl.usuario");
            Validador.validarNoVacio(password, "registro.lbl.contrasena");
            Validador.validarNoVacio(nombre, "registro.lbl.nombre");
            Validador.validarNoVacio(apellido, "registro.lbl.apellido");
            Validador.validarNoVacio(telefono, "registro.lbl.telefono");
            Validador.validarNoVacio(edadStr, "registro.lbl.edad");

            Validador.validarCedulaEcuatoriana(username);
            Validador.validarContrasenaSegura(password);

            int edad = Validador.validarEntero(edadStr, "registro.lbl.edad");
            if (edad < 0) {
                throw new IllegalArgumentException(Idioma.get("registro.msj.edadinvalida"));
            }

            Usuario usuarioExistente = usuarioDAO.buscarPorUsername(username);
            if (usuarioExistente != null) {
                registrarUsuarioView.mostrarMensaje("registro.msj.yaexiste");
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

        } catch (CedulaExeption | ContraseñaExeption | IllegalArgumentException ex) {
            registrarUsuarioView.mostrarMensaje(ex.getMessage());
        } catch (Exception ex) {
            registrarUsuarioView.mostrarMensaje("Error inesperado al registrar: " + ex.getMessage());
            ex.printStackTrace();
        }
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
    /**
     * Crea un nuevo usuario desde la vista de creaci\u00f3n de usuario para administradores.
     * Valida que el nombre de usuario y la contrase\u00f1a no est\u00e9n vac\u00edos y que la contrase\u00f1a sea segura.
     */
    private void crearUsuarioDesdeCrearView() {
        String username = usuarioCrearView.getTxtUsuario().getText().trim();
        String password = new String(usuarioCrearView.getTxtContrasena().getPassword());

        try {
            Validador.validarNoVacio(username, "usuario.crear.lbl.usuario");
            Validador.validarContrasenaSegura(password);

            if (usuarioDAO.buscarPorUsername(username) != null) {
                JOptionPane.showMessageDialog(usuarioCrearView, Idioma.get("usuario.controller.extiste"), Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevo = new Usuario(username, password, Rol.USUARIO, Genero.OTROS, "", "", "", 0);
            usuarioDAO.crear(nuevo);
            JOptionPane.showMessageDialog(usuarioCrearView, Idioma.get("usuario.controller.msj.creado"), Idioma.get("login.info"), JOptionPane.INFORMATION_MESSAGE);
            usuarioCrearView.getTxtUsuario().setText("");
            usuarioCrearView.getTxtContrasena().setText("");
            if (usuarioAdminView != null) listarUsuarios();
            if (usuarioBuscarView != null) listarTodosAction();
        } catch (ContraseñaExeption | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(usuarioCrearView, ex.getMessage(), Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(usuarioCrearView, "Error inesperado al crear usuario: " + ex.getMessage(), Idioma.get("main.error"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    /**
     * Modifica los datos del usuario autenticado.
     * Valida todos los campos de datos personales y la contrase\u00f1a, asegurando que el nuevo username
     * no est\u00e9 ya en uso por otro usuario.
     */
    private void modificarDatosUsuario() {
        String nuevoUsername = usuarioModificarDatosView.getTxtUsuario().getText().trim();
        String nuevaContra = new String(usuarioModificarDatosView.getTxtContra().getPassword());
        String nuevoNombre = usuarioModificarDatosView.getTxtNombre().getText().trim();
        String nuevoApellido = usuarioModificarDatosView.getTxtApellido().getText().trim();
        String nuevoTelefono = usuarioModificarDatosView.getTxtTelefono().getText().trim();
        String edadTexto = usuarioModificarDatosView.getTxtEdad().getText().trim();
        Genero nuevoGenero = (Genero) usuarioModificarDatosView.getCbxGenero().getSelectedItem();

        try {
            Validador.validarNoVacio(nuevoUsername, "usuario.modificar.lbl.usuario");
            Validador.validarNoVacio(nuevaContra, "usuario.modificar.lbl.contrasena");
            Validador.validarNoVacio(nuevoNombre, "usuario.modificar.lbl.nombre");
            Validador.validarNoVacio(nuevoApellido, "usuario.modificar.lbl.apellido");
            Validador.validarNoVacio(nuevoTelefono, "usuario.modificar.lbl.telefono");
            Validador.validarNoVacio(edadTexto, "usuario.modificar.lbl.edad");

            Validador.validarContrasenaSegura(nuevaContra);

            int nuevaEdad = Validador.validarEntero(edadTexto, "usuario.modificar.lbl.edad");
            if (nuevaEdad < 0) {
                throw new IllegalArgumentException(Idioma.get("registro.msj.edadinvalida"));
            }

            if (!nuevoUsername.equals(usuarioAutenticado.getUsername()) && usuarioDAO.buscarPorUsername(nuevoUsername) != null) {
                JOptionPane.showMessageDialog(usuarioModificarDatosView, Idioma.get("usuario.controller.msj.usoyaenuso"), Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
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

            JOptionPane.showMessageDialog(usuarioModificarDatosView, Idioma.get("usuario.controller.msj.actualizadodatos"), Idioma.get("login.info"), JOptionPane.INFORMATION_MESSAGE);
            usuarioModificarDatosView.mostrarDatosUsuario(usuarioAutenticado);

            if (usuarioAdminView != null) listarUsuarios();
            if (usuarioBuscarView != null) listarTodosAction();
        } catch (ContraseñaExeption | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(usuarioModificarDatosView, ex.getMessage(), Idioma.get("login.error"), JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(usuarioModificarDatosView, "Error inesperado al modificar datos: " + ex.getMessage(), Idioma.get("main.error"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void mostrarMensaje(String mensaje) {
        if (usuarioModificarDatosView != null) {
            JOptionPane.showMessageDialog(usuarioModificarDatosView, mensaje);
        }
    }

}