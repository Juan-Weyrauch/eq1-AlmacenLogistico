package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDALista;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ColaCircular<T> implements TDACola<T> {

    // This the whole queue bru
    private T[] vector;

    // Posición del primer elemento
    private int frente;

    // Posición donde se insertará el siguiente elemento
    private int finalCola;

    // Cantidad actual de elementos
    private int size;

    // Tamaño máximo del buffer
    private int capacidad;

    public ColaCircular(int capacidad) {

        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }

        this.capacidad = capacidad;
        this.vector = (T[]) new Object[capacidad];
        this.frente = 0;
        this.finalCola = 0;
        this.size = 0;
    }

    @Override
    public boolean poneEnCola(T dato) {
        checkElementNull(dato);

        // Cola llena
        if (size == capacidad) {
            return false;
        }

        vector[finalCola] = dato;

        // Wraparound
        finalCola = (finalCola + 1) % capacidad;
        size++;

        return true;
    }

    @Override
    public T quitaDeCola() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola circular está vacía");
        }

        T removedData = vector[frente];
        vector[frente] = null;

        // Wraparound
        frente = (frente + 1) % capacidad;

        size--;

        return removedData;
    }

    @Override
    public T frente() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola circular está vacía");
        }

        return vector[frente];
    }

    @Override
    public void agregar(T elem) {
        checkElementNull(elem);

        if (!poneEnCola(elem)) {
            throw new IllegalStateException("La cola circular está llena");
        }
    }

    @Override
    public void agregar(int index, T elem) {
        checkElementNull(elem);
        checkIndexForAdd(index);

        if (size == capacidad) {
            throw new IllegalStateException("La cola circular está llena");
        }

        // Si se agrega al final, equivale a ponerEnCola
        if (index == size) {
            poneEnCola(elem);
            return;
        }

        /*
         * Mover los elementos una posición hacia la derecha
         * usando índices lógicos de la cola.
         */
        for (int i = size; i > index; i--) {
            vector[physicalIndex(i)] = vector[physicalIndex(i - 1)];
        }

        vector[physicalIndex(index)] = elem;

        size++;

        finalCola = (frente + size) % capacidad;
    }

    @Override
    public T obtener(int index) {
        checkIndexOutOfBounds(index);

        return vector[physicalIndex(index)];
    }

    @Override
    public T remover(int index) {
        checkIndexOutOfBounds(index);

        // Remover el frente puede hacerse en O(1)
        if (index == 0) {
            return quitaDeCola();
        }

        T dato = vector[physicalIndex(index)];

        // Desplazar los elementos posteriores
        for (int i = index; i < size - 1; i++) {
            vector[physicalIndex(i)] = vector[physicalIndex(i + 1)];
        }

        // Limpiar la última posición ocupada
        vector[physicalIndex(size - 1)] = null;

        size--;

        finalCola = (frente + size) % capacidad;

        return dato;
    }

    @Override
    public boolean remover(T elem) {
        checkElementNull(elem);

        int index = indiceDe(elem);

        if (index == -1) {
            return false;
        }

        remover(index);

        return true;
    }

    @Override
    public boolean contiene(T elem) {
        checkElementNull(elem);

        for (int i = 0; i < size; i++) {

            if (vector[physicalIndex(i)].equals(elem)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int indiceDe(T elem) {
        checkElementNull(elem);

        for (int i = 0; i < size; i++) {

            if (vector[physicalIndex(i)].equals(elem)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate cannot be null");
        }

        for (int i = 0; i < size; i++) {

            T dato = vector[physicalIndex(i)];

            if (predicate.test(dato)) {
                return dato;
            }
        }

        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        ListaSimple<T> sorted = new ListaSimple<>();

        for (int i = 0; i < size; i++) {

            T elem = vector[physicalIndex(i)];

            int position = 0;

            while (position < sorted.tamaño()
                    && comparator.compare(
                    sorted.obtener(position), elem) <= 0) {

                position++;
            }

            sorted.agregar(position, elem);
        }

        return sorted;
    }

    @Override
    public int tamaño() {
        return this.size;
    }

    @Override
    public boolean esVacio() {
        return this.size == 0;
    }

    public boolean estaLlena() {
        return this.size == this.capacidad;
    }

    @Override
    public void vaciar() {

        for (int i = 0; i < capacidad; i++) {
            vector[i] = null;
        }

        frente = 0;
        finalCola = 0;
        size = 0;
    }

    /*
     * Convierte un índice lógico de la cola
     * en la posición real dentro del vector.
     *
     * Ejemplo:
     *
     * frente = 3
     * capacidad = 5
     *
     * logicalIndex 0 → physicalIndex 3
     * logicalIndex 1 → physicalIndex 4
     * logicalIndex 2 → physicalIndex 0
     * logicalIndex 3 → physicalIndex 1
     */
    private int physicalIndex(int logicalIndex) {
        return (frente + logicalIndex) % capacidad;
    }

    private void checkIndexOutOfBounds(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. Queue size: "
                            + size
                            + ", index: "
                            + index);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. Queue size: "
                            + size
                            + ", index: "
                            + index);
        }
    }

    private void checkElementNull(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException(
                    "Input element cannot be null");
        }
    }
}