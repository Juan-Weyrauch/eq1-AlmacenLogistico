package ucu.edu.aed.model;

import ucu.edu.aed.structures.ListaSimple;

/**
 * Representa una entrega de productos realizada por un proveedor.
 */
public class EntregaProveedor implements OperacionLogistica {

    /** Identificador único de la entrega. */
    private String id;

    /** Proveedor responsable de la entrega. */
    private Proveedor proveedor;

    /** Productos incluidos en la entrega. */
    private ListaSimple<LineaEntrega> lineas;

    /**
     * Crea una nueva entrega de proveedor.
     *
     * @param id identificador único de la entrega
     * @param proveedor proveedor responsable de la entrega
     */
    public EntregaProveedor(String id, Proveedor proveedor) {
        this.id = id;
        this.proveedor = proveedor;
    }

    /**
     * Agrega una línea de productos a la entrega.
     *
     * @param linea línea a agregar
     */
    public void agregarLinea(LineaEntrega linea) {
        // A implementar.
    }

    /**
     * Obtiene el identificador de la entrega.
     *
     * @return identificador de la entrega
     */
    @Override
    public String getId() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el proveedor responsable de la entrega.
     *
     * @return proveedor
     */
    public Proveedor getProveedor() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene las líneas que componen la entrega.
     *
     * @return lista de líneas de entrega
     */
    public ListaSimple<LineaEntrega> getLineas() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtiene el tipo de operación logística.
     *
     * @return tipo de operación
     */
    @Override
    public TipoOperacion getTipoOperacion() {
        throw new UnsupportedOperationException();
    }
}