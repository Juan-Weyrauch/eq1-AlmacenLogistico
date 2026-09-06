package ucu.edu.aed.structures.linear;

import java.util.NoSuchElementException;

import ucu.edu.aed.tda.linear.TDAPila;

public class Pila<T> extends ListaSimple<T> implements TDAPila<T> {

    public Pila() {
        super();
    }

    @Override
    public T tope() {
        if (this.esVacio()) {
            throw new NoSuchElementException("La pila está vacía");
        }

        return this.obtener(0);
    }

    @Override
    public T saca() {
        if (this.esVacio()) {
            throw new NoSuchElementException("La pila está vacía");
        }

        return this.remover(0);
    }

    @Override
    public void mete(T dato) {
        this.agregar(0, dato);
    }
}