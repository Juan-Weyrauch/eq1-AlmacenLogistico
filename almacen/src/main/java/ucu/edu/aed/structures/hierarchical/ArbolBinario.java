package ucu.edu.aed.structures.hierarchical;

import java.util.function.Consumer;

import ucu.edu.aed.structures.element.Nodo;
import ucu.edu.aed.structures.linear.Cola;
import ucu.edu.aed.tda.element.TDAElemento;
import ucu.edu.aed.tda.hierarchical.TDAArbolBinario;

public class ArbolBinario<T> implements TDAArbolBinario<T> {

    private TDAElemento<T> raiz;

    public ArbolBinario() {
        this.raiz = null;
    }

    public ArbolBinario(TDAElemento<T> raiz) {
        this.raiz = raiz;
    }

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "criterioBusqueda en buscar no puede ser null");
        }

        if (this.esVacio()) {
            return null;
        }

        TDAElemento<T> encontrado = this.raiz.buscar(criterioBusqueda);

        return encontrado == null
                ? null
                : encontrado.getDato();
    }

    @Override
    public TDAElemento<T> obtenerRaiz() {
        return this.raiz;
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

        // El nodo a eliminar es la raíz
        if (criterio == 0) {
            eliminarRaiz();
            return true;
        }

        // El nodo a eliminar está debajo de la raíz
        TDAElemento<T> nodoEliminado = this.raiz.eliminar(criterioBusqueda);

        return nodoEliminado != null;
    }

    private void eliminarRaiz() {

        TDAElemento<T> hijoIzquierdo = this.raiz.getHijoIzquierdo();
        TDAElemento<T> hijoDerecho = this.raiz.getHijoDerecho();

        // CASO 1: la raíz es una hoja
        if (hijoIzquierdo == null && hijoDerecho == null) {
            this.raiz = null;
            return;
        }

        // CASO 2: solamente tiene hijo derecho
        if (hijoIzquierdo == null) {
            this.raiz = hijoDerecho;
            return;
        }

        // CASO 3: solamente tiene hijo izquierdo
        if (hijoDerecho == null) {
            this.raiz = hijoIzquierdo;
            return;
        }

        // CASO 4: tiene los dos hijos
        TDAElemento<T> padreSucesor = this.raiz;
        TDAElemento<T> sucesor = hijoDerecho;

        // buscamos el menor del subárbol derecho
        while (sucesor.getHijoIzquierdo() != null) {
            padreSucesor = sucesor;
            sucesor = sucesor.getHijoIzquierdo();
        }

        /*
         * Si el sucesor no era directamente el hijo derecho
         * de la raíz, lo sacamos de su posición original.
         */
        if (padreSucesor != this.raiz) {
            padreSucesor.setHijoIzquierdo(
                    sucesor.getHijoDerecho());

            sucesor.setHijoDerecho(hijoDerecho);
        }

        sucesor.setHijoIzquierdo(hijoIzquierdo);

        this.raiz = sucesor;
    }

    @Override
    public boolean insertar(Comparable<T> dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "dato en el metodo 'insertar' es nulo");
        }

        // si el arbol esta vacio
        if (this.raizEsNula()) {
            this.raiz = new Nodo<T>(convertirADato(dato));
            return true;
        }

        // sino, delego al nodo
        return this.raiz.insertar(dato);
    }

    @SuppressWarnings("unchecked")
    private T convertirADato(Comparable<T> dato) {
        return (T) dato;
    }

    // son lo mismo, la de arriba se (me) hace mas facil de leer
    // ademas separa las responsabilidades bros.
    // @Override
    // public boolean insertar(Comparable<T> dato) {
    // if (raizEsNula()) {
    // this.raiz = new Nodo<T>((T) dato); // ???
    // return true;
    // } else {
    // return raiz.insertar(dato);
    // }
    // }

    @Override
    public void inOrder(Consumer<T> consumidor) {
        if (!raizEsNula()) {
            this.raiz.inOrder(elemento -> consumidor.accept(elemento.getDato()));
        }
    }

    @Override
    public void preOrder(Consumer<T> consumidor) {
        if (!raizEsNula()) {
            this.raiz.preOrder(elemento -> consumidor.accept(elemento.getDato()));
        }
    }

    @Override
    public void postOrder(Consumer<T> consumidor) {
        if (!raizEsNula()) {
            this.raiz.postOrder(elemento -> consumidor.accept(elemento.getDato()));
        }
    }

    // el recorrido es O(n)
    public void porNiveles(Consumer<T> consumidor) {
        if (consumidor == null) {
            throw new IllegalArgumentException(
                    "consumidor en el metodo 'porNiveles' es nulo");
        }

        if (this.raizEsNula()) {
            return;
        }

        Cola<TDAElemento<T>> cola = new Cola<>();

        cola.poneEnCola(this.raiz);

        // aca arranca el recorrido del arbol BFS
        while (!cola.esVacio()) {

            TDAElemento<T> nodoActual = cola.frente();
            cola.quitaDeCola();

            consumidor.accept(nodoActual.getDato());

            if (nodoActual.getHijoIzquierdo() != null) {
                cola.poneEnCola(nodoActual.getHijoIzquierdo());
            }

            if (nodoActual.getHijoDerecho() != null) {
                cola.poneEnCola(nodoActual.getHijoDerecho());
            }
        }
    }

    @Override
    public boolean esVacio() {
        return this.raiz == null;
    }

    @Override
    public int cantidadNodos() {
        return this.raizEsNula() ? 0 : this.raiz.cantidadNodos();
    }

    @Override
    public int cantidadHojas() {
        return this.raizEsNula() ? 0 : this.raiz.cantidadHojas();
    }

    @Override
    public int cantidadNodosInternos() {
        return this.raizEsNula() ? 0 : this.raiz.cantidadNodosInternos();
    }

    // yo se que no hace falta pero (me) ayuda a la lectura
    private boolean raizEsNula() {
        return this.raiz == null;
    }

}
