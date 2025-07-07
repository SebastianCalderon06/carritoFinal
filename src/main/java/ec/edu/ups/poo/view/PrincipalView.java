package ec.edu.ups.poo.view;

import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;

public class PrincipalView extends JFrame {
    private JDesktopPane jDesktopPane;

    private JMenuBar menuBar;

    private JMenu menuProducto;
    private JMenuItem menuItemCrearProducto;
    private JMenuItem menuItemEditarProducto;
    private JMenuItem menuItemEliminarProducto;
    private JMenuItem menuItemBuscarProducto;

    private JMenu menuCarrito;
    private JMenuItem menuItemCarrito;
    private JMenuItem menuItemCarritoListar;
    private JMenuItem menuItemCarritoBuscar;
    private JMenuItem menuItemCarritoModificar;
    private JMenuItem menuItemCarritoEliminar;

    private JMenu menuAdmin;
    private JMenuItem menuItemGestionarUsuarios;
    private JMenuItem menuItemBuscarUsuario;
    private JMenuItem menuItemCrearUsuario;

    private JMenu menuUser;
    private JMenuItem menuItemUser;

    private JMenu menuIdiomas;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemEspañol;
    private JMenuItem menuItemFrances;

    private JMenuItem menuItemCerrarSesion;

    public PrincipalView() {
        jDesktopPane = new JDesktopPaneConFondo();
        initComponents();
        actualizarTextos();
    }

    private void initComponents() {
        menuBar = new JMenuBar();

        // Producto
        menuProducto = new JMenu();
        menuItemCrearProducto = new JMenuItem();
        menuItemEditarProducto = new JMenuItem();
        menuItemEliminarProducto = new JMenuItem();
        menuItemBuscarProducto = new JMenuItem();
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEditarProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemBuscarProducto);
        menuBar.add(menuProducto);

        // Carrito
        menuCarrito = new JMenu();
        menuItemCarrito = new JMenuItem();
        menuItemCarritoListar = new JMenuItem();
        menuItemCarritoBuscar = new JMenuItem();
        menuItemCarritoModificar = new JMenuItem();
        menuItemCarritoEliminar = new JMenuItem();
        menuCarrito.add(menuItemCarrito);
        menuCarrito.add(menuItemCarritoListar);
        menuCarrito.add(menuItemCarritoBuscar);
        menuCarrito.add(menuItemCarritoModificar);
        menuCarrito.add(menuItemCarritoEliminar);
        menuBar.add(menuCarrito);

        // Admin
        menuAdmin = new JMenu();
        menuItemGestionarUsuarios = new JMenuItem();
        menuItemBuscarUsuario = new JMenuItem();
        menuItemCrearUsuario = new JMenuItem();
        menuAdmin.add(menuItemGestionarUsuarios);
        menuAdmin.add(menuItemBuscarUsuario);
        menuAdmin.add(menuItemCrearUsuario);
        menuBar.add(menuAdmin);

        // Usuario
        menuUser = new JMenu();
        menuItemUser = new JMenuItem();
        menuUser.add(menuItemUser);
        menuBar.add(menuUser);

