package ucu.edu.aed.structures.element;

import java.util.function.Consumer;

import ucu.edu.aed.structures.linear.ListaSimple;
import ucu.edu.aed.tda.linear.TDALista;

public class NodoGeneral<T> {

    private T dato;
    private final TDALista<NodoGeneral<T>> hijos;

    public NodoGeneral(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "NodoGeneral: dato en el constructor es nulo");
        }

        this.dato = dato;
        this.hijos = new ListaSimple<>();
    }

    public T getDato() {
        return this.dato;
    }

    public void setDato(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "NodoGeneral: dato en el metodo 'setDato' es nulo");
        }

        this.dato = dato;
    }

    public TDALista<NodoGeneral<T>> obtenerHijos() {
        return this.hijos;
    }

    public void agregarHijo(NodoGeneral<T> hijo) {
        if (hijo == null) {
            throw new IllegalArgumentException(
                    "NodoGeneral: hijo en el metodo 'agregarHijo' es nulo");
        }

        this.hijos.agregar(hijo);
    }

    public boolean eliminarHijo(NodoGeneral<T> nodo) {
        return this.hijos.remover(nodo);
    }

    public int cantidadHijos() {
        return this.hijos.tamaño();
    }

    public NodoGeneral<T> buscar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "NodoGeneral: criterioBusqueda en el metodo 'buscar' es nulo");
        }

        if (criterioBusqueda.compareTo(this.dato) == 0) {
            return this;
        }

        for (int i = 0; i < this.hijos.tamaño(); i++) {
            NodoGeneral<T> encontrado =
                    this.hijos.obtener(i).buscar(criterioBusqueda);

            if (encontrado != null) {
                return encontrado;
            }
        }

        return null;
    }

    public void preOrder(Consumer<NodoGeneral<T>> consumidor) {
        validarConsumidor(consumidor, "preOrder");

        consumidor.accept(this);

        for (int i = 0; i < this.hijos.tamaño(); i++) {
            this.hijos.obtener(i).preOrder(consumidor);
        }
    }

    public void postOrder(Consumer<NodoGeneral<T>> consumidor) {
        validarConsumidor(consumidor, "postOrder");

        for (int i = 0; i < this.hijos.tamaño(); i++) {
            this.hijos.obtener(i).postOrder(consumidor);
        }

        consumidor.accept(this);
    }

    public boolean esHoja() {
        return this.hijos.esVacio();
    }

    public int cantidadNodos() {
        int cantidad = 1;

        for (int i = 0; i < this.hijos.tamaño(); i++) {
            cantidad += this.hijos.obtener(i).cantidadNodos();
        }

        return cantidad;
    }

    public int altura() {
        if (this.esHoja()) {
            return 1;
        }

        int alturaMaxima = 0;

        for (int i = 0; i < this.hijos.tamaño(); i++) {
            alturaMaxima = Math.max(alturaMaxima, this.hijos.obtener(i).altura());
        }

        return 1 + alturaMaxima;
    }

    private void validarConsumidor(Consumer<?> consumidor, String metodo) {
        if (consumidor == null) {
            throw new IllegalArgumentException(
                    "NodoGeneral: consumidor en el metodo '"
                            + metodo
                            + "' es nulo");
        }
    }
}
