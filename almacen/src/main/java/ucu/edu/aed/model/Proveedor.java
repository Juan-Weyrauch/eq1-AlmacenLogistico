package ucu.edu.aed.model;

/**
 * Representa un proveedor que entrega productos al depósito.
 */
public class Proveedor {

    /** Identificador único del proveedor. */
    private String id;

    /** Nombre del proveedor. */
    private String nombre;

    /**
     * Crea un nuevo proveedor.
     *
     * @param id identificador único del proveedor
     * @param nombre nombre del proveedor
     */
    public Proveedor(String id, String nombre) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador del proveedor no puede ser nulo o vacío.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del proveedor no puede ser nulo o vacío.");
        }

        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el identificador del proveedor.
     *
     * @return identificador del proveedor
     */
    public String getId() {
        return this.id;
    }

    /**
     * Obtiene el nombre del proveedor.
     *
     * @return nombre del proveedor
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Devuelve una representación textual del proveedor.
     *
     * @return información del proveedor
     */
    @Override
    public String toString() {
        return "Proveedor{" +
                "id='" + this.id + '\'' +
                ", nombre='" + this.nombre + '\'' +
                '}';
    }
}