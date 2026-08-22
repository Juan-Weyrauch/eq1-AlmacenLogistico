package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDALista;

import java.util.Comparator;
import java.util.function.Predicate;

public class ListaArray<T> implements TDALista<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int size;

    public ListaArray() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.size = 0;
    }

    public ListaArray(int capacidadInicial) {
        if (capacidadInicial <= 0) {
            throw new IllegalArgumentException(
                    "Initial capacity must be greater than zero");
        }

        this.elementos = new Object[capacidadInicial];
        this.size = 0;
    }

    @Override
    public void agregar(T elem) {
        checkElementNull(elem);
        asegurarCapacidad();

        this.elementos[this.size] = elem;
        this.size++;
    }

    @Override
    public void agregar(int index, T elem) {
        checkElementNull(elem);
        checkIndexForAdd(index);
        asegurarCapacidad();

        for (int i = this.size; i > index; i--) {
            this.elementos[i] = this.elementos[i - 1];
        }

        this.elementos[index] = elem;
        this.size++;
    }

    @Override
    public T obtener(int index) {
        checkIndexOutOfBounds(index);

        return elementoEn(index);
    }

    @Override
    public T remover(int index) {
        checkIndexOutOfBounds(index);

        T eliminado = elementoEn(index);

        for (int i = index; i < this.size - 1; i++) {
            this.elementos[i] = this.elementos[i + 1];
        }

        this.elementos[this.size - 1] = null;
        this.size--;

        return eliminado;
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
        return indiceDe(elem) != -1;
    }

    @Override
    public int indiceDe(T elem) {
        checkElementNull(elem);

        for (int i = 0; i < this.size; i++) {

            if (this.elementos[i].equals(elem)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        checkPredicateNull(criterio);

        for (int i = 0; i < this.size; i++) {

            T actual = elementoEn(i);

            if (criterio.test(actual)) {
                return actual;
            }
        }

        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        checkComparatorNull(comparator);

        ListaArray<T> ordenada =
                new ListaArray<>(Math.max(CAPACIDAD_INICIAL, this.size));

        for (int i = 0; i < this.size; i++) {
            ordenada.agregar(elementoEn(i));
        }

        for (int i = 1; i < ordenada.size; i++) {

            T actual = ordenada.elementoEn(i);
            int j = i - 1;

            while (j >= 0
                    && comparator.compare(
                            ordenada.elementoEn(j), actual) > 0) {

                ordenada.elementos[j + 1] =
                        ordenada.elementos[j];

                j--;
            }

            ordenada.elementos[j + 1] = actual;
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
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.size = 0;
    }

    // =================== HELPER METHODS ====================

    private void asegurarCapacidad() {
        if (this.size < this.elementos.length) {
            return;
        }

        Object[] nuevoVector =
                new Object[this.elementos.length * 2];

        for (int i = 0; i < this.elementos.length; i++) {
            nuevoVector[i] = this.elementos[i];
        }

        this.elementos = nuevoVector;
    }

    @SuppressWarnings("unchecked")
    private T elementoEn(int index) {
        return (T) this.elementos[index];
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