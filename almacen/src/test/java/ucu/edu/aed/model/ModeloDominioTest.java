package ucu.edu.aed.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModeloDominioTest {

    @Test
    void productoDebeConstruirseConDatosValidos() {
        Producto producto =
                new Producto("P001", "Arroz", "Paquete de 1 kg");

        assertEquals("P001", producto.getCodigo());
        assertEquals("Arroz", producto.getNombre());
        assertEquals("Paquete de 1 kg", producto.getDescripcion());
    }

    @Test
    void productoDebeRechazarCodigoONombreInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(
                        null,
                        "Arroz",
                        "Descripción"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(
                        "   ",
                        "Arroz",
                        "Descripción"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(
                        "P001",
                        null,
                        "Descripción"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Producto(
                        "P001",
                        "   ",
                        "Descripción"));
    }

    @Test
    void proveedorDebeConstruirseYValidarDatos() {
        Proveedor proveedor =
                new Proveedor("PR001", "Proveedor Norte");

        assertEquals("PR001", proveedor.getId());
        assertEquals("Proveedor Norte", proveedor.getNombre());

        assertThrows(
                IllegalArgumentException.class,
                () -> new Proveedor(
                        null,
                        "Proveedor Norte"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Proveedor(
                        "PR001",
                        null));
    }

    @Test
    void sucursalDebeConstruirseYValidarDatos() {
        Sucursal sucursal =
                new Sucursal("S001", "Centro", 250);

        assertEquals("S001", sucursal.getId());
        assertEquals("Centro", sucursal.getNombre());
        assertEquals(250, sucursal.getCantidadClientes());

        assertThrows(
                IllegalArgumentException.class,
                () -> new Sucursal(
                        null,
                        "Centro",
                        250));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Sucursal(
                        "S001",
                        null,
                        250));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Sucursal(
                        "S001",
                        "Centro",
                        -1));
    }

    @Test
    void lineaEntregaDebeAceptarSoloProductoYCantidadValidos() {
        Producto producto =
                new Producto("P001", "Arroz", "Paquete de 1 kg");

        LineaEntrega linea =
                new LineaEntrega(producto, 10);

        assertSame(producto, linea.getProducto());
        assertEquals(10, linea.getCantidad());

        assertThrows(
                IllegalArgumentException.class,
                () -> new LineaEntrega(null, 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> new LineaEntrega(producto, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> new LineaEntrega(producto, -1));
    }

    @Test
    void lineaPedidoDebeAceptarSoloProductoYCantidadValidos() {
        Producto producto =
                new Producto("P001", "Arroz", "Paquete de 1 kg");

        LineaPedido linea =
                new LineaPedido(producto, 5);

        assertSame(producto, linea.getProducto());
        assertEquals(5, linea.getCantidad());

        assertThrows(
                IllegalArgumentException.class,
                () -> new LineaPedido(null, 5));

        assertThrows(
                IllegalArgumentException.class,
                () -> new LineaPedido(producto, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> new LineaPedido(producto, -1));
    }

    @Test
    void entregaProveedorDebeInicializarLineasYSerDescarga() {
        Proveedor proveedor =
                new Proveedor("PR001", "Proveedor Norte");

        Producto producto =
                new Producto("P001", "Arroz", "Paquete de 1 kg");

        EntregaProveedor entrega =
                new EntregaProveedor("E001", proveedor);

        assertEquals("E001", entrega.getId());
        assertSame(proveedor, entrega.getProveedor());
        assertTrue(entrega.getLineas().esVacio());

        assertEquals(
                TipoOperacion.DESCARGA,
                entrega.getTipoOperacion());

        entrega.agregarLinea(
                new LineaEntrega(producto, 10));

        assertEquals(
                1,
                entrega.getLineas().tamaño());

        /*
         * Esta excepción la produce ListaSimple.agregar(),
         * no EntregaProveedor.
         */
        assertThrows(
                IllegalArgumentException.class,
                () -> entrega.agregarLinea(null));
    }

    @Test
    void entregaProveedorDebeRechazarIdOProveedorInvalido() {
        Proveedor proveedor =
                new Proveedor("PR001", "Proveedor Norte");

        assertThrows(
                IllegalArgumentException.class,
                () -> new EntregaProveedor(
                        null,
                        proveedor));

        assertThrows(
                IllegalArgumentException.class,
                () -> new EntregaProveedor(
                        "   ",
                        proveedor));

        assertThrows(
                IllegalArgumentException.class,
                () -> new EntregaProveedor(
                        "E001",
                        null));
    }

    @Test
    void pedidoDebeInicializarLineasCalcularPrioridadYSerCarga() {
        Sucursal sucursal =
                new Sucursal("S001", "Centro", 250);

        Producto producto =
                new Producto("P001", "Arroz", "Paquete de 1 kg");

        PedidoReabastecimiento pedido =
                new PedidoReabastecimiento(
                        "PE001",
                        sucursal);

        assertEquals("PE001", pedido.getId());
        assertSame(sucursal, pedido.getSucursal());
        assertTrue(pedido.getLineas().esVacio());

        assertEquals(
                250,
                pedido.getPrioridad());

        assertEquals(
                TipoOperacion.CARGA,
                pedido.getTipoOperacion());

        pedido.agregarLinea(
                new LineaPedido(producto, 5));

        assertEquals(
                1,
                pedido.getLineas().tamaño());

        /*
         * Esta excepción la produce ListaSimple.agregar(),
         * no PedidoReabastecimiento.
         */
        assertThrows(
                IllegalArgumentException.class,
                () -> pedido.agregarLinea(null));
    }

    @Test
    void pedidoDebeRechazarIdOSucursalInvalida() {
        Sucursal sucursal =
                new Sucursal("S001", "Centro", 250);

        assertThrows(
                IllegalArgumentException.class,
                () -> new PedidoReabastecimiento(
                        null,
                        sucursal));

        assertThrows(
                IllegalArgumentException.class,
                () -> new PedidoReabastecimiento(
                        "   ",
                        sucursal));

        assertThrows(
                IllegalArgumentException.class,
                () -> new PedidoReabastecimiento(
                        "PE001",
                        null));
    }
}