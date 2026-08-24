package ucu.edu.aed.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ucu.edu.aed.model.*;
import ucu.edu.aed.structures.ListaSimple;

import static org.junit.jupiter.api.Assertions.*;

class AlmacenLogisticoTest {
    private AlmacenLogistico almacen;
    private TerminalCarga terminal1;
    private TerminalCarga terminal2;
    private Producto productoPrueba;
    private Proveedor proveedorPrueba;

    @BeforeEach
    void setUp() {
        almacen = new AlmacenLogistico();
        terminal1 = new TerminalCarga(1);
        terminal2 = new TerminalCarga(2);
        productoPrueba = new Producto("1", "prueba", "es una prueba");
        proveedorPrueba = new Proveedor("1", "prueba");
    }

    @Test
    void registrarTerminal() {
        almacen.registrarTerminal(terminal1);
        assertTrue(almacen.getTerminales().contiene(terminal1));
    }

    @Test
    void registrarTerminalDuplicada() {
        almacen.registrarTerminal(terminal1);
        TerminalCarga duplicada = new TerminalCarga(1);
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                almacen.registrarTerminal(duplicada);
            }
        });
    }

    @Test
    void buscarTerminalLibre() {
        almacen.registrarTerminal(terminal1);
        assertEquals(terminal1, almacen.buscarTerminalLibre());
    }

    @Test
    void buscarTerminalLibreSinTermialLibre() {
        assertNull(almacen.buscarTerminalLibre());
    }

    @Test
    void registrarProducto() {
        almacen.registrarProducto(productoPrueba, 10);
        assertEquals(10, almacen.getInventario().obtenerStock(productoPrueba.getCodigo()));
    }

    @Test
    void registrarLlegadaProveedor() {
        EntregaProveedor entrega = new EntregaProveedor("1", proveedorPrueba);
        almacen.registrarLlegadaProveedor(entrega);

        assertEquals(entrega, almacen.buscarEntregaPendientePorProveedor(proveedorPrueba.getId()));
    }

    @Test
    void asignarProximaEntregaConTerminalLibre() {
        almacen.registrarTerminal(terminal1);
        EntregaProveedor entrega = new EntregaProveedor("1", proveedorPrueba);
        almacen.registrarLlegadaProveedor(entrega);

        TerminalCarga asignada = almacen.asignarProximaEntrega();

        assertEquals(terminal1, asignada);
        assertEquals(entrega, terminal1.getOperacionActual());
        assertFalse(terminal1.estaLibre());
    }

    @Test
    void asignarProximaEntregaSinTerminalLibreNoSacaDeLaCola() {
        EntregaProveedor entrega = new EntregaProveedor("1", proveedorPrueba);
        almacen.registrarLlegadaProveedor(entrega);

        TerminalCarga asignada = almacen.asignarProximaEntrega();

        assertNull(asignada);
        assertEquals(entrega, almacen.buscarEntregaPendientePorProveedor(proveedorPrueba.getId()));
    }

    @Test
    void finalizarDescarga() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarProducto(productoPrueba, 5);

        EntregaProveedor entrega = new EntregaProveedor("1", proveedorPrueba);
        entrega.agregarLinea(new LineaEntrega(productoPrueba, 10));
        almacen.registrarLlegadaProveedor(entrega);
        almacen.asignarProximaEntrega();

        almacen.finalizarDescarga(terminal1.getNumero());

        assertEquals(15, almacen.getInventario().obtenerStock(productoPrueba.getCodigo()));
        assertTrue(terminal1.estaLibre());
    }

    @Test
    void finalizarDescargaTerminalInexistente() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                almacen.finalizarDescarga(99);
            }
        });
    }

    @Test
    void finalizarDescargaSinDescargaEnCurso() {
        almacen.registrarTerminal(terminal1);
        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() {
                almacen.finalizarDescarga(terminal1.getNumero());
            }
        });
    }

    @Test
    void registrarPedidoReabastecimiento() {
        Sucursal unaSucursal = new Sucursal("1", "prueba", 10);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento("21", unaSucursal);
        almacen.registrarPedidoReabastecimiento(pedido);
        assertEquals(pedido, almacen.buscarPedidoPendientePorSucursal("1"));
    }

    @Test
    void despacharProximoPedidoConStock() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarProducto(productoPrueba, 10);

        Sucursal sucursal = new Sucursal("1", "prueba", 10);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento("21", sucursal);
        pedido.agregarLinea(new LineaPedido(productoPrueba, 4));
        almacen.registrarPedidoReabastecimiento(pedido);

        TerminalCarga asignada = almacen.despacharProximoPedido();

        assertEquals(terminal1, asignada);
        assertEquals(6, almacen.getInventario().obtenerStock(productoPrueba.getCodigo()));
        assertEquals(pedido, terminal1.getOperacionActual());
        assertNull(almacen.buscarPedidoPendientePorSucursal("1"));
    }

    @Test
    void despacharProximoPedidoSinStockNoDescuentaNiSacaDeLaCola() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarProducto(productoPrueba, 2);

        Sucursal sucursal = new Sucursal("1", "prueba", 10);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento("21", sucursal);
        pedido.agregarLinea(new LineaPedido(productoPrueba, 4));
        almacen.registrarPedidoReabastecimiento(pedido);

        TerminalCarga resultado = almacen.despacharProximoPedido();

        assertNull(resultado);
        assertEquals(2, almacen.getInventario().obtenerStock(productoPrueba.getCodigo()));
        assertEquals(pedido, almacen.buscarPedidoPendientePorSucursal("1"));
    }

    @Test
    void despacharPedidoConProductoRepetidoYSinStockTotalNoDescuentaParcialmente() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarProducto(productoPrueba, 6);

        Sucursal sucursal = new Sucursal("1", "prueba", 10);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento("21", sucursal);
        pedido.agregarLinea(new LineaPedido(productoPrueba, 4));
        pedido.agregarLinea(new LineaPedido(productoPrueba, 4));
        almacen.registrarPedidoReabastecimiento(pedido);

        TerminalCarga resultado = almacen.despacharProximoPedido();

        assertNull(resultado);
        assertEquals(6, almacen.getInventario().obtenerStock(productoPrueba.getCodigo()));
        assertEquals(pedido, almacen.buscarPedidoPendientePorSucursal("1"));
        assertTrue(terminal1.estaLibre());
    }

    @Test
    void despacharProximoPedidoSinTerminalLibre() {
        almacen.registrarProducto(productoPrueba, 10);

        Sucursal sucursal = new Sucursal("1", "prueba", 10);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento("21", sucursal);
        pedido.agregarLinea(new LineaPedido(productoPrueba, 4));
        almacen.registrarPedidoReabastecimiento(pedido);

        assertNull(almacen.despacharProximoPedido());
    }

    @Test
    void despacharPedidosPriorizaSucursalConMasClientes() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarProducto(productoPrueba, 20);

        PedidoReabastecimiento prioridadBaja = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 50));
        prioridadBaja.agregarLinea(new LineaPedido(productoPrueba, 2));

        PedidoReabastecimiento prioridadAlta = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 200));
        prioridadAlta.agregarLinea(new LineaPedido(productoPrueba, 2));

        almacen.registrarPedidoReabastecimiento(prioridadBaja);
        almacen.registrarPedidoReabastecimiento(prioridadAlta);

        TerminalCarga asignada = almacen.despacharProximoPedido();

        assertEquals(prioridadAlta, asignada.getOperacionActual());
        assertEquals(prioridadBaja, almacen.buscarPedidoPendientePorSucursal("S1"));
        assertNull(almacen.buscarPedidoPendientePorSucursal("S2"));
    }

    @Test
    void pedidosConMismaPrioridadMantienenFIFO() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarProducto(productoPrueba, 20);

        PedidoReabastecimiento primero = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 100));
        primero.agregarLinea(new LineaPedido(productoPrueba, 2));

        PedidoReabastecimiento segundo = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 100));
        segundo.agregarLinea(new LineaPedido(productoPrueba, 2));

        almacen.registrarPedidoReabastecimiento(primero);
        almacen.registrarPedidoReabastecimiento(segundo);

        TerminalCarga asignada = almacen.despacharProximoPedido();

        assertEquals(primero, asignada.getOperacionActual());
        assertEquals(segundo, almacen.buscarPedidoPendientePorSucursal("S2"));
    }

    @Test
    void buscarTerminalLibreIgnoraTerminalDeshabilitada() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarTerminal(terminal2);
        terminal1.deshabilitar();

        assertEquals(terminal2, almacen.buscarTerminalLibre());
    }

    @Test
    void finalizarCarga() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarProducto(productoPrueba, 10);

        Sucursal sucursal = new Sucursal("1", "prueba", 10);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento("21", sucursal);
        pedido.agregarLinea(new LineaPedido(productoPrueba, 4));
        almacen.registrarPedidoReabastecimiento(pedido);
        almacen.despacharProximoPedido();

        almacen.finalizarCarga(terminal1.getNumero());

        assertTrue(terminal1.estaLibre());
    }

    @Test
    void finalizarCargaTerminalInexistente() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                almacen.finalizarCarga(99);
            }
        });
    }

    @Test
    void finalizarCargaSinCargaEnCurso() {
        almacen.registrarTerminal(terminal1);
        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() {
                almacen.finalizarCarga(terminal1.getNumero());
            }
        });
    }

    @Test
    void cantidadTotalUnidadesEnInventario() {
        almacen.registrarProducto(productoPrueba, 7);
        assertEquals(7, almacen.cantidadTotalUnidadesEnInventario());
    }

    @Test
    void cantidadTotalUnidadesEnInventarioSinProductos() {
        assertEquals(0, almacen.cantidadTotalUnidadesEnInventario());
    }

    @Test
    void contarTerminalesPorEstado() {
        almacen.registrarTerminal(terminal1);
        almacen.registrarTerminal(terminal2);
        assertEquals(2, almacen.contarTerminalesPorEstado(EstadoTerminal.LIBRE));
        assertEquals(0, almacen.contarTerminalesPorEstado(EstadoTerminal.OCUPADA));
    }

    @Test
    void getInventario() {
        almacen.getInventario().registrarProducto(productoPrueba, 3);
        assertEquals(productoPrueba.getCodigo(), almacen.getInventario().buscarItem("1").getProducto().getCodigo());
    }

    @Test
    void getTerminales() {
        assertTrue(almacen.getTerminales().esVacio());
        almacen.registrarTerminal(terminal1);
        almacen.registrarTerminal(terminal2);
        assertEquals(2, almacen.getTerminales().tamaño());
    }

    @Test
    void productosConStockBajo() {
        almacen.registrarProducto(productoPrueba, 3);

        ListaSimple<ItemInventario> resultado = almacen.productosConStockBajo(5);
        assertEquals(1, resultado.tamaño());
        assertEquals(productoPrueba.getCodigo(), resultado.obtener(0).getProducto().getCodigo());
    }

    @Test
    void buscarEntregaPendientePorProveedorLaEncuentra() {
        EntregaProveedor entrega = new EntregaProveedor("1", proveedorPrueba);
        almacen.registrarLlegadaProveedor(entrega);
        EntregaProveedor encontrada = almacen.buscarEntregaPendientePorProveedor(proveedorPrueba.getId());
        assertEquals(entrega, encontrada);
    }

    @Test
    void buscarEntregaPendientePorProveedorNoEncuentra() {
        assertNull(almacen.buscarEntregaPendientePorProveedor("Id no existe"));
    }

    @Test
    void buscarPedidoPendientePorSucursalLoEncuentra() {
        Sucursal sucursal = new Sucursal("1", "prueba", 10);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento("21", sucursal);
        almacen.registrarPedidoReabastecimiento(pedido);

        assertEquals(pedido, almacen.buscarPedidoPendientePorSucursal("1"));
    }

    @Test
    void buscarPedidoPendientePorSucursalNoEncuentra() {
        assertNull(almacen.buscarPedidoPendientePorSucursal("Id no existe"));
    }

    @Test
    void entregasDeProveedorMantienenFIFO() {
        almacen.registrarTerminal(terminal1);

        EntregaProveedor primera = new EntregaProveedor("E1", new Proveedor("P1", "Proveedor 1"));

        EntregaProveedor segunda = new EntregaProveedor("E2", new Proveedor("P2", "Proveedor 2"));

        almacen.registrarLlegadaProveedor(primera);
        almacen.registrarLlegadaProveedor(segunda);

        TerminalCarga asignada = almacen.asignarProximaEntrega();

        assertEquals(primera, asignada.getOperacionActual());
        assertEquals(
                segunda,
                almacen.buscarEntregaPendientePorProveedor("P2"));
    }
}