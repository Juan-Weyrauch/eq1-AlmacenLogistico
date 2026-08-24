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
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador de la entrega no puede ser nulo o vacío.");
        }

        if (proveedor == null) {
            throw new IllegalArgumentException(
                    "El proveedor no puede ser nulo.");
        }

        this.id = id;
        this.proveedor = proveedor;
        this.lineas = new ListaSimple<>();
    }

    /**
     * Agrega una línea de productos a la entrega.
     *
     * La validación de elementos nulos es responsabilidad
     * de la implementación de ListaSimple.
     *
     * @param linea línea a agregar
     */
    public void agregarLinea(LineaEntrega linea) {
        this.lineas.agregar(linea);
    }

    /**
     * Obtiene el identificador de la entrega.
     *
     * @return identificador de la entrega
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Obtiene el proveedor responsable de la entrega.
     *
     * @return proveedor
     */
    public Proveedor getProveedor() {
        return this.proveedor;
    }

    /**
     * Obtiene las líneas que componen la entrega.
     *
     * @return lista de líneas de entrega
     */
    public ListaSimple<LineaEntrega> getLineas() {
        return this.lineas;
    }

    /**
     * Obtiene el tipo de operación logística.
     *
     * Una entrega de proveedor corresponde a una descarga,
     * ya que los productos recibidos son descargados en el almacén.
     *
     * @return tipo de operación
     */
    @Override
    public TipoOperacion getTipoOperacion() {
        return TipoOperacion.DESCARGA;
    }

    /**
     * Devuelve una representación textual de la entrega.
     *
     * @return información de la entrega
     */
    @Override
    public String toString() {
        return "EntregaProveedor{" +
                "id='" + this.id + '\'' +
                ", proveedor=" + this.proveedor +
                ", cantidadLineas=" + this.lineas.tamaño() +
                '}';
    }
}