package ucu.edu.aed.model;

/**
 * Representa una terminal utilizada para realizar operaciones de carga y
 * descarga en el deposito.
 *
 * <p>Invariantes: una terminal libre o deshabilitada no tiene operacion
 * asignada; una terminal ocupada siempre tiene una operacion actual. Una
 * terminal solo puede recibir operaciones cuando esta libre.</p>
 *
 * <p>Complejidad: todas las transiciones de estado son O(1).</p>
 */
public class TerminalCarga {

    /** Numero que identifica a la terminal. */
    private final int numero;

    /** Estado actual de la terminal. */
    private EstadoTerminal estado;

    /** Operacion logistica actualmente asignada a la terminal. */
    private OperacionLogistica operacionActual;

    /**
     * Crea una nueva terminal de carga.
     *
     * @param numero numero identificador de la terminal
     */
    public TerminalCarga(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException(
                    "El numero de terminal debe ser mayor a cero");
        }

        this.numero = numero;
        this.estado = EstadoTerminal.LIBRE;
        this.operacionActual = null;
    }

    /**
     * Obtiene el numero de la terminal.
     *
     * @return numero de terminal
     */
    public int getNumero() {
        return this.numero;
    }

    /**
     * Obtiene el estado actual de la terminal.
     *
     * @return estado de la terminal
     */
    public EstadoTerminal getEstado() {
        return this.estado;
    }

    /**
     * Obtiene la operacion actualmente asignada a la terminal.
     *
     * @return operacion logistica actual
     */
    public OperacionLogistica getOperacionActual() {
        return this.operacionActual;
    }

    /**
     * Determina si la terminal se encuentra libre.
     *
     * @return true si la terminal esta libre
     */
    public boolean estaLibre() {
        return this.estado == EstadoTerminal.LIBRE;
    }

    /**
     * Asigna una operacion logistica a la terminal.
     *
     * @param operacion operacion a asignar
     */
    public void asignarOperacion(OperacionLogistica operacion) {
        if (operacion == null) {
            throw new IllegalArgumentException("La operacion no puede ser null");
        }

        if (!estaLibre()) {
            throw new IllegalStateException(
                    "Solo se puede asignar una operacion a una terminal libre");
        }

        this.operacionActual = operacion;
        this.estado = EstadoTerminal.OCUPADA;
    }

    /**
     * Libera la terminal luego de finalizar una operacion.
     */
    public void liberar() {
        if (this.estado != EstadoTerminal.OCUPADA) {
            throw new IllegalStateException(
                    "Solo se puede liberar una terminal ocupada");
        }

        this.operacionActual = null;
        this.estado = EstadoTerminal.LIBRE;
    }

    /**
     * Deshabilita la terminal.
     *
     * <p>Si la terminal esta ocupada, la operacion se rechaza para no perder
     * la referencia a la operacion en curso. Primero debe liberarse la
     * terminal y luego deshabilitarse.</p>
     */
    public void deshabilitar() {
        if (this.estado == EstadoTerminal.OCUPADA) {
            throw new IllegalStateException(
                    "No se puede deshabilitar una terminal ocupada");
        }

        this.operacionActual = null;
        this.estado = EstadoTerminal.DESHABILITADA;
    }

    /**
     * Habilita nuevamente la terminal.
     */
    public void habilitar() {
        if (this.estado == EstadoTerminal.OCUPADA) {
            throw new IllegalStateException(
                    "No se puede habilitar una terminal ocupada");
        }

        this.operacionActual = null;
        this.estado = EstadoTerminal.LIBRE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TerminalCarga otra)) return false;
        return this.numero == otra.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }
}