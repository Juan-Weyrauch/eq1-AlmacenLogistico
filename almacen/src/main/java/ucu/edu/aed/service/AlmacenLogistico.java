package ucu.edu.aed.service;

import ucu.edu.aed.model.*;
import ucu.edu.aed.structures.Cola;
import ucu.edu.aed.structures.ColaPrioridad;
import ucu.edu.aed.structures.ListaArray;
import ucu.edu.aed.structures.ListaSimple;

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
        this.inventario = new Inventario();
        this.terminales = new ListaArray<>();
        this.entregasPendientes = new Cola<>();
        //this.pedidosPendientes = new ColaPrioridad<>(); -> me pide un atributo ¿?
    }

    private TerminalCarga buscarTerminalPorNumero(int numero) {
        for (int i = 0; i < terminales.tamaño(); i++) {
            TerminalCarga terminal = terminales.obtener(i);
            if (terminal.getNumero() == numero) {
                return terminal;
            }
        }
        return null;
    }

    /**
     * Registra terminales.
     *
     * @param terminal terminal a registrar
     */
    public void registrarTerminal(TerminalCarga terminal) {
        if (terminales.contiene(terminal)) {
            throw new IllegalArgumentException("Terminal ya registrada " + terminal.getNumero());
        }
        terminales.agregar(terminal);
    }

    /**
     * Registra un producto en el inventario.
     *
     * @param producto producto a registrar
     * @param stockInicial cantidad inicial disponible
     */
    public void registrarProducto(Producto producto, int stockInicial) {
        inventario.registrarProducto(producto, stockInicial);
    }

    /**
     * Registra la llegada de una entrega realizada por un proveedor.
     *
     * @param entrega entrega recibida
     */
    public void registrarLlegadaProveedor(EntregaProveedor entrega) {
        entregasPendientes.agregar(entrega);
    }

    /**
     * Asigna la próxima entrega pendiente a una terminal libre.
     *
     * Las entregas se procesan respetando su orden de llegada.
     *
     * @return terminal asignada a la entrega
     */
    public TerminalCarga asignarProximaEntrega() {
        TerminalCarga terminal = buscarTerminalLibre();
        if (terminal == null) {
            return null;
        }
        EntregaProveedor entrega = entregasPendientes.quitaDeCola();
        terminal.asignarOperacion(entrega);
        return terminal;
    }

    /**
     * Finaliza una operación de descarga y actualiza el inventario.
     *
     * @param numeroTerminal número de la terminal utilizada
     */
    public void finalizarDescarga(int numeroTerminal) {
        TerminalCarga terminal = buscarTerminalPorNumero(numeroTerminal);
        if (terminal == null) {
            throw new IllegalArgumentException("No existe la terminal " + numeroTerminal);
        }

        OperacionLogistica operacion = terminal.getOperacionActual();
        if (operacion == null || operacion.getTipoOperacion() != TipoOperacion.DESCARGA) {
            throw new IllegalStateException("La terminal " + numeroTerminal + " no tiene una descarga en curso");
        }

        EntregaProveedor entrega = (EntregaProveedor) operacion;
        ListaSimple<LineaEntrega> lineas = entrega.getLineas();
        for (int i = 0; i < lineas.tamaño(); i++) {
            LineaEntrega linea = lineas.obtener(i);
            inventario.aumentarStock(linea.getProducto(), linea.getCantidad());
        }
        terminal.liberar();
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
        TerminalCarga terminal = buscarTerminalPorNumero(numeroTerminal);
        if (terminal == null) {
            throw new IllegalArgumentException("No existe la terminal " + numeroTerminal);
        }

        OperacionLogistica operacion = terminal.getOperacionActual();
        if (operacion == null || operacion.getTipoOperacion() != TipoOperacion.CARGA) {
            throw new IllegalStateException("La terminal " + numeroTerminal + " no tiene una carga en curso");
        }

        terminal.liberar();
    }

    /**
     * Busca una terminal que se encuentre libre.
     *
     * @return primera terminal libre encontrada
     */
    public TerminalCarga buscarTerminalLibre() {
        for (int i = 0; i < terminales.tamaño(); i++) {
            TerminalCarga terminal = terminales.obtener(i);
            if (terminal.getEstado() == EstadoTerminal.LIBRE) {
                return terminal;
            }
        }
        return null;
    }

    /**
     * Cuenta cuántas unidades hay en total en el inventario, sumando el stock
     * de todos los productos registrados.
     *
     * @return cantidad total de unidades en inventario
     */
    public int cantidadTotalUnidadesEnInventario() {
        int total = 0;
        ListaArray<ItemInventario> items = inventario.getItems();
        for (int i = 0; i < items.tamaño(); i++) {
            total += items.obtener(i).getStock();
        }
        return total;
    }

    /**
     * Cuenta cuántas terminales se encuentran en un estado determinado.
     *
     * @param estado estado a contar
     * @return cantidad de terminales en ese estado
     */
    public int contarTerminalesPorEstado(EstadoTerminal estado) {
        int contador = 0;
        for (int i = 0; i < terminales.tamaño(); i++) {
            if (terminales.obtener(i).getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Obtiene el inventario del almacén.
     *
     * @return inventario
     */
    public Inventario getInventario() {
        return inventario;
    }

    /**
     * Obtiene las terminales del almacén.
     *
     * @return lista de terminales
     */
    public ListaArray<TerminalCarga> getTerminales() {
        return terminales;
    }

    /**
     * Busca los productos cuyo stock actual está por debajo de un umbral dado.
     *
     * @param umbral cantidad límite para considerar el stock como bajo
     * @return ítems de inventario con stock por debajo del umbral
     */
    public ListaSimple<ItemInventario> productosConStockBajo(int umbral) {
        ListaSimple<ItemInventario> resultado = new ListaSimple<>();
        ListaArray<ItemInventario> items = inventario.getItems();
        for (int i = 0; i < items.tamaño(); i++) {
            ItemInventario item = items.obtener(i);
            if (item.getStock() < umbral) {
                resultado.agregar(item);
            }
        }
        return resultado;
    }

    /**
     * Busca, entre las entregas pendientes, la primera correspondiente a un proveedor.
     *
     * @param proveedorId identificador del proveedor buscado
     * @return entrega pendiente de ese proveedor, o null si no hay ninguna
     */
    public EntregaProveedor buscarEntregaPendientePorProveedor(String proveedorId) {
        for (int i = 0; i < entregasPendientes.tamaño(); i++) {
            EntregaProveedor entrega = entregasPendientes.obtener(i);
            if (entrega.getProveedor().getId().equals(proveedorId)) {
                return entrega;
            }
        }
        return null;
    }

    /**
     * Busca, entre los pedidos pendientes, el primero correspondiente a una sucursal.
     *
     * @param sucursalId identificador de la sucursal buscada
     * @return pedido pendiente de esa sucursal, o null si no hay ninguno
     */
    public PedidoReabastecimiento buscarPedidoPendientePorSucursal(String sucursalId) {
        for (int i = 0; i < pedidosPendientes.tamaño(); i++) {
            PedidoReabastecimiento pedido = pedidosPendientes.obtener(i);
            if (pedido.getSucursal().getId().equals(sucursalId)) {
                return pedido;
            }
        }
        return null;
    }
}