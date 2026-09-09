package ucu.edu.aed.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.model.*;
import ucu.edu.aed.structures.linear.ListaArray;

import static org.junit.jupiter.api.Assertions.*;

/** Escenarios completos sobre las estructuras reales de los cinco integrantes. */
class IntegracionHito2Test {
    private AlmacenLogistico almacen;
    private Producto producto;
    private TerminalCarga terminal;

    @BeforeEach
    void preparar() {
        almacen = new AlmacenLogistico();
        producto = new Producto("P", "Producto", "", 2);
        terminal = new TerminalCarga(1);
        almacen.registrarTerminal(terminal);
        almacen.agregarSector("", new Sector("A", "Zona A", TipoSector.ZONA, 20));
        almacen.agregarSector("A", new Sector("1", "Posicion 1", TipoSector.POSICION, 10));
        almacen.agregarSector("A", new Sector("2", "Posicion 2", TipoSector.POSICION, 10));
        almacen.agregarSector("", new Sector("B", "Zona B", TipoSector.ZONA, 20));
        almacen.agregarSector("B", new Sector("1", "Posicion B", TipoSector.POSICION, 20));
        almacen.registrarProducto(producto, 0);
    }

    private EntregaProveedor entrega(String id, Producto p, int cantidad) {
        EntregaProveedor entrega = new EntregaProveedor(id, new Proveedor("V", "Proveedor"));
        entrega.agregarLinea(new LineaProducto(p, cantidad));
        return entrega;
    }

    private void recibir(int cantidad) {
        almacen.registrarLlegadaProveedor(entrega("E", producto, cantidad));
        assertSame(terminal, almacen.asignarProximaEntrega());
        almacen.finalizarDescarga(1);
    }

    private PedidoReabastecimiento pedido(String id, int prioridad, int cantidad) {
        PedidoReabastecimiento pedido = new PedidoReabastecimiento(id,
                new Sucursal(id, "Sucursal", prioridad));
        pedido.agregarLinea(new LineaProducto(producto, cantidad));
        almacen.registrarPedidoReabastecimiento(pedido);
        return pedido;
    }

    private void verificarStock(int esperado) {
        ItemInventario item = almacen.buscarProducto("P");
        assertEquals(esperado, item.getStock());
        assertTrue(item.stockCoincideConUbicaciones());
        assertEquals(esperado * 2, almacen.obtenerOcupacionSector(""));
    }

    @Test
    void recepcionDistribuyeYComparteUbicacionesConDeposito() {
        recibir(8);
        verificarStock(8);
        assertEquals(10, almacen.obtenerOcupacionSector("A.1"));
        assertEquals(6, almacen.obtenerOcupacionSector("A.2"));
        assertSame(almacen.buscarProducto("P").buscarUbicacion(almacen.buscarSector("A.1")),
                almacen.obtenerMercaderiaSector("A.1").obtener(0));
        assertTrue(terminal.estaLibre());
    }

    @Test
    void descargaSinCapacidadNoModificaStockNiLiberaTerminal() {
        recibir(3);
        EntregaProveedor entrega = entrega("Grande", producto, 18);
        almacen.registrarLlegadaProveedor(entrega);
        almacen.asignarProximaEntrega();
        assertThrows(IllegalStateException.class, () -> almacen.finalizarDescarga(1));
        verificarStock(3);
        assertSame(entrega, terminal.getOperacionActual());
    }

    @Test
    void descargaConSegundaLineaInvalidaEsAtomica() {
        EntregaProveedor entrega = entrega("Mixta", producto, 2);
        entrega.agregarLinea(new LineaProducto(new Producto("X", "Desconocido", ""), 1));
        almacen.registrarLlegadaProveedor(entrega);
        almacen.asignarProximaEntrega();
        assertThrows(IllegalStateException.class, () -> almacen.finalizarDescarga(1));
        verificarStock(0);
        assertSame(entrega, terminal.getOperacionActual());
    }

    @Test
    void lineasRepetidasReservanCapacidadAcumuladaAntesDeDescargar() {
        EntregaProveedor entrega = entrega("Repetida", producto, 12);
        entrega.agregarLinea(new LineaProducto(producto, 9));
        almacen.registrarLlegadaProveedor(entrega);
        almacen.asignarProximaEntrega();
        assertThrows(IllegalStateException.class, () -> almacen.finalizarDescarga(1));
        verificarStock(0);
        assertSame(entrega, terminal.getOperacionActual());
    }

