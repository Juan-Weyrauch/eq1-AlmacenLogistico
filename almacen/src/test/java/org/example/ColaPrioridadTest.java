package org.example;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.structures.ColaPrioridad;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ColaPrioridadTest {

    @Test
    void estructuraVacia() {
        ColaPrioridad<Integer> cola =
                new ColaPrioridad<>(Integer::compareTo);

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    @Test
    void unElemento() {
        ColaPrioridad<Integer> cola =
                new ColaPrioridad<>(Integer::compareTo);

        cola.poneEnCola(10);

        assertEquals(10, cola.frente());
    }

    @Test
    void multiplesElementos() {
        ColaPrioridad<Integer> cola =
                new ColaPrioridad<>(Integer::compareTo);

        cola.poneEnCola(10);
        cola.poneEnCola(30);
        cola.poneEnCola(20);

        assertEquals(30, cola.frente());
        assertEquals(3, cola.tamaño());
    }

    @Test
    void probarPrioridad() {
        ColaPrioridad<Integer> cola =
                new ColaPrioridad<>(Integer::compareTo);

        cola.poneEnCola(10);
        cola.poneEnCola(50);
        cola.poneEnCola(30);
        cola.poneEnCola(20);
        cola.poneEnCola(40);

        assertEquals(50, cola.quitaDeCola());
        assertEquals(40, cola.quitaDeCola());
        assertEquals(30, cola.quitaDeCola());
        assertEquals(20, cola.quitaDeCola());
        assertEquals(10, cola.quitaDeCola());

        assertTrue(cola.esVacio());
    }

    @Test
    void vaciarYReutilizacion() {
        ColaPrioridad<Integer> cola =
                new ColaPrioridad<>(Integer::compareTo);

        cola.poneEnCola(10);
        cola.poneEnCola(30);

        cola.vaciar();

        assertTrue(cola.esVacio());

        cola.poneEnCola(20);

        assertEquals(20, cola.frente());
    }

    @Test
    void colaVacia() {
        ColaPrioridad<Integer> cola =
                new ColaPrioridad<>(Integer::compareTo);

        assertThrows(NoSuchElementException.class,
                cola::frente);

        assertThrows(NoSuchElementException.class,
                cola::quitaDeCola);
    }
}