package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDAConjunto;

public class Conjunto<T> extends ListaSimple<T> implements TDAConjunto<T> {

    public Conjunto() {
        super();
    }

    @Override
    public void agregar(T elem) {
        if (!this.contiene(elem)) {
            super.agregar(elem);
        }
    }

    @Override
    public void agregar(int index, T elem) {

        // El índice debe validarse siempre,
        // incluso si el elemento ya pertenece al conjunto.
        if (index < 0 || index > this.tamaño()) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds. Set size: "
                            + this.tamaño()
                            + ", index: "
                            + index);
        }

        if (!this.contiene(elem)) {
            super.agregar(index, elem);
        }
    }

    @Override
    public TDAConjunto<T> union(TDAConjunto<T> otro) {
        checkConjuntoNull(otro);

        TDAConjunto<T> resultado = new Conjunto<>();

        // Agregar elementos de este conjunto
        for (int i = 0; i < this.tamaño(); i++) {
            resultado.agregar(this.obtener(i));
        }

        // Agregar elementos del otro conjunto
        for (int i = 0; i < otro.tamaño(); i++) {
            resultado.agregar(otro.obtener(i));
        }

        return resultado;
    }

    @Override
    public TDAConjunto<T> interseccion(TDAConjunto<T> otro) {
        checkConjuntoNull(otro);

        TDAConjunto<T> resultado = new Conjunto<>();

        for (int i = 0; i < this.tamaño(); i++) {
            T elem = this.obtener(i);

            if (otro.contiene(elem)) {
                resultado.agregar(elem);
            }
        }

        return resultado;
    }

    @Override
    public TDAConjunto<T> diferencia(TDAConjunto<T> otro) {
        checkConjuntoNull(otro);

        TDAConjunto<T> resultado = new Conjunto<>();

        for (int i = 0; i < this.tamaño(); i++) {
            T elem = this.obtener(i);

            if (!otro.contiene(elem)) {
                resultado.agregar(elem);
            }
        }

        return resultado;
    }

    @Override
    public boolean esSubconjuntoDe(TDAConjunto<T> otro) {
        checkConjuntoNull(otro);

        for (int i = 0; i < this.tamaño(); i++) {
            if (!otro.contiene(this.obtener(i))) {
                return false;
            }
        }

        return true;
    }

    private void checkConjuntoNull(TDAConjunto<T> otro) {
        if (otro == null) {
            throw new IllegalArgumentException(
                    "El conjunto no puede ser null");
        }
    }
}