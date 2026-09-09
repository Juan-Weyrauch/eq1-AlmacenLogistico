package ucu.edu.aed.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DepositoIntegracionTest {
    private Deposito preparar() {
        Deposito d = new Deposito();
        d.agregarSector("", new Sector("A", "A", TipoSector.ZONA, 10));
        d.agregarSector("A", new Sector("P", "P", TipoSector.POSICION, 10));
        d.agregarUbicacion(new UbicacionStock(new Producto("X", "X", ""),
                d.buscarSectorPorRuta("A.P"), 10));
        return d;
    }

    @Test
    void movimientoValidaTodosLosAncestrosAntesDeCambiarElArbol() {
        Deposito d = preparar();
        d.agregarSector("", new Sector("B", "B", TipoSector.ZONA, 5));
        d.agregarSector("B", new Sector("C", "C", TipoSector.ZONA, 20));
        assertThrows(IllegalArgumentException.class, () -> d.moverSector("A.P", "B.C"));
        assertNotNull(d.buscarSectorPorRuta("A.P"));
        assertNull(d.buscarSectorPorRuta("B.C.P"));
        assertEquals(10, d.obtenerOcupacion("A"));
    }

    @Test
    void movimientoDentroDelMismoAncestroLlenoEsValido() {
        Deposito d = preparar();
        d.agregarSector("A", new Sector("C", "C", TipoSector.ZONA, 10));
        d.moverSector("A.P", "A.C");
        assertEquals(10, d.obtenerOcupacion("A.C.P"));
        assertEquals(10, d.obtenerOcupacion("A"));
    }

    @Test
    void nombresDuplicadosCiclosYRaizSeRechazanSinPerderContenido() {
        Deposito d = preparar();
        assertThrows(IllegalArgumentException.class,
                () -> d.agregarSector("A", new Sector("P", "Otro", TipoSector.POSICION, 10)));
        assertThrows(IllegalArgumentException.class, () -> d.moverSector("", "A"));
        d.agregarSector("A", new Sector("C", "C", TipoSector.ZONA, 10));
        assertThrows(IllegalArgumentException.class, () -> d.moverSector("A", "A.C"));
        d.agregarSector("", new Sector("B", "B", TipoSector.ZONA, 20));
        d.agregarSector("B", new Sector("P", "P", TipoSector.POSICION, 10));
        assertThrows(IllegalArgumentException.class, () -> d.moverSector("A.P", "B"));
        assertEquals(10, d.obtenerOcupacion("A"));
    }

    @Test
    void sectoresDeshabilitadosExcluyenTodaSuRama() {
        Deposito d = preparar();
        d.buscarSectorPorRuta("A").deshabilitar();
        assertTrue(d.listarPosicionesHabilitadas().esVacio());
        assertThrows(IllegalArgumentException.class,
                () -> d.agregarSector("A", new Sector("Q", "Q", TipoSector.POSICION, 10)));
    }
}
