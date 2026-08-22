package ucu.edu.aed.structures;

import ucu.edu.aed.tda.TDACola;

import java.util.NoSuchElementException;

public class Cola<T> extends Lista<T> implements TDACola<T> {

    public Cola() {
        super();
    }

    @Override
    public T frente() {
        if (this.esVacio()) {
            throw new NoSuchElementException("La cola está vacía");
        }

        return this.obtener(0);
    }

    @Override
    public boolean poneEnCola(T dato) {
        this.agregar(dato);
        return true;
    }

    @Override
    public T quitaDeCola() {
        if (this.esVacio()) {
            throw new NoSuchElementException("La cola está vacía");
        }

        return this.remover(0);
    }
}