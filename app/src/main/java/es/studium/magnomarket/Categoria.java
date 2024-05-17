package es.studium.magnomarket;

public class Categoria {
    private int idCategoria;
    private String nombreCategoria;

    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    @Override
    public String toString() {
        return getNombreCategoria();
    }
}
