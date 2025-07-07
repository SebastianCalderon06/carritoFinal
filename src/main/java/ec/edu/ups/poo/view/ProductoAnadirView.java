package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoAnadirView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JButton btnGuardar;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JLabel lblPrecio;
    private JLabel lblNombre;
    private JLabel lblCodigo;
    private JLabel lblAgregar;
    private JButton btnEliminar;

    public ProductoAnadirView() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/icons/save-floppy-svgrepo-com.png")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/icons/erase-svgrepo-com.png")));
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("producto.anadir.titulo"));
        lblAgregar.setText(Idioma.get("producto.anadir.lbl.agregar"));
        lblCodigo.setText(Idioma.get("producto.anadir.lbl.codigo"));
        lblNombre.setText(Idioma.get("producto.anadir.lbl.nombre"));
        lblPrecio.setText(Idioma.get("producto.anadir.lbl.precio"));
        btnGuardar.setText(Idioma.get("producto.anadir.btn.guardar"));
        btnEliminar.setText(Idioma.get("producto.anadir.btn.limpiar"));
        btnGuardar.setToolTipText(Idioma.get("producto.anadir.tooltip.guardar"));
        btnEliminar.setToolTipText(Idioma.get("producto.anadir.tooltip.limpiar"));
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
    }

    public void mostrarProductos(List<Producto> productos) {
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(JButton btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public void setTxtCodigo(JTextField txtCodigo) {
        this.txtCodigo = txtCodigo;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public void setTxtPrecio(JTextField txtPrecio) {
        this.txtPrecio = txtPrecio;
    }

    public JLabel getLblPrecio() {
        return lblPrecio;
    }

    public void setLblPrecio(JLabel lblPrecio) {
        this.lblPrecio = lblPrecio;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }

    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    public void setLblCodigo(JLabel lblCodigo) {
        this.lblCodigo = lblCodigo;
    }

    public JLabel getLblAgregar() {
        return lblAgregar;
    }

    public void setLblAgregar(JLabel lblAgregar) {
        this.lblAgregar = lblAgregar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}