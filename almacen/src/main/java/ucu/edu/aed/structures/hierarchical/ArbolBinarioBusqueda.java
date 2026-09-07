package ucu.edu.aed.structures.hierarchical;

import java.util.function.Consumer;

import ucu.edu.aed.structures.element.Nodo;
import ucu.edu.aed.structures.linear.Cola;
import ucu.edu.aed.tda.element.TDAElemento;
import ucu.edu.aed.tda.hierarchical.TDAArbolBinario;

public class ArbolBinarioBusqueda<T> implements TDAArbolBinario<T> {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private TDAElemento<T> raiz;

    // =========================================================
    // CONSTRUCTORES
    // =========================================================

    public ArbolBinarioBusqueda() {
        this.raiz = null;
    }

    public ArbolBinarioBusqueda(TDAElemento<T> raiz) {
        this.raiz = raiz;
    }

    // =========================================================
    // CONSULTAS BÁSICAS DEL ÁRBOL
    // =========================================================

    @Override
    public TDAElemento<T> obtenerRaiz() {
        return this.raiz;
    }

    @Override
    public boolean esVacio() {
        return this.raiz == null;
    }

    // =========================================================
    // OPERACIONES DEL ÁRBOL BINARIO DE BÚSQUEDA
    // =========================================================

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "criterioBusqueda en el metodo 'buscar' es nulo");
        }

        if (this.raizEsNula()) {
            return null;
        }

        TDAElemento<T> encontrado = this.raiz.buscar(criterioBusqueda);

        return encontrado == null
                ? null
                : encontrado.getDato();
    }

    @Override
    public boolean insertar(Comparable<T> dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "dato en el metodo 'insertar' es nulo");
        }

        // Si el árbol está vacío, el nuevo nodo se convierte en la raíz.
        if (this.raizEsNula()) {
            this.raiz = new Nodo<>(convertirADato(dato));
            return true;
        }

        // Si ya existe una raíz, la inserción se delega al nodo.
        return this.raiz.insertar(dato);
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "criterioBusqueda en el metodo 'eliminar' es nulo");
        }

        if (this.raizEsNula()) {
            return false;
        }

        int criterio = criterioBusqueda.compareTo(this.raiz.getDato());

        /*
         * La eliminación de la raíz debe manejarse desde el árbol,
         * ya que puede implicar reemplazar la referencia a la raíz.
         */
        if (criterio == 0) {
            eliminarRaiz();
            return true;
        }

        /*
         * Si el elemento no es la raíz, el nodo puede resolver
         * recursivamente la eliminación dentro de su subárbol.
         */
        TDAElemento<T> nodoEliminado = this.raiz.eliminar(criterioBusqueda);

        return nodoEliminado != null;
    }

    // =========================================================
    // RECORRIDOS
    // =========================================================

    @Override
    public void inOrder(Consumer<T> consumidor) {
        validarConsumidor(consumidor, "inOrder");

        if (!this.raizEsNula()) {
            this.raiz.inOrder(
                    elemento -> consumidor.accept(elemento.getDato()));
        }
    }

    @Override
    public void preOrder(Consumer<T> consumidor) {
        validarConsumidor(consumidor, "preOrder");

        if (!this.raizEsNula()) {
            this.raiz.preOrder(
                    elemento -> consumidor.accept(elemento.getDato()));
        }
    }

    @Override
    public void postOrder(Consumer<T> consumidor) {
        validarConsumidor(consumidor, "postOrder");

        if (!this.raizEsNula()) {
            this.raiz.postOrder(
                    elemento -> consumidor.accept(elemento.getDato()));
        }
    }

    /**
     * Recorre el árbol por niveles utilizando BFS.
     *
     * Se utiliza la implementación propia de Cola para mantener
     * los nodos pendientes de procesamiento.
     *
     * Complejidad temporal: O(n), siendo n la cantidad de nodos.
     */
    public void porNiveles(Consumer<T> consumidor) {
        validarConsumidor(consumidor, "porNiveles");

        if (this.raizEsNula()) {
            return;
        }

        Cola<TDAElemento<T>> cola = new Cola<>();
        cola.poneEnCola(this.raiz);

        while (!cola.esVacio()) {
            TDAElemento<T> nodoActual = cola.frente();
            cola.quitaDeCola();

            consumidor.accept(nodoActual.getDato());

            if (nodoActual.getHijoIzquierdo() != null) {
                cola.poneEnCola(
                        nodoActual.getHijoIzquierdo());
            }

            if (nodoActual.getHijoDerecho() != null) {
                cola.poneEnCola(
                        nodoActual.getHijoDerecho());
            }
        }
    }

    // =========================================================
    // INFORMACIÓN ESTRUCTURAL
    // =========================================================

    @Override
    public int cantidadNodos() {
        return this.raizEsNula()
                ? 0
                : this.raiz.cantidadNodos();
    }

    @Override
    public int cantidadHojas() {
        return this.raizEsNula()
                ? 0
                : this.raiz.cantidadHojas();
    }

    @Override
    public int cantidadNodosInternos() {
        return this.raizEsNula()
                ? 0
                : this.raiz.cantidadNodosInternos();
    }

    /**
     * Retorna la altura total del árbol.
     *
     * Convención utilizada:
     * - árbol vacío: altura 0
     * - árbol con únicamente la raíz: altura 1
     */
    public int altura() {
        return this.raizEsNula()
                ? 0
                : this.raiz.altura();
    }

    // =========================================================
    // MÉTODOS PROTEGIDOS PARA HERENCIA
    // =========================================================

    /**
     * Permite que estructuras derivadas, como un AVL,
     * puedan reemplazar la raíz después de una rotación
     * sin exponer directamente el atributo.
     */
    protected void establecerRaiz(TDAElemento<T> nuevaRaiz) {
        this.raiz = nuevaRaiz;
    }

    /**
     * Permite a las subclases consultar si existe una raíz
     * sin acceder directamente al atributo privado.
     */
    protected boolean raizEsNula() {
        return this.raiz == null;
    }

    /**
     * Convierte el Comparable recibido por la interfaz al tipo
     * genérico almacenado por el árbol.
     */
    @SuppressWarnings("unchecked")
    protected T convertirADato(Comparable<T> dato) {
        return (T) dato;
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    /**
     * Elimina específicamente la raíz del ABB.
     *
     * Contempla los cuatro casos posibles:
     * 1. raíz hoja;
     * 2. únicamente hijo derecho;
     * 3. únicamente hijo izquierdo;
     * 4. dos hijos.
     *
     * Cuando existen dos hijos se utiliza el sucesor in-order:
     * el menor elemento del subárbol derecho.
     */
    private void eliminarRaiz() {
        TDAElemento<T> hijoIzquierdo = this.raiz.getHijoIzquierdo();

        TDAElemento<T> hijoDerecho = this.raiz.getHijoDerecho();

        // Caso 1: la raíz es una hoja.
        if (hijoIzquierdo == null && hijoDerecho == null) {
            this.raiz = null;
            return;
        }

        // Caso 2: solamente existe hijo derecho.
        if (hijoIzquierdo == null) {
            this.raiz = hijoDerecho;
            return;
        }

        // Caso 3: solamente existe hijo izquierdo.
        if (hijoDerecho == null) {
            this.raiz = hijoIzquierdo;
            return;
        }

        /*
         * Caso 4: existen ambos hijos.
         *
         * Se busca el menor elemento del subárbol derecho,
         * que corresponde al sucesor in-order de la raíz.
         */
        TDAElemento<T> padreSucesor = this.raiz;
        TDAElemento<T> sucesor = hijoDerecho;

        while (sucesor.getHijoIzquierdo() != null) {
            padreSucesor = sucesor;
            sucesor = sucesor.getHijoIzquierdo();
        }

        /*
         * Si el sucesor no es el hijo derecho inmediato,
         * debe ser removido de su posición original antes
         * de convertirse en la nueva raíz.
         */
        if (padreSucesor != this.raiz) {
            padreSucesor.setHijoIzquierdo(
                    sucesor.getHijoDerecho());

            sucesor.setHijoDerecho(hijoDerecho);
        }

        sucesor.setHijoIzquierdo(hijoIzquierdo);

        this.raiz = sucesor;
    }

    /**
     * Centraliza la política de los recorridos frente
     * a un Consumer nulo.
     */
    private void validarConsumidor(
            Consumer<T> consumidor,
            String metodo) {

        if (consumidor == null) {
            throw new IllegalArgumentException(
                    "ABB: consumidor en el metodo '"
                            + metodo
                            + "' es nulo");
        }
    }
}