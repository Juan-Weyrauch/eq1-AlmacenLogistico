package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDALista;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Cola<T> implements TDACola<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> frente;
    private Node<T> finalCola;
    private int size;

    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.size = 0;
    }

    // =============== OPERACIONES DE Cola ==============

    @Override
    public T frente() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola está vacía");
        }

        return this.frente.data;
    }

    @Override
    public boolean poneEnCola(T dato) {
        checkElementNull(dato);

        Node<T> nuevo = new Node<>(dato);

        if (esVacio()) {
            this.frente = nuevo;
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

        T dato = this.frente.data;
        this.frente = this.frente.next;
        this.size--;

        if (this.frente == null) {
            this.finalCola = null;
        }

        return dato;
    }

    // =============== OPERACIONES DE TDALista ==============

    @Override
    public void agregar(T elem) {
        poneEnCola(elem);
    }

    @Override
    public void agregar(int index, T elem) {
        checkElementNull(elem);
        checkIndexForAdd(index);

        if (index == this.size) {
            poneEnCola(elem);
            return;
        }

        Node<T> nuevo = new Node<>(elem);

        if (index == 0) {
            nuevo.next = this.frente;
            this.frente = nuevo;

            if (this.size == 0) {
                this.finalCola = nuevo;
            }

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

        if (index == 0) {
            return quitaDeCola();
        }

        Node<T> anterior = obtenerNodo(index - 1);
        Node<T> eliminado = anterior.next;

        anterior.next = eliminado.next;

        if (eliminado == this.finalCola) {
            this.finalCola = anterior;
        }

        this.size--;

        return eliminado.data;
    }

    @Override
    public boolean remover(T elem) {
        checkElementNull(elem);

        if (esVacio()) {
            return false;
        }

        if (this.frente.data.equals(elem)) {
            quitaDeCola();
            return true;
        }

        Node<T> actual = this.frente;

        while (actual.next != null) {

            if (actual.next.data.equals(elem)) {

                if (actual.next == this.finalCola) {
                    this.finalCola = actual;
                }

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

        Node<T> actual = this.frente;
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
        if (criterio == null) {
            throw new IllegalArgumentException("Predicate cannot be null");
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
    public TDALista<T> ordenar(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        Cola<T> ordenada = new Cola<>();

        Node<T> actual = this.frente;

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
        this.frente = null;
        this.finalCola = null;
        this.size = 0;
    }

    // =================== HELPER METHODS ====================

    private Node<T> obtenerNodo(int index) {
        Node<T> actual = this.frente;

        for (int i = 0; i < index; i++) {
            actual = actual.next;
        }

        return actual;
    }

    private void insertarOrdenado(T elem, Comparator<T> comparator) {
        Node<T> nuevo = new Node<>(elem);

        if (esVacio()) {
            this.frente = nuevo;
            this.finalCola = nuevo;
            this.size++;
            return;
        }

        if (comparator.compare(elem, this.frente.data) <= 0) {
            nuevo.next = this.frente;
            this.frente = nuevo;
            this.size++;
            return;
        }

        Node<T> actual = this.frente;

        while (actual.next != null
                && comparator.compare(elem, actual.next.data) > 0) {
            actual = actual.next;
        }

        nuevo.next = actual.next;
        actual.next = nuevo;

        if (nuevo.next == null) {
            this.finalCola = nuevo;
        }

        this.size++;
    }

    private void checkIndexOutOfBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. Queue size: "
                            + this.size + ", index: " + index);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. Queue size: "
                            + this.size + ", index: " + index);
        }
    }

    private void checkElementNull(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException(
                    "Input element cannot be null");
        }
    }
}