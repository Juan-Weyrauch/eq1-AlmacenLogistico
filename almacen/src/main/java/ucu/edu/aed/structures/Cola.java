package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDACola;

import java.util.NoSuchElementException;

public class Cola<T> extends ListaSimple<T> implements TDACola<T> {

    private Node<T> finalCola;

    public Cola() {
        super();
        this.finalCola = null;
    }

    @Override
    public T frente() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return this.head.data;
    }

    @Override
    public boolean poneEnCola(T dato) {
        checkElementNull(dato);

        Node<T> nuevo = new Node<>(dato);

        if (esVacio()) {
            this.head = nuevo;
            this.finalCola = nuevo;
        } else {
            this.finalCola.next = nuevo;
            this.finalCola = nuevo;
        }

        this.size++;
        return true;
    }

    @Override
    public T quitaDeCola() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola está vacía");
        }

        T dato = this.head.data;
        this.head = this.head.next;
        this.size--;

        if (this.head == null) {
            this.finalCola = null;
        }

        return dato;
    }

    @Override
    public void agregar(T elem) {
        poneEnCola(elem);
    }


    @Override
    public void agregar(int index, T elem) {
        throw new UnsupportedOperationException(
                "No se puede insertar en una posición arbitraria de una cola; use poneEnCola()");
    }

    @Override
    public T remover(int index) {
        throw new UnsupportedOperationException(
                "No se puede remover de una posición arbitraria de una cola; use quitaDeCola()");
    }

    @Override
    public boolean remover(T elem) {
        throw new UnsupportedOperationException(
                "No se puede remover un elemento arbitrario de una cola; use quitaDeCola()");
    }

    private void checkElementNull(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Input element cannot be null");
        }
    }
}