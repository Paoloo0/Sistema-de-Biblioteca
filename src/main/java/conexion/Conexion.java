package conexion;

import java.sql.*;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/db_biblioteca?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection obtenerConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return con;
    }
    
    public static void cerrar(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
