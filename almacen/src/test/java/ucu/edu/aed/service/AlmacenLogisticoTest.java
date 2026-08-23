package ucu.edu.aed.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.model.TerminalCarga;

import static org.junit.jupiter.api.Assertions.*;

class AlmacenLogisticoTest {
    private AlmacenLogistico almacen;
    private TerminalCarga terminal1;
    private TerminalCarga terminal2;

    @BeforeEach
    void setUp() {
        almacen = new AlmacenLogistico();
        terminal1 = new TerminalCarga(1);
        terminal2 = new TerminalCarga(2);

    }

    @Test
    void registrarTerminal() {
    }

    @Test
    void registrarProducto() {
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
    void buscarTerminalLibre() {
    }

    @Test
    void cantidadTotalUnidadesEnInventario() {
    }

    @Test
    void contarTerminalesPorEstado() {
    }

    @Test
    void getInventario() {
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