package ucu.edu.aed.structures.linear;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Heap máximo genérico sobre ListaArray<T>. El elemento de mayor prioridad
 * (según el Comparator provisto) siempre queda en el índice 0.
 *
 * Esta clase NO sabe nada sobre políticas de desempate, dominio, ni nada
 * fuera de "mantener el invariante de heap según el Comparator que le den".
 * Cualquier lógica de negocio (como el desempate FIFO) vive en quien la usa.
 *
 * Invariante de heap: para todo índice i con hijos dentro de rango,
 * valor(i) >= valor(hijoIzquierdo(i)) y valor(i) >= valor(hijoDerecho(i)).
 *
 * Complejidades:
 *   poneEnCola() : O(log n)
 *   quitaDeCola(): O(log n)
 *   frente()     : O(1)
 *   buscar()     : O(n)
 *   reordenar()  : O(n) localizar + O(log n) reacomodar
 */
public class Heap<T> {

    private final ListaArray<T> datos;
    private final Comparator<T> comparador;

    public Heap(Comparator<T> comparador) {
        if (comparador == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        this.datos = new ListaArray<>();
        this.comparador = comparador;
    }

    public int tamaño() {
        return datos.tamaño();
    }

    public boolean esVacio() {
        return datos.esVacio();
    }

    public T frente() {
        return datos.obtener(0);
    }

    public void poneEnCola(T elemento) {
        datos.agregar(elemento);
        heapifyUp(datos.tamaño() - 1);
    }

    public T quitaDeCola() {
        T raiz = datos.obtener(0);
        int ultimoIndice = datos.tamaño() - 1;
        T ultimo = datos.obtener(ultimoIndice);
        datos.remover(ultimoIndice); // índice == size-1 -> sin corrimiento, O(1)

        if (!datos.esVacio()) {
            datos.reemplazar(0, ultimo);
            heapifyDown(0);
        }
        return raiz;
    }

    public T buscar(Predicate<T> criterio) {
        return datos.buscar(criterio);
    }

    public void reordenar(Predicate<T> criterio) {
        int indice = localizarIndice(criterio);
        if (indice == -1) {
            throw new NoSuchElementException("El elemento no está en el heap");
        }
        heapifyUp(indice);
        heapifyDown(indice);
    }

    public void vaciar() {
        datos.vaciar();
    }

    // =================== OPERACIONES INTERNAS DEL HEAP (Parte B) ====================

    private int padre(int indice) {
        return (indice - 1) / 2;
    }

    private int hijoIzquierdo(int indice) {
        return 2 * indice + 1;
    }

    private int hijoDerecho(int indice) {
        return 2 * indice + 2;
    }

    private void swap(int i, int j) {
        T valorI = datos.obtener(i);
        T valorJ = datos.obtener(j);
        datos.reemplazar(i, valorJ);
        datos.reemplazar(j, valorI);
    }

    private void heapifyUp(int indice) {
        while (indice > 0) {
            int indicePadre = padre(indice);
            if (comparador.compare(datos.obtener(indice), datos.obtener(indicePadre)) > 0) {
                swap(indice, indicePadre);
                indice = indicePadre;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int indice) {
        int n = datos.tamaño();
        while (true) {
            int izquierdo = hijoIzquierdo(indice);
            int derecho = hijoDerecho(indice);
            int mayor = indice;

            if (izquierdo < n && comparador.compare(datos.obtener(izquierdo), datos.obtener(mayor)) > 0) {
                mayor = izquierdo;
            }
            if (derecho < n && comparador.compare(datos.obtener(derecho), datos.obtener(mayor)) > 0) {
                mayor = derecho;
            }
            if (mayor == indice) {
                break;
            }
            swap(indice, mayor);
            indice = mayor;
        }
    }

    private int localizarIndice(Predicate<T> criterio) {
        for (int i = 0; i < datos.tamaño(); i++) {
            if (criterio.test(datos.obtener(i))) {
                return i;
            }
        }
        return -1;
    }
}