package ucu.edu.aed.model;

/**
 * Representa un producto incluido dentro de una entrega de proveedor.
 */
public class LineaEntrega {

    /** Producto entregado. */
    private Producto producto;

    /** Cantidad entregada del producto. */
    private int cantidad;

    /**
     * Crea una nueva línea de entrega.
     *
     * @param producto producto entregado
     * @param cantidad cantidad entregada
     */
    public LineaEntrega(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto entregado.
     *
     * @return producto entregado
     */
    public Producto getProducto() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene la cantidad entregada.
     *
     * @return cantidad entregada
     */
    public int getCantidad() {
        throw new UnsupportedOperationException();
    }
}