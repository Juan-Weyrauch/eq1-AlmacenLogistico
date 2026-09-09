package ucu.edu.aed.tda.hierarchical;

import java.util.function.Consumer;

import ucu.edu.aed.tda.linear.TDALista;

public interface TDAArbolGeneral<T> {

    boolean esVacio();

    T obtenerRaiz();

    void agregarRaiz(T dato);

    boolean agregarHijo(Comparable<T> criterioPadre, T dato);

    T buscarHijoDirecto(Comparable<T> criterioPadre, Comparable<T> criterioHijo);

    T buscar(Comparable<T> criterioBusqueda);

    boolean eliminar(Comparable<T> criterioBusqueda);

    boolean moverSubarbol(Comparable<T> criterioOrigen, Comparable<T> criterioDestino);

    TDALista<T> obtenerCamino(Comparable<T> criterioBusqueda);

    void preOrden(Consumer<T> consumidor);

    void postOrden(Consumer<T> consumidor);

    void porNiveles(Consumer<T> consumidor);

    void recorrerSubarbolPreOrden(Comparable<T> criterioRaiz, Consumer<T> consumidor);

    int cantidadNodos();

    int altura();
}
