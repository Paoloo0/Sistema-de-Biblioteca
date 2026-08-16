package vista;

import dao.*;
import modelo.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FrmPrincipal extends javax.swing.JFrame {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final LibroDAO libroDAO = new LibroDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloEmpleados;
    private DefaultTableModel modeloLibros;
    private DefaultTableModel modeloDetallePrestamo; 
    private DefaultTableModel modeloHistorialPrestamos;
    private DefaultTableModel modeloVerDetallePrestamo; 

    private final List<Libro> listaLibrosPrestamo = new ArrayList<>();
    private int idEmpleadoLogueado = 1;
    private String nombreEmpleadoLogueado = "";
    private String rolUsuario = "Administrador";

    public FrmPrincipal() {
        this("Administrador", "Ana Torres Vega", 1);
    }

    public FrmPrincipal(String rol, String nombreEmp, int idEmp) {
        this.rolUsuario = rol;
        this.nombreEmpleadoLogueado = nombreEmp;
        this.idEmpleadoLogueado = idEmp;
        initComponents();
        this.setLocationRelativeTo(null); 
        configurarTablas();
        cargarDatos();
        aplicarRol();
    }

    private void configurarTablas() {
        modeloUsuarios = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Teléfono", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblUsuarios.setModel(modeloUsuarios);

        modeloEmpleados = new DefaultTableModel(new Object[]{"ID", "Nombre", "Puesto", "Usuario", "Contraseña"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblEmpleados.setModel(modeloEmpleados);

        modeloLibros = new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Editorial", "ISBN", "Año", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblLibros.setModel(modeloLibros);

        modeloDetallePrestamo = new DefaultTableModel(new Object[]{"ID Libro", "Título", "Autor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblDetallePrestamo.setModel(modeloDetallePrestamo);

        modeloHistorialPrestamos = new DefaultTableModel(new Object[]{"ID Préstamo", "Usuario", "Empleado", "F. Salida", "F. Dev. Máxima", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblHistorialPrestamos.setModel(modeloHistorialPrestamos);

        modeloVerDetallePrestamo = new DefaultTableModel(new Object[]{"ID Libro", "Título", "Autor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblVerDetallePrestamo.setModel(modeloVerDetallePrestamo);
    }

    private void cargarDatos() {
        listarUsuarios();
        listarEmpleados();
        listarLibros();
        listarPrestamos();

        cargarComboUsuarios();
        if (rolUsuario.equals("Administrador")) {
            cargarComboEmpleados();
        } else {
            cargarEmpleadoActivo();
        }
        cargarComboLibros();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date hoy = new java.util.Date();
        txtFechaSalida.setText(sdf.format(hoy));

        Calendar cal = Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(Calendar.DAY_OF_YEAR, 7); 
        txtFechaDevolucion.setText(sdf.format(cal.getTime()));
    }

    private void listarUsuarios() {
        modeloUsuarios.setRowCount(0);
        List<Usuario> lista = usuarioDAO.listar();
        for (Usuario u : lista) {
            modeloUsuarios.addRow(new Object[]{u.getIdUsuario(), u.getNombre(), u.getDireccion(), u.getTelefono(), u.getCorreo()});
        }
    }

    private void listarEmpleados() {
        modeloEmpleados.setRowCount(0);
        List<Empleado> lista = empleadoDAO.listar();
        for (Empleado e : lista) {
            modeloEmpleados.addRow(new Object[]{e.getIdEmpleado(), e.getNombre(), e.getPuesto(), e.getUsuario(), e.getContrasena()});
        }
    }

    private void listarLibros() {
        modeloLibros.setRowCount(0);
        List<Libro> lista = libroDAO.listar();
        for (Libro l : lista) {
            modeloLibros.addRow(new Object[]{l.getIdLibro(), l.getTitulo(), l.getAutor(), l.getEditorial(), l.getIsbn(), l.getAnio(), l.getStock()});
        }
    }

    private void listarPrestamos() {
        modeloHistorialPrestamos.setRowCount(0);
        List<Prestamo> lista = prestamoDAO.listar();
        for (Prestamo p : lista) {
            modeloHistorialPrestamos.addRow(new Object[]{
                p.getIdPrestamo(),
                p.getNombreUsuario(),
                p.getNombreEmpleado(),
                p.getFechaSalida(),
                p.getFechaDevolucionMaxima(),
                p.getEstado()
            });
        }
    }

    private void cargarComboUsuarios() {
        cboUsuario.removeAllItems();
        List<Usuario> lista = usuarioDAO.listar();
        for (Usuario u : lista) {
            cboUsuario.addItem(u);
        }
    }

    private void cargarEmpleadoActivo() {
        txtEmpleadoActivo.setText(nombreEmpleadoLogueado);
    }

    private void cargarComboEmpleados() {
        cboEmpleado.removeAllItems();
        List<Empleado> lista = empleadoDAO.listar();
        for (Empleado e : lista) {
            cboEmpleado.addItem(e);
        }
    }

    private void aplicarRol() {
        if (rolUsuario.equals("Empleado")) {
            tabbedPanePrincipal.remove(pnlEmpleados);
            cboEmpleado.setVisible(false);
            txtEmpleadoActivo.setVisible(true);
        } else {
            cboEmpleado.setVisible(true);
            txtEmpleadoActivo.setVisible(false);
            cargarComboEmpleados();
        }
    }

    private void cargarComboLibros() {
        cboLibro.removeAllItems();
        List<Libro> lista = libroDAO.listar();
        for (Libro l : lista) {
            cboLibro.addItem(l);
        }
    }

    private void btnGuardarUsuarioActionPerformed(ActionEvent evt) {
        String nombre = txtNombreUsuario.getText().trim();
        String direccion = txtDireccionUsuario.getText().trim();
        String telefono = txtTelefonoUsuario.getText().trim();
        String correo = txtCorreoUsuario.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioDAO.existeNombre(nombre)) {
            JOptionPane.showMessageDialog(this, "El usuario ya existe.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario(nombre, direccion, telefono, correo);
        if (usuarioDAO.insertar(u)) {
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            limpiarCamposUsuario();
            listarUsuarios();
            cargarComboUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEditarUsuarioActionPerformed(ActionEvent evt) {
        String idStr = txtIdUsuario.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para editar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idStr);
        String nombre = txtNombreUsuario.getText().trim();
        String direccion = txtDireccionUsuario.getText().trim();
        String telefono = txtTelefonoUsuario.getText().trim();
        String correo = txtCorreoUsuario.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario(id, nombre, direccion, telefono, correo);
        if (usuarioDAO.modificar(u)) {
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
            limpiarCamposUsuario();
            listarUsuarios();
            cargarComboUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEliminarUsuarioActionPerformed(ActionEvent evt) {
        String idStr = txtIdUsuario.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(idStr);
            if (usuarioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                limpiarCamposUsuario();
                listarUsuarios();
                cargarComboUsuarios();
                listarPrestamos(); 
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void tblUsuariosMouseClicked(MouseEvent evt) {
        int fila = tblUsuarios.getSelectedRow();
        if (fila >= 0) {
            txtIdUsuario.setText(tblUsuarios.getValueAt(fila, 0).toString());
            txtNombreUsuario.setText(tblUsuarios.getValueAt(fila, 1).toString());
            txtDireccionUsuario.setText(tblUsuarios.getValueAt(fila, 2) != null ? tblUsuarios.getValueAt(fila, 2).toString() : "");
            txtTelefonoUsuario.setText(tblUsuarios.getValueAt(fila, 3) != null ? tblUsuarios.getValueAt(fila, 3).toString() : "");
            txtCorreoUsuario.setText(tblUsuarios.getValueAt(fila, 4) != null ? tblUsuarios.getValueAt(fila, 4).toString() : "");
        }
    }

    private void limpiarCamposUsuario() {
        txtIdUsuario.setText("");
        txtNombreUsuario.setText("");
        txtDireccionUsuario.setText("");
        txtTelefonoUsuario.setText("");
        txtCorreoUsuario.setText("");
        tblUsuarios.clearSelection();
    }

    private void btnGuardarEmpleadoActionPerformed(ActionEvent evt) {
        String nombre = txtNombreEmpleado.getText().trim();
        String puesto = txtPuestoEmpleado.getText().trim();
        String usuario = txtUsuarioEmpleado.getText().trim();
        String contrasena = txtContrasenaEmpleado.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (empleadoDAO.existeNombre(nombre)) {
            JOptionPane.showMessageDialog(this, "El empleado ya existe.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado emp = new Empleado(nombre, puesto, usuario, contrasena);
        if (empleadoDAO.insertar(emp)) {
            JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.");
            limpiarCamposEmpleado();
            listarEmpleados();
            if (rolUsuario.equals("Administrador")) {
                cargarComboEmpleados();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar empleado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEditarEmpleadoActionPerformed(ActionEvent evt) {
        String idStr = txtIdEmpleado.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla para editar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idStr);
        String nombre = txtNombreEmpleado.getText().trim();
        String puesto = txtPuestoEmpleado.getText().trim();
        String usuario = txtUsuarioEmpleado.getText().trim();
        String contrasena = txtContrasenaEmpleado.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado emp = new Empleado(id, nombre, puesto, usuario, contrasena);
        if (empleadoDAO.modificar(emp)) {
            JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente.");
            limpiarCamposEmpleado();
            listarEmpleados();
            if (rolUsuario.equals("Administrador")) {
                cargarComboEmpleados();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar empleado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEliminarEmpleadoActionPerformed(ActionEvent evt) {
        String idStr = txtIdEmpleado.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este empleado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(idStr);
            if (empleadoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente.");
                limpiarCamposEmpleado();
                listarEmpleados();
                if (rolUsuario.equals("Administrador")) {
                    cargarComboEmpleados();
                }
                listarPrestamos(); 
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void tblEmpleadosMouseClicked(MouseEvent evt) {
        int fila = tblEmpleados.getSelectedRow();
        if (fila >= 0) {
            txtIdEmpleado.setText(tblEmpleados.getValueAt(fila, 0).toString());
            txtNombreEmpleado.setText(tblEmpleados.getValueAt(fila, 1).toString());
            txtPuestoEmpleado.setText(tblEmpleados.getValueAt(fila, 2) != null ? tblEmpleados.getValueAt(fila, 2).toString() : "");
            txtUsuarioEmpleado.setText(tblEmpleados.getValueAt(fila, 3) != null ? tblEmpleados.getValueAt(fila, 3).toString() : "");
            txtContrasenaEmpleado.setText(tblEmpleados.getValueAt(fila, 4) != null ? tblEmpleados.getValueAt(fila, 4).toString() : "");
        }
    }

    private void limpiarCamposEmpleado() {
        txtIdEmpleado.setText("");
        txtNombreEmpleado.setText("");
        txtPuestoEmpleado.setText("");
        txtUsuarioEmpleado.setText("");
        txtContrasenaEmpleado.setText("");
        tblEmpleados.clearSelection();
    }

    private void btnGuardarLibroActionPerformed(ActionEvent evt) {
        String titulo = txtTituloLibro.getText().trim();
        String autor = txtAutorLibro.getText().trim();
        String editorial = txtEditorialLibro.getText().trim();
        String isbn = txtIsbnLibro.getText().trim();
        String anioStr = txtAnioLibro.getText().trim();
        String stockStr = txtStockLibro.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título y autor son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isbn.matches("^\\d{3}-\\d{10}$")) {
            JOptionPane.showMessageDialog(this, "El ISBN debe tener el formato de 13 dígitos y un guion (Ej: 978-3245678345).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (libroDAO.existeIsbn(isbn)) {
            JOptionPane.showMessageDialog(this, "El libro con este ISBN ya está registrado.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!anioStr.matches("^\\d{4}$")) {
            JOptionPane.showMessageDialog(this, "El año debe tener exactamente 4 números.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!stockStr.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int anio = Integer.parseInt(anioStr);
        int stock = Integer.parseInt(stockStr);

        Libro lib = new Libro(titulo, autor, editorial, isbn, anio, stock);
        if (libroDAO.insertar(lib)) {
            JOptionPane.showMessageDialog(this, "Libro registrado correctamente.");
            limpiarCamposLibro();
            listarLibros();
            cargarComboLibros();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar libro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEditarLibroActionPerformed(ActionEvent evt) {
        String idStr = txtIdLibro.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para editar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idStr);
        String titulo = txtTituloLibro.getText().trim();
        String autor = txtAutorLibro.getText().trim();
        String editorial = txtEditorialLibro.getText().trim();
        String isbn = txtIsbnLibro.getText().trim();
        String anioStr = txtAnioLibro.getText().trim();
        String stockStr = txtStockLibro.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título y autor son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isbn.matches("^\\d{3}-\\d{10}$")) {
            JOptionPane.showMessageDialog(this, "El ISBN debe tener el formato de 13 dígitos y un guion (Ej: 978-3245678345).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!anioStr.matches("^\\d{4}$")) {
            JOptionPane.showMessageDialog(this, "El año debe tener exactamente 4 números.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!stockStr.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int anio = Integer.parseInt(anioStr);
        int stock = Integer.parseInt(stockStr);

        Libro lib = new Libro(id, titulo, autor, editorial, isbn, anio, stock);
        if (libroDAO.modificar(lib)) {
            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.");
            limpiarCamposLibro();
            listarLibros();
            cargarComboLibros();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar libro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEliminarLibroActionPerformed(ActionEvent evt) {
        String idStr = txtIdLibro.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este libro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(idStr);
            if (libroDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.");
                limpiarCamposLibro();
                listarLibros();
                cargarComboLibros();
                listarPrestamos(); 
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar libro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void tblLibrosMouseClicked(MouseEvent evt) {
        int fila = tblLibros.getSelectedRow();
        if (fila >= 0) {
            txtIdLibro.setText(tblLibros.getValueAt(fila, 0).toString());
            txtTituloLibro.setText(tblLibros.getValueAt(fila, 1).toString());
            txtAutorLibro.setText(tblLibros.getValueAt(fila, 2).toString());
            txtEditorialLibro.setText(tblLibros.getValueAt(fila, 3) != null ? tblLibros.getValueAt(fila, 3).toString() : "");
            txtIsbnLibro.setText(tblLibros.getValueAt(fila, 4) != null ? tblLibros.getValueAt(fila, 4).toString() : "");
            txtAnioLibro.setText(tblLibros.getValueAt(fila, 5) != null ? tblLibros.getValueAt(fila, 5).toString() : "");
            txtStockLibro.setText(tblLibros.getValueAt(fila, 6) != null ? tblLibros.getValueAt(fila, 6).toString() : "0");
        }
    }

    private void limpiarCamposLibro() {
        txtIdLibro.setText("");
        txtTituloLibro.setText("");
        txtAutorLibro.setText("");
        txtEditorialLibro.setText("");
        txtIsbnLibro.setText("");
        txtAnioLibro.setText("");
        txtStockLibro.setText("");
        tblLibros.clearSelection();
    }

    private void btnAgregarLibroActionPerformed(ActionEvent evt) {
        Libro libroSeleccionado = (Libro) cboLibro.getSelectedItem();
        if (libroSeleccionado == null) {
            return;
        }

        if (libroSeleccionado.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "No hay stock disponible de este libro.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Libro l : listaLibrosPrestamo) {
            if (l.getIdLibro() == libroSeleccionado.getIdLibro()) {
                JOptionPane.showMessageDialog(this, "Este libro ya ha sido agregado al préstamo actual.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        listaLibrosPrestamo.add(libroSeleccionado);
        modeloDetallePrestamo.addRow(new Object[]{
            libroSeleccionado.getIdLibro(),
            libroSeleccionado.getTitulo(),
            libroSeleccionado.getAutor()
        });
    }

    private void btnQuitarLibroActionPerformed(ActionEvent evt) {
        int fila = tblDetallePrestamo.getSelectedRow();
        if (fila >= 0) {
            listaLibrosPrestamo.remove(fila);
            modeloDetallePrestamo.removeRow(fila);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la lista temporal para removerlo.", "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnGuardarPrestamoActionPerformed(ActionEvent evt) {
        Usuario usuario = (Usuario) cboUsuario.getSelectedItem();
        String fechaSalidaStr = txtFechaSalida.getText().trim();
        String fechaDevolucionStr = txtFechaDevolucion.getText().trim();

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaLibrosPrestamo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un libro al préstamo.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date fechaSalida = null;
        Date fechaDevolucion = null;
        try {
            fechaSalida = Date.valueOf(fechaSalidaStr);
            fechaDevolucion = Date.valueOf(fechaDevolucionStr);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Las fechas deben tener el formato AAAA-MM-DD (Ej: 2026-07-01).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Prestamo p = new Prestamo();
        p.setIdUsuario(usuario.getIdUsuario());
        
        int idEmp = idEmpleadoLogueado;
        if (rolUsuario.equals("Administrador")) {
            Empleado emp = (Empleado) cboEmpleado.getSelectedItem();
            if (emp != null) {
                idEmp = emp.getIdEmpleado();
            }
        }
        p.setIdEmpleado(idEmp);
        p.setFechaSalida(fechaSalida);
        p.setFechaDevolucionMaxima(fechaDevolucion);
        p.setEstado("Prestado");

        for (Libro l : listaLibrosPrestamo) {
            PrestamoDetalle det = new PrestamoDetalle();
            det.setIdLibro(l.getIdLibro());
            p.agregarDetalle(det);
        }

        if (prestamoDAO.registrarPrestamo(p)) {
            JOptionPane.showMessageDialog(this, "Préstamo registrado exitosamente en la base de datos.");
            listaLibrosPrestamo.clear();
            modeloDetallePrestamo.setRowCount(0);
            listarPrestamos(); 
            listarLibros();
            cargarComboLibros();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el préstamo. Se realizó Rollback.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tblHistorialPrestamosMouseClicked(MouseEvent evt) {
        int fila = tblHistorialPrestamos.getSelectedRow();
        if (fila >= 0) {
            int idPrestamo = Integer.parseInt(tblHistorialPrestamos.getValueAt(fila, 0).toString());
            modeloVerDetallePrestamo.setRowCount(0);
            List<PrestamoDetalle> detalles = prestamoDAO.listarDetalles(idPrestamo);
            for (PrestamoDetalle d : detalles) {
                modeloVerDetallePrestamo.addRow(new Object[]{
                    d.getIdLibro(),
                    d.getTituloLibro(),
                    d.getAutorLibro()
                });
            }
        }
    }

    private void btnDevolverPrestamoActionPerformed(ActionEvent evt) {
        int fila = tblHistorialPrestamos.getSelectedRow();
        if (fila >= 0) {
            int idPrestamo = Integer.parseInt(tblHistorialPrestamos.getValueAt(fila, 0).toString());
            String estadoActual = tblHistorialPrestamos.getValueAt(fila, 5).toString();

            if (estadoActual.equals("Devuelto")) {
                JOptionPane.showMessageDialog(this, "Este préstamo ya figura como devuelto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Confirmar devolución de los libros de este préstamo?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (prestamoDAO.actualizarEstado(idPrestamo, "Devuelto")) {
                    JOptionPane.showMessageDialog(this, "Estado actualizado a 'Devuelto'.");
                    listarPrestamos();
                    listarLibros();
                    cargarComboLibros();
                    modeloVerDetallePrestamo.setRowCount(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla superior para registrar su devolución.", "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void txtBuscarLibroCatalogoKeyReleased(java.awt.event.KeyEvent evt) {
        String criterio = txtBuscarLibroCatalogo.getText().trim();
        modeloLibros.setRowCount(0);
        List<Libro> lista = libroDAO.buscar(criterio);
        for (Libro l : lista) {
            modeloLibros.addRow(new Object[]{l.getIdLibro(), l.getTitulo(), l.getAutor(), l.getEditorial(), l.getIsbn(), l.getAnio(), l.getStock()});
        }
    }

    private void txtBuscarLibroPrestamoKeyReleased(java.awt.event.KeyEvent evt) {
        String criterio = txtBuscarLibroPrestamo.getText().trim();
        cboLibro.removeAllItems();
        List<Libro> lista = libroDAO.buscar(criterio);
        for (Libro l : lista) {
            cboLibro.addItem(l);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        tabbedPanePrincipal = new javax.swing.JTabbedPane();
        pnlUsuarios = new javax.swing.JPanel();
        pnlEmpleados = new javax.swing.JPanel();
        pnlLibros = new javax.swing.JPanel();
        pnlNuevoPrestamo = new javax.swing.JPanel();
        pnlHistorial = new javax.swing.JPanel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SISTEMA DE PRESTAMOS DE BIBLIOTECA - CONTROL");
        setPreferredSize(new java.awt.Dimension(1024, 700));

        pnlUsuarios.setLayout(new java.awt.BorderLayout(10, 10));
        JPanel pnlFormUsuario = new JPanel(new java.awt.GridLayout(6, 2, 5, 5));
        pnlFormUsuario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos del Usuario", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));
        
        txtIdUsuario = new JTextField(); txtIdUsuario.setEditable(false);
        txtNombreUsuario = new JTextField();
        txtDireccionUsuario = new JTextField();
        txtTelefonoUsuario = new JTextField();
        txtCorreoUsuario = new JTextField();

        pnlFormUsuario.add(new JLabel(" ID Usuario:")); pnlFormUsuario.add(txtIdUsuario);
        pnlFormUsuario.add(new JLabel(" Nombre Completo:")); pnlFormUsuario.add(txtNombreUsuario);
        pnlFormUsuario.add(new JLabel(" Dirección:")); pnlFormUsuario.add(txtDireccionUsuario);
        pnlFormUsuario.add(new JLabel(" Teléfono:")); pnlFormUsuario.add(txtTelefonoUsuario);
        pnlFormUsuario.add(new JLabel(" Correo Electrónico:")); pnlFormUsuario.add(txtCorreoUsuario);

        JPanel pnlBotonesUsuario = new JPanel(new java.awt.FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGuardarUsuario = new JButton("Guardar");
        btnEditarUsuario = new JButton("Modificar");
        btnEliminarUsuario = new JButton("Eliminar");
        JButton btnLimpiarUsuario = new JButton("Limpiar");
        
        pnlBotonesUsuario.add(btnGuardarUsuario);
        pnlBotonesUsuario.add(btnEditarUsuario);
        pnlBotonesUsuario.add(btnEliminarUsuario);
        pnlBotonesUsuario.add(btnLimpiarUsuario);
        pnlFormUsuario.add(new JLabel()); 
        pnlFormUsuario.add(pnlBotonesUsuario);

        tblUsuarios = new JTable();
        JScrollPane spUsuarios = new JScrollPane(tblUsuarios);
        
        pnlUsuarios.add(pnlFormUsuario, BorderLayout.NORTH);
        pnlUsuarios.add(spUsuarios, BorderLayout.CENTER);

        pnlEmpleados.setLayout(new java.awt.BorderLayout(10, 10));
        JPanel pnlFormEmpleado = new JPanel(new java.awt.GridLayout(6, 2, 5, 5));
        pnlFormEmpleado.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos del Empleado", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));
        
        txtIdEmpleado = new JTextField(); txtIdEmpleado.setEditable(false);
        txtNombreEmpleado = new JTextField();
        txtPuestoEmpleado = new JTextField();
        txtUsuarioEmpleado = new JTextField();
        txtContrasenaEmpleado = new JTextField();

        pnlFormEmpleado.add(new JLabel(" ID Empleado:")); pnlFormEmpleado.add(txtIdEmpleado);
        pnlFormEmpleado.add(new JLabel(" Nombre Completo:")); pnlFormEmpleado.add(txtNombreEmpleado);
        pnlFormEmpleado.add(new JLabel(" Puesto/Cargo:")); pnlFormEmpleado.add(txtPuestoEmpleado);
        pnlFormEmpleado.add(new JLabel(" Usuario Acceso:")); pnlFormEmpleado.add(txtUsuarioEmpleado);
        pnlFormEmpleado.add(new JLabel(" Contraseña Acceso:")); pnlFormEmpleado.add(txtContrasenaEmpleado);

        JPanel pnlBotonesEmpleado = new JPanel(new java.awt.FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGuardarEmpleado = new JButton("Guardar");
        btnEditarEmpleado = new JButton("Modificar");
        btnEliminarEmpleado = new JButton("Eliminar");
        JButton btnLimpiarEmpleado = new JButton("Limpiar");

        pnlBotonesEmpleado.add(btnGuardarEmpleado);
        pnlBotonesEmpleado.add(btnEditarEmpleado);
        pnlBotonesEmpleado.add(btnEliminarEmpleado);
        pnlBotonesEmpleado.add(btnLimpiarEmpleado);
        pnlFormEmpleado.add(new JLabel());
        pnlFormEmpleado.add(pnlBotonesEmpleado);

        tblEmpleados = new JTable();
        JScrollPane spEmpleados = new JScrollPane(tblEmpleados);

        pnlEmpleados.add(pnlFormEmpleado, BorderLayout.NORTH);
        pnlEmpleados.add(spEmpleados, BorderLayout.CENTER);

        pnlLibros.setLayout(new java.awt.BorderLayout(10, 10));
        JPanel pnlFormLibro = new JPanel(new java.awt.GridLayout(8, 2, 5, 5));
        pnlFormLibro.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos del Libro", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));

        txtIdLibro = new JTextField(); txtIdLibro.setEditable(false);
        txtTituloLibro = new JTextField();
        txtAutorLibro = new JTextField();
        txtEditorialLibro = new JTextField();
        txtIsbnLibro = new JTextField();
        txtAnioLibro = new JTextField();
        txtStockLibro = new JTextField();

        pnlFormLibro.add(new JLabel(" ID Libro:")); pnlFormLibro.add(txtIdLibro);
        pnlFormLibro.add(new JLabel(" Título del Libro:")); pnlFormLibro.add(txtTituloLibro);
        pnlFormLibro.add(new JLabel(" Autor:")); pnlFormLibro.add(txtAutorLibro);
        pnlFormLibro.add(new JLabel(" Editorial:")); pnlFormLibro.add(txtEditorialLibro);
        pnlFormLibro.add(new JLabel(" ISBN:")); pnlFormLibro.add(txtIsbnLibro);
        pnlFormLibro.add(new JLabel(" Año de Publicación:")); pnlFormLibro.add(txtAnioLibro);
        pnlFormLibro.add(new JLabel(" Stock disponible:")); pnlFormLibro.add(txtStockLibro);

        JPanel pnlBotonesLibro = new JPanel(new java.awt.FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGuardarLibro = new JButton("Guardar");
        btnEditarLibro = new JButton("Modificar");
        btnEliminarLibro = new JButton("Eliminar");
        JButton btnLimpiarLibro = new JButton("Limpiar");

        pnlBotonesLibro.add(btnGuardarLibro);
        pnlBotonesLibro.add(btnEditarLibro);
        pnlBotonesLibro.add(btnEliminarLibro);
        pnlBotonesLibro.add(btnLimpiarLibro);
        pnlFormLibro.add(new JLabel());
        pnlFormLibro.add(pnlBotonesLibro);

        tblLibros = new JTable();
        JScrollPane spLibros = new JScrollPane(tblLibros);
        
        JPanel pnlFiltroLibro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtBuscarLibroCatalogo = new JTextField(20);
        pnlFiltroLibro.add(new JLabel("Buscar Libro:"));
        pnlFiltroLibro.add(txtBuscarLibroCatalogo);

        JPanel pnlCentroLibros = new JPanel(new BorderLayout());
        pnlCentroLibros.add(pnlFiltroLibro, BorderLayout.NORTH);
        pnlCentroLibros.add(spLibros, BorderLayout.CENTER);

        pnlLibros.add(pnlFormLibro, BorderLayout.NORTH);
        pnlLibros.add(pnlCentroLibros, BorderLayout.CENTER);

        pnlNuevoPrestamo.setLayout(new java.awt.BorderLayout(10, 10));
        JPanel pnlFormPrestamo = new JPanel(new java.awt.GridBagLayout());
        pnlFormPrestamo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Registrar Préstamo de Libros", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboUsuario = new JComboBox<>();
        cboEmpleado = new JComboBox<>();
        txtEmpleadoActivo = new JTextField(12);
        txtEmpleadoActivo.setEditable(false);
        txtFechaSalida = new JTextField(12);
        txtFechaDevolucion = new JTextField(12);
        txtBuscarLibroPrestamo = new JTextField(12);
        cboLibro = new JComboBox<>();
        btnAgregarLibro = new JButton("Agregar Libro");
        btnQuitarLibro = new JButton("Quitar Libro");
        btnGuardarPrestamo = new JButton("REGISTRAR PRESTAMO (TRANSACCION)");
        btnGuardarPrestamo.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGuardarPrestamo.setBackground(new Color(33, 115, 70));
        btnGuardarPrestamo.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; pnlFormPrestamo.add(new JLabel("Seleccionar Usuario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; pnlFormPrestamo.add(cboUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; pnlFormPrestamo.add(new JLabel("Empleado que Gestiona:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; 
        pnlFormPrestamo.add(cboEmpleado, gbc);
        pnlFormPrestamo.add(txtEmpleadoActivo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; pnlFormPrestamo.add(new JLabel("Fecha Salida (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; pnlFormPrestamo.add(txtFechaSalida, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; pnlFormPrestamo.add(new JLabel("Fecha Dev. Máxima (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; pnlFormPrestamo.add(txtFechaDevolucion, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; pnlFormPrestamo.add(new JLabel("Escribe para buscar libro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0; pnlFormPrestamo.add(txtBuscarLibroPrestamo, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0; pnlFormPrestamo.add(new JLabel("Seleccionar Libro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0; pnlFormPrestamo.add(cboLibro, gbc);

        JPanel pnlAccionesLibro = new JPanel(new java.awt.FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlAccionesLibro.add(btnAgregarLibro);
        pnlAccionesLibro.add(btnQuitarLibro);
        gbc.gridx = 1; gbc.gridy = 6; gbc.weightx = 0; pnlFormPrestamo.add(pnlAccionesLibro, gbc);

        tblDetallePrestamo = new JTable();
        JScrollPane spDetallePrestamo = new JScrollPane(tblDetallePrestamo);
        spDetallePrestamo.setBorder(BorderFactory.createTitledBorder("Lista de Libros Agregados temporalmente"));

        JPanel pnlAccionGuardar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlAccionGuardar.add(btnGuardarPrestamo);

        pnlNuevoPrestamo.add(pnlFormPrestamo, BorderLayout.NORTH);
        pnlNuevoPrestamo.add(spDetallePrestamo, BorderLayout.CENTER);
        pnlNuevoPrestamo.add(pnlAccionGuardar, BorderLayout.SOUTH);

        pnlHistorial.setLayout(new java.awt.BorderLayout(10, 10));
        
        tblHistorialPrestamos = new JTable();
        JScrollPane spHistorial = new JScrollPane(tblHistorialPrestamos);
        spHistorial.setBorder(BorderFactory.createTitledBorder("Registro General de Préstamos"));

        tblVerDetallePrestamo = new JTable();
        JScrollPane spVerDetalles = new JScrollPane(tblVerDetallePrestamo);
        spVerDetalles.setPreferredSize(new Dimension(300, 200));
        spVerDetalles.setBorder(BorderFactory.createTitledBorder("Detalle de Libros en el Préstamo Seleccionado"));

        JPanel pnlControlHistorial = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnDevolverPrestamo = new JButton("Registrar Devolución (Marcar como Devuelto)");
        btnDevolverPrestamo.setFont(new Font("Tahoma", Font.BOLD, 11));
        pnlControlHistorial.add(btnDevolverPrestamo);

        JSplitPane splitHistorial = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spHistorial, spVerDetalles);
        splitHistorial.setDividerLocation(250);

        pnlHistorial.add(splitHistorial, BorderLayout.CENTER);
        pnlHistorial.add(pnlControlHistorial, BorderLayout.SOUTH);

        tabbedPanePrincipal.addTab("Usuarios", pnlUsuarios);
        tabbedPanePrincipal.addTab("Empleados", pnlEmpleados);
        tabbedPanePrincipal.addTab("Libros", pnlLibros);
        tabbedPanePrincipal.addTab("Nuevo Préstamo", pnlNuevoPrestamo);
        tabbedPanePrincipal.addTab("Historial Préstamos", pnlHistorial);

        getContentPane().add(tabbedPanePrincipal, BorderLayout.CENTER);

        btnGuardarUsuario.addActionListener(this::btnGuardarUsuarioActionPerformed);
        btnEditarUsuario.addActionListener(this::btnEditarUsuarioActionPerformed);
        btnEliminarUsuario.addActionListener(this::btnEliminarUsuarioActionPerformed);
        btnLimpiarUsuario.addActionListener(e -> limpiarCamposUsuario());
        tblUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });

        btnGuardarEmpleado.addActionListener(this::btnGuardarEmpleadoActionPerformed);
        btnEditarEmpleado.addActionListener(this::btnEditarEmpleadoActionPerformed);
        btnEliminarEmpleado.addActionListener(this::btnEliminarEmpleadoActionPerformed);
        btnLimpiarEmpleado.addActionListener(e -> limpiarCamposEmpleado());
        tblEmpleados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblEmpleadosMouseClicked(evt);
            }
        });

        btnGuardarLibro.addActionListener(this::btnGuardarLibroActionPerformed);
        btnEditarLibro.addActionListener(this::btnEditarLibroActionPerformed);
        btnEliminarLibro.addActionListener(this::btnEliminarLibroActionPerformed);
        btnLimpiarLibro.addActionListener(e -> limpiarCamposLibro());
        tblLibros.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblLibrosMouseClicked(evt);
            }
        });

        btnAgregarLibro.addActionListener(this::btnAgregarLibroActionPerformed);
        btnQuitarLibro.addActionListener(this::btnQuitarLibroActionPerformed);
        btnGuardarPrestamo.addActionListener(this::btnGuardarPrestamoActionPerformed);

        tblHistorialPrestamos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblHistorialPrestamosMouseClicked(evt);
            }
        });
        btnDevolverPrestamo.addActionListener(this::btnDevolverPrestamoActionPerformed);

        txtBuscarLibroCatalogo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarLibroCatalogoKeyReleased(evt);
            }
        });

        txtBuscarLibroPrestamo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarLibroPrestamoKeyReleased(evt);
            }
        });

        txtIsbnLibro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '-') {
                    evt.consume();
                }
                if (txtIsbnLibro.getText().length() >= 14) {
                    evt.consume();
                }
            }
        });

        txtAnioLibro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
                if (txtAnioLibro.getText().length() >= 4) {
                    evt.consume();
                }
            }
        });

        txtStockLibro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
                if (txtStockLibro.getText().length() >= 5) {
                    evt.consume();
                }
            }
        });

        pack();
    }

    private javax.swing.JTabbedPane tabbedPanePrincipal;
    private javax.swing.JPanel pnlUsuarios;
    private javax.swing.JPanel pnlEmpleados;
    private javax.swing.JPanel pnlLibros;
    private javax.swing.JPanel pnlNuevoPrestamo;
    private javax.swing.JPanel pnlHistorial;

    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNombreUsuario;
    private javax.swing.JTextField txtDireccionUsuario;
    private javax.swing.JTextField txtTelefonoUsuario;
    private javax.swing.JTextField txtCorreoUsuario;
    private javax.swing.JButton btnGuardarUsuario;
    private javax.swing.JButton btnEditarUsuario;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JTable tblUsuarios;

    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtNombreEmpleado;
    private javax.swing.JTextField txtPuestoEmpleado;
    private javax.swing.JTextField txtUsuarioEmpleado;
    private javax.swing.JTextField txtContrasenaEmpleado;
    private javax.swing.JButton btnGuardarEmpleado;
    private javax.swing.JButton btnEditarEmpleado;
    private javax.swing.JButton btnEliminarEmpleado;
    private javax.swing.JTable tblEmpleados;

    private javax.swing.JTextField txtIdLibro;
    private javax.swing.JTextField txtTituloLibro;
    private javax.swing.JTextField txtAutorLibro;
    private javax.swing.JTextField txtEditorialLibro;
    private javax.swing.JTextField txtIsbnLibro;
    private javax.swing.JTextField txtAnioLibro;
    private javax.swing.JTextField txtStockLibro;
    private javax.swing.JButton btnGuardarLibro;
    private javax.swing.JButton btnEditarLibro;
    private javax.swing.JButton btnEliminarLibro;
    private javax.swing.JTable tblLibros;
    private javax.swing.JTextField txtBuscarLibroCatalogo;

    private javax.swing.JComboBox<Usuario> cboUsuario;
    private javax.swing.JComboBox<Empleado> cboEmpleado;
    private javax.swing.JTextField txtEmpleadoActivo;
    private javax.swing.JTextField txtFechaSalida;
    private javax.swing.JTextField txtFechaDevolucion;
    private javax.swing.JTextField txtBuscarLibroPrestamo;
    private javax.swing.JComboBox<Libro> cboLibro;
    private javax.swing.JButton btnAgregarLibro;
    private javax.swing.JButton btnQuitarLibro;
    private javax.swing.JButton btnGuardarPrestamo;
    private javax.swing.JTable tblDetallePrestamo;

    private javax.swing.JTable tblHistorialPrestamos;
    private javax.swing.JTable tblVerDetallePrestamo;
    private javax.swing.JButton btnDevolverPrestamo;
}
