package ucu.edu.aed.model;

/**
 * Representa una terminal utilizada para realizar operaciones
 * de carga y descarga en el depósito.
 */
public class TerminalCarga {

    /** Número que identifica a la terminal. */
    private int numero;

    /** Estado actual de la terminal. */
    private EstadoTerminal estado;

    /** Operación logística actualmente asignada a la terminal. */
    private OperacionLogistica operacionActual;

    /**
     * Crea una nueva terminal de carga.
     *
     * @param numero número identificador de la terminal
     */
    public TerminalCarga(int numero) {
        // A implementar.
    }

    /**
     * Obtiene el número de la terminal.
     *
     * @return número de terminal
     */
    public int getNumero() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el estado actual de la terminal.
     *
     * @return estado de la terminal
     */
    public EstadoTerminal getEstado() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene la operación actualmente asignada a la terminal.
     *
     * @return operación logística actual
     */
    public OperacionLogistica getOperacionActual() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determina si la terminal se encuentra libre.
     *
     * @return true si la terminal está libre
     */
    public boolean estaLibre() {
        throw new UnsupportedOperationException();
    }

    /**
     * Asigna una operación logística a la terminal.
     *
     * @param operacion operación a asignar
     */
    public void asignarOperacion(OperacionLogistica operacion) {
        // A implementar.
    }

    /**
     * Libera la terminal luego de finalizar una operación.
     */
    public void liberar() {
        // A implementar.
    }

    /**
     * Deshabilita la terminal.
     */
    public void deshabilitar() {
        // A implementar.
    }

    /**
     * Habilita nuevamente la terminal.
     */
    public void habilitar() {
        // A implementar.
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TerminalCarga)) return false;
        TerminalCarga otra = (TerminalCarga) obj;
        return this.numero == otra.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }
}