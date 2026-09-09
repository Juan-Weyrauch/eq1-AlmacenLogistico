package ucu.edu.aed.model;

/**
 * Paso de un recorrido de picking.
 *
 * <p>Representa que, al visitar una posicion concreta, deben retirarse cierta
 * cantidad de unidades de un producto.</p>
 */
public class PasoPicking {

    // objeto de resultado; no modifica inventario.
    private final String rutaPosicion;
    private final Producto producto;
    private final int cantidad;

    public PasoPicking(
            String rutaPosicion,
            Producto producto,
            int cantidad) {

        if (rutaPosicion == null || rutaPosicion.isBlank()) {
            throw new IllegalArgumentException(
                    "La ruta de la posicion no puede ser nula o vacia");
        }

        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto del paso de picking no puede ser null");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad del paso de picking debe ser mayor que cero");
        }

        this.rutaPosicion = rutaPosicion;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public String getRutaPosicion() {
        return this.rutaPosicion;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    @Override
    public String toString() {
        return this.rutaPosicion
                + " -> "
                + this.producto.getCodigo()
                + " -> "
                + this.cantidad;
    }
}
