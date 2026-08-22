package ucu.edu.aed.model;

import ucu.edu.aed.structures.ListaArray;

/**
 * Administra los productos y las cantidades almacenadas en el deposito.
 *
 * <p>Invariantes: cada producto registrado tiene un codigo no vacio, los
 * codigos no se repiten y ningun item puede tener stock negativo.</p>
 *
 * <p>Representacion: se utiliza {@link ListaArray} para cumplir con el uso de
 * estructuras lineales propias del proyecto. Las busquedas por codigo son
 * lineales.</p>
 */
public class Inventario {

    /** Lista de items almacenados en el inventario. */
    private final ListaArray<ItemInventario> items;

    /**
     * Crea un inventario vacio.
     */
    public Inventario() {
        this.items = new ListaArray<>();
    }

    /**
     * Registra un producto en el inventario.
     *
     * <p>Politica de duplicados: el codigo de producto es unico. Si ya existe
     * un producto registrado con el mismo codigo, el alta se rechaza. Los
     * cambios de stock deben hacerse con {@link #aumentarStock(Producto, int)}
     * o {@link #disminuirStock(Producto, int)}.</p>
     *
     * <p>Complejidad temporal: O(n), por la busqueda de duplicados.</p>
     *
     * @param producto producto a registrar
     * @param stockInicial cantidad inicial disponible
     */
    public void registrarProducto(Producto producto, int stockInicial) {
        String codigo = obtenerCodigoValido(producto);
        validarCantidadNoNegativa(stockInicial, "El stock inicial no puede ser negativo");

        if (buscarItem(codigo) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un producto registrado con el codigo indicado");
        }

        this.items.agregar(new ItemInventario(producto, stockInicial));
    }

    /**
     * Busca un item del inventario por el codigo de su producto.
     *
     * <p>Complejidad temporal: O(n), porque recorre linealmente los items
     * almacenados en {@link ListaArray}. </p>
     *
     * @param codigoProducto codigo del producto buscado
     * @return item correspondiente al producto, o {@code null} si no existe
     */
    public ItemInventario buscarItem(String codigoProducto) {
        String codigoBuscado = validarCodigo(codigoProducto);

        return this.items.buscar(item ->
                item.getProducto().getCodigo().equals(codigoBuscado));
    }

    /**
     * Obtiene el stock disponible de un producto.
     *
     * <p>Complejidad temporal: O(n). Si el producto no esta registrado,
     * retorna 0.</p>
     *
     * @param codigoProducto codigo del producto
     * @return cantidad disponible
     */
    public int obtenerStock(String codigoProducto) {
        ItemInventario item = buscarItem(codigoProducto);

        if (item == null) {
            return 0;
        }

        return item.getStock();
    }

    /**
     * Incrementa el stock de un producto.
     *
     * <p>Complejidad temporal: O(n), por la busqueda del producto.
     *
     * @param producto producto cuyo stock se incrementara
     * @param cantidad cantidad a agregar
     */
    public void aumentarStock(Producto producto, int cantidad) {
        ItemInventario item = buscarItemExistente(producto);
        item.aumentarStock(cantidad);
    }

    /**
     * Disminuye el stock de un producto.
     *
     * <p>Complejidad temporal: O(n), por la busqueda del producto.</p>
     *
     * @param producto producto cuyo stock se disminuira
     * @param cantidad cantidad a retirar
     */
    public void disminuirStock(Producto producto, int cantidad) {
        ItemInventario item = buscarItemExistente(producto);
        item.disminuirStock(cantidad);
    }

    /**
     * Determina si existe stock suficiente de un producto.
     *
     * <p>Complejidad temporal: O(n), por la busqueda del producto.
     *
     * @param producto producto a verificar
     * @param cantidad cantidad requerida
     * @return true si existe stock suficiente
     */
    public boolean hayStock(Producto producto, int cantidad) {
        validarCantidadNoNegativa(cantidad, "La cantidad requerida no puede ser negativa");
        String codigo = obtenerCodigoValido(producto);
        ItemInventario item = buscarItem(codigo);

        return item != null && item.getStock() >= cantidad;
    }

    /**
     * Obtiene los items almacenados en el inventario.
     *
     * <p>Complejidad temporal: O(n), porque retorna una copia de la lista para
     * no exponer la estructura interna del inventario.</p>
     *
     * @return lista de items del inventario
     */
    public ListaArray<ItemInventario> getItems() {
        ListaArray<ItemInventario> copia = new ListaArray<>();

        for (int i = 0; i < this.items.tamaño(); i++) {
            ItemInventario item = this.items.obtener(i);
            copia.agregar(new ItemInventario(item.getProducto(), item.getStock()));
        }

        return copia;
    }

    private ItemInventario buscarItemExistente(Producto producto) {
        String codigo = obtenerCodigoValido(producto);
        ItemInventario item = buscarItem(codigo);

        if (item == null) {
            throw new IllegalArgumentException(
                    "El producto indicado no esta registrado en el inventario");
        }

        return item;
    }

    private String obtenerCodigoValido(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }

        return validarCodigo(producto.getCodigo());
    }

    private String validarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo del producto no puede ser vacio");
        }

        return codigo;
    }

    private void validarCantidadNoNegativa(int cantidad, String mensaje) {
        if (cantidad < 0) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
