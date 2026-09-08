package ucu.edu.aed.structures.hierarchical;

import ucu.edu.aed.structures.element.NodoAVL;
import ucu.edu.aed.tda.element.TDAElemento;
import ucu.edu.aed.tda.hierarchical.TDAArbolBinario;

public class ArbolAVL<T> extends ArbolBinario<T> implements TDAArbolBinario<T> {

    @Override
    public boolean insertar(Comparable<T> dato) {
        if (dato == null) return false;

        if (esVacio()) {
            raiz = new NodoAVL<>(convertirADato(dato));
            return true;
        }

        boolean[] insertado = {false};

        TDAElemento<T> nuevaRaiz = ((NodoAVL<T>) raiz).insertar(dato, insertado);
        raiz = nuevaRaiz;

        return insertado[0];
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) return false;

        if (esVacio()) return false;

        boolean[] eliminado = {false};

        TDAElemento<T> nuevaRaiz = ((NodoAVL<T>) raiz).eliminar(criterioBusqueda, eliminado);
        raiz = nuevaRaiz;

        return eliminado[0];
    }

    @SuppressWarnings("unchecked")
    private T convertirADato(Comparable<T> dato) {
        return (T) dato;
    }
}