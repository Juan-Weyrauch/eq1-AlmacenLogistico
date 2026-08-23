package org.example;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.structures.ListaDoble;
import ucu.edu.aed.tda.TDALista;

import static org.junit.jupiter.api.Assertions.*;

class ListaDobleTest {

    @Test
    void estructuraVacia() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    @Test
    void unElemento() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);

        assertEquals(10, lista.obtener(0));
        assertEquals(1, lista.tamaño());
    }

    @Test
    void multiplesElementos() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(10, lista.obtener(0));
        assertEquals(20, lista.obtener(1));
        assertEquals(30, lista.obtener(2));
    }

    @Test
    void insercionInicialMediaYFinal() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(0, 5);
        lista.agregar(2, 20);
        lista.agregar(4, 40);

        assertEquals(5, lista.tamaño());
        assertEquals(5, lista.obtener(0));
        assertEquals(10, lista.obtener(1));
        assertEquals(20, lista.obtener(2));
        assertEquals(30, lista.obtener(3));
        assertEquals(40, lista.obtener(4));
    }

    @Test
    void eliminacionInicialMediaYFinal() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);
        lista.agregar(40);

        assertEquals(10, lista.remover(0));
        assertEquals(30, lista.remover(1));
        assertEquals(40, lista.remover(1));

        assertTrue(lista.esVacio());
    }

    @Test
    void busquedas() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertTrue(lista.contiene(20));
        assertEquals(1, lista.indiceDe(20));

        assertEquals(30, lista.buscar(x -> x > 25));
        assertNull(lista.buscar(x -> x > 100));
    }

    @Test
    void indicesInvalidos() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> lista.obtener(-1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> lista.obtener(1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> lista.agregar(2, 20));

        assertThrows(IndexOutOfBoundsException.class,
                () -> lista.remover(1));
    }

    @Test
    void vaciarYReutilizacion() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);

        lista.vaciar();

        assertTrue(lista.esVacio());

        lista.agregar(30);

        assertEquals(30, lista.obtener(0));
    }

    @Test
    void ordenar() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(30);
        lista.agregar(10);
        lista.agregar(20);

        TDALista<Integer> ordenada =
                lista.ordenar(Integer::compareTo);

        assertEquals(10, ordenada.obtener(0));
        assertEquals(20, ordenada.obtener(1));
        assertEquals(30, ordenada.obtener(2));
    }
}