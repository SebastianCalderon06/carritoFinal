package ec.edu.ups.poo.view;

import ec.edu.ups.poo.controlador.CarritoController;
import ec.edu.ups.poo.controlador.ProductoController;
import ec.edu.ups.poo.controlador.UsuarioController;
import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.poo.dao.impl.PreguntaDAOMemoria;
import ec.edu.ups.poo.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.poo.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.poo.dao.impl.archivo.CarritoDAOArchivoTexto;
import ec.edu.ups.poo.dao.impl.archivo.PreguntaDAOArchivoTexto;
import ec.edu.ups.poo.dao.impl.archivo.ProductoDAOArchivoTexto;
import ec.edu.ups.poo.dao.impl.archivo.UsuarioDAOArchivoTexto;
import ec.edu.ups.poo.dao.impl.binario.CarritoDAOArchivoBinario;
import ec.edu.ups.poo.dao.impl.binario.PreguntaDAOArchivoBinario;
import ec.edu.ups.poo.dao.impl.binario.ProductoDAOArchivoBinario;
import ec.edu.ups.poo.dao.impl.binario.UsuarioDAOArchivoBinario;

import ec.edu.ups.poo.modelo.*;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Clase principal de la aplicaci\u00f3n de carrito de compras.
 * Se encarga de inicializar la interfaz de usuario, los DAOs (Data Access Objects),
 * y los controladores, gestionando el flujo principal de la aplicaci\u00f3n.
 * Permite al usuario seleccionar el tipo de almacenamiento de datos al iniciar sesi\u00f3n.
 *
 * @author Tu Nombre del Estudiante
 * @version 1.0
 * @since 2023-01-01
 */
public class Main {

    private static UsuarioDAO usuarioDAO;
    private static ProductoDAO productoDAO;
    private static CarritoDAO carritoDAO;
    private static PreguntaDAO preguntaDAO;
    private static LoginView loginView;

