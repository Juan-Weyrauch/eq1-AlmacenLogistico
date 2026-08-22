package ucu.edu.aed.model;

/**
 * Representa un producto almacenado en el depósito.
 */
public class Producto {

    /** Código único que identifica al producto. */
    private String codigo;

    /** Nombre del producto. */
    private String nombre;

    /** Descripción del producto. */
    private String descripcion;

    /**
     * Crea un nuevo producto.
     *
     * @param codigo código único del producto
     * @param nombre nombre del producto
     * @param descripcion descripción del producto
     */
    public Producto(String codigo, String nombre, String descripcion) {
        // A implementar.
    }

    /**
     * Obtiene el código del producto.
     *
     * @return código del producto
     */
    public String getCodigo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre del producto
     */
    public String getNombre() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return descripción del producto
     */
    public String getDescripcion() {
        throw new UnsupportedOperationException();
    }
}