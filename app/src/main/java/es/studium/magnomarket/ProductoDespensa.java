package es.studium.magnomarket;

import java.time.LocalDate;

public class ProductoDespensa {
    private int idProductoDespensa;
    private String nombreProductoDespensa;
    private String imagenProductoDespensa;
    private LocalDate fechaCaducidadProductoDespensa;
    private int cantidadProductoDespensa;
    private String unidadProductoDespensa;
    private int autoanadirAListaCompraDespensa;
    private int cantidadMinParaAnadirDespensa;
    private String tiendaProductoDespensa;
    private int idCategoriaFK;
    private int idUsuarioFK;

    public ProductoDespensa(int idProductoDespensa, String nombreProductoDespensa, String imagenProductoDespensa, LocalDate fechaCaducidadProductoDespensa, int cantidadProductoDespensa, String unidadProductoDespensa, int autoanadirAListaCompraDespensa, int cantidadMinParaAnadirDespensa, String tiendaProductoDespensa, int idCategoriaFK, int idUsuarioFK) {
        this.idProductoDespensa = idProductoDespensa;
        this.nombreProductoDespensa = nombreProductoDespensa;
        this.imagenProductoDespensa = imagenProductoDespensa;
        this.fechaCaducidadProductoDespensa = fechaCaducidadProductoDespensa;
        this.cantidadProductoDespensa = cantidadProductoDespensa;
        this.unidadProductoDespensa = unidadProductoDespensa;
        this.autoanadirAListaCompraDespensa = autoanadirAListaCompraDespensa;
        this.cantidadMinParaAnadirDespensa = cantidadMinParaAnadirDespensa;
        this.tiendaProductoDespensa = tiendaProductoDespensa;
        this.idCategoriaFK = idCategoriaFK;
        this.idUsuarioFK = idUsuarioFK;
    }

    public ProductoDespensa(String nombreProductoDespensa, String imagenProductoDespensa, LocalDate fechaCaducidadProductoDespensa, int cantidadProductoDespensa, String unidadProductoDespensa, int autoanadirAListaCompraDespensa, int cantidadMinParaAnadirDespensa, String tiendaProductoDespensa, int idCategoriaFK, int idUsuarioFK) {
        this.nombreProductoDespensa = nombreProductoDespensa;
        this.imagenProductoDespensa = imagenProductoDespensa;
        this.fechaCaducidadProductoDespensa = fechaCaducidadProductoDespensa;
        this.cantidadProductoDespensa = cantidadProductoDespensa;
        this.unidadProductoDespensa = unidadProductoDespensa;
        this.autoanadirAListaCompraDespensa = autoanadirAListaCompraDespensa;
        this.cantidadMinParaAnadirDespensa = cantidadMinParaAnadirDespensa;
        this.tiendaProductoDespensa = tiendaProductoDespensa;
        this.idCategoriaFK = idCategoriaFK;
        this.idUsuarioFK = idUsuarioFK;
    }

    public int getIdProductoDespensa() {
        return idProductoDespensa;
    }

    public String getNombreProductoDespensa() {
        return nombreProductoDespensa;
    }

    public String getImagenProductoDespensa() {
        return imagenProductoDespensa;
    }
    public void setImagenProductoDespensa(String imageUri) {
        this.imagenProductoDespensa = imageUri;
    }

    public LocalDate getFechaCaducidadProductoDespensa() {
        return fechaCaducidadProductoDespensa;
    }

    public int getCantidadProductoDespensa() {
        return cantidadProductoDespensa;
    }

    public String getUnidadProductoDespensa() {
        return unidadProductoDespensa;
    }

    public int getAutoanadirAListaCompraDespensa() {
        return autoanadirAListaCompraDespensa;
    }

    public int getCantidadMinParaAnadirDespensa() {
        return cantidadMinParaAnadirDespensa;
    }

    public String getTiendaProductoDespensa() {
        return tiendaProductoDespensa;
    }

    public int getIdCategoriaFK() {
        return idCategoriaFK;
    }

    public int getIdUsuarioFK() {
        return idUsuarioFK;
    }

    public void setNombreProductoDespensa(String nombreProductoDespensa) {
        this.nombreProductoDespensa = nombreProductoDespensa;
    }

    public void setFechaCaducidadProductoDespensa(LocalDate fechaCaducidadProductoDespensa) {
        this.fechaCaducidadProductoDespensa = fechaCaducidadProductoDespensa;
    }

    public void setCantidadProductoDespensa(int cantidadProductoDespensa) {
        this.cantidadProductoDespensa = cantidadProductoDespensa;
    }

    public void setUnidadProductoDespensa(String unidadProductoDespensa) {
        this.unidadProductoDespensa = unidadProductoDespensa;
    }

    public void setAutoanadirAListaCompraDespensa(int autoanadirAListaCompraDespensa) {
        this.autoanadirAListaCompraDespensa = autoanadirAListaCompraDespensa;
    }

    public void setCantidadMinParaAnadirDespensa(int cantidadMinParaAnadirDespensa) {
        this.cantidadMinParaAnadirDespensa = cantidadMinParaAnadirDespensa;
    }

    public void setTiendaProductoDespensa(String tiendaProductoDespensa) {
        this.tiendaProductoDespensa = tiendaProductoDespensa;
    }

    public void setIdCategoriaFK(int idCategoriaFK) {
        this.idCategoriaFK = idCategoriaFK;
    }

    @Override
    public String toString() {
        return getNombreProductoDespensa();
    }
}
