package ucu.edu.aed.model;

/**
 * Representa una sucursal abastecida por el depósito.
 */
public class Sucursal {

    /** Identificador único de la sucursal. */
    private String id;

    /** Nombre de la sucursal. */
    private String nombre;

    /** Cantidad de clientes utilizada para determinar su prioridad. */
    private int cantidadClientes;

    /**
     * Crea una nueva sucursal.
     *
     * @param id identificador único de la sucursal
     * @param nombre nombre de la sucursal
     * @param cantidadClientes cantidad de clientes de la sucursal
     */
    public Sucursal(String id, String nombre, int cantidadClientes) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador de la sucursal no puede ser nulo o vacío.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la sucursal no puede ser nulo o vacío.");
        }

        if (cantidadClientes < 0) {
            throw new IllegalArgumentException(
                    "La cantidad de clientes no puede ser negativa.");
        }

        this.id = id;
        this.nombre = nombre;
        this.cantidadClientes = cantidadClientes;
    }

    /**
     * Obtiene el identificador de la sucursal.
     *
     * @return identificador de la sucursal
     */
    public String getId() {
        return this.id;
    }

    /**
     * Obtiene el nombre de la sucursal.
     *
     * @return nombre de la sucursal
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Obtiene la cantidad de clientes de la sucursal.
     *
     * @return cantidad de clientes
     */
    public int getCantidadClientes() {
        return this.cantidadClientes;
    }

    /**
     * Devuelve una representación textual de la sucursal.
     *
     * @return información de la sucursal
     */
    @Override
    public String toString() {
        return "Sucursal{" +
                "id='" + this.id + '\'' +
                ", nombre='" + this.nombre + '\'' +
                ", cantidadClientes=" + this.cantidadClientes +
                '}';
    }
}