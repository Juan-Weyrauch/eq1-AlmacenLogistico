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
        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto de la línea de pedido no puede ser nulo.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad solicitada debe ser mayor que cero.");
        }

        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto solicitado.
     *
     * @return producto solicitado
     */
    public Producto getProducto() {
        return this.producto;
    }

    /**
     * Obtiene la cantidad solicitada.
     *
     * @return cantidad solicitada
     */
    public int getCantidad() {
        return this.cantidad;
    }

    /**
     * Devuelve una representación textual de la línea del pedido.
     *
     * @return información de la línea
     */
    @Override
    public String toString() {
        return "LineaPedido{" +
                "producto=" + this.producto +
                ", cantidad=" + this.cantidad +
                '}';
    }
}