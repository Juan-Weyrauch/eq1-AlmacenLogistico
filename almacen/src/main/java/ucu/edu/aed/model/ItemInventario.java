package ucu.edu.aed.model;

import ucu.edu.aed.structures.linear.ListaSimple;

/**
 * Representa un producto junto con su cantidad disponible en inventario.
 *
 * <p>Invariante: el producto asociado no es {@code null} y el stock siempre
 * es mayor o igual a cero.</p>
 *
 * <p>Acceso al stock O(1); busqueda de ubicacion O(u). Las copias por indice
 * sobre ListaSimple cuestan O(u^2), con u ubicaciones.</p>
 */
public class ItemInventario implements Comparable<ItemInventario>{

    /** Producto asociado al item de inventario. */
    private final Producto producto;

    /** Cantidad disponible del producto. */
    private int stock;
    private boolean stockUbicado;

    /*
     *
     * Lista de posiciones físicas del depósito en las que se encuentra
     * almacenado este producto.
     *
     * Se utiliza ListaSimple porque para las ubicaciones predominan los
     * recorridos secuenciales y no se necesita acceso aleatorio por índice.
     * Además, cuando se agrega una nueva ubicación se inserta al inicio,
     * haciendo esa inserción O(1).
     */
    private final ListaSimple<UbicacionStock> ubicaciones;

    /**
     * Crea un nuevo item de inventario.
     *
     * @param producto producto almacenado
     * @param stock cantidad inicial disponible
     */
    public ItemInventario(Producto producto, int stock) {
        validarProducto(producto);
        validarCantidadNoNegativa(stock, "El stock inicial no puede ser negativo");

        this.producto = producto;
        this.stock = stock;

        this.ubicaciones = new ListaSimple<>();
    }

    /**
     * Obtiene el producto asociado.
     *
     * @return producto almacenado
     */
    public Producto getProducto() {
        return this.producto;
    }

    /**
     * Obtiene la cantidad disponible.
     *
     * @return stock actual
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * Incrementa el stock disponible.
     *
     * @param cantidad cantidad a agregar
     */
    public void aumentarStock(int cantidad) {
        exigirStockSinUbicaciones();
        validarCantidadNoNegativa(cantidad, "La cantidad a agregar no puede ser negativa");
        this.stock += cantidad;
    }

    /**
     * Disminuye el stock disponible.
     *
     * @param cantidad cantidad a retirar
     */
    public void disminuirStock(int cantidad) {
        exigirStockSinUbicaciones();
        validarCantidadNoNegativa(cantidad, "La cantidad a retirar no puede ser negativa");

        if (cantidad > this.stock) {
            throw new IllegalArgumentException(
                    "No hay stock suficiente para retirar la cantidad indicada");
        }

        this.stock -= cantidad;
    }

    // ============================================================
    // Operaciones para administrar el stock ubicado físicamente.
    // ============================================================

