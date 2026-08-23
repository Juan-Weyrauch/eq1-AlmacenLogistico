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
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el identificador del proveedor.
     *
     * @return identificador del proveedor
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del proveedor.
     *
     * @return nombre del proveedor
     */
    public String getNombre() {
        return nombre;

    }
}