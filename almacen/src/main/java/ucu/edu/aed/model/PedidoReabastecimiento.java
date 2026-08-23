package ucu.edu.aed.model;

import ucu.edu.aed.structures.ListaSimple;

/**
 * Representa un pedido de reabastecimiento realizado por una sucursal.
 */
public class PedidoReabastecimiento implements OperacionLogistica {

    /** Identificador único del pedido. */
    private String id;

    /** Sucursal que realizó el pedido. */
    private Sucursal sucursal;

    /** Productos solicitados por la sucursal. */
    private ListaSimple<LineaPedido> lineas;

    /**
     * Crea un nuevo pedido de reabastecimiento.
     *
     * @param id identificador único del pedido
     * @param sucursal sucursal que realiza el pedido
     */
    public PedidoReabastecimiento(String id, Sucursal sucursal) {
        this.id = id;
        this.sucursal = sucursal;
    }

    /**
     * Agrega una línea de productos al pedido.
     *
     * @param linea línea a agregar
     */
    public void agregarLinea(LineaPedido linea) {
        // A implementar.
    }

    /**
     * Obtiene el identificador del pedido.
     *
     * @return identificador del pedido
     */
    @Override
    public String getId() {
        return id;

    }

    /**
     * Obtiene la sucursal que realizó el pedido.
     *
     * @return sucursal solicitante
     */
    public Sucursal getSucursal() {
        return sucursal;

    }

    /**
     * Obtiene las líneas que componen el pedido.
     *
     * @return lista de líneas del pedido
     */
    public ListaSimple<LineaPedido> getLineas() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene la prioridad del pedido.
     *
     * La prioridad está determinada por la cantidad de clientes
     * de la sucursal solicitante.
     *
     * @return prioridad del pedido
     */
    public int getPrioridad() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el tipo de operación logística.
     *
     * @return tipo de operación
     */
    @Override
    public TipoOperacion getTipoOperacion() {
        throw new UnsupportedOperationException();
    }
}