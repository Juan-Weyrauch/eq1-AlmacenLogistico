package ucu.edu.aed.service;

import ucu.edu.aed.model.EntregaProveedor;
import ucu.edu.aed.model.Inventario;
import ucu.edu.aed.model.PedidoReabastecimiento;
import ucu.edu.aed.model.Producto;
import ucu.edu.aed.model.TerminalCarga;
import ucu.edu.aed.structures.Cola;
import ucu.edu.aed.structures.ColaPrioridad;
import ucu.edu.aed.structures.ListaArray;

/**
 * Coordina las principales operaciones del almacén logístico.
 */
public class AlmacenLogistico {

    /** Inventario general del almacén. */
    private Inventario inventario;

    /** Terminales de carga disponibles en el almacén. */
    private ListaArray<TerminalCarga> terminales;

    /** Entregas de proveedores pendientes, procesadas por orden de llegada. */
    private Cola<EntregaProveedor> entregasPendientes;

    /** Pedidos de reabastecimiento pendientes, procesados según su prioridad. */
    private ColaPrioridad<PedidoReabastecimiento> pedidosPendientes;

    /**
     * Crea un nuevo almacén logístico.
     */
    public AlmacenLogistico() {
        // A implementar.
    }

    /**
     * Registra terminales.
     * 
     * @param terminal terminal a registrar
     */
    public void registrarTerminal(TerminalCarga terminal) {
        // A implementar.
    }

    /**
     * Registra un producto en el inventario.
     *
     * @param producto producto a registrar
     * @param stockInicial cantidad inicial disponible
     */
    public void registrarProducto(Producto producto, int stockInicial) {
        // A implementar.
    }

    /**
     * Registra la llegada de una entrega realizada por un proveedor.
     *
     * @param entrega entrega recibida
     */
    public void registrarLlegadaProveedor(EntregaProveedor entrega) {
        // A implementar.
    }

    /**
     * Asigna la próxima entrega pendiente a una terminal libre.
     *
     * Las entregas se procesan respetando su orden de llegada.
     *
     * @return terminal asignada a la entrega
     */
    public TerminalCarga asignarProximaEntrega() {
        throw new UnsupportedOperationException();
    }

    /**
     * Finaliza una operación de descarga y actualiza el inventario.
     *
     * @param numeroTerminal número de la terminal utilizada
     */
    public void finalizarDescarga(int numeroTerminal) {
        // A implementar.
    }

    /**
     * Registra un pedido de reabastecimiento realizado por una sucursal.
     *
     * @param pedido pedido a registrar
     */
    public void registrarPedidoReabastecimiento(PedidoReabastecimiento pedido) {
        // A implementar.
    }

    /**
     * Asigna el pedido pendiente de mayor prioridad a una terminal libre.
     *
     * @return terminal asignada al pedido
     */
    public TerminalCarga despacharProximoPedido() {
        // TODO: 
        // - compruebe que hay stock para todo el pedido;
        // - descuente el stock;
        // - asigne el pedido a la terminal.
        throw new UnsupportedOperationException();
    }

    /**
     * Finaliza una operación de carga.
     *
     * @param numeroTerminal número de la terminal utilizada
     */
    public void finalizarCarga(int numeroTerminal) {
        // A implementar.
        // recorrer las LineaEntrega, aumentar el inventario y después liberar la terminal..
    }

    /**
     * Busca una terminal que se encuentre libre.
     *
     * @return primera terminal libre encontrada
     */
    public TerminalCarga buscarTerminalLibre() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el inventario del almacén.
     *
     * @return inventario
     */
    public Inventario getInventario() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene las terminales del almacén.
     *
     * @return lista de terminales
     */
    public ListaArray<TerminalCarga> getTerminales() {
        throw new UnsupportedOperationException();
    }
}