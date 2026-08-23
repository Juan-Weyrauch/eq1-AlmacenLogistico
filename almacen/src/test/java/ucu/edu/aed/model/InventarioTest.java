package ucu.edu.aed.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventarioTest {

    @Test
    void itemInventarioMantieneStockNoNegativo() {
        Producto producto = producto("P001");
        ItemInventario item = new ItemInventario(producto, 10);

        item.aumentarStock(5);
        item.disminuirStock(15);

        assertSame(producto, item.getProducto());
        assertEquals(0, item.getStock());
        assertThrows(IllegalArgumentException.class, () -> item.disminuirStock(1));
        assertThrows(IllegalArgumentException.class, () -> item.aumentarStock(-1));
        assertThrows(IllegalArgumentException.class, () -> new ItemInventario(producto, -1));
        assertThrows(IllegalArgumentException.class, () -> new ItemInventario(null, 0));
    }

    @Test
    void registraYBuscaProductosPorCodigo() {
        Inventario inventario = new Inventario();
        Producto producto = producto("P001");

        inventario.registrarProducto(producto, 7);

        ItemInventario item = inventario.buscarItem("P001");
        assertNotNull(item);
        assertSame(producto, item.getProducto());
        assertEquals(7, inventario.obtenerStock("P001"));
        assertEquals(0, inventario.obtenerStock("P999"));
        assertEquals(1, inventario.getItems().tamaño());
    }

    @Test
    void rechazaProductosDuplicadosPorCodigo() {
        Inventario inventario = new Inventario();

        inventario.registrarProducto(producto("P001"), 3);

        assertThrows(IllegalArgumentException.class,
                () -> inventario.registrarProducto(producto("P001"), 9));
        assertEquals(3, inventario.obtenerStock("P001"));
    }

    @Test
    void getItemsNoExponeItemsInternos() {
        Inventario inventario = new Inventario();
        inventario.registrarProducto(producto("P001"), 3);

        ItemInventario itemExterno = inventario.getItems().obtener(0);
        itemExterno.aumentarStock(7);

        assertEquals(3, inventario.obtenerStock("P001"));
    }

    @Test
    void aumentaYDisminuyeStockDeProductoExistente() {
        Inventario inventario = new Inventario();
        Producto producto = producto("P001");
        inventario.registrarProducto(producto, 4);

        inventario.aumentarStock(producto, 6);
        inventario.disminuirStock(producto, 5);

        assertEquals(5, inventario.obtenerStock("P001"));
        assertTrue(inventario.hayStock(producto, 5));
        assertFalse(inventario.hayStock(producto, 6));
    }

    @Test
    void rechazaOperacionesInvalidasDeStock() {
        Inventario inventario = new Inventario();
        Producto producto = producto("P001");
        Producto noRegistrado = producto("P999");
        inventario.registrarProducto(producto, 2);

        assertThrows(IllegalArgumentException.class,
                () -> inventario.registrarProducto(producto("P002"), -1));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.aumentarStock(producto, -1));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.disminuirStock(producto, -1));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.disminuirStock(producto, 3));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.aumentarStock(noRegistrado, 1));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.disminuirStock(noRegistrado, 1));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.hayStock(producto, -1));
        assertFalse(inventario.hayStock(noRegistrado, 1));
    }

    @Test
    void validaProductosYCodigos() {
        Inventario inventario = new Inventario();

        assertThrows(IllegalArgumentException.class,
                () -> inventario.registrarProducto(null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.registrarProducto(producto(" "), 1));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.buscarItem(null));
        assertThrows(IllegalArgumentException.class,
                () -> inventario.buscarItem(""));
    }

    private Producto producto(String codigo) {
        return new ProductoDePrueba(codigo);
    }

    private static class ProductoDePrueba extends Producto {

        private final String codigo;

        ProductoDePrueba(String codigo) {
            super(codigo, "Producto " + codigo, "Descripcion de prueba");
            this.codigo = codigo;
        }

        @Override
        public String getCodigo() {
            return this.codigo;
        }
    }
}
