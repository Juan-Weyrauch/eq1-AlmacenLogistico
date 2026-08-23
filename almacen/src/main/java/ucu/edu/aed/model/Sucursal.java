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
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el nombre de la sucursal.
     *
     * @return nombre de la sucursal
     */
    public String getNombre() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene la cantidad de clientes de la sucursal.
     *
     * @return cantidad de clientes
     */
    public int getCantidadClientes() {
        throw new UnsupportedOperationException();
    }
}