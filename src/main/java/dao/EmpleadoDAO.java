package dao;

import conexion.Conexion;
import modelo.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public List<Empleado> listar() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT ID_Empleado, Nombre, Puesto, Usuario, Contrasena FROM EMPLEADO";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("ID_Empleado"));
                emp.setNombre(rs.getString("Nombre"));
                emp.setPuesto(rs.getString("Puesto"));
                emp.setUsuario(rs.getString("Usuario"));
                emp.setContrasena(rs.getString("Contrasena"));
                lista.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Empleado emp) {
        String sql = "INSERT INTO EMPLEADO (Nombre, Puesto, Usuario, Contrasena) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getPuesto());
            ps.setString(3, emp.getUsuario());
            ps.setString(4, emp.getContrasena());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Empleado emp) {
        String sql = "UPDATE EMPLEADO SET Nombre = ?, Puesto = ?, Usuario = ?, Contrasena = ? WHERE ID_Empleado = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getPuesto());
            ps.setString(3, emp.getUsuario());
            ps.setString(4, emp.getContrasena());
            ps.setInt(5, emp.getIdEmpleado());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM EMPLEADO WHERE ID_Empleado = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM EMPLEADO WHERE Nombre = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    public Empleado validarLogin(String usuario, String contrasena) {
        String sql = "SELECT ID_Empleado, Nombre, Puesto FROM EMPLEADO WHERE Usuario = ? AND Contrasena = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado emp = new Empleado();
                    emp.setIdEmpleado(rs.getInt("ID_Empleado"));
                    emp.setNombre(rs.getString("Nombre"));
                    emp.setPuesto(rs.getString("Puesto"));
                    return emp;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
}
