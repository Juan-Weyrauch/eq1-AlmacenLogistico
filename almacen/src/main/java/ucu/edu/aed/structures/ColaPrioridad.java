package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDAColaPrioridad;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class ColaPrioridad<T> extends ListaSimple<T> implements TDAColaPrioridad<T> {

    private final Comparator<T> comparator;

    public ColaPrioridad(Comparator<T> comparator) {
        super();
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        this.comparator = comparator;
    }

    @Override
    public T frente() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola de prioridad está vacía");
        }
        return this.head.data;
    }

    @Override
    public boolean poneEnCola(T dato) {
        checkElementNull(dato);

        Node<T> nuevo = new Node<>(dato);

        if (this.head == null || this.comparator.compare(dato, this.head.data) > 0) {
            nuevo.next = this.head;
            this.head = nuevo;
            this.size++;
            return true;
        }

        Node<T> actual = this.head;
        while (actual.next != null && this.comparator.compare(dato, actual.next.data) <= 0) {
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
            throw new NoSuchElementException("La cola de prioridad está vacía");
        }
        return super.remover(0); // bypassea el override de abajo; O(1) real
    }

    @Override
    public void agregar(T elem) {
        poneEnCola(elem);
    }


    @Override
    public void agregar(int index, T elem) {
        throw new UnsupportedOperationException(
                "No se puede insertar en una posición arbitraria; use poneEnCola()");
    }

    @Override
    public T remover(int index) {
        throw new UnsupportedOperationException(
                "No se puede remover de una posición arbitraria; use quitaDeCola()");
    }

    @Override
    public boolean remover(T elem) {
        throw new UnsupportedOperationException(
                "No se puede remover un elemento arbitrario; use quitaDeCola()");
    }

    private void checkElementNull(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Input element cannot be null");
        }
    }
}