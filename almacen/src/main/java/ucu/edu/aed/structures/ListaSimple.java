package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDALista;

import java.util.Comparator;
import java.util.function.Predicate;

public class ListaSimple<T> implements TDALista<T> {

    protected static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    protected Node<T> head;
    protected int size;

    public ListaSimple() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public void agregar(T elem) {
        checkElementNull(elem);

        Node<T> nuevo = new Node<>(elem);

        if (this.head == null) {
            this.head = nuevo;
        } else {
            Node<T> actual = this.head;

            while (actual.next != null) {
                actual = actual.next;
            }

            actual.next = nuevo;
        }

        this.size++;
    }

    @Override
    public void agregar(int index, T elem) {
        checkElementNull(elem);
        checkIndexForAdd(index);

        Node<T> nuevo = new Node<>(elem);

        if (index == 0) {
            nuevo.next = this.head;
            this.head = nuevo;
        } else {
            Node<T> anterior = obtenerNodo(index - 1);

            nuevo.next = anterior.next;
            anterior.next = nuevo;
        }

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

        Node<T> eliminado;

        if (index == 0) {
            eliminado = this.head;
            this.head = this.head.next;
        } else {
            Node<T> anterior = obtenerNodo(index - 1);

            eliminado = anterior.next;
            anterior.next = eliminado.next;
        }

        this.size--;

        return eliminado.data;
    }

    @Override
    public boolean remover(T elem) {
        checkElementNull(elem);

        if (this.head == null) {
            return false;
        }

        if (this.head.data.equals(elem)) {
            this.head = this.head.next;
            this.size--;

            return true;
        }

        Node<T> actual = this.head;

        while (actual.next != null) {

            if (actual.next.data.equals(elem)) {
                actual.next = actual.next.next;
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

        ListaSimple<T> ordenada = new ListaSimple<>();
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

    private void insertarOrdenado(T elem, Comparator<T> comparator) {
        Node<T> nuevo = new Node<>(elem);

        if (this.head == null
                || comparator.compare(elem, this.head.data) <= 0) {

            nuevo.next = this.head;
            this.head = nuevo;
            this.size++;

            return;
        }

        Node<T> actual = this.head;

        while (actual.next != null
                && comparator.compare(elem, actual.next.data) > 0) {

            actual = actual.next;
        }

        nuevo.next = actual.next;
        actual.next = nuevo;

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