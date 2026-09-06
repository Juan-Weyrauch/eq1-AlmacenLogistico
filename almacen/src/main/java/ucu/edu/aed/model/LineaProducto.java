package ucu.edu.aed.model;

/**
 * Representa un producto y la cantidad asociada dentro de una
 * operación logística, ya sea una entrega de proveedor o un
 * pedido de reabastecimiento.
 */
public class LineaProducto {

    /** Producto asociado a la operación. */
    private final Producto producto;

    /** Cantidad del producto asociada a la operación. */
    private final int cantidad;

    /**
     * Crea una nueva línea de producto.
     *
     * @param producto producto asociado
     * @param cantidad cantidad asociada
     */
    public LineaProducto(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto de la línea no puede ser nulo.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de la línea debe ser mayor que cero.");
        }

        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto asociado.
     *
     * @return producto asociado
     */
    public Producto getProducto() {
        return this.producto;
    }

    /**
     * Obtiene la cantidad asociada.
     *
     * @return cantidad asociada
     */
    public int getCantidad() {
        return this.cantidad;
    }

    @Override
    public String toString() {
        return "LineaProducto{" +
                "producto=" + this.producto +
                ", cantidad=" + this.cantidad +
                '}';
    }
}