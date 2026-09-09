package ucu.edu.aed.structures.element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class NodoGeneralTest {

    @Test
    void constructorDebeRechazarDatoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new NodoGeneral<Integer>(null));
    }

    @Test
    void constructorDebeInicializarDatoYSinHijos() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertEquals(1, nodo.getDato());
        assertTrue(nodo.esHoja());
        assertEquals(0, nodo.cantidadHijos());
    }

    @Test
    void setDatoDebeActualizarDato() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        nodo.setDato(2);

        assertEquals(2, nodo.getDato());
    }

    @Test
    void setDatoDebeRechazarDatoNulo() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.setDato(null));
    }

    @Test
    void agregarHijoDebeRechazarHijoNulo() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.agregarHijo(null));
    }

    @Test
    void agregarHijoDebeDejarDeSerHoja() {
        NodoGeneral<Integer> raiz = new NodoGeneral<>(1);
        NodoGeneral<Integer> hijo = new NodoGeneral<>(2);

        raiz.agregarHijo(hijo);

        assertFalse(raiz.esHoja());
        assertEquals(1, raiz.cantidadHijos());
    }

    @Test
    void eliminarHijoDebeRemoverHijoExistente() {
        NodoGeneral<Integer> raiz = new NodoGeneral<>(1);
        NodoGeneral<Integer> hijo = new NodoGeneral<>(2);

        raiz.agregarHijo(hijo);
        boolean eliminado = raiz.eliminarHijo(hijo);

        assertTrue(eliminado);
        assertTrue(raiz.esHoja());
    }

    @Test
    void eliminarHijoDebeRetornarFalseCuandoNoExiste() {
        NodoGeneral<Integer> raiz = new NodoGeneral<>(1);
        NodoGeneral<Integer> hijo = new NodoGeneral<>(2);

        boolean eliminado = raiz.eliminarHijo(hijo);

        assertFalse(eliminado);
    }

    @Test
    void buscarDebeRechazarCriterioNulo() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.buscar(null));
    }

    @Test
    void buscarDebeEncontrarLaRaiz() {
        NodoGeneral<Integer> raiz = crearArbolEjemplo();

        NodoGeneral<Integer> encontrado = raiz.buscar(1);

        assertEquals(1, encontrado.getDato());
    }

    @Test
    void buscarDebeEncontrarNodoEnProfundidad() {
        NodoGeneral<Integer> raiz = crearArbolEjemplo();

        NodoGeneral<Integer> encontrado = raiz.buscar(5);

        assertEquals(5, encontrado.getDato());
    }

    @Test
    void buscarDebeRetornarNullCuandoNoExiste() {
        NodoGeneral<Integer> raiz = crearArbolEjemplo();

        assertNull(raiz.buscar(100));
    }

    @Test
    void preOrderDebeRechazarConsumidorNulo() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.preOrder(null));
    }

    @Test
    void postOrderDebeRechazarConsumidorNulo() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.postOrder(null));
    }

    @Test
    void preOrderDebeVisitarRaizAntesQueHijos() {
        NodoGeneral<Integer> raiz = crearArbolEjemplo();
        List<Integer> recorrido = new ArrayList<>();

        raiz.preOrder(nodo -> recorrido.add(nodo.getDato()));

        assertEquals(List.of(1, 2, 5, 6, 3, 4), recorrido);
    }

    @Test
    void postOrderDebeVisitarHijosAntesQueRaiz() {
        NodoGeneral<Integer> raiz = crearArbolEjemplo();
        List<Integer> recorrido = new ArrayList<>();

        raiz.postOrder(nodo -> recorrido.add(nodo.getDato()));

        assertEquals(List.of(5, 6, 2, 3, 4, 1), recorrido);
    }

    @Test
    void cantidadNodosDeUnaHojaDebeSerUno() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertEquals(1, nodo.cantidadNodos());
    }

    @Test
    void cantidadNodosDebeContarTodoElArbol() {
        NodoGeneral<Integer> raiz = crearArbolEjemplo();

        assertEquals(6, raiz.cantidadNodos());
    }

    @Test
    void alturaDeUnaHojaDebeSerUno() {
        NodoGeneral<Integer> nodo = new NodoGeneral<>(1);

        assertEquals(1, nodo.altura());
    }

    @Test
    void alturaDebeCalcularCaminoMasLargo() {
        NodoGeneral<Integer> raiz = crearArbolEjemplo();

        assertEquals(3, raiz.altura());
    }

    private NodoGeneral<Integer> crearArbolEjemplo() {
        NodoGeneral<Integer> raiz = new NodoGeneral<>(1);
        NodoGeneral<Integer> b = new NodoGeneral<>(2);
        NodoGeneral<Integer> c = new NodoGeneral<>(3);
        NodoGeneral<Integer> d = new NodoGeneral<>(4);
        NodoGeneral<Integer> e = new NodoGeneral<>(5);
        NodoGeneral<Integer> f = new NodoGeneral<>(6);

        raiz.agregarHijo(b);
        raiz.agregarHijo(c);
        raiz.agregarHijo(d);
        b.agregarHijo(e);
        b.agregarHijo(f);

        return raiz;
    }
}
