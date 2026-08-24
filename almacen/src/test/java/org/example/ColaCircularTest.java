package org.example;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.structures.ColaCircular;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ColaCircularTest {

    @Test
    void estructuraVacia() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
        assertFalse(cola.estaLlena());
    }

    @Test
    void unElemento() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        cola.poneEnCola(10);

        assertEquals(10, cola.frente());
        assertEquals(1, cola.tamaño());
    }

    @Test
    void multiplesElementos() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertTrue(cola.estaLlena());
        assertEquals(10, cola.frente());
    }

    @Test
    void insercionInicialMediaYFinal() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(5);

        cola.agregar(10);
        cola.agregar(30);

        cola.agregar(0, 5);
        cola.agregar(2, 20);
        cola.agregar(4, 40);

        assertEquals(5, cola.obtener(0));
        assertEquals(10, cola.obtener(1));
        assertEquals(20, cola.obtener(2));
        assertEquals(30, cola.obtener(3));
        assertEquals(40, cola.obtener(4));
    }

    @Test
    void eliminacionInicialMediaYFinal() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(5);

        cola.agregar(10);
        cola.agregar(20);
        cola.agregar(30);
        cola.agregar(40);

        assertEquals(10, cola.remover(0));
        assertEquals(30, cola.remover(1));
        assertEquals(40, cola.remover(1));

        assertEquals(20, cola.frente());
    }

    @Test
    void wraparound() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(10, cola.quitaDeCola());

        // finalCola vuelve al principio del vector
        assertTrue(cola.poneEnCola(40));

        assertEquals(20, cola.quitaDeCola());
        assertEquals(30, cola.quitaDeCola());
        assertEquals(40, cola.quitaDeCola());

        assertTrue(cola.esVacio());
    }

    @Test
    void colaLlena() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(2);

        assertTrue(cola.poneEnCola(10));
        assertTrue(cola.poneEnCola(20));

        assertFalse(cola.poneEnCola(30));
        assertTrue(cola.estaLlena());
    }

    @Test
    void indicesInvalidos() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        cola.poneEnCola(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> cola.obtener(-1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> cola.obtener(1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> cola.agregar(2, 20));

        assertThrows(IndexOutOfBoundsException.class,
                () -> cola.remover(1));
    }

    @Test
    void vaciarYReutilizacion() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        cola.poneEnCola(10);
        cola.poneEnCola(20);

        cola.vaciar();

        assertTrue(cola.esVacio());

        cola.poneEnCola(30);

        assertEquals(30, cola.frente());
    }

    @Test
    void prioridadNoCorresponde() {
        // Este test simplemente documenta que ColaCircular
        // NO tiene comportamiento de prioridad.
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        cola.poneEnCola(30);
        cola.poneEnCola(10);
        cola.poneEnCola(20);

        assertEquals(30, cola.quitaDeCola());
    }

    @Test
    void colaVacia() {
        ColaCircular<Integer> cola =
                new ColaCircular<>(3);

        assertThrows(NoSuchElementException.class,
                cola::frente);

        assertThrows(NoSuchElementException.class,
                cola::quitaDeCola);
    }
}