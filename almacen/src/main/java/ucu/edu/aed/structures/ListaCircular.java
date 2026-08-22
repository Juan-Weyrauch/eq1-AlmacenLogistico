package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDALista;

import java.util.Comparator;
import java.util.function.Predicate;

public class ListaCircular<T> implements TDALista<T> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public ListaCircular() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void agregar(T elem) {
        checkElementNull(elem);

        Node<T> nuevo = new Node<>(elem);

        if (this.head == null) {
            this.head = nuevo;
            this.tail = nuevo;

            nuevo.next = nuevo;
        } else {
            nuevo.next = this.head;

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

            this.head = nuevo;
            this.tail.next = this.head;

            this.size++;

            return;
        }

        Node<T> anterior = obtenerNodo(index - 1);

        nuevo.next = anterior.next;
        anterior.next = nuevo;

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

        if (this.size == 1) {
            T dato = this.head.data;

            this.head = null;
            this.tail = null;
            this.size = 0;

            return dato;
        }

        if (index == 0) {
            T dato = this.head.data;

            this.head = this.head.next;
            this.tail.next = this.head;
            this.size--;

            return dato;
        }

        Node<T> anterior = obtenerNodo(index - 1);
        Node<T> eliminado = anterior.next;

        anterior.next = eliminado.next;

        if (eliminado == this.tail) {
            this.tail = anterior;
        }

        this.tail.next = this.head;
        this.size--;

        return eliminado.data;
    }

    @Override
    public boolean remover(T elem) {
        checkElementNull(elem);

        if (this.size == 0) {
            return false;
        }

        if (this.head.data.equals(elem)) {
            remover(0);

            return true;
        }

        Node<T> actual = this.head;

        for (int i = 0; i < this.size - 1; i++) {

            if (actual.next.data.equals(elem)) {

                Node<T> eliminado = actual.next;

                actual.next = eliminado.next;

                if (eliminado == this.tail) {
                    this.tail = actual;
                }

                this.tail.next = this.head;
                this.size--;

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

        for (int i = 0; i < this.size; i++) {

            if (actual.data.equals(elem)) {
                return i;
            }

            actual = actual.next;
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        checkPredicateNull(criterio);

        Node<T> actual = this.head;

        for (int i = 0; i < this.size; i++) {

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

        ListaCircular<T> ordenada =
                new ListaCircular<>();

        Node<T> actual = this.head;

        for (int i = 0; i < this.size; i++) {
            ordenada.insertarOrdenado(
                    actual.data,
                    comparator);

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

        Node<T> actual = this.head;

        for (int i = 0; i < index; i++) {
            actual = actual.next;
        }

        return actual;
    }

    private void insertarOrdenado(
            T elem,
            Comparator<T> comparator) {

        if (this.size == 0) {
            agregar(elem);
            return;
        }

        if (comparator.compare(
                elem,
                this.head.data) <= 0) {

            agregar(0, elem);
            return;
        }

        Node<T> actual = this.head;

        while (actual.next != this.head
                && comparator.compare(
                        elem,
                        actual.next.data) > 0) {

            actual = actual.next;
        }

        Node<T> nuevo = new Node<>(elem);

        nuevo.next = actual.next;
        actual.next = nuevo;

        if (actual == this.tail) {
            this.tail = nuevo;
        }

        this.tail.next = this.head;
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