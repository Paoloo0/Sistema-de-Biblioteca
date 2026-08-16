package vista;

import dao.EmpleadoDAO;
import modelo.Empleado;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FrmLogin extends javax.swing.JFrame {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public FrmLogin() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void btnIngresarActionPerformed(ActionEvent evt) {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado emp = empleadoDAO.validarLogin(usuario, password);
        if (emp != null) {
            String puesto = emp.getPuesto() != null ? emp.getPuesto().toLowerCase() : "";
            String rol = "Empleado";
            if (puesto.contains("principal") || puesto.contains("administrador") || puesto.contains("gerente")) {
                rol = "Administrador";
            }
            
            final String finalRol = rol;
            JOptionPane.showMessageDialog(this, "¡Acceso concedido como " + rol + "!", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
            java.awt.EventQueue.invokeLater(() -> {
                new FrmPrincipal(finalRol, emp.getNombre(), emp.getIdEmpleado()).setVisible(true);
            });
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsuario.requestFocus();
        }
    }

    private void btnSalirActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        pnlFondo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Biblioteca - Login");
        setResizable(false);
        setPreferredSize(new java.awt.Dimension(350, 220));

        pnlFondo.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlFondo.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("INICIO DE SESIÓN");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new java.awt.Insets(5, 5, 15, 5);
        pnlFondo.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        
        lblUsuario.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblUsuario.setText("Usuario:");
        gbc.gridx = 0; gbc.gridy = 1;
        pnlFondo.add(lblUsuario, gbc);

        txtUsuario.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        pnlFondo.add(txtUsuario, gbc);

        lblPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblPassword.setText("Contraseña:");
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        pnlFondo.add(lblPassword, gbc);

        txtPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        pnlFondo.add(txtPassword, gbc);

        JPanel pnlBotones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        
        btnIngresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnIngresar.setText("Ingresar");
        
        btnSalir.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        btnSalir.setText("Salir");

        pnlBotones.add(btnIngresar);
        pnlBotones.add(btnSalir);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 0.0;
        gbc.insets = new java.awt.Insets(15, 5, 5, 5);
        pnlFondo.add(pnlBotones, gbc);

        getContentPane().add(pnlFondo, java.awt.BorderLayout.CENTER);

        btnIngresar.addActionListener(this::btnIngresarActionPerformed);
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        pack();
    }

    private javax.swing.JPanel pnlFondo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnSalir;
}
