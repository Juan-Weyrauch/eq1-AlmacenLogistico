package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDAColaPrioridad;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ColaPrioridad<T>
        implements TDAColaPrioridad<T> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> frente;
    private int size;
    private final Comparator<T> comparator;

    public ColaPrioridad(Comparator<T> comparator) {

        if (comparator == null) {
            throw new IllegalArgumentException(
                    "Comparator cannot be null");
        }

        this.frente = null;
        this.size = 0;
        this.comparator = comparator;
    }

    @Override
    public T frente() {

        if (esVacio()) {
            throw new NoSuchElementException(
                    "La cola de prioridad está vacía");
        }

        return this.frente.data;
    }

    @Override
    public boolean poneEnCola(T dato) {
        checkElementNull(dato);

        Node<T> nuevo = new Node<>(dato);

        /*
         * Si la cola está vacía o el nuevo elemento
         * tiene mayor prioridad que el frente,
         * pasa a ser el nuevo frente.
         */
        if (this.frente == null
                || this.comparator.compare(
                        dato,
                        this.frente.data) > 0) {

            nuevo.next = this.frente;
            this.frente = nuevo;

            this.size++;

            return true;
        }

        Node<T> actual = this.frente;

        /*
         * Avanzamos mientras el siguiente elemento tenga
         * prioridad mayor o igual que el nuevo.
         *
         * El <= 0 permite que, cuando dos elementos tienen
         * la misma prioridad, el nuevo quede después de los
         * que llegaron antes.
         *
         * De esta manera se respeta FIFO en caso de empate.
         */
        while (actual.next != null
                && this.comparator.compare(
                        dato,
                        actual.next.data) <= 0) {

            actual = actual.next;
        }

        nuevo.next = actual.next;
        actual.next = nuevo;

        this.size++;

        return true;
    }

    @Override
    public T quitaDeCola() {

        if (esVacio()) {
            throw new NoSuchElementException(
                    "La cola de prioridad está vacía");
        }

        T dato = this.frente.data;

        this.frente = this.frente.next;
        this.size--;

        return dato;
    }

    @Override
    public T buscar(Predicate<T> criterio) {

        if (criterio == null) {
            throw new IllegalArgumentException(
                    "Predicate cannot be null");
        }

        Node<T> actual = this.frente;

        while (actual != null) {

            if (criterio.test(actual.data)) {
                return actual.data;
            }

            actual = actual.next;
        }

        return null;
    }

    @Override
    public int tamaño() {
        return this.size;
    }

    @Override
    public boolean esVacio() {
        return this.size == 0;
    }

    @Override
    public void vaciar() {
        this.frente = null;
        this.size = 0;
    }

    // =================== HELPER METHODS ====================

    private void checkElementNull(T elem) {

        if (elem == null) {
            throw new IllegalArgumentException(
                    "Input element cannot be null");
        }
    }
}