package dao;

import conexion.Conexion;
import modelo.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public List<Libro> listar() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT ID_Libro, Titulo, Autor, Editorial, ISBN, Anio, Stock FROM LIBRO";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Libro lib = new Libro();
                lib.setIdLibro(rs.getInt("ID_Libro"));
                lib.setTitulo(rs.getString("Titulo"));
                lib.setAutor(rs.getString("Autor"));
                lib.setEditorial(rs.getString("Editorial"));
                lib.setIsbn(rs.getString("ISBN"));
                lib.setAnio(rs.getInt("Anio"));
                lib.setStock(rs.getInt("Stock"));
                lista.add(lib);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Libro lib) {
        String sql = "INSERT INTO LIBRO (Titulo, Autor, Editorial, ISBN, Anio, Stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, lib.getTitulo());
            ps.setString(2, lib.getAutor());
            ps.setString(3, lib.getEditorial());
            ps.setString(4, lib.getIsbn());
            ps.setInt(5, lib.getAnio());
            ps.setInt(6, lib.getStock());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Libro lib) {
        String sql = "UPDATE LIBRO SET Titulo = ?, Autor = ?, Editorial = ?, ISBN = ?, Anio = ?, Stock = ? WHERE ID_Libro = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, lib.getTitulo());
            ps.setString(2, lib.getAutor());
            ps.setString(3, lib.getEditorial());
            ps.setString(4, lib.getIsbn());
            ps.setInt(5, lib.getAnio());
            ps.setInt(6, lib.getStock());
            ps.setInt(7, lib.getIdLibro());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM LIBRO WHERE ID_Libro = ?";
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

    public List<Libro> buscar(String criterio) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT ID_Libro, Titulo, Autor, Editorial, ISBN, Anio, Stock FROM LIBRO WHERE Titulo LIKE ? OR Autor LIKE ? OR ISBN LIKE ?";
        
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String c = "%" + criterio + "%";
            ps.setString(1, c);
            ps.setString(2, c);
            ps.setString(3, c);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro lib = new Libro();
                    lib.setIdLibro(rs.getInt("ID_Libro"));
                    lib.setTitulo(rs.getString("Titulo"));
                    lib.setAutor(rs.getString("Autor"));
                    lib.setEditorial(rs.getString("Editorial"));
                    lib.setIsbn(rs.getString("ISBN"));
                    lib.setAnio(rs.getInt("Anio"));
                    lib.setStock(rs.getInt("Stock"));
                    lista.add(lib);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public boolean existeIsbn(String isbn) {
        String sql = "SELECT COUNT(*) FROM LIBRO WHERE ISBN = ?";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
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

    public boolean actualizarStock(Connection con, int idLibro, int cantidad) throws SQLException {
        String sql = "UPDATE LIBRO SET Stock = Stock + ? WHERE ID_Libro = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idLibro);
            return ps.executeUpdate() > 0;
        }
    }
}
