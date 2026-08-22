package ucu.edu.aed.model;

/**
 * Representa un producto junto con su cantidad disponible en inventario.
 *
 * <p>Invariante: el producto asociado no es {@code null} y el stock siempre
 * es mayor o igual a cero.</p>
 *
 * <p>Complejidad: todas las operaciones de esta clase son O(1).</p>
 */
public class ItemInventario {

    /** Producto asociado al item de inventario. */
    private final Producto producto;

    /** Cantidad disponible del producto. */
    private int stock;

    /**
     * Crea un nuevo item de inventario.
     *
     * @param producto producto almacenado
     * @param stock cantidad inicial disponible
     */
    public ItemInventario(Producto producto, int stock) {
        validarProducto(producto);
        validarCantidadNoNegativa(stock, "El stock inicial no puede ser negativo");

        this.producto = producto;
        this.stock = stock;
    }

    /**
     * Obtiene el producto asociado.
     *
     * @return producto almacenado
     */
    public Producto getProducto() {
        return this.producto;
    }

    /**
     * Obtiene la cantidad disponible.
     *
     * @return stock actual
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * Incrementa el stock disponible.
     *
     * @param cantidad cantidad a agregar
     */
    public void aumentarStock(int cantidad) {
        validarCantidadNoNegativa(cantidad, "La cantidad a agregar no puede ser negativa");
        this.stock += cantidad;
    }

    /**
     * Disminuye el stock disponible.
     *
     * @param cantidad cantidad a retirar
     */
    public void disminuirStock(int cantidad) {
        validarCantidadNoNegativa(cantidad, "La cantidad a retirar no puede ser negativa");

        if (cantidad > this.stock) {
            throw new IllegalArgumentException(
                    "No hay stock suficiente para retirar la cantidad indicada");
        }

        this.stock -= cantidad;
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
    }

    private void validarCantidadNoNegativa(int cantidad, String mensaje) {
        if (cantidad < 0) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
