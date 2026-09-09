package ucu.edu.aed.model;

import ucu.edu.aed.structures.linear.ListaSimple;

/**
 * Representa un pedido de reabastecimiento realizado por una sucursal.
 */
public class PedidoReabastecimiento implements OperacionLogistica {

    /** Identificador único del pedido. */
    private String id;

    /** Sucursal que realizó el pedido. */
    private Sucursal sucursal;

    /** Productos solicitados por la sucursal. */
    private ListaSimple<LineaProducto> lineas;

    /**
     * Prioridad del pedido. Se inicializa a partir de la cantidad de
     * clientes de la sucursal (criterio del Hito 1), pero a partir de ahí
     * es independiente: puede modificarse mientras el pedido está
     * pendiente (Parte D del Hito 2), sin que un cambio posterior en la
     * sucursal la afecte.
     */
    private int prioridad;

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
        this.prioridad = sucursal.getCantidadClientes();
    }

    /**
     * Agrega una línea de productos al pedido.
     *
     * La validación de elementos nulos es responsabilidad
     * de la implementación de ListaSimple.
     *
     * @param linea línea a agregar
     */
    public void agregarLinea(LineaProducto linea) {
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
    public ListaSimple<LineaProducto> getLineas() {
        return this.lineas;
    }

    /**
     * Obtiene la prioridad actual del pedido.
     *
     * Se inicializa según la cantidad de clientes de la sucursal al
     * momento de crear el pedido, pero puede haber sido modificada
     * después con setPrioridad() mientras el pedido estaba pendiente.
     *
     * @return prioridad del pedido
     */
    public int getPrioridad() {
        return this.prioridad;
    }

    /**
     * Modifica la prioridad del pedido mientras está pendiente.
     *
     * Quien llame a este método y el pedido ya esté encolado es
     * responsable de avisarle a la cola de prioridad (reordenar())
     * para que reacomode su posición; este método por sí solo no
     * toca ninguna estructura externa.
     *
     * @param nuevaPrioridad nueva prioridad del pedido
     */
    public void setPrioridad(int nuevaPrioridad) {
        if (nuevaPrioridad < 0) {
            throw new IllegalArgumentException(
                    "La prioridad no puede ser negativa.");
        }

        this.prioridad = nuevaPrioridad;
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