package ucu.edu.aed.structures.element;

import ucu.edu.aed.tda.linear.TDALista;

public class NodoGeneral<T> {
    private T dato;
    private TDALista<NodoGeneral<T>> hijos;

    public T getDato() {
        return this.dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public boolean esHoja() {
        return false;
    }

    public void agregarHijo(NodoGeneral<T> hijo) {

    }

    public boolean eliminarHijo(NodoGeneral<T> nodo) {
        return false;
    }

    public NodoGeneral<T> buscar(T dato) {
        return null;
    }

    public int cantidadHijos() {
        return 0;
    }

    public int cantidadNodos() {
        return 0;
    }

    public int altura() {
        return 0;
    }

    public void recorrer() { } // a evaluar.
}
