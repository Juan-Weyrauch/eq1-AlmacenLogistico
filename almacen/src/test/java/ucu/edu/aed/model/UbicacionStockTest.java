package ucu.edu.aed.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UbicacionStockTest {

    @Test
    void constructorDebeInicializarCampos() {
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        Sector posicion = new Sector("5", "Posicion 5", TipoSector.POSICION, 100);

        UbicacionStock ubicacion = new UbicacionStock(producto, posicion, 10);

        assertEquals(producto, ubicacion.getProducto());
        assertEquals(posicion, ubicacion.getPosicion());
        assertEquals(10, ubicacion.getCantidad());
    }

    @Test
    void constructorDebeRechazarProductoNulo() {
        Sector posicion = new Sector("5", "Posicion 5", TipoSector.POSICION, 100);

        assertThrows(
                IllegalArgumentException.class,
                () -> new UbicacionStock(null, posicion, 10));
    }

    @Test
    void constructorDebeRechazarPosicionNula() {
        Producto producto = new Producto("P001", "Arroz", "desc", 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> new UbicacionStock(producto, null, 10));
    }

    @Test
    void constructorDebeRechazarCantidadNoPositiva() {
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        Sector posicion = new Sector("5", "Posicion 5", TipoSector.POSICION, 100);

        assertThrows(
                IllegalArgumentException.class,
                () -> new UbicacionStock(producto, posicion, 0));
    }

    @Test
    void aumentarCantidadDebeSumar() {
        UbicacionStock ubicacion = crearUbicacion();

        ubicacion.aumentarCantidad(5);

        assertEquals(15, ubicacion.getCantidad());
    }

    @Test
    void aumentarCantidadDebeRechazarValorNoPositivo() {
        UbicacionStock ubicacion = crearUbicacion();

        assertThrows(
                IllegalArgumentException.class,
                () -> ubicacion.aumentarCantidad(0));
    }

    @Test
    void disminuirCantidadDebeRestar() {
        UbicacionStock ubicacion = crearUbicacion();

        ubicacion.disminuirCantidad(4);

        assertEquals(6, ubicacion.getCantidad());
    }

    @Test
    void disminuirCantidadDebeRechazarValorNoPositivo() {
        UbicacionStock ubicacion = crearUbicacion();

        assertThrows(
                IllegalArgumentException.class,
                () -> ubicacion.disminuirCantidad(0));
    }

    @Test
    void disminuirCantidadDebeRechazarCantidadMayorALaExistente() {
        UbicacionStock ubicacion = crearUbicacion();

        assertThrows(
                IllegalArgumentException.class,
                () -> ubicacion.disminuirCantidad(11));
    }

    private UbicacionStock crearUbicacion() {
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        Sector posicion = new Sector("5", "Posicion 5", TipoSector.POSICION, 100);

        return new UbicacionStock(producto, posicion, 10);
    }
}
