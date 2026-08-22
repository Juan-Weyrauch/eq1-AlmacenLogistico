package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDALista;

import java.util.Comparator;
import java.util.function.Predicate;

public class ListaDoble<T> implements TDALista<T> {

    private static class Node<E> {
        E data;
        Node<E> previous;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.previous = null;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public ListaDoble() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void agregar(T elem) {
        checkElementNull(elem);

        Node<T> nuevo = new Node<>(elem);

        if (this.tail == null) {
            this.head = nuevo;
            this.tail = nuevo;
        } else {
            nuevo.previous = this.tail;
            this.tail.next = nuevo;
            this.tail = nuevo;
        }

        this.size++;
    }

    @Override
    public void agregar(int index, T elem) {
        checkElementNull(elem);
        checkIndexForAdd(index);

        if (index == this.size) {
            agregar(elem);
            return;
        }

        Node<T> nuevo = new Node<>(elem);

        if (index == 0) {
            nuevo.next = this.head;

            if (this.head != null) {
                this.head.previous = nuevo;
            }

            this.head = nuevo;

            if (this.tail == null) {
                this.tail = nuevo;
            }

            this.size++;

            return;
        }

        Node<T> actual = obtenerNodo(index);
        Node<T> anterior = actual.previous;

        nuevo.previous = anterior;
        nuevo.next = actual;

        anterior.next = nuevo;
        actual.previous = nuevo;

        this.size++;
    }

    @Override
    public T obtener(int index) {
        checkIndexOutOfBounds(index);

        return obtenerNodo(index).data;
    }

    @Override
    public T remover(int index) {
        checkIndexOutOfBounds(index);

        return removerNodo(obtenerNodo(index));
    }

    @Override
    public boolean remover(T elem) {
        checkElementNull(elem);

        Node<T> actual = this.head;

        while (actual != null) {

            if (actual.data.equals(elem)) {
                removerNodo(actual);
                return true;
            }

            actual = actual.next;
        }

        return false;
    }

    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) != -1;
    }

    @Override
    public int indiceDe(T elem) {
        checkElementNull(elem);

        Node<T> actual = this.head;
        int index = 0;

        while (actual != null) {

            if (actual.data.equals(elem)) {
                return index;
            }

            actual = actual.next;
            index++;
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        checkPredicateNull(criterio);

        Node<T> actual = this.head;

        while (actual != null) {

            if (criterio.test(actual.data)) {
                return actual.data;
            }

            actual = actual.next;
        }

        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        checkComparatorNull(comparator);

        ListaDoble<T> ordenada = new ListaDoble<>();
        Node<T> actual = this.head;

        while (actual != null) {
            ordenada.insertarOrdenado(actual.data, comparator);
            actual = actual.next;
        }

        return ordenada;
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
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // =================== HELPER METHODS ====================

    private Node<T> obtenerNodo(int index) {

        if (index < this.size / 2) {

            Node<T> actual = this.head;

            for (int i = 0; i < index; i++) {
                actual = actual.next;
            }

            return actual;
        }

        Node<T> actual = this.tail;

        for (int i = this.size - 1; i > index; i--) {
            actual = actual.previous;
        }

        return actual;
    }

    private T removerNodo(Node<T> nodo) {

        if (nodo.previous == null) {
            this.head = nodo.next;
        } else {
            nodo.previous.next = nodo.next;
        }

        if (nodo.next == null) {
            this.tail = nodo.previous;
        } else {
            nodo.next.previous = nodo.previous;
        }

        this.size--;

        return nodo.data;
    }

    private void insertarOrdenado(
            T elem,
            Comparator<T> comparator) {

        if (this.head == null) {
            agregar(elem);
            return;
        }

        if (comparator.compare(elem, this.head.data) <= 0) {
            agregar(0, elem);
            return;
        }

        if (comparator.compare(elem, this.tail.data) >= 0) {
            agregar(elem);
            return;
        }

        Node<T> actual = this.head.next;

        while (actual != null
                && comparator.compare(elem, actual.data) > 0) {

            actual = actual.next;
        }

        Node<T> nuevo = new Node<>(elem);
        Node<T> anterior = actual.previous;

        nuevo.previous = anterior;
        nuevo.next = actual;

        anterior.next = nuevo;
        actual.previous = nuevo;

        this.size++;
    }

    private void checkIndexOutOfBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. List size: "
                            + this.size + ", index: " + index);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. List size: "
                            + this.size + ", index: " + index);
        }
    }

    private void checkElementNull(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException(
                    "Input element cannot be null");
        }
    }

    private void checkPredicateNull(Predicate<T> criterio) {
        if (criterio == null) {
            throw new IllegalArgumentException(
                    "Predicate cannot be null");
        }
    }

    private void checkComparatorNull(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException(
                    "Comparator cannot be null");
        }
    }
}