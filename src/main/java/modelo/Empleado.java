package modelo;

public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String puesto;
    private String usuario;
    private String contrasena;

    public Empleado() {
    }

    public Empleado(int idEmpleado, String nombre, String puesto, String usuario, String contrasena) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.puesto = puesto;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Empleado(String nombre, String puesto, String usuario, String contrasena) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Empleado(int idEmpleado, String nombre, String puesto) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.puesto = puesto;
    }

    public Empleado(String nombre, String puesto) {
        this.nombre = nombre;
        this.puesto = puesto;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