    /**
     * Agrega una ubicación física ya existente a este item.
     *
     * La ubicación debe pertenecer al mismo producto y no puede existir
     * previamente otra ubicación para la misma posición.
     *
     * La nueva ubicación se agrega al inicio de ListaSimple porque no es
     * necesario mantenerlas ordenadas. De esta forma la inserción es O(1).
     *
     * @param ubicacion ubicación que se desea registrar
     * @return la misma ubicación registrada
     */
    public UbicacionStock agregarUbicacion(UbicacionStock ubicacion) {
        validarUbicacion(ubicacion);
        activarStockUbicado();

        if (buscarUbicacion(ubicacion.getPosicion()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una ubicacion para el producto en esa posicion");
        }

        this.ubicaciones.agregar(0, ubicacion);
        this.stock += ubicacion.getCantidad();

        return ubicacion;
    }

    /**
     * Agrega una determinada cantidad del producto en una posición física.
     *
     * Si el producto ya se encuentra en esa posición, aumenta la cantidad
     * existente. Si todavía no se encontraba allí, crea una nueva
     * UbicacionStock.
     *
     * @param posicion posición física en la que se almacena el producto
     * @param cantidad cantidad que se desea agregar
     * @return ubicación creada o actualizada
     */
    public UbicacionStock aumentarEnUbicacion(
            Sector posicion,
            int cantidad) {

        validarPosicion(posicion);
        validarCantidadPositiva(
                cantidad,
                "La cantidad a agregar debe ser mayor que cero");
        activarStockUbicado();

        UbicacionStock ubicacion = buscarUbicacion(posicion);

        if (ubicacion == null) {
            ubicacion = new UbicacionStock(
                    this.producto,
                    posicion,
                    cantidad);

            /*
             * No necesitamos conservar un orden particular entre las
             * ubicaciones, por lo que agregamos al inicio en O(1).
             */
            this.ubicaciones.agregar(0, ubicacion);
        } else {
            ubicacion.aumentarCantidad(cantidad);
        }

        /*
         * El stock general continúa siendo la cantidad total del producto,
         * como ya ocurría en el Hito 1.
         */
        this.stock += cantidad;

        return ubicacion;
    }

    /**
     * Retira una cantidad del producto desde una posición específica.
     *
     * Si se retira toda la cantidad existente en esa posición, la
     * UbicacionStock se elimina de la lista para no conservar una ubicación
     * con cantidad cero.
     *
     * @param posicion posición desde la cual se retira el producto
     * @param cantidad cantidad que se desea retirar
     * @return true si la ubicación quedó vacía y fue eliminada
     */
    public boolean retirarDeUbicacion(
            Sector posicion,
            int cantidad) {

        validarPosicion(posicion);
        validarCantidadPositiva(
                cantidad,
                "La cantidad a retirar debe ser mayor que cero");

        UbicacionStock ubicacion = buscarUbicacion(posicion);

        if (ubicacion == null) {
            throw new IllegalArgumentException(
                    "El producto no se encuentra en la posicion indicada");
        }

        if (cantidad > ubicacion.getCantidad()) {
            throw new IllegalArgumentException(
                    "La ubicacion no contiene suficiente cantidad del producto");
        }

        /*
         * Si se retira exactamente todo lo que había en la posición,
         * eliminamos directamente la ubicación.
         */
        if (cantidad == ubicacion.getCantidad()) {
            this.ubicaciones.remover(ubicacion);
            this.stock -= cantidad;

            return true;
        }

        ubicacion.disminuirCantidad(cantidad);
        this.stock -= cantidad;

        return false;
    }

    /**
     * Elimina una ubicación únicamente cuando su cantidad es cero.
     *
     * Este método funciona como operación auxiliar. Normalmente,
     * retirarDeUbicacion elimina directamente la ubicación cuando se retira
     * toda su cantidad.
     *
     * @param ubicacion ubicación que se desea eliminar
     * @return true si la ubicación fue eliminada
     */
    public boolean eliminarUbicacionVacia(UbicacionStock ubicacion) {
        if (ubicacion == null) {
            return false;
        }

        if (ubicacion.getCantidad() != 0) {
            return false;
        }

        return this.ubicaciones.remover(ubicacion);
    }

    /**
     * Busca la ubicación correspondiente a una determinada posición.
     *
     * La búsqueda sobre ListaSimple es secuencial, por lo que su complejidad
     * es O(u), siendo u la cantidad de ubicaciones de este producto.
     *
     * @param posicion posición que se desea buscar
     * @return ubicación correspondiente o null si no existe
     */
    public UbicacionStock buscarUbicacion(Sector posicion) {
        validarPosicion(posicion);

        return this.ubicaciones.buscar(
                ubicacion -> ubicacion.getPosicion() == posicion);
    }

    /**
     * Devuelve una copia de la estructura que contiene las ubicaciones.
     *
     * Se crea una nueva ListaSimple para evitar que código externo pueda
     * agregar, eliminar o vaciar directamente la lista interna del item.
     *
     * Las instancias UbicacionStock no se duplican porque representan las
     * mismas ubicaciones físicas utilizadas por el depósito.
     *
     * @return copia estructural de las ubicaciones
     */
    public ListaSimple<UbicacionStock> obtenerUbicaciones() {
        ListaSimple<UbicacionStock> copia = new ListaSimple<>();

        /*
         * ListaSimple no dispone de un iterador público. Por eso se utilizan
         * las operaciones propias de la estructura implementada en el
         * proyecto y no colecciones de la API de Java.
         */
        for (int i = 0; i < this.ubicaciones.tamaño(); i++) {
            copia.agregar(
                    0,
                    this.ubicaciones.obtener(i));
        }

        return copia;
    }

    /**
     * Verifica que el stock general coincida con la suma de todas las
     * cantidades distribuidas físicamente.
     *
     * Este método resulta útil para verificar la consistencia del modelo
     * utilizado en los nuevos flujos del Hito 2.
     *
     * @return true si el stock coincide con la suma de ubicaciones
     */
    public boolean stockCoincideConUbicaciones() {
        int suma = 0;

        for (int i = 0; i < this.ubicaciones.tamaño(); i++) {
            suma += this.ubicaciones
                    .obtener(i)
                    .getCantidad();
        }

        return suma == this.stock;
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
    }

    /** Activa el modelo fisico sin inventar ubicaciones para cantidades previas. */
    public void activarStockUbicado() {
        if (!stockUbicado && stock != 0) {
            throw new IllegalStateException("Debe migrar el stock existente antes de ubicarlo");
        }
        stockUbicado = true;
    }

    private void exigirStockSinUbicaciones() {
        if (stockUbicado) {
            throw new IllegalStateException("El stock ubicado debe modificarse por posicion");
        }
    }

    /** Copia independiente para consultas; conserva las cantidades por posicion. */
    public ItemInventario copiar() {
        ItemInventario copia = new ItemInventario(producto, stockUbicado ? 0 : stock);
        if (stockUbicado) {
            copia.activarStockUbicado();
            for (int i = 0; i < ubicaciones.tamaño(); i++) {
                UbicacionStock ubicacion = ubicaciones.obtener(i);
                copia.agregarUbicacion(new UbicacionStock(producto,
                        ubicacion.getPosicion(), ubicacion.getCantidad()));
            }
        }
        return copia;
    }

    private void validarUbicacion(UbicacionStock ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException(
                    "La ubicacion no puede ser null");
        }

        if (!ubicacion.getProducto()
                .getCodigo()
                .equals(this.producto.getCodigo())) {

            throw new IllegalArgumentException(
                    "La ubicacion corresponde a otro producto");
        }
    }

    private void validarPosicion(Sector posicion) {
        if (posicion == null) {
            throw new IllegalArgumentException(
                    "La posicion no puede ser null");
        }
    }

    private void validarCantidadNoNegativa(int cantidad, String mensaje) {
        if (cantidad < 0) {
            throw new IllegalArgumentException(mensaje);
        }
    }


    private void validarCantidadPositiva(int cantidad, String mensaje) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    @Override
    public int compareTo(ItemInventario otro) {
        return this.producto.getCodigo().compareTo(
                otro.producto.getCodigo()
        );
    }
}
