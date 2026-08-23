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
        this.lineas = new ListaSimple<>();
    }

    /**
     * Agrega una línea de productos a la entrega.
     *
     * @param linea línea a agregar
     */
    public void agregarLinea(LineaEntrega linea) {
        lineas.agregar(linea);
    }

    /**
     * Obtiene el identificador de la entrega.
     *
     * @return identificador de la entrega
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Obtiene el proveedor responsable de la entrega.
     *
     * @return proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Obtiene las líneas que componen la entrega.
     *
     * @return lista de líneas de entrega
     */
    public ListaSimple<LineaEntrega> getLineas() {
        return lineas;
    }

    /**
     * Obtiene el tipo de operación logística.
     *
     * @return tipo de operación
     */
    @Override
    public TipoOperacion getTipoOperacion() {
        return TipoOperacion.DESCARGA;
    }
}