    /**
     * M\u00e9todo principal de la aplicaci\u00f3n.
     * Inicializa la interfaz de login, gestiona la selecci\u00f3n del tipo de almacenamiento
     * y las dependencias de DAOs, y lanza la aplicaci\u00f3n principal una vez que el usuario se autentica.
     *
     * @param args Argumentos de la l\u00ednea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loginView = new LoginView();
            loginView.setVisible(true);

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario usuarioAutenticado = loginView.getUsuarioAutenticado();

                    if (usuarioAutenticado == null) {
                        System.exit(0);
                        return;
                    }

                    String tipoAlmacenamiento = (String) loginView.getCbxTipoAlmacenamiento().getSelectedItem();
                    String rutaArchivos = loginView.getTxtRutaArchivos().getText();

                    if (tipoAlmacenamiento.contains("Archivos")) {
                        if (rutaArchivos.isEmpty()) {
                            JOptionPane.showMessageDialog(loginView, Idioma.get("login.error.rutaVacia"), Idioma.get("main.error"), JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                            return;
                        }
                        File dir = new File(rutaArchivos);
                        if (!dir.exists()) {
                            if (!dir.mkdirs()) {
                                JOptionPane.showMessageDialog(loginView, Idioma.get("main.error.crearDirectorio") + rutaArchivos, Idioma.get("main.error"), JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                                return;
                            }
                        }
                    }

                    try {
                        switch (tipoAlmacenamiento) {
                            case "Memoria":
                                usuarioDAO = new UsuarioDAOMemoria();
                                productoDAO = new ProductoDAOMemoria();
                                carritoDAO = new CarritoDAOMemoria();
                                preguntaDAO = new PreguntaDAOMemoria();
                                break;
                            case "Archivos de Texto":
                                usuarioDAO = new UsuarioDAOArchivoTexto(rutaArchivos + File.separator + "usuarios.txt");
                                productoDAO = new ProductoDAOArchivoTexto(rutaArchivos + File.separator + "productos.txt");
                                carritoDAO = new CarritoDAOArchivoTexto(rutaArchivos + File.separator + "carritos.txt", productoDAO, usuarioDAO);
                                preguntaDAO = new PreguntaDAOArchivoTexto(rutaArchivos + File.separator + "preguntas.txt");
                                break;
                            case "Archivos Binarios":
                                usuarioDAO = new UsuarioDAOArchivoBinario(rutaArchivos + File.separator + "usuarios.dat");
                                productoDAO = new ProductoDAOArchivoBinario(rutaArchivos + File.separator + "productos.dat");
                                carritoDAO = new CarritoDAOArchivoBinario(rutaArchivos + File.separator + "carritos.dat", productoDAO, usuarioDAO);
                                preguntaDAO = new PreguntaDAOArchivoBinario(rutaArchivos + File.separator + "preguntas.dat");
                                break;
                            default:
                                JOptionPane.showMessageDialog(loginView, Idioma.get("main.error.tipoAlmacenamiento"), Idioma.get("main.error"), JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                                return;
                        }

                        inicializarPreguntasSeguridad();
                        cargarDatosIniciales();

                        ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                        ProductoListaView productoListaView = new ProductoListaView();
                        ProductoEditar productoEditar = new ProductoEditar();
                        ProductoEliminar productoEliminar = new ProductoEliminar();

                        CarritoAnadirView carritoAnadirView = new CarritoAnadirView();
                        CarritoListarView carritoListarView = new CarritoListarView();
                        CarritoBuscarView carritoBuscarView = new CarritoBuscarView();
                        CarritoModificarView carritoModificarView = new CarritoModificarView();
                        CarritoEliminarView carritoEliminarView = new CarritoEliminarView();

                        UsuarioAdminView usuarioAdminView = new UsuarioAdminView();
                        UsuarioBuscarView usuarioBuscarView = new UsuarioBuscarView();
                        UsuarioCrearView usuarioCrearView = new UsuarioCrearView();
                        UsuarioModificarDatosView usuarioModificarDatosView = new UsuarioModificarDatosView();
                        RegistrarUsuario registrarUsuarioView = new RegistrarUsuario();


                        UsuarioController usuarioController = new UsuarioController(
                                usuarioDAO, preguntaDAO,
                                loginView, registrarUsuarioView,
                                usuarioAdminView, usuarioBuscarView,
                                usuarioCrearView, usuarioModificarDatosView);

                        ProductoController productoController = new ProductoController(
                                productoDAO, productoAnadirView, productoListaView,
                                productoEditar, productoEliminar, carritoAnadirView);

                        CarritoController carritoController = new CarritoController(
                                carritoDAO, productoDAO, carritoAnadirView,
                                usuarioAutenticado, carritoListarView,
                                carritoBuscarView, carritoModificarView,
                                carritoEliminarView);

                        iniciarAplicacionPrincipal(
                                usuarioAutenticado,
                                usuarioController,
                                productoAnadirView, productoListaView, productoEditar, productoEliminar,
                                carritoAnadirView, carritoListarView, carritoBuscarView, carritoModificarView, carritoEliminarView,
                                usuarioAdminView, usuarioBuscarView, usuarioCrearView, usuarioModificarDatosView,
                                carritoController);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(loginView, Idioma.get("main.error.inicializacionDAOs") + ex.getMessage(), Idioma.get("main.error"), JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        System.exit(1);
                    }
                }
            });
        });
    }

    /**
     * Inicializa las preguntas de seguridad predefinidas en el sistema.
     * Las preguntas solo se a\u00f1aden si a\u00fan no existen en el DAO.
     */
    private static void inicializarPreguntasSeguridad() {
        if (preguntaDAO.buscarPorId(1) == null) preguntaDAO.crear(new Pregunta(1, "\u00bfNombre de tu primera mascota?"));
        if (preguntaDAO.buscarPorId(2) == null) preguntaDAO.crear(new Pregunta(2, "\u00bfCiudad de nacimiento?"));
        if (preguntaDAO.buscarPorId(3) == null) preguntaDAO.crear(new Pregunta(3, "\u00bfComida favorita?"));
        if (preguntaDAO.buscarPorId(4) == null) preguntaDAO.crear(new Pregunta(4, "\u00bfAnimal favorito?"));
        if (preguntaDAO.buscarPorId(5) == null) preguntaDAO.crear(new Pregunta(5, "\u00bfNombre de tu mejor amigo de la infancia?"));
        if (preguntaDAO.buscarPorId(6) == null) preguntaDAO.crear(new Pregunta(6, "\u00bfCom\u00f3 se llama tu escuela primaria?"));
        if (preguntaDAO.buscarPorId(7) == null) preguntaDAO.crear(new Pregunta(7,"\u00bfCu\u00e1l es el segundo nombre de tu padre?"));
        if (preguntaDAO.buscarPorId(8) == null) preguntaDAO.crear(new Pregunta(8,"\u00bfCu\u00e1l fue tu primer empleo?"));
        if (preguntaDAO.buscarPorId(9) == null) preguntaDAO.crear(new Pregunta(9,"\u00bfC\u00f3mo se llama tu pel\u00edcula favorita?"));
        if (preguntaDAO.buscarPorId(10) == null) preguntaDAO.crear(new Pregunta(10,"\u00bfCu\u00e1l es el nombre de tu primer profeso(a)? "));
    }

