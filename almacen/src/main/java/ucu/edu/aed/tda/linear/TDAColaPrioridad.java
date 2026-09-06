package ucu.edu.aed.tda.linear;

import java.util.function.Predicate;

public interface TDAColaPrioridad<T> {

    T frente();

    boolean poneEnCola(T dato);

    T quitaDeCola();

    T buscar(Predicate<T> criterio);

    int tamaño();

    boolean esVacio();

    void vaciar();
}