package modelo;

public class PrestamoDetalle {
    private int idPrestamo;
    private int idLibro;
    
    
    private String tituloLibro;
    private String autorLibro;

    
    public PrestamoDetalle() {
    }

    
    public PrestamoDetalle(int idPrestamo, int idLibro) {
        this.idPrestamo = idPrestamo;
        this.idLibro = idLibro;
    }

    
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getAutorLibro() {
        return autorLibro;
    }

    public void setAutorLibro(String autorLibro) {
        this.autorLibro = autorLibro;
    }
}