        // Cerrar sesión (a la derecha)
        menuItemCerrarSesion = new JMenuItem();
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuItemCerrarSesion);

        // Idiomas
        menuIdiomas = new JMenu();
        menuItemIngles = new JMenuItem();
        menuItemEspañol = new JMenuItem();
        menuItemFrances = new JMenuItem();
        menuIdiomas.add(menuItemIngles);
        menuIdiomas.add(menuItemEspañol);
        menuIdiomas.add(menuItemFrances);
        menuBar.add(menuIdiomas);

        setJMenuBar(menuBar);
        setContentPane(jDesktopPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Idioma.get("titulo"));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("titulo"));

        menuProducto.setText(Idioma.get("menu.producto"));
        menuItemCrearProducto.setText(Idioma.get("menu.producto.crear"));
        menuItemEditarProducto.setText(Idioma.get("menu.producto.editar"));
        menuItemEliminarProducto.setText(Idioma.get("menu.producto.eliminar"));
        menuItemBuscarProducto.setText(Idioma.get("menu.producto.buscar"));

        menuCarrito.setText(Idioma.get("menu.carrito"));
        menuItemCarrito.setText(Idioma.get("menu.carrito.anadir"));
        menuItemCarritoListar.setText(Idioma.get("menu.carrito.listar"));
        menuItemCarritoBuscar.setText(Idioma.get("menu.carrito.buscar"));
        menuItemCarritoModificar.setText(Idioma.get("menu.carrito.modificar"));
        menuItemCarritoEliminar.setText(Idioma.get("menu.carrito.eliminar"));

        menuAdmin.setText(Idioma.get("menu.administrador"));
        menuItemGestionarUsuarios.setText(Idioma.get("menu.administrador.gestionar"));
        menuItemBuscarUsuario.setText(Idioma.get("menu.administrador.buscar"));
        menuItemCrearUsuario.setText(Idioma.get("menu.administrador.crear"));

        menuUser.setText(Idioma.get("menu.usuario"));
        menuItemUser.setText(Idioma.get("menu.usuario.usuario"));

        menuItemCerrarSesion.setText(Idioma.get("menu.cerrarSesion.cerrar"));

        menuIdiomas.setText(Idioma.get("menu.idiomas"));
        menuItemIngles.setText(Idioma.get("menu.idiomas.ingles"));
        menuItemEspañol.setText(Idioma.get("menu.idiomas.español"));
        menuItemFrances.setText(Idioma.get("menu.idiomas.frances"));
    }

    public void configurarParaRolUsuario() {
        menuProducto.setVisible(false);
        menuAdmin.setVisible(false);
        menuUser.setVisible(true);
        menuCarrito.setVisible(true);
    }

    public void configurarParaRolAdmin() {
        menuProducto.setVisible(true);
        menuAdmin.setVisible(true);
        menuUser.setVisible(false);
        menuCarrito.setVisible(true);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void cambiarIdiomas(String lang, String country) {
        Idioma.setIdioma(lang, country);
        actualizarTextos();
    }

    // Getters públicos

    public JMenuItem getMenuItemCrearProducto() { return menuItemCrearProducto; }
    public JMenuItem getMenuItemEditarProducto() { return menuItemEditarProducto; }
    public JMenuItem getMenuItemEliminarProducto() { return menuItemEliminarProducto; }
    public JMenuItem getMenuItemBuscarProducto() { return menuItemBuscarProducto; }
    public JMenuItem getMenuItemCarrito() { return menuItemCarrito; }
    public JMenuItem getMenuItemCarritoListar() { return menuItemCarritoListar; }
    public JMenuItem getMenuItemCarritoBuscar() { return menuItemCarritoBuscar; }
    public JMenuItem getMenuItemCarritoModificar() { return menuItemCarritoModificar; }
    public JMenuItem getMenuItemCarritoEliminar() { return menuItemCarritoEliminar; }
    public JMenuItem getMenuItemGestionarUsuarios() { return menuItemGestionarUsuarios; }
    public JMenuItem getMenuItemBuscarUsuario() { return menuItemBuscarUsuario; }
    public JMenuItem getMenuItemCrearUsuario() { return menuItemCrearUsuario; }
    public JMenuItem getMenuItemUser() { return menuItemUser; }
    public JMenuItem getMenuItemCerrarSesion() { return menuItemCerrarSesion; }
    public JMenuItem getMenuItemIngles() { return menuItemIngles; }
    public JMenuItem getMenuItemEspañol() { return menuItemEspañol; }
    public JMenuItem getMenuItemFrances() { return menuItemFrances; }
    public JDesktopPane getjDesktopPane() { return jDesktopPane; }
}
