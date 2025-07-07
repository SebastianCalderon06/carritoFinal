package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoListaView extends JInternalFrame {
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JPanel panelListarProducto;
    private JButton btnListar;
    private JLabel lblNombre;
    private DefaultTableModel modelo;

    public ProductoListaView() {
        setContentPane(panelListarProducto);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(500, 500);

        modelo = new DefaultTableModel();
        Object[] columnas = {"", "", ""}; // Internacionalizadas abajo
        modelo.setColumnIdentifiers(columnas);
        tblProductos.setModel(modelo);
        btnListar.setIcon(new ImageIcon(getClass().getResource("/icons/list-svgrepo-com.png")));
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/icons/search-plus-svgrepo-com.png")));
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("producto.listar.titulo"));
        btnBuscar.setText(Idioma.get("producto.listar.btn.buscar"));
        btnListar.setText(Idioma.get("producto.listar.btn.listar"));
        lblNombre.setText(Idioma.get("producto.listar.lbl.nombre"));

        modelo.setColumnIdentifiers(new Object[]{
                Idioma.get("producto.listar.tbl.codigo"),
                Idioma.get("producto.listar.tbl.nombre"),
                Idioma.get("producto.listar.tbl.precio")
        });

        tblProductos.setToolTipText(Idioma.get("producto.listar.tbl.tooltip"));
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public void setTxtBuscar(JTextField txtBuscar) {
        this.txtBuscar = txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JTable getTblProductos() {
        return tblProductos;
    }

    public void setTblProductos(JTable tblProductos) {
        this.tblProductos = tblProductos;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
    }

    public void cargarDatos(List<Producto> productos) {
        modelo.setNumRows(0);

        for (Producto producto : productos) {
            Object[] filaProducto = {producto.getCodigo(), producto.getNombre(), producto.getPrecio()};
            modelo.addRow(filaProducto);
        }
    }
}