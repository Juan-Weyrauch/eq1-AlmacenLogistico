package ucu.edu.aed.model;

import ucu.edu.aed.structures.hierarchical.ArbolAVL;
import ucu.edu.aed.structures.linear.ListaArray;

/**
 * Administra los productos y las cantidades almacenadas en el deposito.
 *
 * <p>Invariantes: cada producto registrado tiene un codigo no vacio, los
 * codigos no se repiten y ningun item puede tener stock negativo.</p>
 *
 * Representacion: se utiliza un {@link ArbolAVL} ordenado por codigo de
 * producto. Las busquedas, altas y modificaciones se realizan en O(log n).
 */
public class Inventario {

    /** Árbol AVL de items almacenados en el inventario. */
    private final ArbolAVL<ItemInventario> items;

    /**
     * Crea un inventario vacio.
     */
    public Inventario() {
        this.items = new ArbolAVL<>();
    }

    /**
     * Registra un producto en el inventario.
     *
     * <p>Politica de duplicados: el codigo de producto es unico. Si ya existe
     * un producto registrado con el mismo codigo, el alta se rechaza. Los
     * cambios de stock deben hacerse con {@link #aumentarStock(Producto, int)}
     * o {@link #disminuirStock(Producto, int)}.</p>
     *
     * <p>Complejidad temporal: O(log n), debido a la búsqueda de duplicados
     * en el árbol AVL y a la inserción balanceada.</p>
     */
    public void registrarProducto(Producto producto, int stockInicial) {
        String codigo = obtenerCodigoValido(producto);
        validarCantidadNoNegativa(stockInicial, "El stock inicial no puede ser negativo");

        if (buscarItem(codigo) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un producto registrado con el codigo indicado");
        }

        this.items.insertar(new ItemInventario(producto, stockInicial));
    }

    /**
     * Busca un item del inventario por el codigo de su producto.
     *
     * <p>Complejidad temporal: O(log n), debido a la búsqueda en el árbol AVL.</p>
     *
     * @param codigoProducto codigo del producto buscado
     * @return item correspondiente al producto, o {@code null} si no existe
     */
    public ItemInventario buscarItem(String codigoProducto) {
        String codigoBuscado = validarCodigo(codigoProducto);

        return this.items.buscar(item -> codigoBuscado.compareTo(item.getProducto().getCodigo()));
    }

    /**
     * Obtiene el stock disponible de un producto.
     *

     * <p>Complejidad temporal: O(log n). Si el producto no está registrado,
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
     * <p>Complejidad temporal: O(log n), por la búsqueda del producto
     * en el árbol AVL.</p>
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
     * <p>Complejidad temporal: O(log n), por la búsqueda del producto
     * en el árbol AVL.</p>
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
     * <p>Complejidad temporal: O(log n).</p>
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
     * <p>Recorrido AVL O(n), mas la copia de las ubicaciones de cada item.
     * Con acceso por indice sobre ListaSimple, las copias cuestan O(sum(u_i^2)).</p>
     *
     * @return lista de items del inventario
     */

    public ListaArray<ItemInventario> getItems() {
        ListaArray<ItemInventario> copia = new ListaArray<>();
        this.items.inOrder(item -> copia.agregar(item.copiar()));
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
