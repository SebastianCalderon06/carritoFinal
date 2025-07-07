package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoEditar extends JInternalFrame {
    private JPanel panelEditarProducto;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton btnListar1;
    private JButton btnEditar;
    private JTable tblEditarProducto;
    private DefaultTableModel modelo;

    public ProductoEditar() {
        setContentPane(panelEditarProducto);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        setSize(600, 500);
        //setVisible(true);

        modelo = new DefaultTableModel();
        Object[] columnas = {"", "", ""}; // Internacionalizadas abajo
        modelo.setColumnIdentifiers(columnas);
        tblEditarProducto.setModel(modelo);

        tblEditarProducto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tblEditarProducto.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String nombre = (String) tblEditarProducto.getValueAt(filaSeleccionada, 1);
                    txtNombre.setText(nombre);
                }
            }
        });
        btnListar1.setIcon(new ImageIcon(getClass().getResource("/icons/list-svgrepo-com.png")));
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/icons/edit-3-svgrepo-com.png")));
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("producto.editar.titulo"));
        lblNombre.setText(Idioma.get("producto.editar.lbl.nombre"));
        btnListar1.setText(Idioma.get("producto.editar.btn.listar"));
        btnEditar.setText(Idioma.get("producto.editar.btn.editar"));

        modelo.setColumnIdentifiers(new Object[]{
                Idioma.get("producto.editar.tbl.codigo"),
                Idioma.get("producto.editar.tbl.nombre"),
                Idioma.get("producto.editar.tbl.precio")
        });

        tblEditarProducto.setToolTipText(Idioma.get("producto.editar.tbl.tooltip"));
    }

    public JPanel getPanelEditarProducto() {
        return panelEditarProducto;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JButton getBtnListar1() {
        return btnListar1;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JTable getTblEditarProducto() {
        return tblEditarProducto;
    }

    public void cargarDatos(List<Producto> productos) {
        modelo.setNumRows(0);
        for (Producto producto : productos) {
            Object[] filaProducto = {producto.getCodigo(), producto.getNombre(), producto.getPrecio()};
            modelo.addRow(filaProducto);
        }
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        tblEditarProducto.clearSelection();
    }

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("producto.editar.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }
}