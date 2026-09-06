package ucu.edu.aed.structures.hierarchical;

import ucu.edu.aed.structures.element.NodoGeneral;

public class ArbolGeneral<T> {
    private NodoGeneral<T> raiz;

    public NodoGeneral<T> obtenerRaiz() {
        return this.raiz;
    }

    public boolean esVacio() {
        return this.raiz == null;
    }

    public NodoGeneral<T> buscar() {
        return null;
    }

    public void insertar(NodoGeneral<T> nodo) {

    }

    public void eliminar(NodoGeneral<T> nodo) {

    }

    public void cantidadNodos() {

    }

    public void altura() {

    }

    public void moverSubarbol(NodoGeneral<T> nodoPadre) {

    }
}
