package org.example;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.structures.Conjunto;
import ucu.edu.aed.tda.TDAConjunto;

import static org.junit.jupiter.api.Assertions.*;

class ConjuntoTest {

    @Test
    void estructuraVacia() {
        Conjunto<Integer> conjunto = new Conjunto<>();

        assertTrue(conjunto.esVacio());
        assertEquals(0, conjunto.tamaño());
    }

    @Test
    void agregarNoPermiteDuplicados() {
        Conjunto<Integer> conjunto = new Conjunto<>();

        conjunto.agregar(10);
        conjunto.agregar(10);
        conjunto.agregar(20);

        assertEquals(2, conjunto.tamaño());
        assertEquals(10, conjunto.obtener(0));
        assertEquals(20, conjunto.obtener(1));
    }

    @Test
    void agregarEnIndiceNoPermiteDuplicados() {
        Conjunto<Integer> conjunto = new Conjunto<>();

        conjunto.agregar(10);
        conjunto.agregar(20);

        conjunto.agregar(1, 15);
        conjunto.agregar(0, 15);

        assertEquals(3, conjunto.tamaño());

        assertEquals(10, conjunto.obtener(0));
        assertEquals(15, conjunto.obtener(1));
        assertEquals(20, conjunto.obtener(2));
    }

    @Test
    void agregarEnIndiceInvalido() {
        Conjunto<Integer> conjunto = new Conjunto<>();

        conjunto.agregar(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> conjunto.agregar(-1, 20));

        assertThrows(IndexOutOfBoundsException.class,
                () -> conjunto.agregar(2, 20));
    }

    @Test
    void union() {
        Conjunto<Integer> primero = new Conjunto<>();
        Conjunto<Integer> segundo = new Conjunto<>();

        primero.agregar(1);
        primero.agregar(2);
        primero.agregar(3);

        segundo.agregar(3);
        segundo.agregar(4);
        segundo.agregar(5);

        TDAConjunto<Integer> union =
                primero.union(segundo);

        assertEquals(5, union.tamaño());

        assertTrue(union.contiene(1));
        assertTrue(union.contiene(2));
        assertTrue(union.contiene(3));
        assertTrue(union.contiene(4));
        assertTrue(union.contiene(5));
    }

    @Test
    void interseccion() {
        Conjunto<Integer> primero = new Conjunto<>();
        Conjunto<Integer> segundo = new Conjunto<>();

        primero.agregar(1);
        primero.agregar(2);
        primero.agregar(3);

        segundo.agregar(2);
        segundo.agregar(3);
        segundo.agregar(4);

        TDAConjunto<Integer> resultado =
                primero.interseccion(segundo);

        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(2));
        assertTrue(resultado.contiene(3));
        assertFalse(resultado.contiene(1));
        assertFalse(resultado.contiene(4));
    }

    @Test
    void diferencia() {
        Conjunto<Integer> primero = new Conjunto<>();
        Conjunto<Integer> segundo = new Conjunto<>();

        primero.agregar(1);
        primero.agregar(2);
        primero.agregar(3);

        segundo.agregar(2);
        segundo.agregar(3);

        TDAConjunto<Integer> resultado =
                primero.diferencia(segundo);

        assertEquals(1, resultado.tamaño());
        assertTrue(resultado.contiene(1));
        assertFalse(resultado.contiene(2));
        assertFalse(resultado.contiene(3));
    }

    @Test
    void subconjunto() {
        Conjunto<Integer> primero = new Conjunto<>();
        Conjunto<Integer> segundo = new Conjunto<>();

        primero.agregar(1);
        primero.agregar(2);

        segundo.agregar(1);
        segundo.agregar(2);
        segundo.agregar(3);

        assertTrue(primero.esSubconjuntoDe(segundo));
        assertFalse(segundo.esSubconjuntoDe(primero));
    }

    @Test
    void vaciarYReutilizacion() {
        Conjunto<Integer> conjunto = new Conjunto<>();

        conjunto.agregar(10);
        conjunto.agregar(20);

        conjunto.vaciar();

        assertTrue(conjunto.esVacio());

        conjunto.agregar(30);

        assertEquals(30, conjunto.obtener(0));
    }
}