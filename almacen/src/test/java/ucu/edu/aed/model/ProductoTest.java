package ucu.edu.aed.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void constructorViejoDebeUsarEspacioUnitarioPorDefecto() {
        Producto producto = new Producto("P001", "Arroz", "desc");

        assertEquals(1, producto.getEspacioUnitario());
    }

    @Test
    void constructorNuevoDebeAceptarEspacioUnitario() {
        Producto producto = new Producto("P001", "Arroz", "desc", 3);

        assertEquals(3, producto.getEspacioUnitario());
    }

    @Test
    void constructorNuevoDebeRechazarEspacioUnitarioNoPositivo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto("P001", "Arroz", "desc", 0));
    }
}
