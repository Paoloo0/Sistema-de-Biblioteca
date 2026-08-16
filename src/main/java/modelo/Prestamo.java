package modelo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Prestamo {
    private int idPrestamo;
    private int idUsuario;
    private int idEmpleado;
    private Date fechaSalida;
    private Date fechaDevolucionMaxima;
    private String estado;
    
    
    private String nombreUsuario;
    private String nombreEmpleado;
    
    
    private List<PrestamoDetalle> detalles = new ArrayList<>();

    
    public Prestamo() {
    }

    
    public Prestamo(int idPrestamo, int idUsuario, int idEmpleado, Date fechaSalida, Date fechaDevolucionMaxima, String estado) {
        this.idPrestamo = idPrestamo;
        this.idUsuario = idUsuario;
        this.idEmpleado = idEmpleado;
        this.fechaSalida = fechaSalida;
        this.fechaDevolucionMaxima = fechaDevolucionMaxima;
        this.estado = estado;
    }

    
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaDevolucionMaxima() {
        return fechaDevolucionMaxima;
    }

    public void setFechaDevolucionMaxima(Date fechaDevolucionMaxima) {
        this.fechaDevolucionMaxima = fechaDevolucionMaxima;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public List<PrestamoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<PrestamoDetalle> detalles) {
        this.detalles = detalles;
    }
    
    public void agregarDetalle(PrestamoDetalle detalle) {
        this.detalles.add(detalle);
    }
}
