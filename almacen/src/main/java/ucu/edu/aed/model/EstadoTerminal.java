package ucu.edu.aed.model;

/**
 * Representa los posibles estados de una terminal de carga.
 */
public enum EstadoTerminal {

    /** La terminal se encuentra disponible para realizar una operación. */
    LIBRE,

    /** La terminal se encuentra realizando una operación. */
    OCUPADA,

    /** La terminal no se encuentra disponible para ser utilizada. */
    DESHABILITADA
}