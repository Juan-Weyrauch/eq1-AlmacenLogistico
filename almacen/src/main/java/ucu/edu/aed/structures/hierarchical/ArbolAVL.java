package ucu.edu.aed.structures.hierarchical;

import ucu.edu.aed.structures.element.NodoAVL;
import ucu.edu.aed.tda.element.TDAElemento;
import ucu.edu.aed.tda.hierarchical.TDAArbolBinario;

public class ArbolAVL<T> extends ArbolBinarioBusqueda<T> implements TDAArbolBinario<T> {

    @Override
    public boolean insertar(Comparable<T> dato) {
        if (dato == null) return false;

        if (raizEsNula()) {
            establecerRaiz(new NodoAVL<>(convertirADato(dato)));
            return true;
        }

        boolean[] insertado = {false};

        TDAElemento<T> nuevaRaiz = ((NodoAVL<T>) obtenerRaiz()).insertar(dato, insertado);
        establecerRaiz(nuevaRaiz);

        return insertado[0];
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) return false;

        if (raizEsNula()) return false;

        boolean[] eliminado = {false};

        TDAElemento<T> nuevaRaiz = ((NodoAVL<T>) obtenerRaiz()).eliminar(criterioBusqueda, eliminado);
        establecerRaiz(nuevaRaiz);

        return eliminado[0];
    }
}