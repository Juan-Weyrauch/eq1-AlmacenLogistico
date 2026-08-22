package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDALista;

import java.util.Comparator;
import java.util.function.Predicate;

public class Lista<T> implements TDALista<T> {

    protected class Node<T> {
        T data;
        Node<T> next;
    }

    private Node<T> head;
    private int size;

    public Lista() {
        this.head = null;
        this.size = 0;
    }
    
    @Override
    public void agregar(T elem) {
        checkElementNull(elem);

        Node<T> input = new Node<>();
        input.data = elem;

        if (this.head == null) {
            head = input;
        } else {
            Node<T> currentNode = head;

            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }

            currentNode.next = input;
        }

        this.size++;
    }

    @Override
    public void agregar(int index, T elem) {
        checkElementNull(elem);
        checkIndexForAdd(index);

        Node<T> input = new Node<>();
        input.data = elem;

        // Insert at beginning
        if (index == 0) {
            input.next = this.head;
            this.head = input;
        } else {
            Node<T> currentNode = this.head;

            // Go to the node before the desired index (input 2, i = 1. ex: [0][1][2][...])
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.next;
            }

            input.next = currentNode.next;
            currentNode.next = input;
        }
        this.size++;
    }

    @Override
    public T obtener(int index) {
        checkIndexOutOfBounds(index);

        Node<T> currentNode = this.head;

        // Go to de desired node
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        return currentNode.data;
    }

    @Override
    public T remover(int index) {
        checkIndexOutOfBounds(index);

        Node<T> returnNode;

        // Remove first element
        if (index == 0) {
            returnNode = this.head;
            this.head = this.head.next;
        } else {
            Node<T> currentNode = this.head; // we know its not the first one

            // Go to the node BEFORE the desired node
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.next;
            }

            returnNode = currentNode.next;
            currentNode.next = returnNode.next;
        }

        this.size--;

        return returnNode.data;
    }

    @Override
    public boolean remover(T elem) {
        checkElementNull(elem);

        if (this.head == null) {
            return false;
        }

        // Caso especial: el elemento esta en head
        if (this.head.data.equals(elem)) {
            this.head = this.head.next;
            this.size--;
            return true;
        }

        Node<T> currentNode = this.head;

        while (currentNode.next != null) {
            if (currentNode.next.data.equals(elem)) {
                currentNode.next = currentNode.next.next;
                this.size--;
                return true;
            }
            currentNode = currentNode.next;
        }

        return false;
    }

    @Override
    public boolean contiene(T elem) {
        checkElementNull(elem);

        Node<T> currentNode = this.head;

        while (currentNode != null) {
            if (currentNode.data.equals(elem)) {
                return true;
            }

            currentNode = currentNode.next;
        }

        return false;
    }

    @Override
    public int indiceDe(T elem) {
        checkElementNull(elem);

        Node<T> currentNode = this.head;
        int index = 0;

        while (currentNode != null) {

            if (currentNode.data.equals(elem)) {
                return index;
            }

            currentNode = currentNode.next;
            index++;
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate cannot be null");
        }

        Node<T> currentNode = this.head;

        while (currentNode != null) { // currentNode instead of currentNode.next!
            if (predicate.test(currentNode.data)) {
                return currentNode.data;
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        Lista<T> sorted = new Lista<>();
        Node<T> currentNode = this.head;

        while (currentNode != null) {
            sorted.insertSorted(currentNode.data, comparator);
            currentNode = currentNode.next;
        }

        return sorted;
    }

    @Override
    public int tamaño() {
        return this.size;
    }

    @Override
    public boolean esVacio() {
        return this.head == null;
    }

    @Override
    public void vaciar() {
        this.head = null;
        size = 0;
    }

    // ======================================================
    // =================== HELPER METHODS ===================
    // ======================================================

    private void insertSorted(T elem, Comparator<T> comparator) {
        Node<T> newNode = new Node<>();
        newNode.data = elem;

        // Insertar al principio
        if (this.head == null ||
                comparator.compare(elem, this.head.data) <= 0) {

            newNode.next = this.head;
            this.head = newNode;
        } else {
            Node<T> currentNode = this.head;

            // Buscar dónde insertar
            while (currentNode.next != null &&
                    comparator.compare(elem, currentNode.next.data) > 0) {

                currentNode = currentNode.next;
            }

            // Insertar entre currentNode y currentNode.next
            newNode.next = currentNode.next;
            currentNode.next = newNode;
        }

        this.size++;
    }

    /*
     * Para obtener y remover.
     *
     * Si size = 3:
     * índices válidos = 0, 1, 2
     */
    private void checkIndexOutOfBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. List size: " + this.size + ", index: " + index);
        }
    }

    /*
     * Para agregar.
     *
     * Si size = 3:
     * se puede agregar en 0, 1, 2 o 3.
     */
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. List size: " + this.size + ", index: " + index);
        }
    }

    private void checkElementNull(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Input element cannot be null");
        }
    }

}
