package ucu.edu.aed.model;

/**
 * Define las operaciones comunes de toda operación logística
 * que puede ser asignada a una terminal de carga.
 */
public interface OperacionLogistica {

    /**
     * Obtiene el identificador único de la operación.
     *
     * @return identificador de la operación
     */
    String getId();

    /**
     * Obtiene el tipo de operación logística.
     *
     * @return tipo de operación
     */
    TipoOperacion getTipoOperacion();
}