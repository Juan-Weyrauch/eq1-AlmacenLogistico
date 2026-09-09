package ucu.edu.aed.model;

public class UbicacionStock {

    private final Producto producto;
    private final Sector posicion;
    private int cantidad;

    public UbicacionStock(Producto producto, Sector posicion, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "UbicacionStock: producto no puede ser nulo");
        }

        if (posicion == null) {
            throw new IllegalArgumentException(
                    "UbicacionStock: posicion no puede ser nula");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "UbicacionStock: cantidad debe ser mayor que cero");
        }

        this.producto = producto;
        this.posicion = posicion;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public Sector getPosicion() {
        return this.posicion;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public void aumentarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "UbicacionStock: cantidad a aumentar debe ser mayor que cero");
        }

        this.cantidad += cantidad;
    }

    public void disminuirCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "UbicacionStock: cantidad a disminuir debe ser mayor que cero");
        }

        if (cantidad > this.cantidad) {
            throw new IllegalArgumentException(
                    "UbicacionStock: no hay suficiente cantidad para disminuir");
        }

        this.cantidad -= cantidad;
    }

    @Override
    public String toString() {
        return "UbicacionStock{" +
                "producto=" + this.producto.getCodigo() +
                ", posicion=" + this.posicion.getCodigoLocal() +
                ", cantidad=" + this.cantidad +
                '}';
    }
}
