package ucu.edu.aed.structures.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ucu.edu.aed.model.PedidoReabastecimiento;
import ucu.edu.aed.model.Sucursal;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ColaPrioridadTest {

    private ColaPrioridad<PedidoReabastecimiento> cola;

    @BeforeEach
    void setUp() {
        Comparator<PedidoReabastecimiento> comparador =
                Comparator.comparingInt(PedidoReabastecimiento::getPrioridad);
        cola = new ColaPrioridad<>(comparador);
    }

    @Test
    void heapVacio() {
        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());

        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() {
                cola.frente();
            }
        });

        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() {
                cola.quitaDeCola();
            }
        });
    }

    @Test
    void unPedido() {
        PedidoReabastecimiento pedido = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 10));

        cola.poneEnCola(pedido);

        assertFalse(cola.esVacio());
        assertEquals(1, cola.tamaño());
        assertEquals(pedido, cola.frente());
    }

    @Test
    void multiplesPrioridades() {
        PedidoReabastecimiento bajo = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 10));
        PedidoReabastecimiento medio = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 50));
        PedidoReabastecimiento alto = new PedidoReabastecimiento(
                "P3", new Sucursal("S3", "Sucursal 3", 100));

        cola.poneEnCola(bajo);
        cola.poneEnCola(alto);
        cola.poneEnCola(medio);

        assertEquals(3, cola.tamaño());
        assertEquals(alto, cola.frente());
    }

    @Test
    void extraccionEnOrdenCorrecto() {
        PedidoReabastecimiento bajo = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 10));
        PedidoReabastecimiento medio = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 50));
        PedidoReabastecimiento alto = new PedidoReabastecimiento(
                "P3", new Sucursal("S3", "Sucursal 3", 100));

        cola.poneEnCola(medio);
        cola.poneEnCola(bajo);
        cola.poneEnCola(alto);

        assertEquals(alto, cola.quitaDeCola());
        assertEquals(medio, cola.quitaDeCola());
        assertEquals(bajo, cola.quitaDeCola());
        assertTrue(cola.esVacio());
    }

    @Test
    void empatesFIFO() {
        PedidoReabastecimiento primero = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 100));
        PedidoReabastecimiento segundo = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 100));

        cola.poneEnCola(primero);
        cola.poneEnCola(segundo);

        assertEquals(primero, cola.quitaDeCola());
        assertEquals(segundo, cola.quitaDeCola());
    }

    @Test
    void subirPrioridad() {
        PedidoReabastecimiento bajo = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 10));
        PedidoReabastecimiento alto = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 50));

        cola.poneEnCola(bajo);
        cola.poneEnCola(alto);
        assertEquals(alto, cola.frente());

        bajo.setPrioridad(100);
        cola.reordenar(bajo);

        assertEquals(bajo, cola.frente());
    }

    @Test
    void bajarPrioridad() {
        PedidoReabastecimiento alto = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 100));
        PedidoReabastecimiento bajo = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 10));

        cola.poneEnCola(alto);
        cola.poneEnCola(bajo);
        assertEquals(alto, cola.frente());

        alto.setPrioridad(1);
        cola.reordenar(alto);

        assertEquals(bajo, cola.frente());
    }

    @Test
    void prioridadDelMaximo() {
        PedidoReabastecimiento bajo = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 10));
        PedidoReabastecimiento alto = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 100));

        cola.poneEnCola(bajo);
        cola.poneEnCola(alto);

        assertEquals(100, cola.frente().getPrioridad());
    }

    @Test
    void vaciarYReutilizar() {
        cola.poneEnCola(new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 10)));
        cola.poneEnCola(new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 20)));

        cola.vaciar();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());

        PedidoReabastecimiento primero = new PedidoReabastecimiento(
                "P3", new Sucursal("S3", "Sucursal 3", 100));
        PedidoReabastecimiento segundo = new PedidoReabastecimiento(
                "P4", new Sucursal("S4", "Sucursal 4", 100));

        cola.poneEnCola(primero);
        cola.poneEnCola(segundo);

        assertEquals(primero, cola.quitaDeCola());
        assertEquals(segundo, cola.quitaDeCola());
    }

    @Test
    void busqueda() {
        PedidoReabastecimiento pedidoS1 = new PedidoReabastecimiento(
                "P1", new Sucursal("S1", "Sucursal 1", 10));
        PedidoReabastecimiento pedidoS2 = new PedidoReabastecimiento(
                "P2", new Sucursal("S2", "Sucursal 2", 50));

        cola.poneEnCola(pedidoS1);
        cola.poneEnCola(pedidoS2);

        PedidoReabastecimiento encontrado =
                cola.buscar(p -> p.getSucursal().getId().equals("S2"));
        assertEquals(pedidoS2, encontrado);

        PedidoReabastecimiento noEncontrado =
                cola.buscar(p -> p.getSucursal().getId().equals("Id no existe"));
        assertNull(noEncontrado);
    }

    @Test
    void casosNulos() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                new ColaPrioridad<PedidoReabastecimiento>(null);
            }
        });

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                cola.poneEnCola(null);
            }
        });

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                cola.buscar(null);
            }
        });

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                cola.reordenar(null);
            }
        });

        final PedidoReabastecimiento pedidoNoEncolado = new PedidoReabastecimiento(
                "P99", new Sucursal("S99", "Sucursal 99", 10));

        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() {
                cola.reordenar(pedidoNoEncolado);
            }
        });
    }
}