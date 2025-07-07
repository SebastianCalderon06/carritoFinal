package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoEliminar extends JInternalFrame {
    private JPanel panelEliminarProducto;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton LISTARButton;
    private JButton ELIMINARButton;
    private JTable tblEliminarProducto;
    private DefaultTableModel modelo;

    public ProductoEliminar() {
        setContentPane(panelEliminarProducto);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setSize(600, 500);

        // Inicializar el modelo de la tabla
        modelo = new DefaultTableModel();
        Object[] columnas = {"", "", ""}; // Internacionalizadas abajo
        modelo.setColumnIdentifiers(columnas);
        tblEliminarProducto.setModel(modelo);

        // Configurar selección de fila para mostrar información
        tblEliminarProducto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tblEliminarProducto.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String nombre = (String) tblEliminarProducto.getValueAt(filaSeleccionada, 1);
                    txtNombre.setText(nombre);
                }
            }
        });
        LISTARButton.setIcon(new ImageIcon(getClass().getResource("/icons/list-svgrepo-com.png")));
        ELIMINARButton.setIcon(new ImageIcon(getClass().getResource("/icons/erase-svgrepo-com.png")));
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("producto.eliminar.titulo"));
        lblNombre.setText(Idioma.get("producto.eliminar.lbl.nombre"));
        LISTARButton.setText(Idioma.get("producto.eliminar.btn.listar"));
        ELIMINARButton.setText(Idioma.get("producto.eliminar.btn.eliminar"));

        modelo.setColumnIdentifiers(new Object[]{
                Idioma.get("producto.eliminar.tbl.codigo"),
                Idioma.get("producto.eliminar.tbl.nombre"),
                Idioma.get("producto.eliminar.tbl.precio")
        });

        tblEliminarProducto.setToolTipText(Idioma.get("producto.eliminar.tbl.tooltip"));
    }

    public JPanel getPanelEliminarProducto() {
        return panelEliminarProducto;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JButton getBtnListar() {
        return LISTARButton;
    }

    public JButton getBtnEliminar() {
        return ELIMINARButton;
    }

    public JTable getTblEliminarProducto() {
        return tblEliminarProducto;
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
        tblEliminarProducto.clearSelection();
    }

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("producto.eliminar.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }
}