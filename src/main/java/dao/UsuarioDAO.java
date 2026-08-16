package dao;

import conexion.Conexion;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT ID_Usuario, Nombre, Direccion, Telefono, Correo FROM USUARIO";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_Usuario"));
                u.setNombre(rs.getString("Nombre"));
                u.setDireccion(rs.getString("Direccion"));
                u.setTelefono(rs.getString("Telefono"));
                u.setCorreo(rs.getString("Correo"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO USUARIO (Nombre, Direccion, Telefono, Correo) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getDireccion());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getCorreo());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Usuario u) {
        String sql = "UPDATE USUARIO SET Nombre = ?, Direccion = ?, Telefono = ?, Correo = ? WHERE ID_Usuario = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getDireccion());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getCorreo());
            ps.setInt(5, u.getIdUsuario());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM USUARIO WHERE ID_Usuario = ?";
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
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE Nombre = ?";
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
}