    /**
     * Carga datos iniciales de usuarios y productos si no existen en el DAO actualmente configurado.
     * Estos datos sirven para prop\u00f3sitos de prueba y demostraci\u00f3n.
     */
    private static void cargarDatosIniciales() {
        if (usuarioDAO.buscarPorUsername("admin") == null) {
            usuarioDAO.crear(new Usuario("admin", "admin", Rol.ADMINISTRADOR, Genero.MASCULINO, "Admin", "Principal", "000000000", 30));
        }
        if (usuarioDAO.buscarPorUsername("user") == null) {
            usuarioDAO.crear(new Usuario("user", "user", Rol.USUARIO, Genero.OTROS, "User", "Demo", "111111111", 18));
        }

        if (productoDAO.buscarPorCodigo(1) == null) productoDAO.crear(new Producto(1, "Laptop Gamer", 1200.00));
        if (productoDAO.buscarPorCodigo(2) == null) productoDAO.crear(new Producto(2, "Teclado Mec\u00e1nico", 85.50));
        if (productoDAO.buscarPorCodigo(3) == null) productoDAO.crear(new Producto(3, "Mouse Inal\u00e1mbrico", 30.00));
        if (productoDAO.buscarPorCodigo(4) == null) productoDAO.crear(new Producto(4, "Monitor 27 pulgadas", 350.00));
    }

    /**
     * Inicia la aplicaci\u00f3n principal del carrito de compras.
     * Configura la vista principal, los men\u00fas seg\u00fan el rol del usuario,
     * y establece los oyentes de eventos para todas las funcionalidades.
     *
     * @param usuarioAutenticado El objeto Usuario que ha iniciado sesi\u00f3n.
     * @param usuarioController Controlador de usuarios, ya configurado con los DAOs.
     * @param productoAnadirView Vista para a\u00f1adir productos.
     * @param productoListaView Vista para listar productos.
     * @param productoEditar Vista para editar productos.
     * @param productoEliminar Vista para eliminar productos.
     * @param carritoAnadirView Vista para a\u00f1adir \u00edtems al carrito.
     * @param carritoListarView Vista para listar carritos.
     * @param carritoBuscarView Vista para buscar carritos.
     * @param carritoModificarView Vista para modificar carritos.
     * @param carritoEliminarView Vista para eliminar carritos.
     * @param usuarioAdminView Vista de administraci\u00f3n de usuarios.
     * @param usuarioBuscarView Vista para buscar usuarios.
     * @param usuarioCrearView Vista para crear usuarios.
     * @param usuarioModificarDatosView Vista para modificar datos de usuario.
     * @param carritoController Controlador de carritos, ya configurado con los DAOs.
     */
    private static void iniciarAplicacionPrincipal(Usuario usuarioAutenticado,
                                                   UsuarioController usuarioController,
                                                   ProductoAnadirView productoAnadirView, ProductoListaView productoListaView,
                                                   ProductoEditar productoEditar, ProductoEliminar productoEliminar,
                                                   CarritoAnadirView carritoAnadirView, CarritoListarView carritoListarView,
                                                   CarritoBuscarView carritoBuscarView, CarritoModificarView carritoModificarView,
                                                   CarritoEliminarView carritoEliminarView,
                                                   UsuarioAdminView usuarioAdminView, UsuarioBuscarView usuarioBuscarView,
                                                   UsuarioCrearView usuarioCrearView, UsuarioModificarDatosView usuarioModificarDatosView,
                                                   CarritoController carritoController) {

        PrincipalView principalView = new PrincipalView();

        if (usuarioAutenticado.getRol() == Rol.USUARIO) {
            principalView.configurarParaRolUsuario();
        } else if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            principalView.configurarParaRolAdmin();
        }

