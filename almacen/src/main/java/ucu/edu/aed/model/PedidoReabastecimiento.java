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
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador del pedido no puede ser nulo o vacío.");
        }

        if (sucursal == null) {
            throw new IllegalArgumentException(
                    "La sucursal no puede ser nula.");
        }

        this.id = id;
        this.sucursal = sucursal;
        this.lineas = new ListaSimple<>();
    }

    /**
     * Agrega una línea de productos al pedido.
     *
     * La validación de elementos nulos es responsabilidad
     * de la implementación de ListaSimple.
     *
     * @param linea línea a agregar
     */
    public void agregarLinea(LineaPedido linea) {
        this.lineas.agregar(linea);
    }

    /**
     * Obtiene el identificador del pedido.
     *
     * @return identificador del pedido
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Obtiene la sucursal que realizó el pedido.
     *
     * @return sucursal solicitante
     */
    public Sucursal getSucursal() {
        return this.sucursal;
    }

    /**
     * Obtiene las líneas que componen el pedido.
     *
     * @return lista de líneas del pedido
     */
    public ListaSimple<LineaPedido> getLineas() {
        return this.lineas;
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
        return this.sucursal.getCantidadClientes();
    }

    /**
     * Obtiene el tipo de operación logística.
     *
     * Un pedido de reabastecimiento corresponde a una carga,
     * ya que los productos son cargados para ser enviados
     * a una sucursal.
     *
     * @return tipo de operación
     */
    @Override
    public TipoOperacion getTipoOperacion() {
        return TipoOperacion.CARGA;
    }

    /**
     * Devuelve una representación textual del pedido.
     *
     * @return información del pedido
     */
    @Override
    public String toString() {
        return "PedidoReabastecimiento{" +
                "id='" + this.id + '\'' +
                ", sucursal=" + this.sucursal +
                ", prioridad=" + getPrioridad() +
                ", cantidadLineas=" + this.lineas.tamaño() +
                '}';
    }
}