    @Test
    void despachoSinTerminalNoRetiraStockNiPedido() {
        recibir(4);
        pedido("Q", 1, 2);
        almacen.deshabilitarTerminal(1);
        assertNull(almacen.despacharProximoPedido());
        verificarStock(4);
        assertEquals(1, almacen.cantidadPedidosPendientes());
        almacen.habilitarTerminal(1);
        assertNotNull(almacen.despacharProximoPedido());
        verificarStock(2);
    }

    @Test
    void recepcionUsaDimensionesDelCatalogoAunqueLaLineaTraigaOtraInstancia() {
        Producto mismaIdentidad = new Producto("P", "Otro nombre", "", 1);
        almacen.registrarLlegadaProveedor(entrega("E", mismaIdentidad, 8));
        almacen.asignarProximaEntrega();
        almacen.finalizarDescarga(1);
        verificarStock(8);
        assertEquals(10, almacen.obtenerOcupacionSector("A.1"));
    }

    @Test
    void entregasSiguenFIFO() {
        EntregaProveedor primera = entrega("1", producto, 1);
        EntregaProveedor segunda = entrega("2", producto, 1);
        almacen.registrarLlegadaProveedor(primera);
        almacen.registrarLlegadaProveedor(segunda);
        almacen.asignarProximaEntrega();
        assertSame(primera, terminal.getOperacionActual());
        almacen.finalizarDescarga(1);
        almacen.asignarProximaEntrega();
        assertSame(segunda, terminal.getOperacionActual());
    }

    @Test
    void despachoConsolidaLineasYPickingSigueDFS() {
        recibir(12);
        PedidoReabastecimiento pedido = pedido("Q", 10, 4);
        pedido.agregarLinea(new LineaProducto(producto, 7));
        ListaArray<PasoPicking> pasos = almacen.generarRecorridoPicking(pedido);
        verificarStock(12); // Consultar el recorrido no retira mercaderia.
        assertEquals(3, pasos.tamaño());
        assertEquals("A.1", pasos.obtener(0).getRutaPosicion());
        assertEquals("A.2", pasos.obtener(1).getRutaPosicion());
        assertEquals("B.1", pasos.obtener(2).getRutaPosicion());
        assertSame(terminal, almacen.despacharProximoPedido());
        verificarStock(1);
        assertTrue(almacen.obtenerMercaderiaSector("A").esVacio());
        assertEquals(0, almacen.cantidadPedidosPendientes());
        assertSame(pedido, terminal.getOperacionActual());
        almacen.finalizarCarga(1);
        assertTrue(terminal.estaLibre());
    }

    @Test
    void pedidoSinStockCompletoNoRetiraNiDesencola() {
        recibir(4);
        PedidoReabastecimiento pedido = pedido("Q", 10, 3);
        pedido.agregarLinea(new LineaProducto(producto, 3));
        assertNull(almacen.despacharProximoPedido());
        verificarStock(4);
        assertEquals(1, almacen.cantidadPedidosPendientes());
        assertTrue(terminal.estaLibre());
    }

    @Test
    void subirYBajarPrioridadConservaFIFOEnEmpates() {
        recibir(8);
        PedidoReabastecimiento primero = pedido("1", 10, 1);
        PedidoReabastecimiento segundo = pedido("2", 5, 1);
        almacen.modificarPrioridadPedido("2", 20);
        almacen.modificarPrioridadPedido("2", 10);
        assertSame(primero, almacen.despacharProximoPedido().getOperacionActual());
        almacen.finalizarCarga(1);
        assertSame(segundo, almacen.despacharProximoPedido().getOperacionActual());
    }

    @Test
    void prioridadMayorPasaAlFrenteYConsultaSucursalFuncionaConHeap() {
        recibir(4);
        pedido("1", 10, 1);
        PedidoReabastecimiento segundo = pedido("2", 5, 1);
        almacen.modificarPrioridadPedido("2", 20);
        assertSame(segundo, almacen.buscarPedidoPendientePorSucursal("2"));
        assertSame(segundo, almacen.despacharProximoPedido().getOperacionActual());
        assertThrows(IllegalArgumentException.class, () -> almacen.modificarPrioridadPedido("2", 2));
    }

