package ucu.edu.aed.model;

/**
 * Representa un producto almacenado en el depósito.
 */
public class Producto implements Comparable<Producto> {

    /** Código único que identifica al producto. */
    private String codigo;

    /** Nombre del producto. */
    private String nombre;

    /** Descripción del producto. */
    private String descripcion;

    /** Espacio (en UC) que ocupa cada unidad del producto. */
    private int espacioUnitario;

    /**
     * Crea un nuevo producto con espacioUnitario por defecto (1 UC).
     *
     * @param codigo      código único del producto
     * @param nombre      nombre del producto
     * @param descripcion descripción del producto
     */
    public Producto(String codigo, String nombre, String descripcion) {
        this(codigo, nombre, descripcion, 1);
    }

    /**
     * Crea un nuevo producto.
     *
     * @param codigo          código único del producto
     * @param nombre          nombre del producto
     * @param descripcion     descripción del producto
     * @param espacioUnitario espacio en UC que ocupa cada unidad
     */
    public Producto(String codigo, String nombre, String descripcion, int espacioUnitario) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código del producto no puede ser nulo o vacío.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del producto no puede ser nulo o vacío.");
        }

        if (espacioUnitario <= 0) {
            throw new IllegalArgumentException(
                    "El espacio unitario del producto debe ser mayor que cero.");
        }

        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.espacioUnitario = espacioUnitario;
    }

    /**
     * Obtiene el código del producto.
     *
     * @return código del producto
     */
    public String getCodigo() {
        return this.codigo;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre del producto
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return descripción del producto
     */
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Obtiene el espacio unitario del producto.
     *
     * @return espacio en UC que ocupa cada unidad
     */
    public int getEspacioUnitario() {
        return this.espacioUnitario;
    }

    /**
     * Devuelve una representación textual del producto.
     *
     * @return información del producto
     */
    @Override
    public String toString() {
        return "Producto{" +
                "codigo='" + this.codigo + '\'' +
                ", nombre='" + this.nombre + '\'' +
                ", descripcion='" + this.descripcion + '\'' +
                '}';
    }

    @Override
    public int compareTo(Producto otro) {
        if (otro == null) {
            throw new IllegalArgumentException(
                    "El producto a comparar no puede ser nulo.");
        }

        return this.codigo.compareTo(otro.codigo);
    }
}