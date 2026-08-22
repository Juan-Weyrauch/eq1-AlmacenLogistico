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
        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto de la línea de entrega no puede ser nulo.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad entregada debe ser mayor que cero.");
        }

        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto entregado.
     *
     * @return producto entregado
     */
    public Producto getProducto() {
        return this.producto;
    }

    /**
     * Obtiene la cantidad entregada.
     *
     * @return cantidad entregada
     */
    public int getCantidad() {
        return this.cantidad;
    }

    /**
     * Devuelve una representación textual de la línea de entrega.
     *
     * @return información de la línea
     */
    @Override
    public String toString() {
        return "LineaEntrega{" +
                "producto=" + this.producto +
                ", cantidad=" + this.cantidad +
                '}';
    }
}