        principalView.mostrarMensaje(Idioma.get("main.bienvenida") + ": " + usuarioAutenticado.getUsername() + " (" + usuarioAutenticado.getRol() + ")");

        configurarEventosProductos(principalView, productoAnadirView, productoListaView, productoEditar, productoEliminar);
        configurarEventosCarrito(principalView, carritoAnadirView, carritoListarView, carritoBuscarView,
                carritoModificarView, carritoEliminarView, carritoController, usuarioAutenticado);
        configurarEventosUsuarios(principalView, usuarioController, usuarioAdminView,
                usuarioBuscarView, usuarioCrearView, usuarioModificarDatosView, usuarioAutenticado);
        configurarEventosIdioma(principalView, carritoAnadirView, carritoBuscarView, carritoEliminarView,
                carritoListarView, carritoModificarView, productoAnadirView, productoEditar,
                productoEliminar, productoListaView, usuarioAdminView, usuarioBuscarView,
                usuarioCrearView, usuarioModificarDatosView);
        configurarEventosCerrarSesion(principalView);

        principalView.setVisible(true);
    }

    /**
     * Configura los oyentes de eventos para los \u00edtems del men\u00fa relacionados con la gesti\u00f3n de productos.
     *
     * @param principalView La vista principal de la aplicaci\u00f3n.
     * @param productoAnadirView Vista para a\u00f1adir productos.
     * @param productoListaView Vista para listar productos.
     * @param productoEditar Vista para editar productos.
     * @param productoEliminar Vista para eliminar productos.
     */
    private static void configurarEventosProductos(PrincipalView principalView, ProductoAnadirView productoAnadirView,
                                                   ProductoListaView productoListaView, ProductoEditar productoEditar,
                                                   ProductoEliminar productoEliminar) {
        principalView.getMenuItemCrearProducto().addActionListener(e -> mostrarVentana(principalView, productoAnadirView));
        principalView.getMenuItemBuscarProducto().addActionListener(e -> mostrarVentana(principalView, productoListaView));
        principalView.getMenuItemEditarProducto().addActionListener(e -> mostrarVentana(principalView, productoEditar));
        principalView.getMenuItemEliminarProducto().addActionListener(e -> mostrarVentana(principalView, productoEliminar));
    }

    /**
     * Configura los oyentes de eventos para los \u00edtems del men\u00fa relacionados con la gesti\u00f3n de carritos.
     *
     * @param principalView La vista principal de la aplicaci\u00f3n.
     * @param carritoAnadirView Vista para a\u00f1adir \u00edtems al carrito.
     * @param carritoListarView Vista para listar carritos.
     * @param carritoBuscarView Vista para buscar carritos.
     * @param carritoModificarView Vista para modificar carritos.
     * @param carritoEliminarView Vista para eliminar carritos.
     * @param carritoController El controlador de carritos.
     * @param usuarioAutenticado El usuario autenticado actual.
     */
    private static void configurarEventosCarrito(PrincipalView principalView,
                                                 CarritoAnadirView carritoAnadirView,
                                                 CarritoListarView carritoListarView,
                                                 CarritoBuscarView carritoBuscarView,
                                                 CarritoModificarView carritoModificarView,
                                                 CarritoEliminarView carritoEliminarView,
                                                 CarritoController carritoController,
                                                 Usuario usuarioAutenticado) {

        principalView.getMenuItemCarrito().addActionListener(e -> mostrarVentana(principalView, carritoAnadirView));

        principalView.getMenuItemCarritoListar().addActionListener(e -> {
            carritoController.listarCarritosDelUsuario();
            mostrarVentana(principalView, carritoListarView);
        });

        principalView.getMenuItemCarritoBuscar().addActionListener(e -> {
            carritoBuscarView.limpiarVista();
            carritoController.mostrarCarritosUsuarioParaBuscar();
            mostrarVentana(principalView, carritoBuscarView);
        });

        principalView.getMenuItemCarritoModificar().addActionListener(e -> {
            carritoModificarView.limpiarVista();
            carritoController.mostrarCarritosUsuarioParaModificar();
            mostrarVentana(principalView, carritoModificarView);
        });

        principalView.getMenuItemCarritoEliminar().addActionListener(e -> {
            carritoEliminarView.limpiarVista();
            carritoController.mostrarCarritosUsuarioParaEliminar();
            mostrarVentana(principalView, carritoEliminarView);
        });
    }

    /**
     * Configura los oyentes de eventos para los \u00edtems del men\u00fa relacionados con la gesti\u00f3n de usuarios.
     * Esto incluye funciones para administradores (gestionar, buscar, crear usuarios)
     * y para usuarios normales (modificar sus propios datos).
     *
     * @param principalView La vista principal de la aplicaci\u00f3n.
     * @param usuarioController El controlador de usuarios.
     * @param usuarioAdminView Vista de administraci\u00f3n de usuarios.
     * @param usuarioBuscarView Vista para buscar usuarios.
     * @param usuarioCrearView Vista para crear usuarios.
     * @param usuarioModificarDatosView Vista para modificar datos de usuario.
     * @param usuarioAutenticado El usuario autenticado actual.
     */
    private static void configurarEventosUsuarios(PrincipalView principalView,
                                                  UsuarioController usuarioController,
                                                  UsuarioAdminView usuarioAdminView,
                                                  UsuarioBuscarView usuarioBuscarView,
                                                  UsuarioCrearView usuarioCrearView,
                                                  UsuarioModificarDatosView usuarioModificarDatosView,
                                                  Usuario usuarioAutenticado) {

        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            principalView.getMenuItemGestionarUsuarios().addActionListener(e -> {
                usuarioController.listarUsuarios();
                mostrarVentana(principalView, usuarioAdminView);
            });

            principalView.getMenuItemBuscarUsuario().addActionListener(e -> {
                usuarioBuscarView.limpiarVista();
                usuarioController.listarTodosAction();
                mostrarVentana(principalView, usuarioBuscarView);
            });

            principalView.getMenuItemCrearUsuario().addActionListener(e -> {
                usuarioCrearView.getTxtUsuario().setText("");
                usuarioCrearView.getTxtContrasena().setText("");
                mostrarVentana(principalView, usuarioCrearView);
            });
        }

        if (usuarioAutenticado.getRol() == Rol.USUARIO) {
            principalView.getMenuItemUser().addActionListener(e -> {
                usuarioModificarDatosView.mostrarDatosUsuario(usuarioAutenticado);
                mostrarVentana(principalView, usuarioModificarDatosView);
            });
        }
    }

    /**
     * Configura los oyentes de eventos para los \u00edtems del men\u00fa de idiomas.
     * Al cambiar el idioma, actualiza los textos de todas las vistas de la aplicaci\u00f3n.
     *
     * @param principalView La vista principal de la aplicaci\u00f3n.
     * @param carritoAnadirView Vista para a\u00f1adir \u00edtems al carrito.
     * @param carritoBuscarView Vista para buscar carritos.
     * @param carritoEliminarView Vista para eliminar carritos.
     * @param carritoListarView Vista para listar carritos.
     * @param carritoModificarView Vista para modificar carritos.
     * @param productoAnadirView Vista para a\u00f1adir productos.
     * @param productoEditar Vista para editar productos.
     * @param productoEliminar Vista para eliminar productos.
     * @param productoListaView Vista para listar productos.
     * @param usuarioAdminView Vista de administraci\u00f3n de usuarios.
     * @param usuarioBuscarView Vista para buscar usuarios.
     * @param usuarioCrearView Vista para crear usuarios.
     * @param usuarioModificarDatosView Vista para modificar datos de usuario.
     */
    private static void configurarEventosIdioma(PrincipalView principalView,
                                                CarritoAnadirView carritoAnadirView,
                                                CarritoBuscarView carritoBuscarView,
                                                CarritoEliminarView carritoEliminarView,
                                                CarritoListarView carritoListarView,
                                                CarritoModificarView carritoModificarView,
                                                ProductoAnadirView productoAnadirView,
                                                ProductoEditar productoEditar,
                                                ProductoEliminar productoEliminar,
                                                ProductoListaView productoListaView,
                                                UsuarioAdminView usuarioAdminView,
                                                UsuarioBuscarView usuarioBuscarView,
                                                UsuarioCrearView usuarioCrearView,
                                                UsuarioModificarDatosView usuarioModificarDatosView) {

        ActionListener cambiarIdiomaListener = e -> {
            String lang = "es", country = "EC";
            if (e.getSource() == principalView.getMenuItemIngles()) {
                lang = "en";
                country = "US";
            } else if (e.getSource() == principalView.getMenuItemFrances()) {
                lang = "fr";
                country = "FR";
            }
            Idioma.setIdioma(lang, country);

            principalView.actualizarTextos();
            carritoAnadirView.actualizarTextos();
            carritoBuscarView.actualizarTextos();
            carritoEliminarView.actualizarTextos();
            carritoListarView.actualizarTextos();
            carritoModificarView.actualizarTextos();
            productoAnadirView.actualizarTextos();
            productoEditar.actualizarTextos();
            productoEliminar.actualizarTextos();
            productoListaView.actualizarTextos();
            usuarioAdminView.actualizarTextos();
            usuarioBuscarView.actualizarTextos();
            usuarioCrearView.actualizarTextos();
            usuarioModificarDatosView.actualizarTextos();
        };

        principalView.getMenuItemEspañol().addActionListener(cambiarIdiomaListener);
        principalView.getMenuItemFrances().addActionListener(cambiarIdiomaListener);
        principalView.getMenuItemIngles().addActionListener(cambiarIdiomaListener);
    }

    /**
     * Configura el oyente de eventos para la opci\u00f3n de "Cerrar Sesi\u00f3n".
     * Pide confirmaci\u00f3n al usuario y, si se acepta, cierra la aplicaci\u00f3n principal
     * y vuelve a mostrar la ventana de login para un nuevo inicio de sesi\u00f3n.
     *
     * @param principalView La vista principal de la aplicaci\u00f3n.
     */
    private static void configurarEventosCerrarSesion(PrincipalView principalView) {
        principalView.getMenuItemCerrarSesion().addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(principalView, Idioma.get("menu.cerrar"), Idioma.get("menu.cerrarse"), JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                principalView.dispose();
                SwingUtilities.invokeLater(() -> {
                    loginView.setUsuarioAutenticado(null);
                    loginView.setVisible(true);
                    if (loginView.getTxtUsername() != null) loginView.getTxtUsername().setText("");
                    if (loginView.getTxtPassword() != null) loginView.getTxtPassword().setText("");
                });
            }
        });
    }

    /**
     * Muestra una ventana interna (JInternalFrame) dentro del JDesktopPane de la vista principal.
     * Si la ventana ya est\u00e1 visible, la mueve al frente.
     *
     * @param principal La vista principal que contiene el JDesktopPane.
     * @param ventana La ventana interna (JInternalFrame) a mostrar.
     */
    private static void mostrarVentana(PrincipalView principal, JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            principal.getjDesktopPane().add(ventana);
            ventana.setVisible(true);
        }
        ventana.moveToFront();
    }
}