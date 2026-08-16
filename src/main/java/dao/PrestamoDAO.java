package dao;

import conexion.Conexion;
import modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public boolean registrarPrestamo(Prestamo p) {
        String sqlPrestamo = "INSERT INTO PRESTAMO (ID_Usuario, ID_Empleado, Fecha_Salida, Fecha_Devolucion_Maxima, Estado) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO PRESTAMO_DETALLE (ID_Prestamo, ID_Libro) VALUES (?, ?)";
        String sqlStock = "UPDATE LIBRO SET Stock = Stock - 1 WHERE ID_Libro = ?";
        
        Connection con = null;
        PreparedStatement psPrestamo = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;
        ResultSet rsKeys = null;
        
        try {
            con = Conexion.obtenerConexion();
            if (con == null) return false;
            
            con.setAutoCommit(false);
            
            psPrestamo = con.prepareStatement(sqlPrestamo, Statement.RETURN_GENERATED_KEYS);
            psPrestamo.setInt(1, p.getIdUsuario());
            psPrestamo.setInt(2, p.getIdEmpleado());
            psPrestamo.setDate(3, p.getFechaSalida());
            psPrestamo.setDate(4, p.getFechaDevolucionMaxima());
            psPrestamo.setString(5, p.getEstado());
            
            int filasCabecera = psPrestamo.executeUpdate();
            if (filasCabecera == 0) {
                throw new SQLException("Error");
            }
            
            rsKeys = psPrestamo.getGeneratedKeys();
            int idPrestamoGenerado = 0;
            if (rsKeys.next()) {
                idPrestamoGenerado = rsKeys.getInt(1);
            } else {
                throw new SQLException("Error");
            }
            
            psDetalle = con.prepareStatement(sqlDetalle);
            psStock = con.prepareStatement(sqlStock);
            
            for (PrestamoDetalle det : p.getDetalles()) {
                psDetalle.setInt(1, idPrestamoGenerado);
                psDetalle.setInt(2, det.getIdLibro());
                psDetalle.addBatch();
                
                psStock.setInt(1, det.getIdLibro());
                psStock.addBatch();
            }
            
            psDetalle.executeBatch();
            psStock.executeBatch();
            
            con.commit();
            return true;
            
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psPrestamo != null) psPrestamo.close();
                if (psDetalle != null) psDetalle.close();
                if (psStock != null) psStock.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public List<Prestamo> listar() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.ID_Prestamo, p.ID_Usuario, u.Nombre AS NombreUsuario, "
                   + "p.ID_Empleado, e.Nombre AS NombreEmpleado, "
                   + "p.Fecha_Salida, p.Fecha_Devolucion_Maxima, p.Estado "
                   + "FROM PRESTAMO p "
                   + "INNER JOIN USUARIO u ON p.ID_Usuario = u.ID_Usuario "
                   + "INNER JOIN EMPLEADO e ON p.ID_Empleado = e.ID_Empleado "
                   + "ORDER BY p.ID_Prestamo DESC";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setIdPrestamo(rs.getInt("ID_Prestamo"));
                p.setIdUsuario(rs.getInt("ID_Usuario"));
                p.setNombreUsuario(rs.getString("NombreUsuario"));
                p.setIdEmpleado(rs.getInt("ID_Empleado"));
                p.setNombreEmpleado(rs.getString("NombreEmpleado"));
                p.setFechaSalida(rs.getDate("Fecha_Salida"));
                p.setFechaDevolucionMaxima(rs.getDate("Fecha_Devolucion_Maxima"));
                p.setEstado(rs.getString("Estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public List<PrestamoDetalle> listarDetalles(int idPrestamo) {
        List<PrestamoDetalle> lista = new ArrayList<>();
        String sql = "SELECT pd.ID_Prestamo, pd.ID_Libro, l.Titulo AS TituloLibro, l.Autor AS AutorLibro "
                   + "FROM PRESTAMO_DETALLE pd "
                   + "INNER JOIN LIBRO l ON pd.ID_Libro = l.ID_Libro "
                   + "WHERE pd.ID_Prestamo = ?";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrestamoDetalle pd = new PrestamoDetalle();
                    pd.setIdPrestamo(rs.getInt("ID_Prestamo"));
                    pd.setIdLibro(rs.getInt("ID_Libro"));
                    pd.setTituloLibro(rs.getString("TituloLibro"));
                    pd.setAutorLibro(rs.getString("AutorLibro"));
                    lista.add(pd);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstado(int idPrestamo, String nuevoEstado) {
        String sql = "UPDATE PRESTAMO SET Estado = ? WHERE ID_Prestamo = ?";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psStock = null;
        try {
            con = Conexion.obtenerConexion();
            if (con == null) return false;
            con.setAutoCommit(false);
            
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPrestamo);
            int filas = ps.executeUpdate();
            
            if (filas > 0 && nuevoEstado.equals("Devuelto")) {
                List<PrestamoDetalle> detalles = listarDetalles(idPrestamo);
                String sqlStock = "UPDATE LIBRO SET Stock = Stock + 1 WHERE ID_Libro = ?";
                psStock = con.prepareStatement(sqlStock);
                for (PrestamoDetalle det : detalles) {
                    psStock.setInt(1, det.getIdLibro());
                    psStock.addBatch();
                }
                psStock.executeBatch();
            }
            
            con.commit();
            return filas > 0;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }
            System.err.println("Error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (psStock != null) psStock.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
