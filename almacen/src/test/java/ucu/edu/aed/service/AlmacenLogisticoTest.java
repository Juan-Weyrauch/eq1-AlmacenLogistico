package ucu.edu.aed.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.model.Producto;
import ucu.edu.aed.model.TerminalCarga;

import static org.junit.jupiter.api.Assertions.*;

class AlmacenLogisticoTest {
    private AlmacenLogistico almacen;
    private TerminalCarga terminal1;
    private TerminalCarga terminal2;

    private Producto productoPrueba;

    @BeforeEach
    void setUp() {
        almacen = new AlmacenLogistico();
        terminal1 = new TerminalCarga(1);
        terminal2 = new TerminalCarga(2);
        productoPrueba = new Producto("1","prueba","es una prueba");

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
        assertThrows(IllegalArgumentException.class, () -> almacen.registrarTerminal(duplicada));
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
    }

    @Test
    void asignarProximaEntrega() {
    }

    @Test
    void finalizarDescarga() {
    }

    @Test
    void registrarPedidoReabastecimiento() {
    }

    @Test
    void despacharProximoPedido() {
    }

    @Test
    void finalizarCarga() {
    }

    @Test
    void cantidadTotalUnidadesEnInventario() {
    }

    @Test
    void contarTerminalesPorEstado() {
    }

    @Test
    void getInventario() {
        almacen.getInventario().registrarProducto(productoPrueba, 3);
        assertEquals(productoPrueba.getCodigo(),almacen.getInventario().buscarItem("1").getProducto().getCodigo());
    }


    @Test
    void getTerminales() {
    }

    @Test
    void productosConStockBajo() {
    }

    @Test
    void buscarEntregaPendientePorProveedor() {
    }

    @Test
    void buscarPedidoPendientePorSucursal() {
    }
}