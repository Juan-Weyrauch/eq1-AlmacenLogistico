package ucu.edu.aed.service;

import ucu.edu.aed.model.*;
import ucu.edu.aed.structures.Cola;
import ucu.edu.aed.structures.ColaPrioridad;
import ucu.edu.aed.structures.ListaArray;
import ucu.edu.aed.structures.ListaSimple;

import java.util.Comparator;


public class AlmacenLogistico {

    private Inventario inventario;

    private ListaArray<TerminalCarga> terminales;

    private Cola<EntregaProveedor> entregasPendientes;

    private ColaPrioridad<PedidoReabastecimiento> pedidosPendientes;


    public AlmacenLogistico() {
        this.inventario = new Inventario();
        this.terminales = new ListaArray<>();
        this.entregasPendientes = new Cola<>();
        this.pedidosPendientes = new ColaPrioridad<>(
                Comparator.comparingInt(PedidoReabastecimiento::getPrioridad)
        );
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


    public void registrarTerminal(TerminalCarga terminal) {
        if (terminales.contiene(terminal)) {
            throw new IllegalArgumentException("Terminal ya registrada " + terminal.getNumero());
        }
        terminales.agregar(terminal);
    }


    public void registrarProducto(Producto producto, int stockInicial) {
        inventario.registrarProducto(producto, stockInicial);
    }

    public void registrarLlegadaProveedor(EntregaProveedor entrega) {

        entregasPendientes.agregar(entrega);
    }


    public TerminalCarga asignarProximaEntrega() {
        TerminalCarga terminal = buscarTerminalLibre();
        if (terminal == null) {
            return null;
        }
        EntregaProveedor entrega = entregasPendientes.quitaDeCola();
        terminal.asignarOperacion(entrega);
        return terminal;
    }

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

    public void registrarPedidoReabastecimiento(PedidoReabastecimiento pedido) {
        if(pedido == null){
            throw new IllegalArgumentException("El pedido no puede ser null");
        }
        pedidosPendientes.poneEnCola(pedido);
    }
    public TerminalCarga despacharProximoPedido() {
        if(pedidosPendientes.esVacio()){
            return null;
        }
        TerminalCarga terminal = buscarTerminalLibre();
        if(terminal == null){
            return null;
        }

        PedidoReabastecimiento pedido = pedidosPendientes.frente();
        ListaSimple<LineaPedido> lineas = pedido.getLineas();

        //Verifico que haya stock de todos los productos
        for(int i=0; i < lineas.tamaño(); i++){
            LineaPedido linea = lineas.obtener(i);
            if(!inventario.hayStock(linea.getProducto(), linea.getCantidad())){
                return null;
            }
        }

        //si hay stock de todos losproductos los disminuyo
        for (int i = 0; i < lineas.tamaño(); i++) {
            LineaPedido linea = lineas.obtener(i);
            inventario.disminuirStock(linea.getProducto(), linea.getCantidad());
        }

        pedidosPendientes.quitaDeCola();
        terminal.asignarOperacion(pedido);
        return terminal;
    }


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

    public TerminalCarga buscarTerminalLibre() {
        for (int i = 0; i < terminales.tamaño(); i++) {
            TerminalCarga terminal = terminales.obtener(i);
            if (terminal.getEstado() == EstadoTerminal.LIBRE) {
                return terminal;
            }
        }
        return null;
    }

    public int cantidadTotalUnidadesEnInventario() {
        int total = 0;
        ListaArray<ItemInventario> items = inventario.getItems();
        for (int i = 0; i < items.tamaño(); i++) {
            total += items.obtener(i).getStock();
        }
        return total;
    }

    public int contarTerminalesPorEstado(EstadoTerminal estado) {
        int contador = 0;
        for (int i = 0; i < terminales.tamaño(); i++) {
            if (terminales.obtener(i).getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public ListaArray<TerminalCarga> getTerminales() {
        return terminales;
    }

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

    public EntregaProveedor buscarEntregaPendientePorProveedor(String proveedorId) {
        for (int i = 0; i < entregasPendientes.tamaño(); i++) {
            EntregaProveedor entrega = entregasPendientes.obtener(i);
            if (entrega.getProveedor().getId().equals(proveedorId)) {
                return entrega;
            }
        }
        return null;
    }

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