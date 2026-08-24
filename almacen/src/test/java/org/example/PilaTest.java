package org.example;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.structures.Pila;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PilaTest {

    @Test
    void estructuraVacia() {
        Pila<Integer> pila = new Pila<>();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    @Test
    void unElemento() {
        Pila<Integer> pila = new Pila<>();

        pila.mete(10);

        assertFalse(pila.esVacio());
        assertEquals(1, pila.tamaño());
        assertEquals(10, pila.tope());
    }

    @Test
    void multiplesElementos() {
        Pila<Integer> pila = new Pila<>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(30, pila.tope());
        assertEquals(3, pila.tamaño());
    }

    @Test
    void comportamientoLIFO() {
        Pila<Integer> pila = new Pila<>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(30, pila.saca());
        assertEquals(20, pila.saca());
        assertEquals(10, pila.saca());

        assertTrue(pila.esVacio());
    }

    @Test
    void vaciarYReutilizacion() {
        Pila<Integer> pila = new Pila<>();

        pila.mete(10);
        pila.mete(20);

        pila.vaciar();

        assertTrue(pila.esVacio());

        pila.mete(30);

        assertEquals(30, pila.tope());
    }

    @Test
    void sacarPilaVacia() {
        Pila<Integer> pila = new Pila<>();

        assertThrows(NoSuchElementException.class,
                pila::saca);

        assertThrows(NoSuchElementException.class,
                pila::tope);
    }
}