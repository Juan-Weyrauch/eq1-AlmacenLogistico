package ucu.edu.aed.model;

/**
 * Representa un producto solicitado dentro de un pedido de reabastecimiento.
 */
public class LineaPedido {

    /** Producto solicitado. */
    private Producto producto;

    /** Cantidad solicitada del producto. */
    private int cantidad;

    /**
     * Crea una nueva línea de pedido.
     *
     * @param producto producto solicitado
     * @param cantidad cantidad solicitada
     */
    public LineaPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto solicitado.
     *
     * @return producto solicitado
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Obtiene la cantidad solicitada.
     *
     * @return cantidad solicitada
     */
    public int getCantidad() {
        return cantidad;
    }
}