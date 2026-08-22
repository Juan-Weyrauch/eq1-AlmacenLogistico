package ucu.edu.aed.model;

/**
 * Representa un producto junto con su cantidad disponible en inventario.
 */
public class ItemInventario {

    /** Producto asociado al ítem de inventario. */
    private Producto producto;

    /** Cantidad disponible del producto. */
    private int stock;

    /**
     * Crea un nuevo ítem de inventario.
     *
     * @param producto producto almacenado
     * @param stock cantidad inicial disponible
     */
    public ItemInventario(Producto producto, int stock) {
        // A implementar.
    }

    /**
     * Obtiene el producto asociado.
     *
     * @return producto almacenado
     */
    public Producto getProducto() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene la cantidad disponible.
     *
     * @return stock actual
     */
    public int getStock() {
        throw new UnsupportedOperationException();
    }

    /**
     * Incrementa el stock disponible.
     *
     * @param cantidad cantidad a agregar
     */
    public void aumentarStock(int cantidad) {
        // A implementar.
    }

    /**
     * Disminuye el stock disponible.
     *
     * @param cantidad cantidad a retirar
     */
    public void disminuirStock(int cantidad) {
        // A implementar.
    }
}