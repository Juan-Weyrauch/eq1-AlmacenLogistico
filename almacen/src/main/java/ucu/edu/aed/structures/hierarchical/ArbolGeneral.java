package ucu.edu.aed.structures.hierarchical;

import java.util.function.Consumer;

import ucu.edu.aed.structures.element.NodoGeneral;
import ucu.edu.aed.structures.linear.Cola;
import ucu.edu.aed.structures.linear.ListaSimple;
import ucu.edu.aed.tda.hierarchical.TDAArbolGeneral;
import ucu.edu.aed.tda.linear.TDALista;

public class ArbolGeneral<T> implements TDAArbolGeneral<T> {

    private NodoGeneral<T> raiz;

    @Override
    public boolean esVacio() {
        return this.raiz == null;
    }

    @Override
    public T obtenerRaiz() {
        return this.raiz == null ? null : this.raiz.getDato();
    }

    @Override
    public void agregarRaiz(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: dato en el metodo 'agregarRaiz' es nulo");
        }

        if (this.raiz != null) {
            throw new IllegalStateException(
                    "ArbolGeneral: ya existe una raiz");
        }

        this.raiz = new NodoGeneral<>(dato);
    }

    @Override
    public boolean agregarHijo(Comparable<T> criterioPadre, T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: dato en el metodo 'agregarHijo' es nulo");
        }

        NodoGeneral<T> nodoPadre = this.buscarNodo(criterioPadre);

        if (nodoPadre == null) {
            return false;
        }

        nodoPadre.agregarHijo(new NodoGeneral<>(dato));

        return true;
    }

    @Override
    public T buscarHijoDirecto(Comparable<T> criterioPadre, Comparable<T> criterioHijo) {
        if (criterioHijo == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: criterioHijo en el metodo 'buscarHijoDirecto' es nulo");
        }

        NodoGeneral<T> nodoPadre = this.buscarNodo(criterioPadre);

        if (nodoPadre == null) {
            return null;
        }

        TDALista<NodoGeneral<T>> hijos = nodoPadre.obtenerHijos();

        for (int i = 0; i < hijos.tamaño(); i++) {
            NodoGeneral<T> hijo = hijos.obtener(i);

            if (criterioHijo.compareTo(hijo.getDato()) == 0) {
                return hijo.getDato();
            }
        }

        return null;
    }

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        NodoGeneral<T> encontrado = this.buscarNodo(criterioBusqueda);

        return encontrado == null ? null : encontrado.getDato();
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: criterioBusqueda en el metodo 'eliminar' es nulo");
        }

        if (this.raiz == null) {
            return false;
        }

        if (criterioBusqueda.compareTo(this.raiz.getDato()) == 0) {
            this.raiz = null;
            return true;
        }

        return this.eliminarRecursivo(this.raiz, criterioBusqueda);
    }

    @Override
    public boolean moverSubarbol(Comparable<T> criterioOrigen, Comparable<T> criterioDestino) {
        if (criterioOrigen == null || criterioDestino == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: criterioOrigen/criterioDestino en el metodo 'moverSubarbol' es nulo");
        }

        NodoGeneral<T> nodoOrigen = this.buscarNodo(criterioOrigen);
        NodoGeneral<T> nodoDestino = this.buscarNodo(criterioDestino);

        if (nodoOrigen == null || nodoDestino == null) {
            return false;
        }

        if (nodoOrigen == this.raiz) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: no se puede mover la raiz");
        }

        if (this.contieneNodo(nodoOrigen, nodoDestino)) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: el destino no puede pertenecer al propio subarbol que se intenta mover");
        }

        this.desconectar(this.raiz, nodoOrigen);
        nodoDestino.agregarHijo(nodoOrigen);

        return true;
    }

    @Override
    public TDALista<T> obtenerCamino(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: criterioBusqueda en el metodo 'obtenerCamino' es nulo");
        }

        TDALista<T> camino = new ListaSimple<>();

        if (this.raiz != null) {
            this.construirCamino(this.raiz, criterioBusqueda, camino);
        }

        return camino;
    }

    @Override
    public void preOrden(Consumer<T> consumidor) {
        this.validarConsumidor(consumidor, "preOrden");

        if (this.raiz != null) {
            this.raiz.preOrder(nodo -> consumidor.accept(nodo.getDato()));
        }
    }

    @Override
    public void postOrden(Consumer<T> consumidor) {
        this.validarConsumidor(consumidor, "postOrden");

        if (this.raiz != null) {
            this.raiz.postOrder(nodo -> consumidor.accept(nodo.getDato()));
        }
    }

    @Override
    public void porNiveles(Consumer<T> consumidor) {
        this.validarConsumidor(consumidor, "porNiveles");

        if (this.raiz == null) {
            return;
        }

        Cola<NodoGeneral<T>> cola = new Cola<>();
        cola.poneEnCola(this.raiz);

        while (!cola.esVacio()) {
            NodoGeneral<T> actual = cola.frente();
            cola.quitaDeCola();

            consumidor.accept(actual.getDato());

            TDALista<NodoGeneral<T>> hijos = actual.obtenerHijos();

            for (int i = 0; i < hijos.tamaño(); i++) {
                cola.poneEnCola(hijos.obtener(i));
            }
        }
    }

    @Override
    public void recorrerSubarbolPreOrden(Comparable<T> criterioRaiz, Consumer<T> consumidor) {
        this.validarConsumidor(consumidor, "recorrerSubarbolPreOrden");

        NodoGeneral<T> nodo = this.buscarNodo(criterioRaiz);

        if (nodo != null) {
            nodo.preOrder(n -> consumidor.accept(n.getDato()));
        }
    }

    @Override
    public int cantidadNodos() {
        return this.raiz == null ? 0 : this.raiz.cantidadNodos();
    }

    @Override
    public int altura() {
        return this.raiz == null ? 0 : this.raiz.altura();
    }

    private NodoGeneral<T> buscarNodo(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: criterioBusqueda es nulo");
        }

        return this.raiz == null ? null : this.raiz.buscar(criterioBusqueda);
    }

    private boolean eliminarRecursivo(NodoGeneral<T> actual, Comparable<T> criterio) {
        TDALista<NodoGeneral<T>> hijos = actual.obtenerHijos();

        for (int i = 0; i < hijos.tamaño(); i++) {
            NodoGeneral<T> hijo = hijos.obtener(i);

            if (criterio.compareTo(hijo.getDato()) == 0) {
                actual.eliminarHijo(hijo);
                return true;
            }

            if (this.eliminarRecursivo(hijo, criterio)) {
                return true;
            }
        }

        return false;
    }

    private boolean desconectar(NodoGeneral<T> actual, NodoGeneral<T> objetivo) {
        TDALista<NodoGeneral<T>> hijos = actual.obtenerHijos();

        for (int i = 0; i < hijos.tamaño(); i++) {
            NodoGeneral<T> hijo = hijos.obtener(i);

            if (hijo == objetivo) {
                actual.eliminarHijo(hijo);
                return true;
            }

            if (this.desconectar(hijo, objetivo)) {
                return true;
            }
        }

        return false;
    }

    private boolean contieneNodo(NodoGeneral<T> actual, NodoGeneral<T> objetivo) {
        if (actual == objetivo) {
            return true;
        }

        TDALista<NodoGeneral<T>> hijos = actual.obtenerHijos();

        for (int i = 0; i < hijos.tamaño(); i++) {
            if (this.contieneNodo(hijos.obtener(i), objetivo)) {
                return true;
            }
        }

        return false;
    }

    private boolean construirCamino(
            NodoGeneral<T> actual,
            Comparable<T> criterio,
            TDALista<T> caminoAcumulado) {

        caminoAcumulado.agregar(actual.getDato());

        if (criterio.compareTo(actual.getDato()) == 0) {
            return true;
        }

        TDALista<NodoGeneral<T>> hijos = actual.obtenerHijos();

        for (int i = 0; i < hijos.tamaño(); i++) {
            if (this.construirCamino(hijos.obtener(i), criterio, caminoAcumulado)) {
                return true;
            }
        }

        caminoAcumulado.remover(caminoAcumulado.tamaño() - 1);

        return false;
    }

    private void validarConsumidor(Consumer<T> consumidor, String metodo) {
        if (consumidor == null) {
            throw new IllegalArgumentException(
                    "ArbolGeneral: consumidor en el metodo '"
                            + metodo
                            + "' es nulo");
        }
    }
}
