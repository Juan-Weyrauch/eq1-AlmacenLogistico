package ucu.edu.aed.structures.element;

import java.util.function.Consumer;

import ucu.edu.aed.tda.element.TDAElemento;

public class Nodo<T> implements TDAElemento<T> {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private T dato;
    private TDAElemento<T> hijoIzquierdo;
    private TDAElemento<T> hijoDerecho;


    // =========================================================
    // CONSTRUCTORES
    // =========================================================

    /**
     * Crea un nodo sin hijos.
     */
    public Nodo(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "Nodo: dato en el constructor es nulo");
        }

        this.dato = dato;
    }

    /**
     * Crea un nodo con referencias iniciales a sus hijos.
     * Ambos hijos pueden ser nulos.
     */
    public Nodo(
            T dato,
            TDAElemento<T> hijoIzquierdo,
            TDAElemento<T> hijoDerecho) {

        this(dato);
        this.hijoIzquierdo = hijoIzquierdo;
        this.hijoDerecho = hijoDerecho;
    }


    // =========================================================
    // ACCESO AL DATO Y A LOS HIJOS
    // =========================================================

    @Override
    public T getDato() {
        return this.dato;
    }

    @Override
    public void setDato(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "Nodo: dato en el metodo 'setDato' es nulo");
        }

        this.dato = dato;
    }

    @Override
    public TDAElemento<T> getHijoIzquierdo() {
        return this.hijoIzquierdo;
    }

    @Override
    public void setHijoIzquierdo(TDAElemento<T> hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    @Override
    public TDAElemento<T> getHijoDerecho() {
        return this.hijoDerecho;
    }

    @Override
    public void setHijoDerecho(TDAElemento<T> hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }


    // =========================================================
    // OPERACIONES DEL ÁRBOL BINARIO DE BÚSQUEDA
    // =========================================================

    @Override
    public TDAElemento<T> buscar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "Nodo: criterioBusqueda en el metodo 'buscar' es nulo");
        }

        int criterio = criterioBusqueda.compareTo(this.dato);

        if (criterio == 0) {
            return this;
        }

        if (criterio < 0) {
            return this.hijoIzquierdo == null
                    ? null
                    : this.hijoIzquierdo.buscar(criterioBusqueda);
        }

        return this.hijoDerecho == null
                ? null
                : this.hijoDerecho.buscar(criterioBusqueda);
    }

    @Override
    public boolean insertar(Comparable<T> nuevoDato) {
        if (nuevoDato == null) {
            throw new IllegalArgumentException(
                    "Nodo: nuevoDato en el metodo 'insertar' es nulo");
        }

        int comparacion = nuevoDato.compareTo(this.dato);

        // El ABB no admite elementos duplicados.
        if (comparacion == 0) {
            return false;
        }

        if (comparacion < 0) {
            if (this.hijoIzquierdo != null) {
                return this.hijoIzquierdo.insertar(nuevoDato);
            }

            this.hijoIzquierdo =
                    crearNodo(convertirADato(nuevoDato));

            return true;
        }

        if (this.hijoDerecho != null) {
            return this.hijoDerecho.insertar(nuevoDato);
        }

        this.hijoDerecho =
                crearNodo(convertirADato(nuevoDato));

        return true;
    }

    @Override
    public TDAElemento<T> eliminar(
            Comparable<T> criterioBusqueda) {

        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "Nodo: criterioBusqueda en el metodo 'eliminar' es nulo");
        }

        int criterio =
                criterioBusqueda.compareTo(this.dato);

        /*
         * Un nodo no puede reemplazarse a sí mismo dentro del árbol.
         * La eliminación de la raíz debe ser resuelta por la estructura
         * que mantiene la referencia a dicha raíz.
         */
        if (criterio == 0) {
            throw new IllegalStateException(
                    "Nodo: la eliminacion de la raiz debe ser manejada por el arbol");
        }

        return eliminarRecursivo(
                this,
                criterioBusqueda);
    }


    // =========================================================
    // RECORRIDOS
    // =========================================================

    @Override
    public void inOrder(
            Consumer<TDAElemento<T>> consumidor) {

        validarConsumidor(consumidor, "inOrder");

        // hijo izquierdo -> nodo actual -> hijo derecho
        if (this.hijoIzquierdo != null) {
            this.hijoIzquierdo.inOrder(consumidor);
        }

        consumidor.accept(this);

        if (this.hijoDerecho != null) {
            this.hijoDerecho.inOrder(consumidor);
        }
    }

    @Override
    public void preOrder(
            Consumer<TDAElemento<T>> consumidor) {

        validarConsumidor(consumidor, "preOrder");

        // nodo actual -> hijo izquierdo -> hijo derecho
        consumidor.accept(this);

        if (this.hijoIzquierdo != null) {
            this.hijoIzquierdo.preOrder(consumidor);
        }

        if (this.hijoDerecho != null) {
            this.hijoDerecho.preOrder(consumidor);
        }
    }

    @Override
    public void postOrder(
            Consumer<TDAElemento<T>> consumidor) {

        validarConsumidor(consumidor, "postOrder");

        // hijo izquierdo -> hijo derecho -> nodo actual
        if (this.hijoIzquierdo != null) {
            this.hijoIzquierdo.postOrder(consumidor);
        }

        if (this.hijoDerecho != null) {
            this.hijoDerecho.postOrder(consumidor);
        }

        consumidor.accept(this);
    }


    // =========================================================
    // INFORMACIÓN ESTRUCTURAL
    // =========================================================

    @Override
    public boolean esHoja() {
        return this.hijoIzquierdo == null
                && this.hijoDerecho == null;
    }

    @Override
    public int cantidadNodos() {
        int cantidad = 1;

        if (this.hijoIzquierdo != null) {
            cantidad +=
                    this.hijoIzquierdo.cantidadNodos();
        }

        if (this.hijoDerecho != null) {
            cantidad +=
                    this.hijoDerecho.cantidadNodos();
        }

        return cantidad;
    }

    @Override
    public int cantidadHojas() {
        if (this.esHoja()) {
            return 1;
        }

        int cantidad = 0;

        if (this.hijoIzquierdo != null) {
            cantidad +=
                    this.hijoIzquierdo.cantidadHojas();
        }

        if (this.hijoDerecho != null) {
            cantidad +=
                    this.hijoDerecho.cantidadHojas();
        }

        return cantidad;
    }

    @Override
    public int cantidadNodosInternos() {
        return this.cantidadNodos()
                - this.cantidadHojas();
    }

    @Override
    public int altura() {
        int alturaIzquierda = 0;
        int alturaDerecha = 0;

        if (this.hijoIzquierdo != null) {
            alturaIzquierda =
                    this.hijoIzquierdo.altura();
        }

        if (this.hijoDerecho != null) {
            alturaDerecha =
                    this.hijoDerecho.altura();
        }

        /*
         * La altura se mide como la cantidad de nodos
         * del camino más largo desde este nodo hasta una hoja.
         */
        return 1
                + Math.max(
                        alturaIzquierda,
                        alturaDerecha);
    }

    @Override
    public int obtenerNivel(
            Comparable<T> criterioBusqueda) {

        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "Nodo: criterioBusqueda en el metodo 'obtenerNivel' es nulo");
        }

        int comparacion =
                criterioBusqueda.compareTo(this.dato);

        // El nivel del nodo respecto de sí mismo es 0.
        if (comparacion == 0) {
            return 0;
        }

        int nivelHijo;

        if (comparacion < 0) {
            if (this.hijoIzquierdo == null) {
                return -1;
            }

            nivelHijo =
                    this.hijoIzquierdo
                            .obtenerNivel(criterioBusqueda);

        } else {
            if (this.hijoDerecho == null) {
                return -1;
            }

            nivelHijo =
                    this.hijoDerecho
                            .obtenerNivel(criterioBusqueda);
        }

        // Si el dato no existe en el subárbol, propagamos el -1.
        if (nivelHijo == -1) {
            return -1;
        }

        // Cada retorno recursivo representa un nivel adicional.
        return 1 + nivelHijo;
    }


    // =========================================================
    // HELPER METHODS
    // =========================================================

    /**
     * Continúa la búsqueda del nodo a eliminar por debajo
     * del nodo actual.
     */
    private TDAElemento<T> eliminarRecursivo(
            TDAElemento<T> nodoActual,
            Comparable<T> criterioBusqueda) {

        int criterio =
                criterioBusqueda.compareTo(
                        nodoActual.getDato());

        if (criterio < 0) {
            TDAElemento<T> hijo =
                    nodoActual.getHijoIzquierdo();

            if (hijo == null) {
                return null;
            }

            if (criterioBusqueda.compareTo(
                    hijo.getDato()) == 0) {

                return eliminarHijo(
                        nodoActual,
                        hijo,
                        true);
            }

            return eliminarRecursivo(
                    hijo,
                    criterioBusqueda);
        }

        TDAElemento<T> hijo =
                nodoActual.getHijoDerecho();

        if (hijo == null) {
            return null;
        }

        if (criterioBusqueda.compareTo(
                hijo.getDato()) == 0) {

            return eliminarHijo(
                    nodoActual,
                    hijo,
                    false);
        }

        return eliminarRecursivo(
                hijo,
                criterioBusqueda);
    }

    /**
     * Elimina un hijo directo del nodo padre.
     *
     * Si el nodo tiene dos hijos, utiliza como reemplazo
     * su sucesor in-order: el menor elemento de su
     * subárbol derecho.
     *
     * @return el nodo que fue eliminado y desconectado.
     */
    private TDAElemento<T> eliminarHijo(
            TDAElemento<T> padre,
            TDAElemento<T> nodoEliminar,
            boolean esHijoIzquierdo) {

        TDAElemento<T> izquierdo =
                nodoEliminar.getHijoIzquierdo();

        TDAElemento<T> derecho =
                nodoEliminar.getHijoDerecho();

        TDAElemento<T> reemplazo;

        // Caso 1: hoja o nodo con únicamente hijo derecho.
        if (izquierdo == null) {
            reemplazo = derecho;

        // Caso 2: nodo con únicamente hijo izquierdo.
        } else if (derecho == null) {
            reemplazo = izquierdo;

        // Caso 3: nodo con dos hijos.
        } else {
            TDAElemento<T> padreSucesor =
                    nodoEliminar;

            TDAElemento<T> sucesor =
                    derecho;

            /*
             * El sucesor in-order es el menor elemento
             * del subárbol derecho.
             */
            while (sucesor.getHijoIzquierdo() != null) {
                padreSucesor = sucesor;
                sucesor =
                        sucesor.getHijoIzquierdo();
            }

            /*
             * Si el sucesor no es el hijo derecho inmediato,
             * primero se lo desconecta de su posición original.
             */
            if (padreSucesor != nodoEliminar) {
                padreSucesor.setHijoIzquierdo(
                        sucesor.getHijoDerecho());

                sucesor.setHijoDerecho(derecho);
            }

            sucesor.setHijoIzquierdo(izquierdo);
            reemplazo = sucesor;
        }

        /*
         * El padre deja de apuntar al nodo eliminado
         * y pasa a apuntar al reemplazo.
         */
        if (esHijoIzquierdo) {
            padre.setHijoIzquierdo(reemplazo);
        } else {
            padre.setHijoDerecho(reemplazo);
        }

        /*
         * El nodo eliminado queda completamente
         * desconectado del árbol.
         */
        nodoEliminar.setHijoIzquierdo(null);
        nodoEliminar.setHijoDerecho(null);

        return nodoEliminar;
    }

    /**
     * Centraliza la creación de nuevos nodos.
     */
    private TDAElemento<T> crearNodo(T dato) {
        return new Nodo<>(dato);
    }

    /**
     * Convierte el Comparable recibido por la interfaz
     * al tipo almacenado por el nodo.
     */
    @SuppressWarnings("unchecked")
    private T convertirADato(Comparable<T> dato) {
        return (T) dato;
    }

    /**
     * Centraliza la política utilizada por los recorridos
     * cuando reciben un Consumer nulo.
     */
    private void validarConsumidor(
            Consumer<?> consumidor,
            String metodo) {

        if (consumidor == null) {
            throw new IllegalArgumentException(
                    "Nodo: consumidor en el metodo '"
                            + metodo
                            + "' es nulo");
        }
    }
}