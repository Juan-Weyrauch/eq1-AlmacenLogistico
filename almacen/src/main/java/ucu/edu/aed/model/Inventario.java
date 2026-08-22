package ucu.edu.aed.model;

import ucu.edu.aed.structures.ListaArray;

/**
 * Administra los productos y las cantidades almacenadas en el depósito.
 */
public class Inventario {

    /** Lista de ítems almacenados en el inventario. */
    private ListaArray<ItemInventario> items;

    /**
     * Crea un inventario vacío.
     */
    public Inventario() {
        // A implementar.
    }

    /**
     * Registra un producto en el inventario.
     *
     * @param producto producto a registrar
     * @param stockInicial cantidad inicial disponible
     */
    public void registrarProducto(Producto producto, int stockInicial) {
        // A implementar.
    }

    /**
     * Busca un ítem del inventario por el código de su producto.
     *
     * @param codigoProducto código del producto buscado
     * @return ítem correspondiente al producto
     */
    public ItemInventario buscarItem(String codigoProducto) {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el stock disponible de un producto.
     *
     * @param codigoProducto código del producto
     * @return cantidad disponible
     */
    public int obtenerStock(String codigoProducto) {
        throw new UnsupportedOperationException();
    }

    /**
     * Incrementa el stock de un producto.
     *
     * @param producto producto cuyo stock se incrementará
     * @param cantidad cantidad a agregar
     */
    public void aumentarStock(Producto producto, int cantidad) {
        // A implementar.
    }

    /**
     * Disminuye el stock de un producto.
     *
     * @param producto producto cuyo stock se disminuirá
     * @param cantidad cantidad a retirar
     */
    public void disminuirStock(Producto producto, int cantidad) {
        // A implementar.
    }

    /**
     * Determina si existe stock suficiente de un producto.
     *
     * @param producto producto a verificar
     * @param cantidad cantidad requerida
     * @return true si existe stock suficiente
     */
    public boolean hayStock(Producto producto, int cantidad) {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene los ítems almacenados en el inventario.
     *
     * @return lista de ítems del inventario
     */
    public ListaArray<ItemInventario> getItems() {
        throw new UnsupportedOperationException();
    }
}