    @Test
    void inhabilitacionReubicaTodoYExcluyeDescendientesEnFuturasDescargas() {
        recibir(8);
        almacen.inhabilitarSector("A");
        verificarStock(8);
        assertFalse(almacen.buscarSector("A").estaHabilitado());
        assertEquals(0, almacen.obtenerOcupacionSector("A"));
        assertEquals(16, almacen.obtenerOcupacionSector("B"));
        recibir(1);
        verificarStock(9);
        assertEquals(0, almacen.obtenerOcupacionSector("A"));
    }

    @Test
    void inhabilitacionSinCapacidadConservaHabilitacionYContenido() {
        recibir(16);
        assertThrows(IllegalStateException.class, () -> almacen.inhabilitarSector("A"));
        verificarStock(16);
        assertTrue(almacen.buscarSector("A").estaHabilitado());
        assertEquals(20, almacen.obtenerOcupacionSector("A"));
        assertEquals(12, almacen.obtenerOcupacionSector("B"));
    }

    @Test
    void reubicacionConAncestroLlenoNoCuentaDosVecesLaMercaderia() {
        recibir(5);
        almacen.agregarSector("", new Sector("C", "Zona C", TipoSector.ZONA, 10));
        almacen.moverSector("A.1", "C");
        almacen.agregarSector("C", new Sector("2", "Destino", TipoSector.POSICION, 10));
        almacen.inhabilitarSector("A");
        almacen.inhabilitarSector("B");
        almacen.inhabilitarSector("C.1");
        verificarStock(5);
        assertEquals(0, almacen.obtenerOcupacionSector("C.1"));
        assertEquals(10, almacen.obtenerOcupacionSector("C.2"));
    }

    @Test
    void moverSectorPreservaIdentidadYActualizaPicking() {
        recibir(5);
        Sector posicion = almacen.buscarSector("A.1");
        almacen.agregarSector("", new Sector("C", "Zona C", TipoSector.ZONA, 10));
        almacen.moverSector("A.1", "C");
        assertNull(almacen.buscarSector("A.1"));
        assertSame(posicion, almacen.buscarSector("C.1"));
        verificarStock(5);
        PedidoReabastecimiento pedido = pedido("Q", 1, 2);
        assertEquals("C.1", almacen.generarRecorridoPicking(pedido).obtener(0).getRutaPosicion());
    }

    @Test
    void altaConStockInicialEsUbicadaYFalloNoRegistraProducto() {
        Producto nuevo = new Producto("N", "Nuevo", "", 2);
        almacen.registrarProducto(nuevo, 6);
        assertTrue(almacen.buscarProducto("N").stockCoincideConUbicaciones());
        assertEquals(12, almacen.obtenerOcupacionSector(""));
        assertThrows(IllegalStateException.class,
                () -> almacen.registrarProducto(new Producto("X", "Grande", ""), 50));
        assertNull(almacen.buscarProducto("X"));
        assertEquals(12, almacen.obtenerOcupacionSector(""));
    }

    @Test
    void consultasOrdenadasConservanUbicacionesSinExponerCantidadOriginal() {
        recibir(3);
        almacen.registrarProducto(new Producto("A", "Primero", ""), 0);
        ListaArray<ItemInventario> listado = almacen.listarInventarioOrdenado();
        assertEquals("A", listado.obtener(0).getProducto().getCodigo());
        ItemInventario copia = listado.obtener(1);
        assertTrue(copia.stockCoincideConUbicaciones());
        copia.retirarDeUbicacion(almacen.buscarSector("A.1"), 1);
        verificarStock(3);
    }

    @Test
    void stockUbicadoNoSePuedeAlterarMedianteApiAntigua() {
        recibir(3);
        assertThrows(IllegalStateException.class, () -> almacen.getInventario().aumentarStock(producto, 1));
        assertThrows(IllegalStateException.class, () -> almacen.getInventario().disminuirStock(producto, 1));
        verificarStock(3);
    }
}
