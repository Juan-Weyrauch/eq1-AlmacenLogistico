package ucu.edu.aed.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SectorTest {

    @Test
    void constructorDebeInicializarCamposYHabilitadoPorDefecto() {
        Sector sector = new Sector("3", "Zona 3", TipoSector.ZONA, 100);

        assertEquals("3", sector.getCodigoLocal());
        assertEquals("Zona 3", sector.getNombre());
        assertEquals(TipoSector.ZONA, sector.getTipo());
        assertEquals(100, sector.getCapacidad());
        assertTrue(sector.estaHabilitado());
    }

    @Test
    void constructorDebeRechazarCodigoLocalVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Sector("", "Zona 3", TipoSector.ZONA, 100));
    }

    @Test
    void constructorDebeRechazarNombreVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Sector("3", " ", TipoSector.ZONA, 100));
    }

    @Test
    void constructorDebeRechazarTipoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Sector("3", "Zona 3", null, 100));
    }

    @Test
    void constructorDebeRechazarCapacidadNegativa() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Sector("3", "Zona 3", TipoSector.ZONA, -1));
    }

    @Test
    void constructorDebePermitirCapacidadCero() {
        Sector sector = new Sector("3", "Zona 3", TipoSector.ZONA, 0);

        assertEquals(0, sector.getCapacidad());
    }

    @Test
    void deshabilitarYHabilitarDebenCambiarElEstado() {
        Sector sector = new Sector("3", "Zona 3", TipoSector.ZONA, 100);

        sector.deshabilitar();
        assertFalse(sector.estaHabilitado());

        sector.habilitar();
        assertTrue(sector.estaHabilitado());
    }

    @Test
    void agregarUbicacionStockDebeRechazarUbicacionNula() {
        Sector sector = new Sector("5", "Posicion 5", TipoSector.POSICION, 100);

        assertThrows(
                IllegalArgumentException.class,
                () -> sector.agregarUbicacionStock(null));
    }

    @Test
    void agregarYRemoverUbicacionStockDebenActualizarLaLista() {
        Sector sector = new Sector("5", "Posicion 5", TipoSector.POSICION, 100);
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        UbicacionStock ubicacion = new UbicacionStock(producto, sector, 10);

        sector.agregarUbicacionStock(ubicacion);
        assertEquals(1, sector.obtenerUbicacionesStock().tamaño());

        boolean removido = sector.removerUbicacionStock(ubicacion);
        assertTrue(removido);
        assertTrue(sector.obtenerUbicacionesStock().esVacio());
    }

    @Test
    void removerUbicacionStockDebeRetornarFalseCuandoNoExiste() {
        Sector sector = new Sector("5", "Posicion 5", TipoSector.POSICION, 100);
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        UbicacionStock ubicacion = new UbicacionStock(producto, sector, 10);

        assertFalse(sector.removerUbicacionStock(ubicacion));
    }
}
