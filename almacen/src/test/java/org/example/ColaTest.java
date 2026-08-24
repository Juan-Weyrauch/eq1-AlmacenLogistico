package org.example;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.structures.Cola;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ColaTest {

    @Test
    void estructuraVacia() {
        Cola<Integer> cola = new Cola<>();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    @Test
    void unElemento() {
        Cola<Integer> cola = new Cola<>();

        cola.poneEnCola(10);

        assertEquals(10, cola.frente());
        assertEquals(1, cola.tamaño());
    }

    @Test
    void multiplesElementos() {
        Cola<Integer> cola = new Cola<>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(10, cola.frente());
        assertEquals(3, cola.tamaño());
    }

    @Test
    void comportamientoFIFO() {
        Cola<Integer> cola = new Cola<>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(10, cola.quitaDeCola());
        assertEquals(20, cola.quitaDeCola());
        assertEquals(30, cola.quitaDeCola());

        assertTrue(cola.esVacio());
    }

    @Test
    void vaciarYReutilizacion() {
        Cola<Integer> cola = new Cola<>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);

        cola.vaciar();

        assertTrue(cola.esVacio());

        cola.poneEnCola(30);

        assertEquals(30, cola.frente());
    }

    @Test
    void sacarColaVacia() {
        Cola<Integer> cola = new Cola<>();

        assertThrows(NoSuchElementException.class,
                cola::quitaDeCola);

        assertThrows(NoSuchElementException.class,
                cola::frente);
    }
}